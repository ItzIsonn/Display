package me.itzisonn_.display.commands.edit_types;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Color;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.util.ArrayList;

public class BackgroundEditType extends AbstractEditType<TextDisplay> {
    public BackgroundEditType(DisplayPlugin plugin) {
        super(plugin, "background");
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(Player player, String value, DisplayData<TextDisplay> displayData) {
        TextDisplay entity = displayData.getDisplay();
        int id = displayData.getId();

        if (value.equals("?")) {
            String infoValue = entity.getBackgroundColor() == null ? "255,255,255" :
                    entity.getBackgroundColor().getRed() + "," + entity.getBackgroundColor().getGreen() + "," + entity.getBackgroundColor().getBlue();
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
            entity.setBackgroundColor(Color.fromRGB(Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2])));
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