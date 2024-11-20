package me.itzisonn_.display.subcommands;

import lombok.Getter;
import me.itzisonn_.display.DisplayPlugin;
import org.bukkit.entity.Player;

import java.util.ArrayList;

@Getter
public abstract class AbstractSubcommand {
    protected final DisplayPlugin plugin;
    protected final String name;

    protected AbstractSubcommand(DisplayPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public abstract void onCommand(Player player, String[] args);
    public abstract ArrayList<String> onTabComplete(Player player, String[] args);

    protected ArrayList<String> getIDs() {
        ArrayList<String> ids = new ArrayList<>();
        for (int id : plugin.getDisplaysMap().keySet()) {
            ids.add(String.valueOf(id));
        }

        return ids;
    }
}