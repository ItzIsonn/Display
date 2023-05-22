package me.itzisonn_.display;

import me.itzisonn_.display.command.DisplayCommand;
import org.bukkit.Material;
import org.bukkit.entity.*;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

public class Checks {
    private static final Player player = DisplayCommand.getPlayer();
    private static final HashMap<Integer, Entity> displays = Storage.getDisplays();

    public static boolean checkInfo(String[] args) {
        if (args.length == 1) {
            return true;
        }
        else {
            player.sendMessage(Config.getMsg("messages.all.tooManyArguments", null));
            return false;
        }
    }

    public static boolean checkCreate(String[] args) {
        if (args.length < 4) {
            if (args.length >= 2) {
                if (args[1].equalsIgnoreCase("block") || args[1].equalsIgnoreCase("item")) {
                    if (args.length == 3) {
                        if (isInt(args[2])) {
                            int displayID = Integer.parseInt(args[2]);

                            if (!(displays.containsKey(displayID))) {
                                player.sendMessage(Config.getMsg("messages.createCommand.successfullyCreated", String.valueOf(displayID)));
                                return true;
                            }
                            else {
                                player.sendMessage(Config.getMsg("messages.createCommand.idAlreadyInUse", String.valueOf(displayID)));
                                return false;
                            }
                        }
                        else {
                            player.sendMessage(Config.getMsg("messages.createCommand.invalidId", null));
                            return false;
                        }
                    }
                    else {
                        player.sendMessage(Config.getMsg("messages.createCommand.notFoundId", null));
                        return false;
                    }
                }
                else {
                    player.sendMessage(Config.getMsg("messages.createCommand.unknownObjectType", null));
                    return false;
                }
            }
            else {
                player.sendMessage(Config.getMsg("messages.createCommand.notFoundObjectType", null));
                return false;
            }
        }
        else {
            player.sendMessage(Config.getMsg("messages.all.tooManyArguments", null));
            return false;
        }
    }

    public static boolean checkDelete(String[] args) {
        if (args.length < 3) {
            if (args.length == 2) {
                if (Checks.isInt(args[1])) {
                    int displayID = Integer.parseInt(args[1]);
                    Entity entity = displays.get(displayID);

                    if (displays.containsKey(displayID) && !entity.isDead()) {
                        player.sendMessage(Config.getMsg("messages.other.successfullyDeleted", String.valueOf(displayID)));
                        return true;
                    }
                    else {
                        player.sendMessage(Config.getMsg("messages.other.idDoesNotExist", String.valueOf(displayID)));
                        return false;
                    }
                }
                else {
                    player.sendMessage(Config.getMsg("messages.other.invalidId", null));
                    return false;
                }
            }
            else {
                player.sendMessage(Config.getMsg("messages.other.notFoundId", null));
                return false;
            }
        }
        else {
            player.sendMessage(Config.getMsg("messages.all.tooManyArguments", null));
            return false;
        }
    }

    public static boolean checkEdit(String[] args) {
        if (args.length < 5) {
            if (args.length >= 2) {
                if (Checks.isInt(args[1])) {
                    int displayID = Integer.parseInt(args[1]);
                    Entity entity = displays.get(displayID);

                    if (displays.containsKey(displayID) && !entity.isDead()) {
                        if (args.length >= 3) {
                            if (entity.getType() == EntityType.BLOCK_DISPLAY) {
                                if (args[2].equalsIgnoreCase("blocktype")) {
                                    if (args.length == 4) {
                                        if (Material.getMaterial(args[3].toUpperCase()) != null && Objects.requireNonNull(Material.getMaterial(args[3].toUpperCase())).isBlock() && Material.getMaterial(args[3].toUpperCase()) != Material.AIR) {
                                            edittype = args[2];
                                            value = args[3];
                                            player.sendMessage(Config.getMsg("messages.editCommand.successfullyEdited", String.valueOf(displayID)));
                                            return true;
                                        }
                                        else {
                                            player.sendMessage(Config.getMsg("messages.editCommand.unknownBlock", null));
                                            return false;
                                        }
                                    }
                                    else {
                                        player.sendMessage(Config.getMsg("messages.editCommand.notFoundBlock", null));
                                        return false;
                                    }
                                }
                                else if (!args[2].equalsIgnoreCase("glowing")) {
                                    player.sendMessage(Config.getMsg("messages.editCommand.unknownEditType", null));
                                    return false;
                                }
                            }

                            if (entity.getType() == EntityType.ITEM_DISPLAY) {
                                if (args[2].equalsIgnoreCase("itemtype")) {
                                    if (args.length == 4) {
                                        if (Material.getMaterial(args[3].toUpperCase()) != null && !Objects.requireNonNull(Material.getMaterial(args[3].toUpperCase())).isBlock()) {
                                            edittype = args[2];
                                            value = args[3];
                                            player.sendMessage(Config.getMsg("messages.editCommand.successfullyEdited", String.valueOf(displayID)));
                                            return true;
                                        }
                                        else {
                                            player.sendMessage(Config.getMsg("messages.editCommand.unknownItem", null));
                                            return false;
                                        }
                                    }
                                    else {
                                        player.sendMessage(Config.getMsg("messages.editCommand.notFoundItem", null));
                                        return false;
                                    }
                                }
                                else if (!args[2].equalsIgnoreCase("glowing")) {
                                    player.sendMessage(Config.getMsg("messages.editCommand.unknownEditType", null));
                                    return false;
                                }
                            }
                        }
                        else {
                            player.sendMessage(Config.getMsg("messages.editCommand.notFoundEditType", null));
                            return false;

                        }

                        if (args[2].equalsIgnoreCase("glowing")) {
                            if (args.length == 3) {
                                edittype = args[2];
                                value = String.valueOf(!entity.isGlowing());
                                player.sendMessage(Config.getMsg("messages.editCommand.successfullyEdited", String.valueOf(displayID)));
                                return true;
                            }
                            else {
                                player.sendMessage(Config.getMsg("messages.all.tooManyArguments", null));
                                return false;
                            }
                        }
                    }
                    else {
                        player.sendMessage(Config.getMsg("messages.other.idDoesNotExist", String.valueOf(displayID)));
                        return false;
                    }
                }
                else {
                    player.sendMessage(Config.getMsg("messages.other.invalidId", null));
                    return false;
                }
            }
            else {
                player.sendMessage(Config.getMsg("messages.other.notFoundId", null));
                return false;
            }
        }
        else {
            player.sendMessage(Config.getMsg("messages.all.tooManyArguments", null));
            return false;
        }
        return false;
    }

