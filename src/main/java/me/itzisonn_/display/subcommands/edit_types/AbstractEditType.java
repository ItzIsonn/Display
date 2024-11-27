package me.itzisonn_.display.subcommands.edit_types;

import lombok.Getter;
import me.itzisonn_.display.DisplayPlugin;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Set;

@Getter
public abstract class AbstractEditType {
    protected final DisplayPlugin plugin;
    protected final String name;
    protected final Set<EntityType> entityTypes;

    protected AbstractEditType(DisplayPlugin plugin, String name, Set<EntityType> entityTypes) {
        this.plugin = plugin;
        this.name = name;
        this.entityTypes = entityTypes;
    }

    public abstract boolean onCommand(Player player, String value, Display entity, int id);
    public abstract ArrayList<String> onTabComplete(EntityType type);
}
