package me.itzisonn_.display.commands;

import me.itzisonn_.display.Config;
import me.itzisonn_.display.Display;
import me.itzisonn_.display.Utils;
import me.itzisonn_.display.maincommand.DisplayCommand;
import org.bukkit.entity.Player;

public class Reload {
    private static final Player player = DisplayCommand.getPlayer();



    public static void reloadConfig() {
        Display.getInstance().reloadConfig();
        Config.reloadConfig();
        Utils.getScheduler().cancel();
        Utils.startTextUpdating();
    }



    public static boolean checkReload(String[] args) {
        if (player.hasPermission("display.command.reload") || player.hasPermission("display.command.*")) {
            if (args.length == 1) {
                player.sendMessage(Config.getMsg("messages.successfully.reloaded", null));
                return true;
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