package me.itzisonn_.display.subcommands.edit_types;

import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.Utils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.util.ArrayList;
import java.util.Set;

public class AlignmentEditType extends AbstractEditType {
    public AlignmentEditType(DisplayPlugin plugin) {
        super(plugin, "alignment", Set.of(EntityType.TEXT_DISPLAY));
    }

    @Override
    public boolean onCommand(Player player, String value, Display entity, int id) {
        if (value.equals("?")) {
            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditInfo().getComponent(player,
                    Placeholder.parsed("id", String.valueOf(id)),
                    Placeholder.parsed("type", "alignment"),
                    Placeholder.parsed("value", ((TextDisplay) entity).getAlignment().name())));
            return false;
        }

        try {
            ((TextDisplay) entity).setAlignment(TextDisplay.TextAlignment.valueOf(value.toUpperCase()));
            return true;
        }
        catch (IllegalArgumentException ignore) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
            return false;
        }
    }

    @Override
    public ArrayList<String> onTabComplete(EntityType type) {
        ArrayList<String> list = Utils.getAlignment();
        list.add("?");
        return list;
    }
}