package me.itzisonn_.display.subcommands.edit_types;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Set;

public class ShadowEditType extends AbstractEditType {
    public ShadowEditType(DisplayPlugin plugin) {
        super(plugin, "shadow", Set.of(EntityType.BLOCK_DISPLAY, EntityType.ITEM_DISPLAY, EntityType.TEXT_DISPLAY));
    }

    @Override
    public boolean onCommand(Player player, String value, Display entity, int id) {
        if (value.equals("?")) {
            player.sendMessage(plugin.getConfigManager().getSuccessfully("edit.info", String.valueOf(id), player,
                    Placeholder.parsed("type", "shadow"),
                    Placeholder.parsed("value", entity.getShadowRadius() + "," + entity.getShadowStrength())));
            return false;
        }

        String[] shadow = value.split(",");
        if (shadow.length != 2) {
            player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
            return false;
        }

        try {
            float radius = Float.parseFloat(shadow[0]);
            float strength = Float.parseFloat(shadow[1]);
            entity.setShadowRadius(radius);
            entity.setShadowStrength(strength);
            return true;
        }
        catch (IllegalArgumentException ignore) {
            player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
            return false;
        }
    }

    @Override
    public ArrayList<String> onTabComplete(EntityType type) {
        return Lists.newArrayList("<radius>,<strength>", "?");
    }
}