package me.itzisonn_.display.subcommands.edit_types;

import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.Utils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Set;

public class MaterialEditType extends AbstractEditType {
    public MaterialEditType(DisplayPlugin plugin) {
        super(plugin, "material", Set.of(EntityType.BLOCK_DISPLAY, EntityType.ITEM_DISPLAY));
    }

    @Override
    public boolean onCommand(Player player, String value, Display entity, int id) {
        if (value.equals("?")) {
            String infoValue = "?";
            if (entity instanceof BlockDisplay blockDisplay) infoValue = blockDisplay.getBlock().getMaterial().toString();
            else if (entity instanceof ItemDisplay itemDisplay && itemDisplay.getItemStack() != null) infoValue = itemDisplay.getItemStack().getType().toString();
            player.sendMessage(plugin.getConfigManager().getSuccessfully("edit.info", String.valueOf(id), player,
                    Placeholder.parsed("type", "material"),
                    Placeholder.parsed("value", infoValue)));
            return false;
        }

        Material material;
        try {
            material = Material.valueOf(value.toUpperCase());
        }
        catch (IllegalArgumentException ignore) {
            player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
            return false;
        }

        if (entity.getType() == EntityType.BLOCK_DISPLAY) {
            if (!material.isBlock()) {
                player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
                return false;
            }
            if (material == Material.CAVE_AIR || material == Material.AIR || material == Material.BARRIER) {
                player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
                return false;
            }

            ((BlockDisplay) entity).setBlock(Bukkit.createBlockData(material));
        }
        else if (entity.getType() == EntityType.ITEM_DISPLAY) {
            if (!material.isItem()) {
                player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
                return false;
            }

            ((ItemDisplay) entity).setItemStack(new ItemStack(material));
            return true;
        }
        return true;
    }

    @Override
    public ArrayList<String> onTabComplete(EntityType type) {
        ArrayList<String> materialList;
        if (type == EntityType.BLOCK_DISPLAY) materialList = Utils.getBlockList();
        else materialList = Utils.getItemList();

        materialList.add("?");
        return materialList;
    }
}