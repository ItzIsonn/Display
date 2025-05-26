package me.itzisonn_.display.commands.edit_types;

import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.Utils;
import me.itzisonn_.display.manager.DisplayData;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.util.ArrayList;

public class AlignmentEditType extends AbstractEditType<TextDisplay> {
    public AlignmentEditType(DisplayPlugin plugin) {
        super(plugin, "alignment");
    }

    @Override
    public boolean onCommand(Player player, String value, DisplayData<TextDisplay> displayData) {
        TextDisplay entity = displayData.getDisplay();
        int id = displayData.getId();

        if (value.equals("?")) {
            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditInfo().getComponent(player,
                    Placeholder.parsed("id", String.valueOf(id)),
                    Placeholder.parsed("type", "alignment"),
                    Placeholder.parsed("value", entity.getAlignment().name())));
            return false;
        }

        try {
            entity.setAlignment(TextDisplay.TextAlignment.valueOf(value.toUpperCase()));
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