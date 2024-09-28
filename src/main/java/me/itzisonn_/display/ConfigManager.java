package me.itzisonn_.display;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ConfigManager {
    private final DisplayPlugin plugin;
    private FileConfiguration config;

    public ConfigManager(DisplayPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }



    public boolean isPapiEnabled() {
        return config.getBoolean("enablePapi", true);
    }

    public int getTextUpdateInterval() {
        return config.getInt("textUpdateInterval", 20);
    }

    public String getDefaultValue(String path) {
        return config.getString("defaultValues." + path, "?");
    }

    public String getPrefix() {
        return config.getString("messages.prefix", "?messages.prefix?");
    }

    public ArrayList<String> getInfo() {
        ArrayList<String> info = new ArrayList<>();
        config.getStringList("messages.info").forEach(string -> info.add(string.replace("%prefix%", getPrefix())));
        return info;
    }

    public String getEditingText() {
        return config.getString("messages.editingText", "?");
    }

    public String getListTitle() {
        return config.getString("messages.list.title", "?");
    }

    public String getListFormat() {
        return config.getString("messages.list.format", "?");
    }

    public String getListEmpty() {
        return config.getString("messages.list.empty", "?");
    }

    public Component getError(String path, String id, Player player) {
        String string = config.getString("messages.errors." + path, "?messages.errors." + path + "?").replace("%prefix%", getPrefix());
        if (id != null) string = string.replace("%id%", id);
        return MiniMessage.miniMessage().deserialize(plugin.getUtils().parsePlaceholders(player, string));
    }

    public Component getSuccessfully(String path, String id, Player player) {
        String string = config.getString("messages.successfully." + path, "?messages.successfully." + path + "?").replace("%prefix%", getPrefix());
        if (id != null) string = string.replace("%id%", id);
        return MiniMessage.miniMessage().deserialize(plugin.getUtils().parsePlaceholders(player, string));
    }



    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }
}