package me.itzisonn_.display;

import me.itzisonn_.display.command.DisplayCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Display extends JavaPlugin {
    private static Display instance;
    private Storage data;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        data = new Storage("displays.yml");

        new DisplayCommand();
        Bukkit.getPluginManager().registerEvents(new Events(), this);

        Bukkit.getServer().getConsoleSender().sendMessage("[Display] Enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getServer().getConsoleSender().sendMessage("[Display] Disabled!");
    }



    public static Display getInstance() { return instance; }
    public static Storage getData() { return instance.data; }
}