    public static boolean checkTphere(String[] args) {
        if (args.length <= 2) {
            if (args.length == 2) {
                if (Checks.isInt(args[1])) {
                    int displayID = Integer.parseInt(args[1]);
                    Entity entity = displays.get(displayID);

                    if (displays.containsKey(displayID) && !entity.isDead()) {
                        player.sendMessage(Config.getMsg("messages.other.successfullyTphere", String.valueOf(displayID)));
                        return true;
                    }
                    else {
                        player.sendMessage(Config.getMsg("messages.other.idDoesNotExist", String.valueOf(displayID)));
                        return false;
                    }
                }
                else {
                    player.sendMessage(Config.getMsg("messages.other.invalidId", null));
                    return false;
                }
            }
            else {
                player.sendMessage(Config.getMsg("messages.other.notFoundId", null));
                return false;
            }
        }
        else {
            player.sendMessage(Config.getMsg("messages.all.tooManyArguments", null));
            return false;
        }
    }

    public static boolean checkTpcoords(String[] args) {
        if (args.length <= 6) {
            if (args.length >= 2) {
                if (Checks.isInt(args[1])) {
                    int displayID = Integer.parseInt(args[1]);
                    Entity entity = displays.get(displayID);

                    if (displays.containsKey(displayID) && !entity.isDead()) {
                        if (args.length >= 3) {
                            if (args[2].equalsIgnoreCase("overworld") || args[2].equalsIgnoreCase("the_nether") || args[2].equalsIgnoreCase("the_end")) {
                                if (args.length == 6) {
                                    if (isDouble(args[3]) && isDouble(args[4]) && isDouble(args[5])) {
                                        x = args[3];
                                        y = args[4];
                                        z = args[5];
                                        world = args[2];
                                        player.sendMessage(Config.getMsg("messages.tpcoordsCommand.successfullyTpcoords", String.valueOf(displayID)));
                                        return true;
                                    }
                                    else {
                                        player.sendMessage(Config.getMsg("messages.tpcoordsCommand.invalidCoords", null));
                                        return false;
                                    }
                                }
                                else {
                                    player.sendMessage(Config.getMsg("messages.tpcoordsCommand.notFoundCoords", null));
                                    return false;
                                }
                            }
                            else {
                                player.sendMessage(Config.getMsg("messages.tpcoordsCommand.unknownDimension", null));
                                return false;
                            }
                        }
                        else {
                            player.sendMessage(Config.getMsg("messages.tpcoordsCommand.notFoundDimension", null));
                            return false;
                        }
                    }
                    else {
                        player.sendMessage(Config.getMsg("messages.other.idDoesNotExist", String.valueOf(displayID)));
                        return false;
                    }
                }
                else {
                    player.sendMessage(Config.getMsg("messages.other.invalidId", null));
                    return false;
                }
            }
            else {
                player.sendMessage(Config.getMsg("messages.other.notFoundId", null));
                return false;
            }
        }
        else {
            player.sendMessage(Config.getMsg("messages.all.tooManyArguments", null));
            return false;
        }
    }

    public static boolean checkTpto(String[] args) {
        if (args.length <= 2) {
            if (args.length == 2) {
                if (Checks.isInt(args[1])) {
                    int displayID = Integer.parseInt(args[1]);
                    Entity entity = displays.get(displayID);

                    if (displays.containsKey(displayID) && !entity.isDead()) {
                        player.sendMessage(Config.getMsg("messages.other.successfullyTpto", String.valueOf(displayID)));
                        return true;
                    }
                    else {
                        player.sendMessage(Config.getMsg("messages.other.idDoesNotExist", String.valueOf(displayID)));
                        return false;
                    }
                }
                else {
                    player.sendMessage(Config.getMsg("messages.other.invalidId", null));
                    return false;
                }
            }
            else {
                player.sendMessage(Config.getMsg("messages.other.notFoundId", null));
                return false;
            }
        }
        else {
            player.sendMessage(Config.getMsg("messages.all.tooManyArguments", null));
            return false;
        }
    }



    private static String edittype;
    private static String value;
    private static String x;
    private static String y;
    private static String z;
    private static String world;

    public static String getEdittype() {
        return edittype;
    }
    public static String getValue() {
        return value;
    }
    public static String getX() {
        return x;
    }
    public static String getY() {
        return y;
    }
    public static String getZ() {
        return z;
    }
    public static String getWorld() {
        return world;
    }

    public static boolean isInt(String str) { return !str.equals("0") && Pattern.matches("^\\d+$", str); }
    public static boolean isDouble(String str) { return str != null && str.matches("[-+]?\\d*\\.?\\d+"); }
}