package me.itzisonn_.display.commands.edit_types;

import lombok.Getter;
import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Set;

@Getter
public abstract class AbstractMultipleEditType extends AbstractEditType<Display> {
    protected final Set<EntityType> entityTypes;

    protected AbstractMultipleEditType(DisplayPlugin plugin, String name, Set<EntityType> entityTypes) {
        super(plugin, name);
        this.entityTypes = entityTypes;
    }

    public abstract boolean onCommand(Player player, String value, DisplayData<Display> displayData);
    public abstract ArrayList<String> onTabComplete(EntityType type);
}
