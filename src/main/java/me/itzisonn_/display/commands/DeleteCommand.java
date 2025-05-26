package me.itzisonn_.display.commands;

import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class DeleteCommand extends AbstractCommand {
    public DeleteCommand(DisplayPlugin plugin) {
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
            for (DisplayData<?> displayData : plugin.getDisplayManager().getAll()) {
                Display entity = displayData.getDisplay();
                entity.remove();
                plugin.getDisplayManager().remove(displayData.getId());
            }

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

            DisplayData<?> displayData = plugin.getDisplayManager().get(id);
            if (displayData == null) {
                player.sendMessage(plugin.getConfigManager().getErrorsSection().getIdDoesNotExist().getComponent(player, id));
                return;
            }

            Display entity = displayData.getDisplay();
            if (entity.isDead()) {
                player.sendMessage(plugin.getConfigManager().getErrorsSection().getIdDoesNotExist().getComponent(player, id));
                return;
            }

            entity.remove();
            plugin.getDisplayManager().remove(id);

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
