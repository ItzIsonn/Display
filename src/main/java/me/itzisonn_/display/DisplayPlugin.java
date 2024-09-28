package me.itzisonn_.display;

import lombok.Getter;
import me.itzisonn_.display.command.DisplayCommand;
import me.itzisonn_.display.command.DisplayTab;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;

@Getter
public class DisplayPlugin extends JavaPlugin {
    private Utils utils;
    private ConfigManager configManager;
    private final NamespacedKey nskDisplayId = new NamespacedKey(this, "displayID");
    private final NamespacedKey nskDisplayText = new NamespacedKey(this, "displayText");
    private boolean isHookedPapi = false;
    private final HashMap<Integer, Display> displaysMap = new HashMap<>();
    private final HashMap<Player, Integer> playersEditingMap = new HashMap<>();

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        utils = new Utils(this);

        saveDefaultConfig();
        configManager.reloadConfig();

        new DisplayCommand(this);
        new DisplayTab(this);
        Bukkit.getPluginManager().registerEvents(new EventManager(this), this);


        if (configManager.isPapiEnabled()) {
            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                getLogger().log(Level.INFO, "Successfully hooked into PlaceholderAPI!");
                isHookedPapi = true;
            }
            else {
                getLogger().log(Level.SEVERE, "Can't find PlaceholderAPI plugin!");
            }
        }

        utils.startTextUpdating();


        ArrayList<Integer> ids = new ArrayList<>();

        for (World world : Bukkit.getWorlds()) {
            for (Display entity : world.getEntitiesByClass(org.bukkit.entity.Display.class)) {
                PersistentDataContainer data = entity.getPersistentDataContainer();

                if (!data.has(nskDisplayId, PersistentDataType.INTEGER)) continue;

                int id = Objects.requireNonNull(data.get(nskDisplayId, PersistentDataType.INTEGER));
                displaysMap.put(id, entity);
                ids.add(id);
            }
        }

        if (!ids.isEmpty()) getLogger().log(Level.INFO, "Loaded entities with ids: " + ids.toString().replaceAll("[\\[\\]]", ""));

        getLogger().log(Level.INFO, "Enabled!");
    }

    @Override
    public void onDisable() {
        utils.getScheduler().cancel();
        getLogger().log(Level.INFO, "Disabled!");
    }
}