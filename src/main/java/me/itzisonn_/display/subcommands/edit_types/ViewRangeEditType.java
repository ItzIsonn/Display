package me.itzisonn_.display.subcommands.edit_types;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Set;

public class ViewRangeEditType extends AbstractEditType {
    public ViewRangeEditType(DisplayPlugin plugin) {
        super(plugin, "view_range", Set.of(EntityType.BLOCK_DISPLAY, EntityType.ITEM_DISPLAY, EntityType.TEXT_DISPLAY));
    }

    @Override
    public boolean onCommand(Player player, String value, Display entity, int id) {
        if (value.equals("?")) {
            player.sendMessage(plugin.getConfigManager().getSuccessfully("edit.info", String.valueOf(id), player,
                    Placeholder.parsed("type", "view_range"),
                    Placeholder.parsed("value", String.valueOf(entity.getViewRange()))));
            return false;
        }

        try {
            entity.setViewRange(Float.parseFloat(value));
            return true;
        }
        catch (IllegalArgumentException ignore) {
            player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
            return false;
        }
    }

    @Override
    public ArrayList<String> onTabComplete(EntityType type) {
        return Lists.newArrayList("<range>", "?");
    }
}