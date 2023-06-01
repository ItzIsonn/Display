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

public class Tpto {
    private static final Player player = DisplayCommand.getPlayer();
    private static final HashMap<Integer, Entity> displays = Display.getDisplays();



    public static void tpTo(int id) {
        Entity entity = displays.get(id);
        Location location = entity.getLocation();

        if (entity.getType() == EntityType.BLOCK_DISPLAY) {
            location = new Location(location.getWorld(), location.getX() + 0.5, location.getY(), location.getZ() + 0.5);
        }
        else if (entity.getType() == EntityType.ITEM_DISPLAY) {
            location = new Location(location.getWorld(), location.getX(), location.getY() - 0.5, location.getZ());
        }

        player.teleport(location);
    }



    public static boolean checkTpto(String[] args) {
        if (player.hasPermission("display.command.tphere") || player.hasPermission("display.command.*")) {
            if (args.length <= 2) {
                if (args.length == 2) {
                    if (Utils.isInt(args[1])) {
                        int id = Integer.parseInt(args[1]);
                        Entity entity = displays.get(id);

                        if (displays.containsKey(id) && !entity.isDead()) {
                            player.sendMessage(Config.getMsg("messages.successfully.teleportedTo", new String[]{String.valueOf(id)}));
                            return true;
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