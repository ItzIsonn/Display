package me.itzisonn_.display.manager;

import me.itzisonn_.display.DisplayPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class DisplayManager {
    private final DisplayPlugin plugin;
    private final Set<DisplayData<?>> displayDataSet = new HashSet<>();

    public DisplayManager(DisplayPlugin plugin) {
        this.plugin = plugin;
    }



    public void add(int id, Display entity) throws IllegalArgumentException {
        if (has(id)) throw new IllegalArgumentException("Entity with id " + id + " already exists");
        displayDataSet.add(new DisplayData<>(id, entity));
    }

    public void addIfAbsent(int id, Display entity) {
        if (has(id)) return;
        displayDataSet.add(new DisplayData<>(id, entity));
    }

    public void remove(int id) {
        DisplayData<?> displayData = get(id);
        if (displayData == null) throw new IllegalArgumentException("Entity with id " + id + " doesn't exist");
        displayDataSet.remove(displayData);
    }



    public DisplayData<?> get(int id) {
        for (DisplayData<?> displayData : displayDataSet) {
            if (displayData.getId() == id) return displayData;
        }

        return null;
    }

    public DisplayData<?> get(UUID uuid) {
        for (DisplayData<?> displayData : displayDataSet) {
            if (displayData.getDisplay().getUniqueId().equals(uuid)) return displayData;
        }

        return null;
    }

    public boolean has(int id) {
        return get(id) != null;
    }

    public boolean has(UUID uuid) {
        return get(uuid) != null;
    }



    public Set<DisplayData<?>> getAll() {
        return Set.copyOf(displayDataSet);
    }

    @SuppressWarnings("unchecked")
    public <T extends Display> Set<DisplayData<T>> getAll(Class<T> cls) {
        Set<DisplayData<T>> set = new HashSet<>();

        for (DisplayData<?> displayData : displayDataSet) {
            if (cls.isInstance(displayData.getDisplay())) set.add((DisplayData<T>) displayData);
        }

        return set;
    }



    public void loadDisplays() {
        ArrayList<Integer> ids = new ArrayList<>();

        for (World world : Bukkit.getWorlds()) {
            for (Display entity : world.getEntitiesByClass(Display.class)) {
                PersistentDataContainer data = entity.getPersistentDataContainer();
                if (!data.has(plugin.getNskDisplayId(), PersistentDataType.INTEGER)) continue;

                Integer id = data.get(plugin.getNskDisplayId(), PersistentDataType.INTEGER);
                if (id == null) continue;

                add(id, entity);
                ids.add(id);
            }
        }

        if (!ids.isEmpty()) {
            plugin.getLogger().log(Level.INFO, "Loaded entities with ids: " + ids.toString().replaceAll("[\\[\\]]", ""));
        }
        else {
            plugin.getLogger().log(Level.INFO, "Loaded 0 entities");
        }
    }
}