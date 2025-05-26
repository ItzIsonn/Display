package me.itzisonn_.display.commands.edit_types;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.util.ArrayList;

public class LineWidthEditType extends AbstractEditType<TextDisplay> {
    public LineWidthEditType(DisplayPlugin plugin) {
        super(plugin, "line_width");
    }

    @Override
    public boolean onCommand(Player player, String value, DisplayData<TextDisplay> displayData) {
        TextDisplay entity = displayData.getDisplay();
        int id = displayData.getId();

        if (value.equals("?")) {
            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditInfo().getComponent(player,
                    Placeholder.parsed("id", String.valueOf(id)),
                    Placeholder.parsed("type", "line_width"),
                    Placeholder.parsed("value", String.valueOf(entity.getLineWidth()))));
            return false;
        }

        try {
            entity.setLineWidth(Integer.parseInt(value));
            return true;
        }
        catch (NumberFormatException ignore) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
            return false;
        }
    }

    @Override
    public ArrayList<String> onTabComplete(EntityType type) {
        return Lists.newArrayList("<width>", "?");
    }
}