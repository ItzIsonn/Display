package me.itzisonn_.display;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Objects;

public class Events implements Listener {
    private static final HashMap<Integer, Entity> displays = Storage.getDisplays();
    private static final FileConfiguration config = Storage.getConfig();

    @EventHandler
    public static void onChunckLoad(ChunkLoadEvent e) {
        for (Entity entity : e.getChunk().getEntities()) {
            NamespacedKey namespacedKey = new NamespacedKey(Display.getInstance(), "displayUUID");
            PersistentDataContainer data = entity.getPersistentDataContainer();
            String displayUUID = data.get(namespacedKey, PersistentDataType.STRING);

            for (String stringUuid : Objects.requireNonNull(config.getConfigurationSection("displayEntities")).getKeys(false)) {
                if (Objects.equals(stringUuid, displayUUID)) {
                    int displayID = config.getInt("displayEntities." + stringUuid + ".id");
                    displays.put(displayID, entity);
                    Bukkit.getServer().getConsoleSender().sendMessage("[Display] Entity with ID " + displayID + " was downloaded!");
                }
            }
        }
    }
}
