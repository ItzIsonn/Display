package me.itzisonn_.display.subcommands;

import me.itzisonn_.display.DisplayPlugin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class DeleteSubcommand extends AbstractSubcommand {
    public DeleteSubcommand(DisplayPlugin plugin) {
        super(plugin, "delete");
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length > 1) {
            player.sendMessage(plugin.getConfigManager().getError("tooManyArguments", null, player));
            return;
        }

        if (args.length < 1) {
            player.sendMessage(plugin.getConfigManager().getError("notFoundId", null, player));
            return;
        }

        if (args[0].equals("*")) {
            for (int id : plugin.getDisplaysMap().keySet()) {
                Entity entity = plugin.getDisplaysMap().get(id);
                entity.remove();
            }
            plugin.getDisplaysMap().clear();

            player.sendMessage(plugin.getConfigManager().getSuccessfully("delete.all", null, player));
        }
        else {
            int id;
            try {
                id = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException ignore) {
                player.sendMessage(plugin.getConfigManager().getError("invalidId", args[0], player));
                return;
            }

            Entity entity = plugin.getDisplaysMap().get(id);

            if (!plugin.getDisplaysMap().containsKey(id) || entity.isDead()) {
                player.sendMessage(plugin.getConfigManager().getError("idDoesNotExist", String.valueOf(id), player));
                return;
            }

            entity.remove();
            plugin.getDisplaysMap().remove(id);

            player.sendMessage(plugin.getConfigManager().getSuccessfully("delete.id", String.valueOf(id), player));
        }
    }

    @Override
    public ArrayList<String> onTabComplete(Player player, String[] args) {
        if (args.length == 1) {
            ArrayList<String> list = getIDs();
            list.add("*");
            return list;
        }
        return new ArrayList<>();
    }
}
