package me.itzisonn_.display;

import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Utils {
    private final DisplayPlugin plugin;
    @Getter
    private BukkitTask scheduler;

    public Utils(DisplayPlugin plugin) {
        this.plugin = plugin;
    }

    public void startTextUpdating() {
        int interval = plugin.getConfigManager().getTextUpdateInterval();

        if (interval != 0) scheduler = Bukkit.getServer().getScheduler().runTaskTimer(plugin, () -> {
            for (Entity entity : plugin.getDisplaysMap().values()) {
                if (entity.getType() != EntityType.TEXT_DISPLAY) continue;
                TextDisplay textEntity = (TextDisplay) entity;

                PersistentDataContainer data = textEntity.getPersistentDataContainer();
                if (!data.has(plugin.getNskDisplayText(), PersistentDataType.STRING)) continue;

                String text = data.get(plugin.getNskDisplayText(), PersistentDataType.STRING);
                if (text == null) continue;

                textEntity.text(MiniMessage.miniMessage().deserialize(parsePlaceholders(null, text)));
            }
        }, 0, interval);
    }

    public String parsePlaceholders(Player player, String string) {
        if (plugin.isHookedPapi()) return PlaceholderAPI.setPlaceholders(player, string);
        return string;
    }

    public ArrayList<String> getBlockList() {
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

    public ArrayList<String> getItemList() {
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

    public ArrayList<String> getBillBoardList() {
        ArrayList<String> list = new ArrayList<>();
        for (Display.Billboard billboard : Display.Billboard.values()) {
            list.add(billboard.name().toLowerCase());
        }
        return list;
    }

    public ArrayList<String> getDisplayTransform() {
        ArrayList<String> list = new ArrayList<>();
        for (ItemDisplay.ItemDisplayTransform displayTransform : ItemDisplay.ItemDisplayTransform.values()) {
            list.add(displayTransform.name().toLowerCase());
        }
        return list;
    }

    public ArrayList<String> getAlignment() {
        ArrayList<String> list = new ArrayList<>();
        for (TextDisplay.TextAlignment alignment : TextDisplay.TextAlignment.values()) {
            list.add(alignment.name().toLowerCase());
        }
        return list;
    }

    public boolean parseBoolean(String string) {
        if (string.equalsIgnoreCase("true")) return true;
        else if (string.equalsIgnoreCase("false")) return false;
        throw new IllegalArgumentException();
    }

    public Display getTarget(Player player, int checkDistance) {
        Vector one = player.getEyeLocation().clone().getDirection();
        Location checkingLoc = player.getEyeLocation().clone();
        Entity selectedEntity;
        Collection<Entity> collection;
        Iterator<Entity> iter;

        while (checkDistance >= 0) {
            collection = checkingLoc.getWorld().getNearbyEntities(checkingLoc, 0.5, 0.5, 0.5);
            checkingLoc.add(one);
            iter = collection.iterator();
            while (iter.hasNext()) {
                selectedEntity = iter.next();
                if (selectedEntity != player && selectedEntity instanceof Display display) return display;
            }
            checkDistance--;
        }
        return null;
    }
}