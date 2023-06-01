package me.itzisonn_.display.maincommand;

import com.google.common.collect.Lists;
import me.itzisonn_.display.Display;
import me.itzisonn_.display.Utils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.*;

public class DisplayTab extends AbstractTab {
    private static Player player;
    private static final HashMap<Integer, Entity> displays = Display.getDisplays();

    @Override
    public ArrayList<String> tabComplete(CommandSender sender, String[] args) {
        player = (Player) sender;

        if (args.length == 1) return mainTabComplete();


        if (args.length == 2 && args[0].equalsIgnoreCase("create") && (player.hasPermission("display.command.create") || player.hasPermission("display.command.*")))
            return Lists.newArrayList("block", "item", "text");
        if (args.length == 3 && args[0].equalsIgnoreCase("create") && (player.hasPermission("display.command.create") || player.hasPermission("display.command.*")))
            return Lists.newArrayList("<id>");


        if (args.length == 2 && args[0].equalsIgnoreCase("delete") && (player.hasPermission("display.command.delete") || player.hasPermission("display.command.*")))
            return Lists.newArrayList(getIDs());


        if (args.length == 2 && args[0].equalsIgnoreCase("edit") && (player.hasPermission("display.command.edit") || player.hasPermission("display.command.*")))
            return Lists.newArrayList(getIDs());
        if (Utils.isInt(args[1]) && displays.get(Integer.parseInt(args[1])) != null && (player.hasPermission("display.command.edit") || player.hasPermission("display.command.*"))) {
            Entity entity = displays.get(Integer.parseInt(args[1]));
            EntityType entityType = entity.getType();

            if (args.length == 3 && args[0].equalsIgnoreCase("edit") && entityType == EntityType.BLOCK_DISPLAY)
                return Lists.newArrayList("blocktype", "glowing", "scale");
            if (args.length == 3 && args[0].equalsIgnoreCase("edit") && entityType == EntityType.ITEM_DISPLAY)
                return Lists.newArrayList("itemtype", "glowing", "scale");
            if (args.length == 3 && args[0].equalsIgnoreCase("edit") && entityType == EntityType.TEXT_DISPLAY)
                return Lists.newArrayList("addline", "setline", "removeline", "scale");

            if (args.length == 4 && args[0].equalsIgnoreCase("edit") && entityType == EntityType.BLOCK_DISPLAY && args[2].equalsIgnoreCase("blocktype"))
                return Lists.newArrayList(getBlockList());
            if (args.length == 4 && args[0].equalsIgnoreCase("edit") && entityType == EntityType.ITEM_DISPLAY && args[2].equalsIgnoreCase("itemtype"))
                return Lists.newArrayList(getItemList());

            if (args.length == 4 && args[0].equalsIgnoreCase("edit") && entityType == EntityType.TEXT_DISPLAY && args[2].equalsIgnoreCase("addline"))
                return Lists.newArrayList("<linenumber>");
            if (args.length == 4 && args[0].equalsIgnoreCase("edit") && entityType == EntityType.TEXT_DISPLAY && args[2].equalsIgnoreCase("setline"))
                return Lists.newArrayList("<linenumber>");
            if (args.length == 4 && args[0].equalsIgnoreCase("edit") && entityType == EntityType.TEXT_DISPLAY && args[2].equalsIgnoreCase("removeline"))
                return Lists.newArrayList("<linenumber>");

            if (args.length == 4 && args[0].equalsIgnoreCase("edit") && args[2].equalsIgnoreCase("scale"))
                return Lists.newArrayList("<scalex>");
            if (args.length == 5 && args[0].equalsIgnoreCase("edit") && args[2].equalsIgnoreCase("scale"))
                return Lists.newArrayList("<scaley>");
            if (args.length == 6 && args[0].equalsIgnoreCase("edit") && args[2].equalsIgnoreCase("scale"))
                return Lists.newArrayList("<scalez>");
        }


        if (args.length == 2 && args[0].equalsIgnoreCase("tpcoords") && (player.hasPermission("display.command.tpcoords") || player.hasPermission("display.command.*")))
            return Lists.newArrayList(getIDs());
        if (args.length == 3 && args[0].equalsIgnoreCase("tpcoords") && (player.hasPermission("display.command.tpcoords") || player.hasPermission("display.command.*")))
            return Lists.newArrayList("overworld", "the_nether", "the_end");
        if (args.length == 4 && args[0].equalsIgnoreCase("tpcoords") && (player.hasPermission("display.command.tpcoords") || player.hasPermission("display.command.*")))
            return Lists.newArrayList("<x>");
        if (args.length == 5 && args[0].equalsIgnoreCase("tpcoords") && (player.hasPermission("display.command.tpcoords") || player.hasPermission("display.command.*")))
            return Lists.newArrayList("<y>");
        if (args.length == 6 && args[0].equalsIgnoreCase("tpcoords") && (player.hasPermission("display.command.tpcoords") || player.hasPermission("display.command.*")))
            return Lists.newArrayList("<z>");


        if (args.length == 2 && args[0].equalsIgnoreCase("tphere") && (player.hasPermission("display.command.tphere") || player.hasPermission("display.command.*")))
            return Lists.newArrayList(getIDs());


        if (args.length == 2 && args[0].equalsIgnoreCase("tpto") && (player.hasPermission("display.command.tpto") || player.hasPermission("display.command.*")))
            return Lists.newArrayList(getIDs());


        if (args.length == 2 && args[0].equalsIgnoreCase("changeid") && (player.hasPermission("display.command.changeid") || player.hasPermission("display.command.*")))
            return Lists.newArrayList(getIDs());
        if (args.length == 3 && args[0].equalsIgnoreCase("changeid") && (player.hasPermission("display.command.changeid") || player.hasPermission("display.command.*")))
            return Lists.newArrayList("<id>");

        return Lists.newArrayList();
    }



