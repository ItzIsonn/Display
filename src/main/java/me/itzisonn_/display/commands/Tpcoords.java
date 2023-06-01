package me.itzisonn_.display.commands;

import me.itzisonn_.display.Display;
import me.itzisonn_.display.Utils;
import me.itzisonn_.display.Config;
import me.itzisonn_.display.maincommand.DisplayCommand;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Tpcoords {
    private static final Player player = DisplayCommand.getPlayer();
    private static final HashMap<Integer, Entity> displays = Display.getDisplays();



    public static void tpCoords(int id, Location location) {
        Entity entity = displays.get(id);

        if (entity.getType() == EntityType.ITEM_DISPLAY) {
            location = new Location(location.getWorld(), location.getX() + 0.5, location.getY() + 0.5, location.getZ() + 0.5);
        }
        else if (entity.getType() == EntityType.TEXT_DISPLAY) {
            location = new Location(location.getWorld(), location.getX() + 0.5, location.getY(), location.getZ() + 0.5);
        }

        entity.teleport(location);
    }



    public static boolean checkTpcoords(String[] args) {
        if (player.hasPermission("display.command.tpcoords") || player.hasPermission("display.command.*")) {
            if (args.length <= 6) {
                if (args.length >= 2) {
                    if (Utils.isInt(args[1])) {
                        int id = Integer.parseInt(args[1]);
                        Entity entity = displays.get(id);

                        if (displays.containsKey(id) && !entity.isDead()) {
                            if (args.length >= 3) {
                                if (args[2].equalsIgnoreCase("overworld") || args[2].equalsIgnoreCase("the_nether") || args[2].equalsIgnoreCase("the_end")) {
                                    if (args.length == 6) {
                                        if (Utils.isDouble(args[3]) && Utils.isDouble(args[4]) && Utils.isDouble(args[5])) {
                                            player.sendMessage(Config.getMsg("messages.successfully.teleportedCoords", new String[]{String.valueOf(id), args[3], args[4], args[5], args[2]}));
                                            return true;
                                        }
                                        else {
                                            player.sendMessage(Config.getMsg("messages.errors.invalidCoords", null));
                                            return false;
                                        }
                                    }
                                    else {
                                        player.sendMessage(Config.getMsg("messages.errors.notFoundCoords", null));
                                        return false;
                                    }
                                }
                                else {
                                    player.sendMessage(Config.getMsg("messages.errors.unknownDimension", null));
                                    return false;
                                }
                            }
                            else {
                                player.sendMessage(Config.getMsg("messages.errors.notFoundDimension", null));
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
                player.sendMessage(Config.getMsg("messages.errors.tooManyArguments", null));
                return false;
            }
        }
        else {
            player.sendMessage(Config.getMsg("messages.errors.unknownAction", null));
            return false;
        }
    }
}