package me.itzisonn_.display.subcommands;

import me.itzisonn_.display.DisplayPlugin;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
            player.sendMessage(MiniMessage.miniMessage().deserialize(plugin.getConfigManager().getListEmpty(), Placeholder.parsed("prefix", plugin.getConfigManager().getPrefix())));
            return;
        }

        player.sendMessage(MiniMessage.miniMessage().deserialize(plugin.getConfigManager().getListTitle(), Placeholder.parsed("prefix", plugin.getConfigManager().getPrefix())));
        int pos = 1;
        for (int id : plugin.getDisplaysMap().keySet()) {
            Display entity = plugin.getDisplaysMap().get(id);
            player.sendMessage(MiniMessage.miniMessage().deserialize(plugin.getConfigManager().getListFormat(),
                    Placeholder.parsed("pos", String.valueOf(pos)),
                    Placeholder.parsed("type", entity.getType().name()),
                    Placeholder.parsed("id", String.valueOf(id))));
            pos++;
        }
    }

    @Override
    public ArrayList<String> onTabComplete(Player player, String[] args) {
        return new ArrayList<>();
    }
}
