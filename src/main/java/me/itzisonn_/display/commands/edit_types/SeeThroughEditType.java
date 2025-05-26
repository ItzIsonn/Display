package me.itzisonn_.display.commands.edit_types;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.util.ArrayList;

public class SeeThroughEditType extends AbstractEditType<TextDisplay> {
    public SeeThroughEditType(DisplayPlugin plugin) {
        super(plugin, "see_through");
    }

    @Override
    public boolean onCommand(Player player, String value, DisplayData<TextDisplay> displayData) {
        TextDisplay entity = displayData.getDisplay();
        int id = displayData.getId();

        if (value.equals("?")) {
            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditInfo().getComponent(player,
                    Placeholder.parsed("id", String.valueOf(id)),
                    Placeholder.parsed("type", "see_through"),
                    Placeholder.parsed("value", String.valueOf(entity.isSeeThrough()))));
            return false;
        }

        if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
            return false;
        }

        entity.setSeeThrough(Boolean.parseBoolean(value));
        return true;
    }

    @Override
    public ArrayList<String> onTabComplete(EntityType type) {
        return Lists.newArrayList("true", "false", "?");
    }
}