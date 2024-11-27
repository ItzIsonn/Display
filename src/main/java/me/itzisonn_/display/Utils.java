package me.itzisonn_.display;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Utils {
    private Utils() {}

    public static ArrayList<String> getBlockList() {
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

    public static ArrayList<String> getItemList() {
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

    public static ArrayList<String> getBillBoardList() {
        ArrayList<String> list = new ArrayList<>();
        for (Display.Billboard billboard : Display.Billboard.values()) {
            list.add(billboard.name().toLowerCase());
        }
        return list;
    }

    public static ArrayList<String> getDisplayTransform() {
        ArrayList<String> list = new ArrayList<>();
        for (ItemDisplay.ItemDisplayTransform displayTransform : ItemDisplay.ItemDisplayTransform.values()) {
            list.add(displayTransform.name().toLowerCase());
        }
        return list;
    }

    public static ArrayList<String> getAlignment() {
        ArrayList<String> list = new ArrayList<>();
        for (TextDisplay.TextAlignment alignment : TextDisplay.TextAlignment.values()) {
            list.add(alignment.name().toLowerCase());
        }
        return list;
    }

    public static Display getTarget(Player player, int checkDistance) {
        Vector one = player.getEyeLocation().clone().getDirection();
        Location checkingLoc = player.getEyeLocation().clone();
        Entity selectedEntity;
        Collection<Entity> collection;
        Iterator<Entity> iterator;

        while (checkDistance >= 0) {
            collection = checkingLoc.getWorld().getNearbyEntities(checkingLoc, 0.5, 0.5, 0.5);
            checkingLoc.add(one);
            iterator = collection.iterator();
            while (iterator.hasNext()) {
                selectedEntity = iterator.next();
                if (selectedEntity != player && selectedEntity instanceof Display display) return display;
            }
            checkDistance--;
        }
        return null;
    }
}