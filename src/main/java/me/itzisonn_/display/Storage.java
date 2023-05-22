package me.itzisonn_.display;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Storage{
    private static final HashMap<Integer, Entity> displays = new HashMap<>();
    private static File file;
    private static FileConfiguration config;

    public Storage(String name) {
        file = new File(Display.getInstance().getDataFolder(), name);

        try {
            if (!file.exists() && !file.createNewFile()) throw new IOException();
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to create displays.yml", e);
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to save displays.yml", e);
        }
    }



    public static HashMap<Integer, Entity> getDisplays() { return displays; }
    public static FileConfiguration getConfig() { return config; }
}
