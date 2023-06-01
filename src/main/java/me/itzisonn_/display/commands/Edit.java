package me.itzisonn_.display.commands;

import me.itzisonn_.display.Utils;
import me.itzisonn_.display.Config;
import me.itzisonn_.display.maincommand.DisplayCommand;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Objects;

public class Edit {
    private static final Player player = DisplayCommand.getPlayer();
    private static final HashMap<Integer, Entity> displays = me.itzisonn_.display.Display.getDisplays();



    public static void editMaterial(int id, Material material) {
        Entity entity = displays.get(id);
        EntityType type = entity.getType();

        if (type == EntityType.BLOCK_DISPLAY) {
            ((BlockDisplay) entity).setBlock(Bukkit.createBlockData(material));
        }
        else if (type == EntityType.ITEM_DISPLAY) {
            ((ItemDisplay) entity).setItemStack(new ItemStack(material));
        }
    }

    public static void editGlowing(int id) {
        Entity entity = displays.get(id);

        boolean glow = !entity.isGlowing();
        entity.setGlowing(glow);
    }

    public static void editScale(int id, float x, float y, float z) {
        Entity entity = displays.get(id);
        Transformation transformation = new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(x, y, z), new Quaternionf());
        ((Display) entity).setTransformation(transformation);
    }

    public static void editText(int id, String editType, int lineNumber, String text) {
        Entity entity = displays.get(id);

        if (editType.equalsIgnoreCase("addline")) {
            String[] allLines = MiniMessage.miniMessage().serialize(((TextDisplay) entity).text()).split("\n");
            StringBuilder linesBefore = new StringBuilder();
            StringBuilder linesAfter = new StringBuilder();

            for (int i = 0; i < lineNumber - 1; i++) {
                linesBefore.append(allLines[i]).append("\n");
            }
            for (int i = lineNumber - 1; i < allLines.length; i++) {
                linesAfter.append("\n").append(allLines[i]);
            }

            String output = linesBefore + text + linesAfter;

            NamespacedKey namespacedKey = new NamespacedKey(me.itzisonn_.display.Display.getInstance(), "displayText");
            PersistentDataContainer data = entity.getPersistentDataContainer();
            data.set(namespacedKey, PersistentDataType.STRING, output);
        }
        else if (editType.equalsIgnoreCase("setline")) {
            String[] allLines = MiniMessage.miniMessage().serialize(((TextDisplay) entity).text()).split("\n");
            StringBuilder linesBefore = new StringBuilder();
            StringBuilder linesAfter = new StringBuilder();

            for (int i = 0; i < lineNumber - 1; i++) {
                linesBefore.append(allLines[i]).append("\n");
            }
            for (int i = lineNumber; i < allLines.length; i++) {
                linesAfter.append("\n").append(allLines[i]);
            }

            String output = linesBefore + text + linesAfter;

            NamespacedKey namespacedKey = new NamespacedKey(me.itzisonn_.display.Display.getInstance(), "displayText");
            PersistentDataContainer data = entity.getPersistentDataContainer();
            data.set(namespacedKey, PersistentDataType.STRING, output);
        }
        else if (editType.equalsIgnoreCase("removeline")) {
            String[] allLines = MiniMessage.miniMessage().serialize(((TextDisplay) entity).text()).split("\n");
            StringBuilder linesBefore = new StringBuilder();
            StringBuilder linesAfter = new StringBuilder();

            for (int i = 0; i < lineNumber - 1; i++) {
                linesBefore.append(allLines[i]).append("\n");
            }
            for (int i = lineNumber; i < allLines.length; i++) {
                linesAfter.append("\n").append(allLines[i]);
            }

            if (allLines.length == lineNumber) {
                linesBefore.setLength(linesBefore.length() - 1);
            }
            else {
                linesAfter.setLength(linesAfter.length() - 1);
            }

            String output = String.valueOf(linesBefore) + linesAfter;

            NamespacedKey namespacedKey = new NamespacedKey(me.itzisonn_.display.Display.getInstance(), "displayText");
            PersistentDataContainer data = entity.getPersistentDataContainer();
            data.set(namespacedKey, PersistentDataType.STRING, output);
        }
    }



    public static boolean checkEditMain(String[] args) {
        if (player.hasPermission("display.command.edit") || player.hasPermission("display.command.*")) {
            if (args.length >= 2) {
                if (Utils.isInt(args[1])) {
                    int id = Integer.parseInt(args[1]);
                    Entity entity = displays.get(id);
                    EntityType type = entity.getType();

                    if (displays.containsKey(id) && !entity.isDead()) {
                        if (args.length >= 3) {
                            if (type == EntityType.BLOCK_DISPLAY) {
                                return checkEditBlock(args, id, entity);
                            }

                            if (entity.getType() == EntityType.ITEM_DISPLAY) {
                                return checkEditItem(args, id, entity);
                            }

                            if (entity.getType() == EntityType.TEXT_DISPLAY) {
                                return checkEditText(args, id, entity);
                            }
                        }
                        else {
                            player.sendMessage(Config.getMsg("messages.errors.notFoundEditType", null));
                            return false;

                        }
                    }
                    else {
                        player.sendMessage(Config.getMsg("messages.errors.idDoesNotExist", new String[]{String.valueOf(id)}));
                        return false;
                    }
                }
                else {
                    player.sendMessage(Config.getMsg("messages.errors.invalidId", null));
                    return false;
                }
            }
            else {
                player.sendMessage(Config.getMsg("messages.errors.notFoundId", null));
                return false;
            }
        }
        else {
            player.sendMessage(Config.getMsg("messages.errors.unknownAction", null));
            return false;
        }

        return false;
    }

    private static boolean checkEditBlock(String[] args, int id, Entity entity) {
        if (args[2].equalsIgnoreCase("blocktype")) {
            if (args.length < 5) {
                if (args.length == 4) {
                    if (Material.getMaterial(args[3].toUpperCase()) != null && Objects.requireNonNull(Material.getMaterial(args[3].toUpperCase())).isBlock() && Material.getMaterial(args[3].toUpperCase()) != Material.AIR && Material.getMaterial(args[3].toUpperCase()) != Material.CAVE_AIR) {
                        player.sendMessage(Config.getMsg("messages.successfully.edited", new String[]{String.valueOf(id), args[2], args[3]}));
                        return true;
                    }
                    else {
                        player.sendMessage(Config.getMsg("messages.errors.unknownBlock", null));
                        return false;
                    }
                }
                else {
                    player.sendMessage(Config.getMsg("messages.errors.notFoundBlock", null));
                    return false;
                }
            }
            else {
                player.sendMessage(Config.getMsg("messages.errors.tooManyArguments", null));
                return false;
            }
        }

        else if (args[2].equalsIgnoreCase("glowing")) {
            if (args.length == 3) {
                player.sendMessage(Config.getMsg("messages.successfully.edited", new String[]{String.valueOf(id), args[2], String.valueOf(!entity.isGlowing())}));
                return true;
            }
            else {
                player.sendMessage(Config.getMsg("messages.errors.tooManyArguments", null));
                return false;
            }
        }

        else if (args[2].equalsIgnoreCase("scale")) {
            if (args.length <= 6) {
                if (args.length == 6) {
                    if (Utils.isFloat(args[3]) && Utils.isFloat(args[4]) && Utils.isFloat(args[5])) {
                        player.sendMessage(Config.getMsg("messages.successfully.editedScale", new String[]{String.valueOf(id), args[3], args[4], args[5]}));
                        return true;
                    }
                    else {
                        player.sendMessage(Config.getMsg("messages.errors.invalidScales", null));
                        return false;
                    }
                }
                else {
                    player.sendMessage(Config.getMsg("messages.errors.notFoundScales", null));
                    return false;
                }
            }
            else {
                player.sendMessage(Config.getMsg("messages.errors.tooManyArguments", null));
                return false;
            }
        }
        else {
            player.sendMessage(Config.getMsg("messages.errors.unknownEditType", null));
            return false;
        }
    }

    private static boolean checkEditItem(String[] args, int id, Entity entity) {
        if (args[2].equalsIgnoreCase("itemtype")) {
            if (args.length < 5) {
                if (args.length == 4) {
                    if (Material.getMaterial(args[3].toUpperCase()) != null && !Objects.requireNonNull(Material.getMaterial(args[3].toUpperCase())).isBlock()) {
                        player.sendMessage(Config.getMsg("messages.successfully.edited", new String[]{String.valueOf(id), args[2], args[3]}));
                        return true;
                    }
                    else {
                        player.sendMessage(Config.getMsg("messages.errors.unknownItem", null));
                        return false;
                    }
                }
                else {
                    player.sendMessage(Config.getMsg("messages.errors.notFoundItem", null));
                    return false;
                }
            }
            else {
                player.sendMessage(Config.getMsg("messages.errors.tooManyArguments", null));
                return false;
            }
        }

        else if (args[2].equalsIgnoreCase("glowing")) {
            if (args.length == 3) {
                player.sendMessage(Config.getMsg("messages.successfully.edited", new String[]{String.valueOf(id), args[2], String.valueOf(!entity.isGlowing())}));
                return true;
            }
            else {
                player.sendMessage(Config.getMsg("messages.errors.tooManyArguments", null));
                return false;
            }
        }

        else if (args[2].equalsIgnoreCase("scale")) {
            if (args.length <= 6) {
                if (args.length == 6) {
                    if (Utils.isFloat(args[3]) && Utils.isFloat(args[4]) && Utils.isFloat(args[5])) {
                        player.sendMessage(Config.getMsg("messages.successfully.editedScale", new String[]{String.valueOf(id), args[3], args[4], args[5]}));
                        return true;
                    }
                    else {
                        player.sendMessage(Config.getMsg("messages.errors.invalidScales", null));
                        return false;
                    }
                }
                else {
                    player.sendMessage(Config.getMsg("messages.errors.notFoundScales", null));
                    return false;
                }
            }
            else {
                player.sendMessage(Config.getMsg("messages.errors.tooManyArguments", null));
                return false;
            }
        }
        else {
            player.sendMessage(Config.getMsg("messages.errors.unknownEditType", null));
            return false;
        }
    }

    private static boolean checkEditText(String[] args, int id, Entity entity) {
        if (args[2].equalsIgnoreCase("addline")) {
            String[] allLines = MiniMessage.miniMessage().serialize(((TextDisplay) entity).text()).split("\n");

            if (args.length >= 4) {
                if (Utils.isInt(args[3])) {
                    if (allLines.length >= Integer.parseInt(args[3]) - 1) {
                        if (args.length >= 5) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 4; i < args.length; i++) {
                                sb.append(args[i]).append(" ");
                            }
                            sb.setLength(sb.length() - 1);
                            String text = "<reset>" + sb;
                            text = text.replaceAll("<br\\s*?>", "");

                            player.sendMessage(Config.getMsg("messages.successfully.editedAddline", new String[]{String.valueOf(id), args[3], text}));
                            return true;
                        }
                        else {
                            player.sendMessage(Config.getMsg("messages.errors.notFoundText", null));
                            return false;
                        }
                    }
                    else {
                        player.sendMessage(Config.getMsg("messages.errors.unknownLineNumber", null));
                        return false;
                    }
                }
                else {
                    player.sendMessage(Config.getMsg("messages.errors.invalidLineNumber", null));
                    return false;
                }
            }
            else {
                player.sendMessage(Config.getMsg("messages.errors.notFoundLineNumber", null));
                return false;
            }
        }

        else if (args[2].equalsIgnoreCase("setline")) {
            String[] allLines = MiniMessage.miniMessage().serialize(((TextDisplay) entity).text()).split("\n");

            if (args.length >= 4) {
                if (Utils.isInt(args[3])) {
                    if (allLines.length >= Integer.parseInt(args[3])) {
                        if (args.length >= 5) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 4; i < args.length; i++) {
                                sb.append(args[i]).append(" ");
                            }
                            sb.setLength(sb.length() - 1);
                            String text = "<reset>" + sb;
                            text = text.replaceAll("<br\\s*?>", "");

                            player.sendMessage(Config.getMsg("messages.successfully.editedSetline", new String[]{String.valueOf(id), args[3], text}));
                            return true;
                        }
                        else {
                            player.sendMessage(Config.getMsg("messages.errors.notFoundText", null));
                            return false;
                        }
                    }
                    else {
                        player.sendMessage(Config.getMsg("messages.errors.unknownLineNumber", null));
                        return false;
                    }
                }
                else {
                    player.sendMessage(Config.getMsg("messages.errors.invalidLineNumber", null));
                    return false;
                }
            }
            else {
                player.sendMessage(Config.getMsg("messages.errors.notFoundLineNumber", null));
                return false;
            }
        }

        else if (args[2].equalsIgnoreCase("removeline")) {
            String[] allLines = MiniMessage.miniMessage().serialize(((TextDisplay) entity).text()).split("\n");

            if (args.length < 5) {
                if (args.length == 4) {
                    if (Utils.isInt(args[3])) {
                        if (allLines.length >= Integer.parseInt(args[3])) {
                            if (allLines.length != 1) {
                                player.sendMessage(Config.getMsg("messages.successfully.editedRemoveline", new String[]{String.valueOf(id), args[3]}));
                                return true;
                            }
                            else {
                                player.sendMessage(Config.getMsg("messages.errors.oneLine", null));
                                return false;
                            }
                        }
                        else {
                            player.sendMessage(Config.getMsg("messages.errors.unknownLineNumber", null));
                            return false;
                        }
                    }
                    else {
                        player.sendMessage(Config.getMsg("messages.errors.invalidLineNumber", null));
                        return false;
                    }
                }
                else {
                    player.sendMessage(Config.getMsg("messages.errors.notFoundLineNumber", null));
                    return false;
                }
            }
            else {
                player.sendMessage(Config.getMsg("messages.errors.tooManyArguments", null));
                return false;
            }
        }

        else if (args[2].equalsIgnoreCase("scale")) {
            if (args.length <= 6) {
                if (args.length == 6) {
                    if (Utils.isFloat(args[3]) && Utils.isFloat(args[4]) && Utils.isFloat(args[5])) {
                        player.sendMessage(Config.getMsg("messages.successfully.editedScale", new String[]{String.valueOf(id), args[3], args[4], args[5]}));
                        return true;
                    }
                    else {
                        player.sendMessage(Config.getMsg("messages.errors.invalidScales", null));
                        return false;
                    }
                }
                else {
                    player.sendMessage(Config.getMsg("messages.errors.notFoundScales", null));
                    return false;
                }
            }
            else {
                player.sendMessage(Config.getMsg("messages.errors.tooManyArguments", null));
                return false;
            }
        }
        else {
            player.sendMessage(Config.getMsg("messages.errors.unknownEditType", null));
            return false;
        }
    }
}