package me.itzisonn_.display.commands.edit_types;

import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.util.ArrayList;

public class AlignmentEditType extends AbstractEditType {
    public AlignmentEditType(DisplayPlugin plugin) {
        super(plugin, "alignment", 1, EntityType.TEXT_DISPLAY);
    }

    @Override
    public boolean onCommand(Player player, DisplayData<?> displayData, String[] args) {
        if (!(displayData.getDisplay() instanceof TextDisplay entity)) return true;
        int id = displayData.getId();

        if (args[0].equals("?")) {
            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditInfo().getComponent(player,
                    Placeholder.parsed("id", String.valueOf(id)),
                    Placeholder.parsed("type", "alignment"),
                    Placeholder.parsed("value", entity.getAlignment().name())));
            return false;
        }

        try {
            entity.setAlignment(TextDisplay.TextAlignment.valueOf(args[0].toUpperCase()));
            return true;
        }
        catch (IllegalArgumentException ignore) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
            return false;
        }
    }

    @Override
    public ArrayList<String> onTabComplete(Player player, DisplayData<?> displayData, String[] args) {
        ArrayList<String> list = getAlignment();
        list.add("?");
        return list;
    }



    private static ArrayList<String> getAlignment() {
        ArrayList<String> list = new ArrayList<>();
        for (TextDisplay.TextAlignment alignment : TextDisplay.TextAlignment.values()) {
            list.add(alignment.name().toLowerCase());
        }
        return list;
    }
}