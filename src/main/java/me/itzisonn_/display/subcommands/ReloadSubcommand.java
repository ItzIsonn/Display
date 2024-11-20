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
            player.sendMessage(plugin.getConfigManager().getError("tooManyArguments", null, player));
            return;
        }

        plugin.getConfigManager().reloadConfig();
        plugin.getUtils().getScheduler().cancel();
        plugin.getUtils().startTextUpdating();

        player.sendMessage(plugin.getConfigManager().getSuccessfully("reload", null, player));
    }

    @Override
    public ArrayList<String> onTabComplete(Player player, String[] args) {
        return new ArrayList<>();
    }
}
