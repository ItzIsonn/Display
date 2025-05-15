package me.itzisonn_.display.subcommands;

import me.itzisonn_.display.DisplayPlugin;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ReloadSubcommand extends AbstractSubcommand {
    public ReloadSubcommand(DisplayPlugin plugin) {
        super(plugin, "reload");
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length > 0) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getTooManyArguments().getComponent(player));
            return;
        }

        plugin.getConfigManager().reloadConfig();
        plugin.hookPapi();
        plugin.getScheduler().cancel();
        plugin.startTextUpdating();

        player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getReload().getComponent(player));
    }

    @Override
    public ArrayList<String> onTabComplete(Player player, String[] args) {
        return new ArrayList<>();
    }
}
