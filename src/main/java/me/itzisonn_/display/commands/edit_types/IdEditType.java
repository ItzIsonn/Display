package me.itzisonn_.display.commands.edit_types;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Set;

public class IdEditType extends AbstractMultipleEditType {
    public IdEditType(DisplayPlugin plugin) {
        super(plugin, "id", Set.of(EntityType.BLOCK_DISPLAY, EntityType.ITEM_DISPLAY, EntityType.TEXT_DISPLAY));
    }

    @Override
    public boolean onCommand(Player player, String value, DisplayData<Display> displayData) {
        try {
            int newId = Integer.parseInt(value);
            displayData.setId(newId);

            PersistentDataContainer data = displayData.getDisplay().getPersistentDataContainer();
            data.set(plugin.getNskDisplayId(), PersistentDataType.INTEGER, newId);
            return true;
        }
        catch (NumberFormatException ignore) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, displayData.getId()));
            return false;
        }
    }

    @Override
    public ArrayList<String> onTabComplete(EntityType type) {
        return Lists.newArrayList("<new_id>");
    }
}