package me.itzisonn_.display.commands.edit_types;

import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.Utils;
import me.itzisonn_.display.manager.DisplayData;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class DisplayTransformEditType extends AbstractEditType<ItemDisplay> {
    public DisplayTransformEditType(DisplayPlugin plugin) {
        super(plugin, "display_transform");
    }

    @Override
    public boolean onCommand(Player player, String value, DisplayData<ItemDisplay> displayData) {
        ItemDisplay entity = displayData.getDisplay();
        int id = displayData.getId();

        if (value.equals("?")) {
            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditInfo().getComponent(player,
                    Placeholder.parsed("id", String.valueOf(id)),
                    Placeholder.parsed("type", "display_transform"),
                    Placeholder.parsed("value", entity.getItemDisplayTransform().name())));
            return false;
        }

        try {
            entity.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.valueOf(value.toUpperCase()));
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