package me.itzisonn_.display.subcommands;

import me.itzisonn_.display.DisplayPlugin;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class HelpSubcommand extends AbstractSubcommand {
    public HelpSubcommand(DisplayPlugin plugin) {
        super(plugin, "help");
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length > 0) {
            player.sendMessage(plugin.getConfigManager().getError("tooManyArguments", null, player));
            return;
        }

        plugin.getConfigManager().getInfo().forEach(player::sendMessage);
    }

    @Override
    public ArrayList<String> onTabComplete(Player player, String[] args) {
        return new ArrayList<>();
    }
}
