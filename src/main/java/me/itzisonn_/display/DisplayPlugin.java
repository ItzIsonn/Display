package me.itzisonn_.display;

import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.itzisonn_.display.config.ConfigManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
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
    private final Map<Integer, Display> displaysMap = new HashMap<>();
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
        startTextUpdating();

        ArrayList<Integer> ids = new ArrayList<>();
        for (World world : Bukkit.getWorlds()) {
            for (Display entity : world.getEntitiesByClass(org.bukkit.entity.Display.class)) {
                PersistentDataContainer data = entity.getPersistentDataContainer();
                if (!data.has(nskDisplayId, PersistentDataType.INTEGER)) continue;

                Integer id = data.get(nskDisplayId, PersistentDataType.INTEGER);
                displaysMap.put(id, entity);
                ids.add(id);
            }
        }

        if (!ids.isEmpty()) {
            getLogger().log(Level.INFO, "Loaded entities with ids: " + ids.toString().replaceAll("[\\[\\]]", ""));
        }
        else {
            getLogger().log(Level.INFO, "Loaded 0 entities");
        }
    }

    @Override
    public void onDisable() {
        scheduler.cancel();
    }



    public void startTextUpdating() {
        int interval = configManager.getGlobalSection().getTextUpdateInterval();

        if (interval != 0) scheduler = Bukkit.getServer().getScheduler().runTaskTimer(this, () -> {
            for (Entity entity : displaysMap.values()) {
                if (entity.getType() != EntityType.TEXT_DISPLAY) continue;
                TextDisplay textEntity = (TextDisplay) entity;

                PersistentDataContainer data = textEntity.getPersistentDataContainer();
                if (!data.has(nskDisplayText, PersistentDataType.STRING)) continue;

                String text = data.get(nskDisplayText, PersistentDataType.STRING);
                if (text == null) continue;

                textEntity.text(miniMessage.deserialize(parsePlaceholders(null, text)));
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

    public MiniMessage getMiniMessage() {
        if (miniMessage == null) return MiniMessage.miniMessage();
        return miniMessage;
    }

    public void updateMiniMessage() {
        TagResolver prefixResolver = TagResolver.resolver("prefix", Tag.inserting(configManager.getGlobalMessagesSection().getPrefix().getComponent()));
        miniMessage = MiniMessage.builder().editTags(builder -> builder.resolver(prefixResolver)).build();
    }
}