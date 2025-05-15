package me.itzisonn_.display.subcommands.edit_types;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Set;

public class IdEditType extends AbstractEditType {
    public IdEditType(DisplayPlugin plugin) {
        super(plugin, "id", Set.of(EntityType.BLOCK_DISPLAY, EntityType.ITEM_DISPLAY, EntityType.TEXT_DISPLAY));
    }

    @Override
    public boolean onCommand(Player player, String value, Display entity, int id) {
        try {
            int newId = Integer.parseInt(value);
            plugin.getDisplaysMap().remove(id);
            plugin.getDisplaysMap().put(newId, entity);

            PersistentDataContainer data = entity.getPersistentDataContainer();
            data.set(plugin.getNskDisplayId(), PersistentDataType.INTEGER, newId);
            return true;
        }
        catch (NumberFormatException ignore) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
            return false;
        }
    }

    @Override
    public ArrayList<String> onTabComplete(EntityType type) {
        return Lists.newArrayList("<new_id>");
    }
}