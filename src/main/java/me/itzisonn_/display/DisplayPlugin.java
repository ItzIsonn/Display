package me.itzisonn_.display;

import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.itzisonn_.display.config.ConfigManager;
import me.itzisonn_.display.manager.DisplayData;
import me.itzisonn_.display.manager.DisplayManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@Getter
public class DisplayPlugin extends JavaPlugin {
    private ConfigManager configManager;
    private MiniMessage miniMessage;

    private final NamespacedKey nskDisplayId = new NamespacedKey(this, "displayID");
    private final NamespacedKey nskDisplayText = new NamespacedKey(this, "displayText");

    private boolean isHookedPapi = false;
    private DisplayProtocolManager displayProtocolManager;
    private final DisplayManager displayManager = new DisplayManager(this);
    private final Map<String, Integer> playersEditingMap = new HashMap<>();
    private BukkitTask scheduler;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        configManager = new ConfigManager(this);

        configManager.getGlobalMessagesSection().getPrefix().updateValue();
        updateMiniMessage();

        configManager.reloadConfig();

        new DisplayCommand(this);
        Bukkit.getPluginManager().registerEvents(new EventManager(this), this);

        hookPapi();
        hookProtocolLib();
        startTextUpdating();

        displayManager.loadDisplays();
    }

    @Override
    public void onDisable() {
        scheduler.cancel();
    }



    public void startTextUpdating() {
        int interval = configManager.getGlobalSection().getTextUpdateInterval();

        if (interval != 0) scheduler = Bukkit.getServer().getScheduler().runTaskTimer(this, () -> {
            for (DisplayData<TextDisplay> displayData : displayManager.getAll(TextDisplay.class)) {
                TextDisplay textEntity = displayData.getDisplay();

                PersistentDataContainer data = textEntity.getPersistentDataContainer();
                if (!data.has(nskDisplayText, PersistentDataType.STRING)) continue;

                String text = data.get(nskDisplayText, PersistentDataType.STRING);
                if (text == null) continue;

                if (displayProtocolManager == null) {
                    textEntity.text(miniMessage.deserialize(parsePlaceholders(null, text)));
                }
                else {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        displayProtocolManager.updateFakeText(player, textEntity.getEntityId(), miniMessage.deserialize(parsePlaceholders(player, text)));
                    }
                }
            }
        }, 0, interval);
    }

    public String parsePlaceholders(Player player, String string) {
        if (isHookedPapi) return PlaceholderAPI.setPlaceholders(player, string);
        return string;
    }

    public void hookPapi() {
        isHookedPapi = false;

        if (configManager.getGlobalSection().isPapiEnabled()) {
            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                getLogger().log(Level.INFO, "Successfully hooked into PlaceholderAPI!");
                isHookedPapi = true;
            }
            else getLogger().log(Level.SEVERE, "Can't find PlaceholderAPI plugin!");
        }
    }

    public void hookProtocolLib() {
        displayProtocolManager = null;

        if (configManager.getGlobalSection().isProtocolLibEnabled()) {
            if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
                getLogger().log(Level.INFO, "Successfully hooked into ProtocolLib!");
                displayProtocolManager = new DisplayProtocolManager();
            }
            else getLogger().log(Level.SEVERE, "Can't find ProtocolLib plugin!");
        }
    }

    public MiniMessage getMiniMessage() {
        if (miniMessage == null) return MiniMessage.miniMessage();
        return miniMessage;
    }

    public void updateMiniMessage() {
        TagResolver prefixResolver = TagResolver.resolver("prefix", Tag.inserting(configManager.getGlobalMessagesSection().getPrefix().getComponent()));
        miniMessage = MiniMessage.builder().editTags(builder -> builder.resolver(prefixResolver)).build();
    }
}