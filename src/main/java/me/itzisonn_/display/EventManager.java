package me.itzisonn_.display;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;
import java.util.logging.Level;

public class EventManager implements Listener {
    private final DisplayPlugin plugin;

    public EventManager(DisplayPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChunckLoad(ChunkLoadEvent e) {
        for (Entity chunkEntity : e.getChunk().getEntities()) {
            if (!(chunkEntity instanceof Display entity)) continue;

            PersistentDataContainer data = entity.getPersistentDataContainer();
            if (!data.has(plugin.getNskDisplayId(), PersistentDataType.INTEGER)) continue;
            int id = Objects.requireNonNull(data.get(plugin.getNskDisplayId(), PersistentDataType.INTEGER));

            if (plugin.getDisplaysMap().containsValue(entity)) continue;

            plugin.getDisplaysMap().put(id, entity);
            plugin.getLogger().log(Level.INFO, "Entity with ID " + id + " was loaded!");
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent e) {
        Player player = e.getPlayer();

        if (!plugin.getPlayersEditingMap().containsKey(player)) return;
        e.setCancelled(true);

        String message = MiniMessage.miniMessage().serialize(e.originalMessage()).replaceAll("\\\\<", "<");
        Entity entity = plugin.getDisplaysMap().get(plugin.getPlayersEditingMap().get(player));

        PersistentDataContainer data = entity.getPersistentDataContainer();
        data.set(plugin.getNskDisplayText(), PersistentDataType.STRING, message);
    }
}
