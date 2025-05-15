package me.itzisonn_.display.subcommands.edit_types;

import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.Utils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Set;

public class DisplayTransformEditType extends AbstractEditType {
    public DisplayTransformEditType(DisplayPlugin plugin) {
        super(plugin, "display_transform", Set.of(EntityType.ITEM_DISPLAY));
    }

    @Override
    public boolean onCommand(Player player, String value, Display entity, int id) {
        if (value.equals("?")) {
            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditInfo().getComponent(player,
                    Placeholder.parsed("id", String.valueOf(id)),
                    Placeholder.parsed("type", "display_transform"),
                    Placeholder.parsed("value", ((ItemDisplay) entity).getItemDisplayTransform().name())));
            return false;
        }

        try {
            ((ItemDisplay) entity).setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.valueOf(value.toUpperCase()));
            return true;
        }
        catch (IllegalArgumentException ignore) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
            return false;
        }
    }

    @Override
    public ArrayList<String> onTabComplete(EntityType type) {
        ArrayList<String> list = Utils.getDisplayTransform();
        list.add("?");
        return list;
    }
}