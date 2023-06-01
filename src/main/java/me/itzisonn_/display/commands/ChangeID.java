package me.itzisonn_.display.commands;

import me.itzisonn_.display.Display;
import me.itzisonn_.display.Utils;
import me.itzisonn_.display.Config;
import me.itzisonn_.display.maincommand.DisplayCommand;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class ChangeID {
    private static final Player player = DisplayCommand.getPlayer();
    private static final HashMap<Integer, Entity> displays = Display.getDisplays();



    public static void changeID(int id, int newID) {
        Entity entity = displays.get(id);
        displays.remove(id);
        displays.put(newID, entity);

        NamespacedKey namespacedKey = new NamespacedKey(Display.getInstance(), "displayID");
        PersistentDataContainer data = entity.getPersistentDataContainer();
        data.set(namespacedKey, PersistentDataType.INTEGER, id);
    }



    public static boolean checkChangeID(String[] args) {
        if (player.hasPermission("display.command.tphere") || player.hasPermission("display.command.*")) {
            if (args.length <= 3) {
                if (args.length >= 2) {
                    if (Utils.isInt(args[1])) {
                        int id = Integer.parseInt(args[1]);
                        Entity entity = displays.get(id);

                        if (displays.containsKey(id) && !entity.isDead()) {
                            if (args.length == 3) {
                                if (Utils.isInt(args[2])) {
                                    String newid = args[2];

                                    if (!(displays.containsKey(Integer.parseInt(newid)))) {
                                        player.sendMessage(Config.getMsg("messages.successfully.changedID", new String[]{String.valueOf(id), newid}));
                                        return true;
                                    }
                                    else {
                                        player.sendMessage(Config.getMsg("messages.errors.idAlreadyInUse", new String[]{newid}));
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