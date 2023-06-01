package me.itzisonn_.display;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Utils implements Listener {
    private static final HashMap<Integer, Entity> displays = Display.getDisplays();
    private static BukkitTask scheduler;



    @EventHandler
    public static void onChunckLoad(ChunkLoadEvent e) {
        for (Entity entity : e.getChunk().getEntities()) {
            NamespacedKey namespacedKey = new NamespacedKey(Display.getInstance(), "displayID");
            PersistentDataContainer data = entity.getPersistentDataContainer();

            if (data.has(namespacedKey, PersistentDataType.INTEGER)) {
                int displayID = Objects.requireNonNull(data.get(namespacedKey, PersistentDataType.INTEGER));
                displays.put(displayID, entity);
                Bukkit.getServer().getConsoleSender().sendMessage("[Display] Entity with ID " + displayID + " was downloaded!");
            }
        }
    }

    public static void downloadEntities() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                NamespacedKey namespacedKey = new NamespacedKey(Display.getInstance(), "displayID");
                PersistentDataContainer data = entity.getPersistentDataContainer();

                if (data.has(namespacedKey, PersistentDataType.INTEGER)) {
                    int displayID = Objects.requireNonNull(data.get(namespacedKey, PersistentDataType.INTEGER));
                    displays.put(displayID, entity);
                    Bukkit.getServer().getConsoleSender().sendMessage("[Display] Entity with ID " + displayID + " was downloaded!");
                }
            }
        }
    }

    public static void startTextUpdating() {
        int interval = Config.getValue("textUpdateInterval");

        if (interval != 0) {
            scheduler = Bukkit.getServer().getScheduler().runTaskTimer(Display.getInstance(), () -> {
                for (World world : Bukkit.getWorlds()) {
                    for (Entity entity : world.getEntities()) {
                        if (entity.getType() == EntityType.TEXT_DISPLAY) {
                            NamespacedKey namespacedKey = new NamespacedKey(me.itzisonn_.display.Display.getInstance(), "displayText");
                            PersistentDataContainer data = entity.getPersistentDataContainer();

                            if (data.has(namespacedKey, PersistentDataType.STRING)) {
                                String text = data.get(namespacedKey, PersistentDataType.STRING);

                                assert text != null;
                                if (Display.isHookedPapi()) {
                                    ((TextDisplay) entity).text(MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(null, text)));
                                }
                                else {
                                    ((TextDisplay) entity).text(MiniMessage.miniMessage().deserialize(text));
                                }
                            }
                        }
                    }
                }
            }, 0, interval * 20L);
        }
    }



    public static BukkitTask getScheduler() { return scheduler; }
    public static boolean isInt(String str) { return !str.equals("0") && str.matches("^\\d+$"); }
    public static boolean isDouble(String str) { return str != null && str.matches("[-+]?\\d*\\.?\\d+"); }
    public static boolean isFloat(String str) { return str != null && str.matches("-?[0-9]*\\.?[0-9]+"); }
}