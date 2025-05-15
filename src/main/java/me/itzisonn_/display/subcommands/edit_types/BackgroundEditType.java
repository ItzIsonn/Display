package me.itzisonn_.display.subcommands.edit_types;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.util.ArrayList;
import java.util.Set;

public class BackgroundEditType extends AbstractEditType {
    public BackgroundEditType(DisplayPlugin plugin) {
        super(plugin, "background", Set.of(EntityType.TEXT_DISPLAY));
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(Player player, String value, Display entity, int id) {
        if (value.equals("?")) {
            String infoValue = ((TextDisplay) entity).getBackgroundColor() == null ? "255,255,255" :
                    ((TextDisplay) entity).getBackgroundColor().getRed() + "," + ((TextDisplay) entity).getBackgroundColor().getGreen() + "," + ((TextDisplay) entity).getBackgroundColor().getBlue();
            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditInfo().getComponent(player,
                    Placeholder.parsed("id", String.valueOf(id)),
                    Placeholder.parsed("type", "background"),
                    Placeholder.parsed("value", infoValue)));
            return false;
        }

        String[] color = value.split(",");
        if (color.length != 3) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
            return false;
        }

        try {
            ((TextDisplay) entity).setBackgroundColor(Color.fromRGB(Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2])));
            return true;
        }
        catch (NumberFormatException ignore) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
            return false;
        }
    }

    @Override
    public ArrayList<String> onTabComplete(EntityType type) {
        return Lists.newArrayList("<r>,<g>,<b>", "?");
    }
}