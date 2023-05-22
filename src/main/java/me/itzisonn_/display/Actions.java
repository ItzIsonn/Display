package me.itzisonn_.display;

import me.itzisonn_.display.command.DisplayCommand;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Actions {
    private static final Player player = DisplayCommand.getPlayer();
    private static final HashMap<Integer, Entity> displays = Storage.getDisplays();

    public static void sendHelp() {
        player.sendMessage(useHex("&#898989[&#fdd134D&#fdbf2ei&#fcac27s&#fc9a21p&#fc881bl&#fb7514a&#fb630ey&#898989] &#fb630eПомощь по команде /display"));
        player.sendMessage(useHex("&#fdd134/display help &8- &#d0d0d0Показать помощь"));
        player.sendMessage(useHex("&#fdd134/display create [block | item] <id> &8- &#d0d0d0Создать объект для отображения"));
        player.sendMessage(useHex("&#fdd134/display delete <id> &8- &#d0d0d0Удалить объект с указанным ID"));
        player.sendMessage(useHex("&#fdd134/display edit <id> ... &8- &#d0d0d0Изменить параметры у объекта с указанным ID"));
        player.sendMessage(useHex("  &#fcac27• blocktype (itemtype) <type> &8- &#d0d0d0Изменить тип блока (предмета)"));
        player.sendMessage(useHex("  &#fcac27• glowing &8- &#d0d0d0Изменить подсветку на противоположное значение"));
        player.sendMessage(useHex("&#fdd134/display tphere <id> &8- &#d0d0d0Переместить объект с указанным ID на координаты исполнителя"));
        player.sendMessage(useHex("&#fdd134/display tpcoords <id> <world> <x> <y> <z> &8- &#d0d0d0Переместить объект с указанным ID на укзанные координаты"));
        player.sendMessage(useHex("&#fdd134/display tpto <id> &8- &#d0d0d0Переместить исполнителя на координаты объекта с указанным ID"));
    }

    public static void createObject(Integer displayID, EntityType type) {
        NamespacedKey namespacedKey = new NamespacedKey(Display.getInstance(), "displayUUID");
        String displayUUID = String.valueOf(UUID.randomUUID());

        if (type == EntityType.BLOCK_DISPLAY) {
            Location location = new Location(player.getWorld(), player.getLocation().getX() - 0.5, player.getLocation().getY(), player.getLocation().getZ() - 0.5);
            BlockDisplay entity = (BlockDisplay) player.getWorld().spawnEntity(location, EntityType.BLOCK_DISPLAY);
            entity.setBlock(Bukkit.createBlockData(Material.STONE));
            displays.put(displayID, entity);
            PersistentDataContainer data = entity.getPersistentDataContainer();
            data.set(namespacedKey, PersistentDataType.STRING, displayUUID);

            Storage.getConfig().set("displayEntities." + displayUUID + ".id", displayID);
            Storage.getConfig().set("displayEntities." + displayUUID + ".type", String.valueOf(entity.getType()));
        }
        else if (type == EntityType.ITEM_DISPLAY) {
            Location location = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() + 0.5, player.getLocation().getZ());
            ItemDisplay entity = (ItemDisplay) player.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
            entity.setItemStack(new ItemStack(Material.APPLE));
            displays.put(displayID, entity);
            PersistentDataContainer data = entity.getPersistentDataContainer();
            data.set(namespacedKey, PersistentDataType.STRING, displayUUID);

            Storage.getConfig().set("displayEntities." + displayUUID + ".id", displayID);
            Storage.getConfig().set("displayEntities." + displayUUID + ".type", String.valueOf(entity.getType()));
            Display.getData().save();
        }
    }

    public static void deleteObject(Integer displayID) {
        Entity entity = displays.get(displayID);
        entity.remove();
        displays.remove(displayID);

        NamespacedKey namespacedKey = new NamespacedKey(Display.getInstance(), "displayUUID");
        PersistentDataContainer data = entity.getPersistentDataContainer();
        String displayUUID = data.get(namespacedKey, PersistentDataType.STRING);

        Storage.getConfig().set("displayEntities." + displayUUID, null);
        Display.getData().save();
    }

    public static void editObject(Integer displayID, String[] args) {
        Entity entity = displays.get(displayID);

        if (entity.getType() == EntityType.BLOCK_DISPLAY) {
            BlockDisplay block = (BlockDisplay) entity;

            if (args[2].equalsIgnoreCase("blocktype")) {
                block.setBlock(Bukkit.createBlockData(Objects.requireNonNull(Material.getMaterial(args[3].toUpperCase()))));
            }
        }
        if (entity.getType() == EntityType.ITEM_DISPLAY) {
            ItemDisplay item = (ItemDisplay) entity;

            if (args[2].equalsIgnoreCase("itemtype")) {
                item.setItemStack(new ItemStack(Objects.requireNonNull(Material.getMaterial(args[3].toUpperCase()))));
            }
        }
        if (args[2].equalsIgnoreCase("glowing")) {
            boolean glow = !entity.isGlowing();
            entity.setGlowing(glow);
        }
    }

    public static void tpHere(Integer displayID) {
        Entity entity = displays.get(displayID);

        if (entity.getType() == EntityType.BLOCK_DISPLAY) {
            Location location = new Location(player.getWorld(), player.getLocation().getX() - 0.5, player.getLocation().getY(), player.getLocation().getZ() - 0.5);
            entity.teleport(location);
        }
        else if (entity.getType() == EntityType.ITEM_DISPLAY) {
            Location location = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() + 0.5, player.getLocation().getZ());
            entity.teleport(location);
        }
    }

    public static void tpCoords(Integer displayID, String world, double x, double y, double z) {
        Entity entity = displays.get(displayID);

        if (entity.getType() == EntityType.BLOCK_DISPLAY) {
            Location location = new Location(Bukkit.getWorld(world), x, y, z);
            entity.teleport(location);
        }
        else if (entity.getType() == EntityType.ITEM_DISPLAY) {
            Location location = new Location(Bukkit.getWorld(world), x + 0.5, y + 0.5, z + 0.5);
            entity.teleport(location);
        }
    }

    public static void tpTo(Integer displayID) {
        Entity entity = displays.get(displayID);

        if (entity.getType() == EntityType.BLOCK_DISPLAY) {
            Location location = new Location(entity.getWorld(), entity.getLocation().getX() + 0.5, entity.getLocation().getY(), entity.getLocation().getZ() + 0.5);
            player.teleport(location);
        }
        else if (entity.getType() == EntityType.ITEM_DISPLAY) {
            Location location = new Location(entity.getWorld(), entity.getLocation().getX(), entity.getLocation().getY() - 0.5, entity.getLocation().getZ());
            player.teleport(location);
        }
    }



    private static String useHex(String message) {
        Pattern pattern = Pattern.compile("(#[a-fA-F0-9]{6})");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();
            for (char c : ch) {
                builder.append("&").append(c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message).replace('&', '§');
    }
}