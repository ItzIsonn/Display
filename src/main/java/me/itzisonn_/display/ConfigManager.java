package me.itzisonn_.display;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class ConfigManager {
    private final DisplayPlugin plugin;
    private FileConfiguration config;

    @Getter
    private boolean isPapiEnabled = false;
    @Getter
    private int textUpdateInterval = 20;
    @Getter
    private final ArrayList<Component> info = new ArrayList<>();
    private final Map<String, String> values = new HashMap<>();

    public ConfigManager(DisplayPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        loadValues();
    }



    private String getAndPut(String path) {
        if (values.get(path) == null) values.put(path, config.getString(path, "?"));
        return values.get(path);
    }

    public String getDefaultValue(String path) {
        return getAndPut("defaultValues." + path);
    }

    public String getPrefix() {
        return getAndPut("messages.prefix");
    }

    public String getEditingText() {
        return getAndPut("messages.editingText");
    }

    public String getListTitle() {
        return getAndPut("messages.list.title");
    }

    public String getListFormat() {
        return getAndPut("messages.list.format");
    }

    public String getListEmpty() {
        return getAndPut("messages.list.empty");
    }

    public Component getError(String path, String id, Player player) {
        String string = getAndPut("messages.errors." + path);
        if (id != null) {
            return MiniMessage.miniMessage().deserialize(plugin.parsePlaceholders(player, string), Placeholder.parsed("prefix", getPrefix()), Placeholder.parsed("id", id));
        }
        return MiniMessage.miniMessage().deserialize(plugin.parsePlaceholders(player, string), Placeholder.parsed("prefix", getPrefix()));
    }

    public Component getSuccessfully(String path, String id, Player player, TagResolver... tagResolvers) {
        String string = getAndPut("messages.successfully." + path);

        List<TagResolver> resolvers = new ArrayList<>(Arrays.stream(tagResolvers).toList());
        resolvers.add(Placeholder.parsed("prefix", getPrefix()));
        if (id != null) {
            resolvers.add(Placeholder.parsed("id", id));
        }

        return MiniMessage.miniMessage().deserialize(plugin.parsePlaceholders(player, string), resolvers.toArray(TagResolver[]::new));
    }



    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        loadValues();
    }

    private void loadValues() {
        isPapiEnabled = config.getBoolean("enablePapi", false);
        textUpdateInterval = config.getInt("textUpdateInterval", 20);
        info.clear();
        config.getStringList("messages.info").forEach(string ->
                info.add(MiniMessage.miniMessage().deserialize(string, Placeholder.parsed("prefix", getPrefix()))));
        values.clear();
    }
}