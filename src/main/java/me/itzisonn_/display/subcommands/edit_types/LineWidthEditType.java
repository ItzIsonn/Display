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

public class LineWidthEditType extends AbstractEditType {
    public LineWidthEditType(DisplayPlugin plugin) {
        super(plugin, "line_width", Set.of(EntityType.TEXT_DISPLAY));
    }

    @Override
    public boolean onCommand(Player player, String value, Display entity, int id) {
        if (value.equals("?")) {
            player.sendMessage(plugin.getConfigManager().getSuccessfully("edit.info", String.valueOf(id), player,
                    Placeholder.parsed("type", "line_width"),
                    Placeholder.parsed("value", String.valueOf(((TextDisplay) entity).getLineWidth()))));
            return false;
        }

        try {
            ((TextDisplay) entity).setLineWidth(Integer.parseInt(value));
            return true;
        }
        catch (NumberFormatException ignore) {
            player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
            return false;
        }
    }

    @Override
    public ArrayList<String> onTabComplete(EntityType type) {
        return Lists.newArrayList("<width>", "?");
    }
}