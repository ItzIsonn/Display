package me.itzisonn_.display.commands;

import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ListCommand extends AbstractCommand {
    public ListCommand(DisplayPlugin plugin) {
        super(plugin, "list");
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length > 0) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getTooManyArguments().getComponent(player));
            return;
        }

        if (plugin.getDisplayManager().getAll().isEmpty()) {
            player.sendMessage(plugin.getConfigManager().getListSection().getEmpty().getComponent(player));
            return;
        }

        player.sendMessage(plugin.getConfigManager().getListSection().getTitle().getComponent(player));
        int pos = 1;
        for (DisplayData<?> displayData : plugin.getDisplayManager().getAll()) {
            Component component = plugin.getConfigManager().getListSection().getFormat().getComponent(player,
                    Placeholder.parsed("pos", String.valueOf(pos)),
                    Placeholder.parsed("type", displayData.getDisplay().getType().name()),
                    Placeholder.parsed("id", String.valueOf(displayData.getId()))
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
