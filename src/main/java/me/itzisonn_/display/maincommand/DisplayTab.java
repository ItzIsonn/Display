package me.itzisonn_.display.maincommand;

import com.google.common.collect.Lists;
import me.itzisonn_.display.Display;
import me.itzisonn_.display.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class DisplayTab extends AbstractTab {
    private final Display display;
    private final Utils utils;

    public DisplayTab(Display display, Utils utils) {
        super("display", display);

        this.display = display;
        this.utils = utils;
    }

    private Player player;



    @Override
    public ArrayList<String> tabComplete(CommandSender sender, String[] args) {
        player = (Player) sender;

        if (args.length == 1) return mainTabComplete();

        switch (args[0].toLowerCase()) {
            case "create" -> {
                if (player.hasPermission("display.create") || player.hasPermission("display.*")) {
                    if (args.length == 2) return Lists.newArrayList("block", "item", "text");
                    if (args.length == 3) return Lists.newArrayList("<id>");
                }
            }

            case "delete" -> {
                if (player.hasPermission("display.delete") || player.hasPermission("display.*")) {
                    if (args.length == 2) return Lists.newArrayList(getIDs(true));
                }
            }

            case "edit" -> {
                if (player.hasPermission("display.edit") || player.hasPermission("display.*")) {
                    if (args.length == 2) return Lists.newArrayList(getIDs(false));

                    //if (utils.isInt(args[1]) && display.displays.get(Integer.parseInt(args[1])) != null) return getEdits(args);
                }
            }

            /*case "info" -> {
                if (player.hasPermission("display.info") || player.hasPermission("display.*")) {
                    if (args.length == 2) return Lists.newArrayList(getIDs(false));
                }
            }*/

            case "tp" -> {
                if (player.hasPermission("display.tp") || player.hasPermission("display.*")) {
                    if (args.length == 2) return Lists.newArrayList(getIDs(false));
                    if (args.length == 3) return Lists.newArrayList("pos", "here", "to");
                    if (args.length == 4 && args[2].equalsIgnoreCase("pos")) return Lists.newArrayList("overworld", "the_nether", "the_end");
                    if (args.length == 5 && args[2].equalsIgnoreCase("pos")) return Lists.newArrayList("<x>");
                    if (args.length == 6 && args[2].equalsIgnoreCase("pos")) return Lists.newArrayList("<y>");
                    if (args.length == 7 && args[2].equalsIgnoreCase("pos")) return Lists.newArrayList("<z>");
                }
            }

            case "clone" -> {
                if (player.hasPermission("display.clone") || player.hasPermission("display.*")) {
                    if (args.length == 2) return Lists.newArrayList(getIDs(false));
                    if (args.length == 3) return Lists.newArrayList("<id>");
                }
            }
        }

        return Lists.newArrayList();
    }



    /*private ArrayList<String> getEdits(String[] args) {
        Entity entity = display.displays.get(Integer.parseInt(args[1]));

        StringBuilder sb1 = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            sb1.append(args[i]);
        }

        String edits = String.valueOf(sb1);
        ArrayList<String> toReturn = Lists.newArrayList();

        switch (entity.getType()) {
            case ITEM_DISPLAY, BLOCK_DISPLAY -> {
                if (!edits.contains("id:")) toReturn.add("id:");
                if (!edits.contains("glowing:")) toReturn.add("glowing:");
                if (!edits.contains("material:")) toReturn.add("material:");
                if (!edits.contains("scale:")) toReturn.add("scale:");
            }
            case TEXT_DISPLAY -> {
                if (!edits.contains("id:")) toReturn.add("id:");
                if (!edits.contains("scale:")) toReturn.add("scale:");
                if (!edits.contains("text:")) toReturn.add("text:");
            }
        }

        return toReturn;
    }
    private List<String> getBlockList() {
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
    private List<String> getItemList() {
        ArrayList<Material> arrayMaterialItem = new ArrayList<>();
        for (Material material : Material.values()) {
            if (!material.isBlock()) arrayMaterialItem.add(material);
        }
        String stringItems = Arrays.toString(arrayMaterialItem.toArray(new Material[0]));
        stringItems = stringItems.replace("[", "").replace("]", "");
        stringItems = stringItems.toLowerCase();

        return new ArrayList<>(Arrays.asList(stringItems.split(", ")));
    }*/
    private List<String> getIDs(boolean addAll) {
        Set<Integer> setIDs = display.displays.keySet();
        String stringIDs = String.valueOf(setIDs);
        stringIDs = stringIDs.replace("[", "").replace("]", "");

        if (addAll) stringIDs += ", all";

        return new ArrayList<>(Arrays.asList(stringIDs.split(", ")));
    }
    public ArrayList<String> mainTabComplete() {
        ArrayList<String> array = new ArrayList<>();

        if (player.hasPermission("display.help") || player.hasPermission("display.*")) {
            array.add("help");
        }
        if (player.hasPermission("display.reload") || player.hasPermission("display.*")) {
            array.add("reload");
        }
        if (player.hasPermission("display.create") || player.hasPermission("display.*")) {
            array.add("create");
        }
        if (player.hasPermission("display.delete") || player.hasPermission("display.*")) {
            array.add("delete");
        }
        if (player.hasPermission("display.edit") || player.hasPermission("display.*")) {
            array.add("edit");
        }
        /*if (player.hasPermission("display.info") || player.hasPermission("display.*")) {
            array.add("info");
        }
        if (player.hasPermission("display.list") || player.hasPermission("display.*")) {
            array.add("list");
        }*/
        if (player.hasPermission("display.tp") || player.hasPermission("display.*")) {
            array.add("tp");
        }
        if (player.hasPermission("display.clone") || player.hasPermission("display.*")) {
            array.add("clone");
        }

        return array;
    }
}
