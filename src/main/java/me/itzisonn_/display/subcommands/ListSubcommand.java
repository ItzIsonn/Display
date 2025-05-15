package me.itzisonn_.display.subcommands;

import me.itzisonn_.display.DisplayPlugin;
import net.kyori.adventure.text.Component;
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
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getTooManyArguments().getComponent(player));
            return;
        }


        if (plugin.getDisplaysMap().isEmpty()) {
            player.sendMessage(plugin.getConfigManager().getListSection().getEmpty().getComponent(player));
            return;
        }

        player.sendMessage(plugin.getConfigManager().getListSection().getTitle().getComponent(player));
        int pos = 1;
        for (int id : plugin.getDisplaysMap().keySet()) {
            Display entity = plugin.getDisplaysMap().get(id);

            Component component = plugin.getConfigManager().getListSection().getFormat().getComponent(player,
                    Placeholder.parsed("pos", String.valueOf(pos)),
                    Placeholder.parsed("type", entity.getType().name()),
                    Placeholder.parsed("id", String.valueOf(id))
            );
            player.sendMessage(component);

            pos++;
        }
    }

    @Override
    public ArrayList<String> onTabComplete(Player player, String[] args) {
        return new ArrayList<>();
    }
}
