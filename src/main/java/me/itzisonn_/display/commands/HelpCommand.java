package me.itzisonn_.display.commands;

import me.itzisonn_.display.DisplayPlugin;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class HelpCommand extends AbstractCommand {
    public HelpCommand(DisplayPlugin plugin) {
        super(plugin, "help");
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length > 0) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getTooManyArguments().getComponent(player));
            return;
        }

        plugin.getConfigManager().getGlobalMessagesSection().getInfo().forEach(player::sendMessage);
    }

    @Override
    public ArrayList<String> onTabComplete(Player player, String[] args) {
        return new ArrayList<>();
    }
}
