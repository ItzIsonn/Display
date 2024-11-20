package me.itzisonn_.display.subcommands;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class CreateSubcommand extends AbstractSubcommand {
    public CreateSubcommand(DisplayPlugin plugin) {
        super(plugin, "create");
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length > 3) {
            player.sendMessage(plugin.getConfigManager().getError("tooManyArguments", null, player));
            return;
        }

        if (args.length < 1) {
            player.sendMessage(plugin.getConfigManager().getError("notFoundObjectType", null, player));
            return;
        }

        if (!args[0].equalsIgnoreCase("block") && !args[0].equalsIgnoreCase("item") && !args[0].equalsIgnoreCase("text") && !args[0].equalsIgnoreCase("clone")) {
            player.sendMessage(plugin.getConfigManager().getError("unknownObjectType", null, player));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(plugin.getConfigManager().getError("notFoundId", null, player));
            return;
        }

        int id;
        try {
            id = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException ignore) {
            player.sendMessage(plugin.getConfigManager().getError("invalidId", args[1], player));
            return;
        }


        if (args[0].equalsIgnoreCase("clone")) {
            if (plugin.getDisplaysMap().containsKey(id)) {
                player.sendMessage(plugin.getConfigManager().getError("idAlreadyInUse", String.valueOf(id), player));
                return;
            }

            if (args.length < 3) {
                player.sendMessage(plugin.getConfigManager().getError("notFoundId", null, player));
                return;
            }

            int cloneId;
            try {
                cloneId = Integer.parseInt(args[2]);
            }
            catch (NumberFormatException ignore) {
                player.sendMessage(plugin.getConfigManager().getError("invalidId", args[2], player));
                return;
            }
            Display cloneEntity = plugin.getDisplaysMap().get(cloneId);

            if (!plugin.getDisplaysMap().containsKey(cloneId) || cloneEntity.isDead()) {
                player.sendMessage(plugin.getConfigManager().getError("idDoesNotExist", String.valueOf(id), player));
                return;
            }


            Location location = player.getLocation();

            Display entity = null;

            switch (cloneEntity.getType()) {
                case BLOCK_DISPLAY -> {
                    location = new Location(location.getWorld(), location.getX() - 0.5, location.getY(), location.getZ() - 0.5);
                    entity = (BlockDisplay) location.getWorld().spawnEntity(location, EntityType.BLOCK_DISPLAY);
                    ((BlockDisplay) entity).setBlock(((BlockDisplay) cloneEntity).getBlock());
                }

                case ITEM_DISPLAY -> {
                    location = new Location(location.getWorld(), location.getX(), location.getY() + 0.5, location.getZ());
                    entity = (ItemDisplay) location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
                    ((ItemDisplay) entity).setItemStack(((ItemDisplay) cloneEntity).getItemStack());
                }

                case TEXT_DISPLAY -> {
                    location = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
                    entity = (TextDisplay) location.getWorld().spawnEntity(location, EntityType.TEXT_DISPLAY);

                    String cloneText = cloneEntity.getPersistentDataContainer().get(plugin.getNskDisplayText(), PersistentDataType.STRING);
                    assert cloneText != null;

                    PersistentDataContainer data = entity.getPersistentDataContainer();
                    data.set(plugin.getNskDisplayText(), PersistentDataType.STRING, cloneText);
                    ((TextDisplay) entity).text(Component.text(cloneText));
                }
            }

            assert entity != null;
            entity.setGlowing(cloneEntity.isGlowing());
            entity.setTransformation(cloneEntity.getTransformation());

            plugin.getDisplaysMap().put(id, entity);
            PersistentDataContainer data = entity.getPersistentDataContainer();
            data.set(plugin.getNskDisplayId(), PersistentDataType.INTEGER, id);

            player.sendMessage(plugin.getConfigManager().getSuccessfully("create.clone", String.valueOf(id), player));
        }

        else {
            if (args.length > 2) {
                player.sendMessage(plugin.getConfigManager().getError("tooManyArguments", null, player));
                return;
            }

            if (plugin.getDisplaysMap().containsKey(id)) {
                player.sendMessage(plugin.getConfigManager().getError("idAlreadyInUse", String.valueOf(id), player));
                return;
            }


            Display entity = null;
            Location location = player.getLocation();

            switch (args[0]) {
                case "block" -> {
                    location = new Location(location.getWorld(), location.getX() - 0.5, location.getY(), location.getZ() - 0.5);
                    entity = (BlockDisplay) player.getWorld().spawnEntity(location, EntityType.BLOCK_DISPLAY);
                    try {
                        ((BlockDisplay) entity).setBlock(Bukkit.createBlockData(Material.valueOf(plugin.getConfigManager().getDefaultValue("block").toUpperCase())));
                    }
                    catch (IllegalArgumentException ignore) {
                        ((BlockDisplay) entity).setBlock(Bukkit.createBlockData(Material.STONE));
                    }
                }

                case "item" -> {
                    location = new Location(location.getWorld(), location.getX(), location.getY() + 0.5, location.getZ());
                    entity = (ItemDisplay) player.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
                    try {
                        ((ItemDisplay) entity).setItemStack(new ItemStack(Material.valueOf(plugin.getConfigManager().getDefaultValue("item").toUpperCase())));
                    }
                    catch (IllegalArgumentException ignore) {
                        ((ItemDisplay) entity).setItemStack(new ItemStack(Material.APPLE));
                    }
                }

                case "text" -> {
                    location = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
                    entity = (TextDisplay) player.getWorld().spawnEntity(location, EntityType.TEXT_DISPLAY);
                    PersistentDataContainer data = entity.getPersistentDataContainer();
                    String text = plugin.getConfigManager().getDefaultValue("text");
                    data.set(plugin.getNskDisplayText(), PersistentDataType.STRING, text);
                    ((TextDisplay) entity).text(MiniMessage.miniMessage().deserialize(text));
                }
            }

            assert entity != null;
            plugin.getDisplaysMap().put(id, entity);
            PersistentDataContainer data = entity.getPersistentDataContainer();
            data.set(plugin.getNskDisplayId(), PersistentDataType.INTEGER, id);

            player.sendMessage(plugin.getConfigManager().getSuccessfully("create.new", String.valueOf(id), player));
        }
    }

    @Override
    public ArrayList<String> onTabComplete(Player player, String[] args) {
        if (args.length == 1) return Lists.newArrayList("block", "item", "text", "clone");
        if (args.length == 2) return Lists.newArrayList("<new_id>");
        if (args.length == 3 && args[0].equalsIgnoreCase("clone")) return getIDs();
        return new ArrayList<>();
    }
}
