package me.itzisonn_.display.commands;

import me.itzisonn_.display.Display;
import me.itzisonn_.display.Utils;
import me.itzisonn_.display.Config;
import me.itzisonn_.display.maincommand.DisplayCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class Create {
    private static final Player player = DisplayCommand.getPlayer();
    private static final HashMap<Integer, Entity> displays = Display.getDisplays();



    public static void createObject(int id, EntityType type) {
        Entity entity = null;
        Location location = player.getLocation();

        if (type == EntityType.BLOCK_DISPLAY) {
            location = new Location(location.getWorld(), location.getX() - 0.5, location.getY(), location.getZ() - 0.5);
            entity = player.getWorld().spawnEntity(location, EntityType.BLOCK_DISPLAY);
            ((BlockDisplay) entity).setBlock(Bukkit.createBlockData(Material.STONE));
        }
        else if (type == EntityType.ITEM_DISPLAY) {
            location = new Location(location.getWorld(), location.getX(), location.getY() + 0.5, location.getZ());
            entity = player.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
            ((ItemDisplay)entity).setItemStack(new ItemStack(Material.APPLE));
        }
        else if (type == EntityType.TEXT_DISPLAY) {
            entity = player.getWorld().spawnEntity(location, EntityType.TEXT_DISPLAY);
            NamespacedKey namespacedKey = new NamespacedKey(Display.getInstance(), "displayText");
            PersistentDataContainer data = entity.getPersistentDataContainer();
            data.set(namespacedKey, PersistentDataType.STRING, "Text");
            ((TextDisplay)entity).text(Component.text("Text"));
        }

        assert entity != null;
        displays.put(id, entity);
        NamespacedKey namespacedKey = new NamespacedKey(Display.getInstance(), "displayID");
        PersistentDataContainer data = entity.getPersistentDataContainer();
        data.set(namespacedKey, PersistentDataType.INTEGER, id);
    }



    public static boolean checkCreate(String[] args) {
        if (player.hasPermission("display.command.create") || player.hasPermission("display.command.*")) {
            if (args.length < 4) {
                if (args.length >= 2) {
                    if (args[1].equalsIgnoreCase("block") || args[1].equalsIgnoreCase("item") || args[1].equalsIgnoreCase("text")) {
                        if (args.length == 3) {
                            if (Utils.isInt(args[2])) {
                                int id = Integer.parseInt(args[2]);

                                if (!(displays.containsKey(id))) {
                                    player.sendMessage(Config.getMsg("messages.successfully.created", new String[]{String.valueOf(id), args[1]}));
                                    return true;
                                }
                                else {
                                    player.sendMessage(Config.getMsg("messages.errors.idAlreadyInUse", new String[]{String.valueOf(id)}));
                                    return false;
                                }
                            }
                            else {
                                player.sendMessage(Config.getMsg("messages.errors.invalidId", null));
                                return false;
                            }
                        }
                        else {
                            player.sendMessage(Config.getMsg("messages.errors.notFoundId", null));
                            return false;
                        }
                    }
                    else {
                        player.sendMessage(Config.getMsg("messages.errors.unknownObjectType", null));
                        return false;
                    }
                }
                else {
                    player.sendMessage(Config.getMsg("messages.errors.notFoundObjectType", null));
                    return false;
                }
            }
            else {
                player.sendMessage(Config.getMsg("messages.errors.tooManyArguments", null));
                return false;
            }
        }
        else {
            player.sendMessage(Config.getMsg("messages.errors.unknownAction", null));
            return false;
        }
    }
}