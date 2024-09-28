package me.itzisonn_.display.command;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DisplayTab implements TabCompleter {
    private final DisplayPlugin plugin;

    public DisplayTab(DisplayPlugin plugin) {
        PluginCommand pluginCommand = plugin.getCommand("display");
        if (pluginCommand != null) pluginCommand.setTabCompleter(this);

        this.plugin = plugin;
    }

    public ArrayList<String> tabComplete(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 1) {
            ArrayList<String> arrayList = new ArrayList<>();

            if (player.hasPermission("display.help") || player.hasPermission("display.*")) arrayList.add("help");
            if (player.hasPermission("display.reload") || player.hasPermission("display.*")) arrayList.add("reload");
            if (player.hasPermission("display.create") || player.hasPermission("display.*")) arrayList.add("create");
            if (player.hasPermission("display.load") || player.hasPermission("display.*")) arrayList.add("load");
            if (player.hasPermission("display.delete") || player.hasPermission("display.*")) arrayList.add("delete");
            if (player.hasPermission("display.list") || player.hasPermission("display.*")) arrayList.add("list");
            if (player.hasPermission("display.edit") || player.hasPermission("display.*")) arrayList.add("edit");
            if (player.hasPermission("display.tp") || player.hasPermission("display.*")) arrayList.add("tp");

            return arrayList;
        }

        switch (args[0].toLowerCase()) {
            case "create" -> {
                if (player.hasPermission("display.create") || player.hasPermission("display.*")) {
                    if (args.length == 2) return Lists.newArrayList("block", "item", "text", "clone");
                    if (args.length == 3) return Lists.newArrayList("<id>");
                    if (args.length == 4 && args[1].equalsIgnoreCase("clone")) return getIDs();
                }
            }

            case "load" -> {
                if (player.hasPermission("display.load") || player.hasPermission("display.*")) {
                    if (args.length == 2) {
                        ArrayList<String> list = Lists.newArrayList("<uuid>");
                        Display target = plugin.getUtils().getTarget(player, 10);
                        if (target != null) list.add(target.getUniqueId().toString());
                        return list;
                    }
                    if (args.length == 3) return Lists.newArrayList("<id>");
                }
            }

            case "delete" -> {
                if (player.hasPermission("display.delete") || player.hasPermission("display.*")) {
                    if (args.length == 2) {
                        ArrayList<String> list = getIDs();
                        list.add("*");
                        return list;
                    }
                }
            }

            case "edit" -> {
                if (player.hasPermission("display.edit") || player.hasPermission("display.*")) {
                    if (args.length == 2) return getIDs();

                    EntityType type;
                    try {
                        Display entity = plugin.getDisplaysMap().get(Integer.parseInt(args[1]));
                        if (entity == null) return new ArrayList<>();
                        type = entity.getType();
                    }
                    catch (NumberFormatException ignore) {
                        return new ArrayList<>();
                    }

                    if (args.length == 3) {
                        ArrayList<String> list = Lists.newArrayList("id", "scale", "billboard", "brightness", "shadow", "view_range");
                        switch (type) {
                            case BLOCK_DISPLAY -> list.addAll(Lists.newArrayList("material", "glowing"));
                            case ITEM_DISPLAY -> list.addAll(Lists.newArrayList("material", "glowing", "display_transform"));
                            case TEXT_DISPLAY -> list.addAll(Lists.newArrayList("text", "alignment", "background", "line_width", "see_through", "text_opacity"));
                        }
                        return list;
                    }

                    if (args.length == 4) {
                        return switch (args[2]) {
                            case "id" -> Lists.newArrayList("<id>");
                            case "scale" -> Lists.newArrayList("<x>,<y>,<z>", "?");
                            case "billboard" -> {
                                ArrayList<String> list = plugin.getUtils().getBillBoardList();
                                list.add("?");
                                yield list;
                            }
                            case "brightness" -> Lists.newArrayList("<block>,<sky>", "?");
                            case "shadow" -> Lists.newArrayList("<radius>,<strength>", "?");
                            case "view_range" -> Lists.newArrayList("<range>", "?");
                            case "material" -> {
                                if (type == EntityType.TEXT_DISPLAY) yield new ArrayList<>();

                                ArrayList<String> materialList;
                                if (type == EntityType.BLOCK_DISPLAY) materialList = plugin.getUtils().getBlockList();
                                else materialList = plugin.getUtils().getItemList();

                                materialList.add("?");
                                yield materialList;
                            }
                            case "glowing" -> {
                                if (type == EntityType.TEXT_DISPLAY) yield new ArrayList<>();
                                yield Lists.newArrayList("<r>,<g>,<b>", "off", "on", "?");
                            }
                            case "display_transform" -> {
                                if (type != EntityType.ITEM_DISPLAY) yield new ArrayList<>();
                                ArrayList<String> list = plugin.getUtils().getDisplayTransform();
                                list.add("?");
                                yield list;
                            }
                            case "text" -> {
                                if (type != EntityType.TEXT_DISPLAY) yield new ArrayList<>();
                                yield Lists.newArrayList("on", "off", "?");
                            }
                            case "alignment" -> {
                                if (type != EntityType.TEXT_DISPLAY) yield new ArrayList<>();
                                ArrayList<String> list = plugin.getUtils().getAlignment();
                                list.add("?");
                                yield list;
                            }
                            case "background" -> {
                                if (type != EntityType.TEXT_DISPLAY) yield new ArrayList<>();
                                yield Lists.newArrayList("<r>,<g>,<b>", "?");
                            }
                            case "line_width" -> {
                                if (type != EntityType.TEXT_DISPLAY) yield new ArrayList<>();
                                yield Lists.newArrayList("<width>", "?");
                            }
                            case "see_through" -> {
                                if (type != EntityType.TEXT_DISPLAY) yield new ArrayList<>();
                                yield Lists.newArrayList("true", "false", "?");
                            }
                            case "text_opacity" -> {
                                if (type != EntityType.TEXT_DISPLAY) yield new ArrayList<>();
                                yield Lists.newArrayList("<opactiy>", "?");
                            }
                            default -> new ArrayList<>();
                        };
                    }
                }
            }
            case "tp" -> {
                if (player.hasPermission("display.tp") || player.hasPermission("display.*")) {
                    if (args.length == 2) return getIDs();
                    if (args.length == 3) return Lists.newArrayList("pos", "here", "to");

                    if (args[2].equalsIgnoreCase("pos")) {
                        if (args.length == 4) {
                            ArrayList<String> worlds = new ArrayList<>();
                            Bukkit.getWorlds().forEach(world -> worlds.add(world.getName()));
                            return worlds;
                        }
                        if (args.length == 5) return Lists.newArrayList("<x>");
                        if (args.length == 6) return Lists.newArrayList("<y>");
                        if (args.length == 7) return Lists.newArrayList("<z>");
                    }
                }
            }
        }

        return Lists.newArrayList();
    }



    private ArrayList<String> getIDs() {
        ArrayList<String> ids = new ArrayList<>();
        for (int id : plugin.getDisplaysMap().keySet()) {
            ids.add(String.valueOf(id));
        }

        return ids;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String str, @NotNull String[] args) {
        ArrayList<String> tabComplete = tabComplete(sender, args);

        String last = args[args.length - 1];
        List<String> result = new ArrayList<>();

        for (String arg : tabComplete) {
            if (arg.toLowerCase().startsWith(last.toLowerCase())) result.add(arg);
        }

        return result;
    }
}