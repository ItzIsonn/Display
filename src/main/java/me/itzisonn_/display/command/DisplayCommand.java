package me.itzisonn_.display.command;

import com.google.common.collect.Lists;
import me.itzisonn_.display.*;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.*;

public class DisplayCommand extends AbstractCommand {
    private static Player player;
    private static final HashMap<Integer, Entity> displays = Storage.getDisplays();

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        else {
            sender.sendMessage(Config.getMsg("messages.errors.onlyPlayerConsole", null));
        }

        if (args.length == 0) {
            player.sendMessage(Config.getMsg("messages.errors.notFull", null));
        }

        else if (args[0].equalsIgnoreCase("help")) {
            if (Checks.checkInfo(args)) {
                Actions.sendHelp();
            }
        }

        else if (args[0].equalsIgnoreCase("create")) {
            if (Checks.checkCreate(args)) {
                if (args[1].equalsIgnoreCase("block")) {
                    Actions.createObject(Integer.parseInt(args[2]), EntityType.BLOCK_DISPLAY);
                }
                else if (args[1].equalsIgnoreCase("item")) {
                    Actions.createObject(Integer.parseInt(args[2]), EntityType.ITEM_DISPLAY);
                }
            }
        }

        else if (args[0].equalsIgnoreCase("delete")) {
            if (Checks.checkDelete(args)) {
                Actions.deleteObject(Integer.parseInt(args[1]));
            }
        }

        else if (args[0].equalsIgnoreCase("edit")) {
            if (Checks.checkEdit(args)) {
                Actions.editObject(Integer.parseInt(args[1]), args);
            }
        }

        else if (args[0].equalsIgnoreCase("tphere")) {
            if (Checks.checkTphere(args)) {
                Actions.tpHere(Integer.parseInt(args[1]));
            }
        }

        else if (args[0].equalsIgnoreCase("tpcoords")) {
            if (Checks.checkTpcoords(args)) {
                Actions.tpCoords(Integer.parseInt(args[1]), args[2], Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]));
            }
        }

        else if (args[0].equalsIgnoreCase("tpto")) {
            if (Checks.checkTpto(args)) {
                Actions.tpTo(Integer.parseInt(args[1]));
            }
        }

        else if (args[0].equalsIgnoreCase("changeid")) {
            if (Checks.checkChangeID(args)) {
                Actions.changeID(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            }
        }

        else {
            player.sendMessage(Config.getMsg("messages.errors.unknownAction", null));
        }
    }

    @Override
    public ArrayList<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) return Lists.newArrayList("help", "create", "delete", "edit", "tphere", "tpcoords", "tpto", "changeid");

        if (args.length == 2 && args[0].equalsIgnoreCase("create")) return Lists.newArrayList("block", "item");
        if (args.length == 3 && args[0].equalsIgnoreCase("create")) return Lists.newArrayList("<id>");

        if (args.length == 2 && args[0].equalsIgnoreCase("delete")) return Lists.newArrayList(getIDs());



        if (args.length == 2 && args[0].equalsIgnoreCase("edit")) return Lists.newArrayList(getIDs());
        if (Checks.isInt(args[1]) && displays.get(Integer.parseInt(args[1])) != null) {
            Entity entity = displays.get(Integer.parseInt(args[1]));
            EntityType entityType = entity.getType();

            if (args.length == 3 && args[0].equalsIgnoreCase("edit") && entityType == EntityType.BLOCK_DISPLAY) return Lists.newArrayList("blocktype", "glowing");
            if (args.length == 3 && args[0].equalsIgnoreCase("edit") && entityType == EntityType.ITEM_DISPLAY) return Lists.newArrayList("itemtype", "glowing");

            if (args.length == 4 && args[0].equalsIgnoreCase("edit") && entityType == EntityType.BLOCK_DISPLAY && args[2].equalsIgnoreCase("blocktype")) return Lists.newArrayList(getBlocks());
            if (args.length == 4 && args[0].equalsIgnoreCase("edit") && entityType == EntityType.ITEM_DISPLAY && args[2].equalsIgnoreCase("itemtype")) return Lists.newArrayList(getItems());
        }



        if (args.length == 2 && args[0].equalsIgnoreCase("tphere")) return Lists.newArrayList(getIDs());

        if (args.length == 2 && args[0].equalsIgnoreCase("tpcoords")) return Lists.newArrayList(getIDs());
        if (args.length == 3 && args[0].equalsIgnoreCase("tpcoords")) return Lists.newArrayList("overworld", "the_nether", "the_end");
        if (args.length == 4 && args[0].equalsIgnoreCase("tpcoords")) return Lists.newArrayList("<x>");
        if (args.length == 5 && args[0].equalsIgnoreCase("tpcoords")) return Lists.newArrayList("<y>");
        if (args.length == 6 && args[0].equalsIgnoreCase("tpcoords")) return Lists.newArrayList("<z>");

        if (args.length == 2 && args[0].equalsIgnoreCase("tpto")) return Lists.newArrayList(getIDs());

        if (args.length == 2 && args[0].equalsIgnoreCase("changeid")) return Lists.newArrayList(getIDs());
        if (args.length == 3 && args[0].equalsIgnoreCase("changeid")) return Lists.newArrayList("<id>");

        return Lists.newArrayList();
    }



    public DisplayCommand() {
        super("display");
    }
    public static Player getPlayer() {
        return player;
    }
    private static List<String> getBlocks() {
        ArrayList<Material> arrayMaterialBlock = new ArrayList<>();

        for (Material material : Material.values()) {
            if (material.isBlock()) arrayMaterialBlock.add(material);
        }

        String stringBlocks = Arrays.toString(arrayMaterialBlock.toArray(new Material[0]));
        stringBlocks = stringBlocks.replace("[", "").replace("]", "");
        stringBlocks = stringBlocks.toLowerCase();
        List<String> blocklist = new ArrayList<>(Arrays.asList(stringBlocks.split(", ")));
        blocklist.remove("air");

        return blocklist;
    }
    private static List<String> getItems() {
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
}