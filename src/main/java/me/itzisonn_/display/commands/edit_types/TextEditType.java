package me.itzisonn_.display.commands.edit_types;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class TextEditType extends AbstractEditType {
    public TextEditType(DisplayPlugin plugin) {
        super(plugin, "text", 1, EntityType.TEXT_DISPLAY);
    }

    @Override
    public boolean onCommand(Player player, DisplayData<?> displayData, String[] args) {
        if (!(displayData.getDisplay() instanceof TextDisplay entity)) return true;
        int id = displayData.getId();

        if (args[0].equals("?")) {
            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditInfo().getComponent(player,
                    Placeholder.parsed("id", String.valueOf(id)),
                    Placeholder.parsed("type", "text"),
                    Placeholder.parsed("value", plugin.getPlayersEditingMap().containsKey(player.getUniqueId().toString()) ? "on" : "off")));
            return false;
        }

        boolean isText;
        if (args[0].equalsIgnoreCase("on")) isText = true;
        else if (args[0].equalsIgnoreCase("off")) isText = false;
        else {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
            return false;
        }

        if (isText) {
            plugin.getPlayersEditingMap().put(player.getUniqueId().toString(), id);

            PersistentDataContainer data = entity.getPersistentDataContainer();
            String text = data.get(plugin.getNskDisplayText(), PersistentDataType.STRING);

            if (text != null) {
                player.sendMessage(plugin.getConfigManager().getGlobalMessagesSection().getEditingText().getComponent(player,
                                Placeholder.parsed("id", String.valueOf(id)),
                                Placeholder.parsed("raw_text", text),
                                Placeholder.parsed("parsed_text", plugin.parsePlaceholders(null, text))));
            }

        }
        else plugin.getPlayersEditingMap().remove(player.getUniqueId().toString());

        return true;
    }

    @Override
    public ArrayList<String> onTabComplete(Player player, DisplayData<?> displayData, String[] args) {
        return Lists.newArrayList("on", "off", "?");
    }
}