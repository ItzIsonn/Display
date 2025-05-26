package me.itzisonn_.display.commands.edit_types;

import lombok.Getter;
import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Set;

@Getter
public abstract class AbstractEditType {
    protected final DisplayPlugin plugin;
    protected final String name;
    protected final Set<EntityType> entityTypes;
    protected final int maxArgs;

    protected AbstractEditType(DisplayPlugin plugin, String name, Set<EntityType> entityTypes, int maxArgs) {
        this.plugin = plugin;
        this.name = name;
        this.entityTypes = entityTypes;
        this.maxArgs = maxArgs;
    }

    protected AbstractEditType(DisplayPlugin plugin, String name, int maxArgs, EntityType... entityTypes) {
        this(plugin, name, Set.of(entityTypes), maxArgs);
    }

    protected AbstractEditType(DisplayPlugin plugin, String name, int maxArgs) {
        this(plugin, name, maxArgs, EntityType.BLOCK_DISPLAY, EntityType.ITEM_DISPLAY, EntityType.TEXT_DISPLAY);
    }

    public abstract boolean onCommand(Player player, DisplayData<?> displayData, String[] args);

    public abstract ArrayList<String> onTabComplete(Player player, DisplayData<?> displayData, String[] args);
}
