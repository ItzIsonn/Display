package me.itzisonn_.display;

import me.itzisonn_.display.maincommand.DisplayCommand;
import me.itzisonn_.display.maincommand.DisplayTab;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Display extends JavaPlugin {
    private static Display instance;
    private static boolean isHookedPapi = false;
    private static final HashMap<Integer, Entity> displays = new HashMap<>();



    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        new DisplayCommand();
        new DisplayTab();
        Bukkit.getPluginManager().registerEvents(new Utils(), this);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            Bukkit.getServer().getConsoleSender().sendMessage("[Display] Successfully hooked into PlaceholderAPI!");
            isHookedPapi = true;
        }

        Utils.startTextUpdating();
        Bukkit.getServer().getConsoleSender().sendMessage("[Display] Enabled!");

        Utils.downloadEntities();
    }

    @Override
    public void onDisable() {
        Utils.getScheduler().cancel();
        Bukkit.getServer().getConsoleSender().sendMessage("[Display] Disabled!");
    }



    public static Display getInstance() { return instance; }
    public static HashMap<Integer, Entity> getDisplays() { return displays; }
    public static boolean isHookedPapi() { return isHookedPapi; }
}