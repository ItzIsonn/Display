package me.itzisonn_.display.commands.edit_types;

import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class MaterialEditType extends AbstractEditType {
    public MaterialEditType(DisplayPlugin plugin) {
        super(plugin, "material", 1, EntityType.BLOCK_DISPLAY, EntityType.ITEM_DISPLAY);
    }

    @Override
    public boolean onCommand(Player player, DisplayData<?> displayData, String[] args) {
        Display entity = displayData.getDisplay();
        int id = displayData.getId();

        if (args[0].equals("?")) {
            String infoValue = "?";
            if (entity instanceof BlockDisplay blockDisplay) infoValue = blockDisplay.getBlock().getMaterial().toString();
            else if (entity instanceof ItemDisplay itemDisplay && itemDisplay.getItemStack() != null) infoValue = itemDisplay.getItemStack().getType().toString();
            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditInfo().getComponent(player,
                    Placeholder.parsed("id", String.valueOf(id)),
                    Placeholder.parsed("type", "material"),
                    Placeholder.parsed("value", infoValue)));
            return false;
        }

        Material material;
        try {
            material = Material.valueOf(args[0].toUpperCase());
        }
        catch (IllegalArgumentException ignore) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
            return false;
        }

        if (entity.getType() == EntityType.BLOCK_DISPLAY) {
            if (!material.isBlock()) {
                player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
                return false;
            }
            if (material == Material.CAVE_AIR || material == Material.AIR || material == Material.BARRIER) {
                player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
                return false;
            }

            ((BlockDisplay) entity).setBlock(Bukkit.createBlockData(material));
        }
        else if (entity.getType() == EntityType.ITEM_DISPLAY) {
            if (!material.isItem()) {
                player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
                return false;
            }

            ((ItemDisplay) entity).setItemStack(new ItemStack(material));
            return true;
        }
        return true;
    }

    @Override
    public ArrayList<String> onTabComplete(Player player, DisplayData<?> displayData, String[] args) {
        ArrayList<String> materialList;
        if (displayData.getDisplay().getType() == EntityType.BLOCK_DISPLAY) materialList = getBlockList();
        else materialList = getItemList();

        materialList.add("?");
        return materialList;
    }



    private static ArrayList<String> getBlockList() {
        ArrayList<Material> materialList = new ArrayList<>();
        for (Material material : Material.values()) {
            if (material.isBlock()) materialList.add(material);
        }

        ArrayList<String> stringList = new ArrayList<>();
        for (Material material : materialList) {
            stringList.add(material.toString().toLowerCase());
        }
        stringList.remove("air");
        stringList.remove("cave_air");
        stringList.remove("barrier");

        return stringList;
    }

    private static ArrayList<String> getItemList() {
        ArrayList<Material> materialList = new ArrayList<>();
        for (Material material : Material.values()) {
            if (material.isItem()) materialList.add(material);
        }

        ArrayList<String> stringList = new ArrayList<>();
        for (Material material : materialList) {
            stringList.add(material.toString().toLowerCase());
        }

        return stringList;
    }
}