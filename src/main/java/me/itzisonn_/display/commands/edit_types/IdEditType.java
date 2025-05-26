package me.itzisonn_.display.commands.edit_types;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class IdEditType extends AbstractEditType {
    public IdEditType(DisplayPlugin plugin) {
        super(plugin, "id", 1);
    }

    @Override
    public boolean onCommand(Player player, DisplayData<?> displayData, String[] args) {
        try {
            int newId = Integer.parseInt(args[0]);
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
    public ArrayList<String> onTabComplete(Player player, DisplayData<?> displayData, String[] args) {
        return Lists.newArrayList("<new_id>");
    }
}