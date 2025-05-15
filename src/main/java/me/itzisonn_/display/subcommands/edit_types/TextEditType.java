package me.itzisonn_.display.subcommands.edit_types;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Set;

public class TextEditType extends AbstractEditType {
    public TextEditType(DisplayPlugin plugin) {
        super(plugin, "text", Set.of(EntityType.TEXT_DISPLAY));
    }

    @Override
    public boolean onCommand(Player player, String value, Display entity, int id) {
        if (value.equals("?")) {
            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditInfo().getComponent(player,
                    Placeholder.parsed("id", String.valueOf(id)),
                    Placeholder.parsed("type", "text"),
                    Placeholder.parsed("value", plugin.getPlayersEditingMap().containsKey(player.getUniqueId().toString()) ? "on" : "off")));
            return false;
        }

        boolean isText;
        if (value.equalsIgnoreCase("on")) isText = true;
        else if (value.equalsIgnoreCase("off")) isText = false;
        else {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
            return false;
        }

        if (isText) {
            plugin.getPlayersEditingMap().put(player.getUniqueId().toString(), id);

            PersistentDataContainer data = entity.getPersistentDataContainer();
            String text = data.get(plugin.getNskDisplayText(), PersistentDataType.STRING);

            if (text != null) {
                String message = "<click:copy_to_clipboard:'" + text + "'><click:suggest_command:'" + text + "'><hover:show_text:'" +
                        plugin.parsePlaceholders(player, text) + "'>";

                player.sendMessage(plugin.getMiniMessage().deserialize(message)
                                .append(plugin.getConfigManager().getGlobalMessagesSection().getEditingText().getComponent(player, id)));
            }

        }
        else plugin.getPlayersEditingMap().remove(player.getUniqueId().toString());
        return true;
    }

    @Override
    public ArrayList<String> onTabComplete(EntityType type) {
        return Lists.newArrayList("on", "off", "?");
    }
}