package me.itzisonn_.display.subcommands;

import me.itzisonn_.display.DisplayPlugin;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ListSubcommand extends AbstractSubcommand {
    public ListSubcommand(DisplayPlugin plugin) {
        super(plugin, "list");
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length > 0) {
            player.sendMessage(plugin.getConfigManager().getError("tooManyArguments", null, player));
            return;
        }


        if (plugin.getDisplaysMap().isEmpty()) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(
                    plugin.getConfigManager().getListEmpty().replace("%prefix%", plugin.getConfigManager().getPrefix())));
            return;
        }

        player.sendMessage(MiniMessage.miniMessage().deserialize(
                plugin.getConfigManager().getListTitle().replace("%prefix%", plugin.getConfigManager().getPrefix())));
        int pos = 1;
        for (int id : plugin.getDisplaysMap().keySet()) {
            Display entity = plugin.getDisplaysMap().get(id);
            player.sendMessage(MiniMessage.miniMessage().deserialize(
                    plugin.getConfigManager().getListFormat()
                            .replace("%pos%", String.valueOf(pos))
                            .replace("%type%", entity.getType().name())
                            .replace("%id%", String.valueOf(id))));
            pos++;
        }
    }

    @Override
    public ArrayList<String> onTabComplete(Player player, String[] args) {
        return new ArrayList<>();
    }
}
