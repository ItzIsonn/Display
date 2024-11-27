package me.itzisonn_.display.subcommands.edit_types;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.util.ArrayList;
import java.util.Set;

public class SeeThroughEditType extends AbstractEditType {
    public SeeThroughEditType(DisplayPlugin plugin) {
        super(plugin, "see_through", Set.of(EntityType.TEXT_DISPLAY));
    }

    @Override
    public boolean onCommand(Player player, String value, Display entity, int id) {
        if (value.equals("?")) {
            player.sendMessage(plugin.getConfigManager().getSuccessfully("edit.info", String.valueOf(id), player,
                    Placeholder.parsed("type", "see_through"),
                    Placeholder.parsed("value", String.valueOf(((TextDisplay) entity).isSeeThrough()))));
            return false;
        }

        if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
            player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
            return false;
        }

        ((TextDisplay) entity).setSeeThrough(Boolean.parseBoolean(value));
        return true;
    }

    @Override
    public ArrayList<String> onTabComplete(EntityType type) {
        return Lists.newArrayList("true", "false", "?");
    }
}