    public DisplayTab() { super("display"); }
    private static List<String> getBlockList() {
        ArrayList<Material> arrayMaterialBlock = new ArrayList<>();

        for (Material material : Material.values()) {
            if (material.isBlock()) arrayMaterialBlock.add(material);
        }

        String stringBlocks = Arrays.toString(arrayMaterialBlock.toArray(new Material[0]));
        stringBlocks = stringBlocks.replace("[", "").replace("]", "");
        stringBlocks = stringBlocks.toLowerCase();
        List<String> blocklist = new ArrayList<>(Arrays.asList(stringBlocks.split(", ")));
        blocklist.remove("air");
        blocklist.remove("cave_air");

        return blocklist;
    }
    private static List<String> getItemList() {
        ArrayList<Material> arrayMaterialItem = new ArrayList<>();
        for (Material material : Material.values()) {
            if (!material.isBlock()) arrayMaterialItem.add(material);
        }
        String stringItems = Arrays.toString(arrayMaterialItem.toArray(new Material[0]));
        stringItems = stringItems.replace("[", "").replace("]", "");
        stringItems = stringItems.toLowerCase();

        return new ArrayList<>(Arrays.asList(stringItems.split(", ")));
    }
    private static List<String> getIDs() {
        Set<Integer> setIDs = displays.keySet();
        String stringIDs = String.valueOf(setIDs);
        stringIDs = stringIDs.replace("[", "").replace("]", "");

        return new ArrayList<>(Arrays.asList(stringIDs.split(", ")));
    }
    public static ArrayList<String> mainTabComplete() {
        ArrayList<String> array = new ArrayList<>();

        if (player.hasPermission("display.command.help") || player.hasPermission("display.command.*")) {
            array.add("help");
        }
        if (player.hasPermission("display.command.reload") || player.hasPermission("display.command.*")) {
            array.add("reload");
        }
        if (player.hasPermission("display.command.create") || player.hasPermission("display.command.*")) {
            array.add("create");
        }
        if (player.hasPermission("display.command.delete") || player.hasPermission("display.command.*")) {
            array.add("delete");
        }
        if (player.hasPermission("display.command.edit") || player.hasPermission("display.command.*")) {
            array.add("edit");
        }
        if (player.hasPermission("display.command.tpcoords") || player.hasPermission("display.command.*")) {
            array.add("tpcoords");
        }
        if (player.hasPermission("display.command.tphere") || player.hasPermission("display.command.*")) {
            array.add("tphere");
        }
        if (player.hasPermission("display.command.tpto") || player.hasPermission("display.command.*")) {
            array.add("tpto");
        }
        if (player.hasPermission("display.command.changeid") || player.hasPermission("display.command.*")) {
            array.add("changeid");
        }

        return array;
    }
}
