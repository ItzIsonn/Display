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
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getTooManyArguments().getComponent(player));
            return;
        }

        if (args.length < 1) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getNotFoundId().getComponent(player));
            return;
        }

        if (args[0].equals("*")) {
            for (int id : plugin.getDisplaysMap().keySet()) {
                Entity entity = plugin.getDisplaysMap().get(id);
                entity.remove();
            }
            plugin.getDisplaysMap().clear();

            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getDeleteAll().getComponent(player));
        }
        else {
            int id;
            try {
                id = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException ignore) {
                player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidId().getComponent(player, args[0]));
                return;
            }

            Entity entity = plugin.getDisplaysMap().get(id);

            if (!plugin.getDisplaysMap().containsKey(id) || entity.isDead()) {
                player.sendMessage(plugin.getConfigManager().getErrorsSection().getIdDoesNotExist().getComponent(player, id));
                return;
            }

            entity.remove();
            plugin.getDisplaysMap().remove(id);

            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getDeleteId().getComponent(player, String.valueOf(id)));
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
