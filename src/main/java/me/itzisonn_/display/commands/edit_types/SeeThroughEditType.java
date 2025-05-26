package me.itzisonn_.display.commands.edit_types;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.util.ArrayList;

public class SeeThroughEditType extends AbstractEditType {
    public SeeThroughEditType(DisplayPlugin plugin) {
        super(plugin, "see_through", 1, EntityType.TEXT_DISPLAY);
    }

    @Override
    public boolean onCommand(Player player, DisplayData<?> displayData, String[] args) {
        if (!(displayData.getDisplay() instanceof TextDisplay entity)) return true;
        int id = displayData.getId();

        if (args.length == 1 && args[0].equals("?")) {
            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditInfo().getComponent(player,
                    Placeholder.parsed("id", String.valueOf(id)),
                    Placeholder.parsed("type", "see_through"),
                    Placeholder.parsed("value", String.valueOf(entity.isSeeThrough()))));
            return false;
        }

        if (!args[0].equalsIgnoreCase("true") && !args[0].equalsIgnoreCase("false")) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
            return false;
        }

        entity.setSeeThrough(Boolean.parseBoolean(args[0]));
        return true;
    }

    @Override
    public ArrayList<String> onTabComplete(Player player, DisplayData<?> displayData, String[] args) {
        return Lists.newArrayList("true", "false", "?");
    }
}