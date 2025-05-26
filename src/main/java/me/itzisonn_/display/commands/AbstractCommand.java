package me.itzisonn_.display.commands;

import lombok.Getter;
import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import org.bukkit.entity.Player;

import java.util.ArrayList;

@Getter
public abstract class AbstractCommand {
    protected final DisplayPlugin plugin;
    protected final String name;

    protected AbstractCommand(DisplayPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public abstract void onCommand(Player player, String[] args);
    public abstract ArrayList<String> onTabComplete(Player player, String[] args);

    protected ArrayList<String> getIDs() {
        ArrayList<String> ids = new ArrayList<>();
        for (DisplayData<?> displayData : plugin.getDisplayManager().getAll()) {
            ids.add(String.valueOf(displayData.getId()));
        }

        return ids;
    }
}