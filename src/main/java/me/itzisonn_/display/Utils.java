package me.itzisonn_.display;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Utils implements Listener {
    private final Display display;
    private final Config config;

    public BukkitTask scheduler;
    //private static Field field = null;

    public Utils(Display display, Config config) {
        this.display = display;
        this.config = config;
    }

    /*static {
        try {
            String version = Pattern.compile("\\.").split(Bukkit.getServer().getClass().getPackage().getName())[3];
            Class<?> mcInventory = Class.forName(String.format("org.bukkit.craftbukkit.%s.inventory.CraftInventory", version));
            field = mcInventory.getDeclaredField("inventory");
            field.setAccessible(true);
        }
        catch (ClassNotFoundException | NoSuchFieldException ignored) {}
    }*/



    @EventHandler
    public void onChunckLoad(ChunkLoadEvent e) {
        for (Entity entity : e.getChunk().getEntities()) {
            PersistentDataContainer data = entity.getPersistentDataContainer();

            if (data.has(display.displayID_nsk, PersistentDataType.INTEGER)) {
                int displayID = Objects.requireNonNull(data.get(display.displayID_nsk, PersistentDataType.INTEGER));
                display.displays.put(displayID, entity);
                Bukkit.getServer().getConsoleSender().sendMessage("[Display] Entity with ID " + displayID + " was downloaded!");
            }
        }
    }

    public void startTextUpdating() {
        int interval = config.getValue("textUpdateInterval");

        if (interval != 0) {
            scheduler = Bukkit.getServer().getScheduler().runTaskTimer(display, this::updateText, 0, interval);
        }
    }

    private void updateText() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getType() == EntityType.TEXT_DISPLAY) {
                    PersistentDataContainer data = entity.getPersistentDataContainer();

                    if (data.has(display.displayText_nsk, PersistentDataType.STRING)) {
                        String text = data.get(display.displayText_nsk, PersistentDataType.STRING);

                        assert text != null;
                        if (display.isHookedPapi) {
                            ((TextDisplay) entity).text(MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(null, text)));
                        }
                        else {
                            ((TextDisplay) entity).text(MiniMessage.miniMessage().deserialize(text));
                        }
                    }
                }
            }
        }
    }

    /*@EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!Objects.equals(e.getCurrentItem(), null)) {
            Inventory s = Bukkit.createInventory(null, 27, Component.text("s"));
            if (isInventory(e.getClickedInventory(), s)) { //info
                assert e.getCurrentItem() != null;
                NamespacedKey namespacedKey = new NamespacedKey(display, "displayItem");
                ItemMeta meta = e.getCurrentItem().getItemMeta();
                PersistentDataContainer data = meta.getPersistentDataContainer();

                if (data.get(namespacedKey, PersistentDataType.STRING) != null) {
                    if (Objects.equals(data.get(namespacedKey, PersistentDataType.STRING), "exit")) {
                        //list.showList((Player) e.getWhoClicked());
                    }
                }

                e.setCancelled(true);
            } else if (isInventory(e.getClickedInventory(), s)) { //list
                assert e.getCurrentItem() != null;
                NamespacedKey namespacedKey = new NamespacedKey(display, "displayItem");
                ItemMeta meta = e.getCurrentItem().getItemMeta();
                PersistentDataContainer data = meta.getPersistentDataContainer();

                if (data.get(namespacedKey, PersistentDataType.STRING) != null && isInt(Objects.requireNonNull(data.get(namespacedKey, PersistentDataType.STRING)))) {
                    //info.showInfo(Integer.parseInt(Objects.requireNonNull(data.get(namespacedKey, PersistentDataType.STRING))), (Player) e.getWhoClicked());
                }

                e.setCancelled(true);
            }
        }
    }*/

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        if (display.editText.containsKey(player)) {
            e.setCancelled(true);

            String message = e.getMessage();
            Entity entity = display.displays.get(display.editText.get(player));

            PersistentDataContainer data = entity.getPersistentDataContainer();
            data.set(display.displayText_nsk, PersistentDataType.STRING, message);
        }
    }



    public boolean isInt(String str) { return !str.equals("0") && str.length() <= 10 && str.matches("^\\d+$"); }
    public boolean isDouble(String str) { return str != null && str.matches("[-+]?\\d*\\.?\\d+"); }
    public boolean isFloat(String str) { return str != null && str.matches("-?[0-9]*\\.?[0-9]+"); }
    public boolean isBoolean(String str) { return str != null && (str.equals("true") || str.equals("false")); }
    /*public boolean isInventory(Inventory inv1, Inventory inv2) {
        try {
            return field.get(inv1) == field.get(inv2);
        }
        catch (IllegalAccessException ignore) {
            return false;
        }
    }*/
}