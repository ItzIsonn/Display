package me.itzisonn_.display.commands.edit_types;

import lombok.Getter;
import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;

@Getter
public abstract class AbstractEditType<T extends Display> {
    protected final DisplayPlugin plugin;
    protected final String name;

    protected AbstractEditType(DisplayPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public abstract boolean onCommand(Player player, String value, DisplayData<T> displayData);
    public abstract ArrayList<String> onTabComplete(EntityType type);
}
