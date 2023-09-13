package me.itzisonn_.display;

import me.itzisonn_.display.maincommand.DisplayCommand;
import me.itzisonn_.display.maincommand.DisplayTab;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;

public class Display extends JavaPlugin {
    private Utils utils;
    public NamespacedKey displayID_nsk = new NamespacedKey(this, "displayID");
    public NamespacedKey displayText_nsk = new NamespacedKey(this, "displayText");
    public boolean isHookedPapi = false;
    public final HashMap<Integer, Entity> displays = new HashMap<>();
    public final HashMap<Player, Integer> editText = new HashMap<>();



    @Override
    public void onEnable() {
        initialize();

        Bukkit.getServer().getConsoleSender().sendMessage("[Display] Enabled!");

        utils.startTextUpdating();
        downloadEntities();

    }

    @Override
    public void onDisable() {
        utils.scheduler.cancel();
        Bukkit.getServer().getConsoleSender().sendMessage("[Display] Disabled!");
    }



    private void initialize() {
        Config config = new Config(this);
        utils = new Utils(this, config);

        config.reloadConfig();

        new DisplayCommand(this, utils, config);
        new DisplayTab(this, utils);
        Bukkit.getPluginManager().registerEvents(utils, this);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            Bukkit.getServer().getConsoleSender().sendMessage("[Display] Successfully hooked into PlaceholderAPI!");
            isHookedPapi = true;
        }
    }


    private void downloadEntities() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                PersistentDataContainer data = entity.getPersistentDataContainer();

                if (data.has(displayID_nsk, PersistentDataType.INTEGER)) {
                    int displayID = Objects.requireNonNull(data.get(displayID_nsk, PersistentDataType.INTEGER));
                    displays.put(displayID, entity);
                    Bukkit.getServer().getConsoleSender().sendMessage("[Display] Entity with ID " + displayID + " was downloaded!");
                }
            }
        }
    }
}