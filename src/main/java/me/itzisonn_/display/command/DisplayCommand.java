package me.itzisonn_.display.command;

import me.clip.placeholderapi.PlaceholderAPI;
import me.itzisonn_.display.DisplayPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Objects;
import java.util.UUID;

public class DisplayCommand implements CommandExecutor {
    private final DisplayPlugin plugin;

    public DisplayCommand(DisplayPlugin plugin) {
        PluginCommand pluginCommand = plugin.getCommand("display");
        if (pluginCommand != null) pluginCommand.setExecutor(this);

        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        execute(sender, args);
        return true;
    }


    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getConfigManager().getError("onlyPlayer", null, null));
            return;
        }

        if (!player.hasPermission("display.*") && !player.hasPermission("display.help") && !player.hasPermission("display.reload") &&
                !player.hasPermission("display.create") && !player.hasPermission("display.load") && !player.hasPermission("display.delete") &&
                !player.hasPermission("display.edit") && !player.hasPermission("display.tp")) {
            player.sendMessage(plugin.getConfigManager().getError("noPermission", null, player));
            return;
        }

        if (args.length == 0) {
            player.sendMessage(plugin.getConfigManager().getError("notFull", null, player));
            return;
        }



        switch (args[0].toLowerCase()) {
            case "help" -> help(player, args);
            case "reload" -> reload(player, args);
            case "create" -> create(player, args);
            case "load" -> load(player, args);
            case "delete" -> delete(player, args);
            case "list" -> list(player, args);
            case "edit" -> edit(player, args);
            case "tp" -> teleport(player, args);
            default -> player.sendMessage(plugin.getConfigManager().getError("unknownAction", null, player));
        }
    }





    private void help(Player player, String[] args) {
        if (!player.hasPermission("display.*") && !player.hasPermission("display.help")) {
            player.sendMessage(plugin.getConfigManager().getError("unknownAction", null, player));
            return;
        }

        if (args.length > 1) {
            player.sendMessage(plugin.getConfigManager().getError("tooManyAgruments", null, player));
            return;
        }


        plugin.getConfigManager().getInfo().forEach(message -> player.sendMessage(MiniMessage.miniMessage().deserialize(message)));
    }



    private void reload(Player player, String[] args) {
        if (!player.hasPermission("display.*") && !player.hasPermission("display.reload")) {
            player.sendMessage(plugin.getConfigManager().getError("unknownAction", null, player));
            return;
        }

        if (args.length > 1) {
            player.sendMessage(plugin.getConfigManager().getError("tooManyAgruments", null, player));
            return;
        }

        
        plugin.getConfigManager().reloadConfig();
        plugin.getUtils().getScheduler().cancel();
        plugin.getUtils().startTextUpdating();

        player.sendMessage(plugin.getConfigManager().getSuccessfully("reload", null, player));
    }



    private void create(Player player, String[] args) {
        if (!player.hasPermission("display.*") && !player.hasPermission("display.create")) {
            player.sendMessage(plugin.getConfigManager().getError("unknownAction", null, player));
            return;
        }

        if (args.length > 4) {
            player.sendMessage(plugin.getConfigManager().getError("tooManyAgruments", null, player));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(plugin.getConfigManager().getError("notFoundObjectType", null, player));
            return;
        }

        if (!args[1].equalsIgnoreCase("block") && !args[1].equalsIgnoreCase("item") && !args[1].equalsIgnoreCase("text") && !args[1].equalsIgnoreCase("clone")) {
            player.sendMessage(plugin.getConfigManager().getError("unknownObjectType", null, player));
            return;
        }

        if (args.length < 3) {
            player.sendMessage(plugin.getConfigManager().getError("notFoundId", null, player));
            return;
        }

        int id;
        try {
            id = Integer.parseInt(args[2]);
        }
        catch (NumberFormatException ignore) {
            player.sendMessage(plugin.getConfigManager().getError("invalidId", args[2], player));
            return;
        }

        
        if (args[1].equalsIgnoreCase("clone")) {
            if (plugin.getDisplaysMap().containsKey(id)) {
                player.sendMessage(plugin.getConfigManager().getError("idAlreadyInUse", String.valueOf(id), player));
                return;
            }

            if (args.length < 4) {
                player.sendMessage(plugin.getConfigManager().getError("notFoundId", null, player));
                return;
            }

            int cloneId;
            try {
                cloneId = Integer.parseInt(args[3]);
            }
            catch (NumberFormatException ignore) {
                player.sendMessage(plugin.getConfigManager().getError("invalidId", args[3], player));
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
            if (args.length > 3) {
                player.sendMessage(plugin.getConfigManager().getError("tooManyAgruments", null, player));
                return;
            }

            if (plugin.getDisplaysMap().containsKey(id)) {
                player.sendMessage(plugin.getConfigManager().getError("idAlreadyInUse", String.valueOf(id), player));
                return;
            }


            Display entity = null;
            Location location = player.getLocation();

            switch (args[1]) {
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



    private void load(Player player, String[] args) {
        if (!player.hasPermission("display.*") && !player.hasPermission("display.load")) {
            player.sendMessage(plugin.getConfigManager().getError("unknownAction", null, player));
            return;
        }

        if (args.length > 3) {
            player.sendMessage(plugin.getConfigManager().getError("tooManyAgruments", null, player));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(plugin.getConfigManager().getError("notFoundUuid", null, player));
            return;
        }

        if (args.length < 3) {
            player.sendMessage(plugin.getConfigManager().getError("notFoundId", null, player));
            return;
        }

        int id;
        try {
            id = Integer.parseInt(args[2]);
        }
        catch (NumberFormatException ignore) {
            player.sendMessage(plugin.getConfigManager().getError("invalidId", args[2], player));
            return;
        }

        if (plugin.getDisplaysMap().containsKey(id)) {
            player.sendMessage(plugin.getConfigManager().getError("idAlreadyInUse", String.valueOf(id), player));
            return;
        }

        Display display;
        try {
            Entity entity = Bukkit.getEntity(UUID.fromString(args[1]));
            if (entity instanceof Display) display = (Display) entity;
            else {
                player.sendMessage(plugin.getConfigManager().getError("invalidEntity", String.valueOf(id), player));
                return;
            }
        }
        catch (IllegalArgumentException ignore) {
            player.sendMessage(plugin.getConfigManager().getError("invalidUuid", String.valueOf(id), player));
            return;
        }

        if (plugin.getDisplaysMap().containsValue(display)) {
            player.sendMessage(plugin.getConfigManager().getError("entityAlreadyInUse", String.valueOf(id), player));
            return;
        }

        plugin.getDisplaysMap().put(id, display);
        PersistentDataContainer data = display.getPersistentDataContainer();
        data.set(plugin.getNskDisplayId(), PersistentDataType.INTEGER, id);

        String message = MiniMessage.miniMessage().serialize(plugin.getConfigManager().getSuccessfully("load", String.valueOf(id), player));
        player.sendMessage(MiniMessage.miniMessage().deserialize(message
                .replace("%uuid%", args[1])));
    }



    private void delete(Player player, String[] args) {
        if (!player.hasPermission("display.*") && !player.hasPermission("display.delete")) {
            player.sendMessage(plugin.getConfigManager().getError("unknownAction", null, player));
            return;
        }

        if (args.length > 2) {
            player.sendMessage(plugin.getConfigManager().getError("tooManyAgruments", null, player));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(plugin.getConfigManager().getError("notFoundId", null, player));
            return;
        }

        if (args[1].equals("*")) {
            for (int id : plugin.getDisplaysMap().keySet()) {
                Entity entity = plugin.getDisplaysMap().get(id);
                entity.remove();
            }
            plugin.getDisplaysMap().clear();

            player.sendMessage(plugin.getConfigManager().getSuccessfully("delete.all", null, player));
        }
        else {
            int id;
            try {
                id = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException ignore) {
                player.sendMessage(plugin.getConfigManager().getError("invalidId", args[1], player));
                return;
            }

            Entity entity = plugin.getDisplaysMap().get(id);

            if (!plugin.getDisplaysMap().containsKey(id) || entity.isDead()) {
                player.sendMessage(plugin.getConfigManager().getError("idDoesNotExist", String.valueOf(id), player));
                return;
            }

            entity.remove();
            plugin.getDisplaysMap().remove(id);

            player.sendMessage(plugin.getConfigManager().getSuccessfully("delete.id", String.valueOf(id), player));
        }
    }



    private void list(Player player, String[] args) {
        if (!player.hasPermission("display.*") && !player.hasPermission("display.help")) {
            player.sendMessage(plugin.getConfigManager().getError("unknownAction", null, player));
            return;
        }

        if (args.length > 1) {
            player.sendMessage(plugin.getConfigManager().getError("tooManyAgruments", null, player));
            return;
        }


        if (plugin.getDisplaysMap().isEmpty()) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(
                    plugin.getConfigManager().getListEmpty().replace("%prefix%", plugin.getConfigManager().getPrefix())));
            return;
        }

        player.sendMessage(MiniMessage.miniMessage().deserialize(
                plugin.getConfigManager().getListTitle().replace("%prefix%", plugin.getConfigManager().getPrefix())));
        int pos = 1;
        for (int id : plugin.getDisplaysMap().keySet()) {
            Display entity = plugin.getDisplaysMap().get(id);
            player.sendMessage(MiniMessage.miniMessage().deserialize(
                    plugin.getConfigManager().getListFormat()
                            .replace("%pos%", String.valueOf(pos))
                            .replace("%type%", entity.getType().name())
                            .replace("%id%", String.valueOf(id))));
            pos++;
        }
    }
    
    

    @SuppressWarnings("deprecation")
    private void edit(Player player, String[] args) {
        if (!player.hasPermission("display.*") && !player.hasPermission("display.edit")) {
            player.sendMessage(plugin.getConfigManager().getError("unknownAction", null, player));
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

        Display entity = plugin.getDisplaysMap().get(id);

        if (!plugin.getDisplaysMap().containsKey(id) || entity.isDead()) {
            player.sendMessage(plugin.getConfigManager().getError("idDoesNotExist", String.valueOf(id), player));
            return;
        }

        if (args.length < 3) {
            player.sendMessage(plugin.getConfigManager().getError("notFoundEditType", String.valueOf(id), player));
            return;
        }

        String type = args[2];

        if (args.length < 4) {
            player.sendMessage(plugin.getConfigManager().getError("notFoundEditValue", String.valueOf(id), player));
            return;
        }

        StringBuilder value = new StringBuilder();
        for (int i = 3; i < args.length; i++) {
            value.append(args[i]);
        }

        switch (type) {
            case "id" -> {
                try {
                    int newId = Integer.parseInt(value.toString());
                    plugin.getDisplaysMap().remove(id);
                    plugin.getDisplaysMap().put(newId, entity);

                    PersistentDataContainer data = entity.getPersistentDataContainer();
                    data.set(plugin.getNskDisplayId(), PersistentDataType.INTEGER, newId);
                }
                catch (NumberFormatException ignore) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
                    return;
                }
            }

            case "scale" -> {
                if (value.toString().equals("?")) {
                    String message = MiniMessage.miniMessage().serialize(plugin.getConfigManager().getSuccessfully("edit.info", String.valueOf(id), player));
                    player.sendMessage(MiniMessage.miniMessage().deserialize(message
                            .replace("%type%", type)
                            .replace("%value%", entity.getTransformation().getScale().x + "," + entity.getTransformation().getScale().y + "," + entity.getTransformation().getScale().z)));
                    return;
                }

                String[] scale = value.toString().split(",");
                if (scale.length != 3) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
                    return;
                }

                try {
                    entity.setTransformation(new Transformation(new Vector3f(), new Quaternionf(),
                            new Vector3f(Float.parseFloat(scale[0]), Float.parseFloat(scale[1]), Float.parseFloat(scale[2])), new Quaternionf()));
                }
                catch (NumberFormatException ignore) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
                    return;
                }
            }

            case "billboard" -> {
                if (value.toString().equals("?")) {
                    String message = MiniMessage.miniMessage().serialize(plugin.getConfigManager().getSuccessfully("edit.info", String.valueOf(id), player));
                    player.sendMessage(MiniMessage.miniMessage().deserialize(message
                            .replace("%type%", type)
                            .replace("%value%", entity.getBillboard().name())));
                    return;
                }

                try {
                    entity.setBillboard(Display.Billboard.valueOf(value.toString().toUpperCase()));
                }
                catch (IllegalArgumentException ignore) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
                    return;
                }
            }

            case "brightness" -> {
                if (value.toString().equals("?")) {
                    String message = MiniMessage.miniMessage().serialize(plugin.getConfigManager().getSuccessfully("edit.info", String.valueOf(id), player));
                    player.sendMessage(MiniMessage.miniMessage().deserialize(message
                            .replace("%type%", type)
                            .replace("%value%", entity.getBrightness() == null ? "?" : entity.getBrightness().getBlockLight() + "," + entity.getBrightness().getSkyLight())));
                    return;
                }

                String[] brightness = value.toString().split(",");
                if (brightness.length != 2) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
                    return;
                }

                try {
                    entity.setBrightness(new Display.Brightness(Integer.parseInt(brightness[0]), Integer.parseInt(brightness[1])));
                }
                catch (IllegalArgumentException ignore) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
                    return;
                }
            }

            case "shadow" -> {
                if (value.toString().equals("?")) {
                    String message = MiniMessage.miniMessage().serialize(plugin.getConfigManager().getSuccessfully("edit.info", String.valueOf(id), player));
                    player.sendMessage(MiniMessage.miniMessage().deserialize(message
                            .replace("%type%", type)
                            .replace("%value%", entity.getShadowRadius() + "," + entity.getShadowStrength())));
                    return;
                }

                String[] shadow = value.toString().split(",");
                if (shadow.length != 2) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
                    return;
                }

                try {
                    entity.setShadowRadius(Float.parseFloat(shadow[0]));
                    entity.setShadowStrength(Float.parseFloat(shadow[1]));
                }
                catch (IllegalArgumentException ignore) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
                    return;
                }
            }

            case "view_range" -> {
                if (value.toString().equals("?")) {
                    String message = MiniMessage.miniMessage().serialize(plugin.getConfigManager().getSuccessfully("edit.info", String.valueOf(id), player));
                    player.sendMessage(MiniMessage.miniMessage().deserialize(message
                            .replace("%type%", type)
                            .replace("%value%", String.valueOf(entity.getViewRange()))));
                    return;
                }

                try {
                    entity.setViewRange(Float.parseFloat(value.toString()));
                }
                catch (IllegalArgumentException ignore) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
                    return;
                }
            }

            case "material" -> {
                if (entity.getType() == EntityType.TEXT_DISPLAY) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditType", String.valueOf(id), player));
                    return;
                }

                if (value.toString().equals("?")) {
                    String message = MiniMessage.miniMessage().serialize(plugin.getConfigManager().getSuccessfully("edit.info", String.valueOf(id), player));
                    String infoValue = "?";
                    if (entity instanceof BlockDisplay blockDisplay) infoValue = blockDisplay.getBlock().getMaterial().toString().toLowerCase();
                    else if (entity instanceof ItemDisplay itemDisplay) infoValue = Objects.requireNonNull(itemDisplay.getItemStack()).getType().toString().toLowerCase();
                    player.sendMessage(MiniMessage.miniMessage().deserialize(message
                            .replace("%type%", type)
                            .replace("%value%", infoValue)));
                    return;
                }

                Material material;
                try {
                    material = Material.valueOf(value.toString().toUpperCase());
                }
                catch (IllegalArgumentException ignore) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
                    return;
                }

                if (entity.getType() == EntityType.BLOCK_DISPLAY) {
                    if (!material.isBlock()) {
                        player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
                        return;
                    }
                    if (material == Material.CAVE_AIR || material == Material.AIR || material == Material.BARRIER) {
                        player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
                        return;
                    }
                    ((BlockDisplay) entity).setBlock(Bukkit.createBlockData(material));
                }
                else if (entity.getType() == EntityType.ITEM_DISPLAY) {
                    if (!material.isItem()) {
                        player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
                        return;
                    }
                    ((ItemDisplay) entity).setItemStack(new ItemStack(material));
                }
            }

            case "glowing" -> {
                if (entity.getType() == EntityType.TEXT_DISPLAY) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditType", String.valueOf(id), player));
                    return;
                }

                if (value.toString().equals("?")) {
                    String message = MiniMessage.miniMessage().serialize(plugin.getConfigManager().getSuccessfully("edit.info", String.valueOf(id), player));
                    String infoValue = entity.getGlowColorOverride() == null ? "255,255,255" :
                            entity.getGlowColorOverride().getRed() + "," + entity.getGlowColorOverride().getGreen() + "," + entity.getGlowColorOverride().getBlue();
                    player.sendMessage(MiniMessage.miniMessage().deserialize(message
                            .replace("%type%", type)
                            .replace("%value%", entity.isGlowing() + ";" + infoValue)));
                    return;
                }

                if (value.toString().equalsIgnoreCase("on")) entity.setGlowing(true);
                else if (value.toString().equalsIgnoreCase("off")) entity.setGlowing(false);
                else {
                    String[] color = value.toString().split(",");
                    if (color.length != 3) {
                        player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
                        return;
                    }

                    try {
                        entity.setGlowColorOverride(Color.fromRGB(Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2])));
                    }
                    catch (NumberFormatException ignore) {
                        player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
                        return;
                    }
                }
            }

            case "display_transform" -> {
                if (entity.getType() != EntityType.ITEM_DISPLAY) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditType", String.valueOf(id), player));
                    return;
                }

                if (value.toString().equals("?")) {
                    String message = MiniMessage.miniMessage().serialize(plugin.getConfigManager().getSuccessfully("edit.info", String.valueOf(id), player));
                    player.sendMessage(MiniMessage.miniMessage().deserialize(message
                            .replace("%type%", type)
                            .replace("%value%", ((ItemDisplay) entity).getItemDisplayTransform().name())));
                    return;
                }

                try {
                    ((ItemDisplay) entity).setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.valueOf(value.toString().toUpperCase()));
                }
                catch (IllegalArgumentException ignore) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
                    return;
                }
            }

            case "text" -> {
                if (entity.getType() != EntityType.TEXT_DISPLAY) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditType", String.valueOf(id), player));
                    return;
                }

                if (value.toString().equals("?")) {
                    String message = MiniMessage.miniMessage().serialize(plugin.getConfigManager().getSuccessfully("edit.info", String.valueOf(id), player));
                    player.sendMessage(MiniMessage.miniMessage().deserialize(message
                            .replace("%type%", type)
                            .replace("%value%", plugin.getPlayersEditingMap().get(player) != null ? "on" : "off")));
                    return;
                }

                boolean isText;
                if (value.toString().equalsIgnoreCase("on")) isText = true;
                else if (value.toString().equalsIgnoreCase("off")) isText = false;
                else {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
                    return;
                }

                if (isText) {
                    plugin.getPlayersEditingMap().put(player, id);

                    PersistentDataContainer data = entity.getPersistentDataContainer();
                    String text = data.get(plugin.getNskDisplayText(), PersistentDataType.STRING);

                    if (text != null) {
                        String message = "<click:copy_to_clipboard:'" + text + "'><click:suggest_command:'" + text + "'><hover:show_text:'";

                        if (plugin.isHookedPapi()) message += PlaceholderAPI.setPlaceholders(player, text);
                        else message += text;

                        player.sendMessage(MiniMessage.miniMessage().deserialize(message + "'>" + plugin.getConfigManager().getEditingText()));
                    }

                }
                else plugin.getPlayersEditingMap().remove(player);
            }

            case "alignment" -> {
                if (entity.getType() != EntityType.TEXT_DISPLAY) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditType", String.valueOf(id), player));
                    return;
                }

                if (value.toString().equals("?")) {
                    String message = MiniMessage.miniMessage().serialize(plugin.getConfigManager().getSuccessfully("edit.info", String.valueOf(id), player));
                    player.sendMessage(MiniMessage.miniMessage().deserialize(message
                            .replace("%type%", type)
                            .replace("%value%", ((TextDisplay) entity).getAlignment().name())));
                    return;
                }

                try {
                    ((TextDisplay) entity).setAlignment(TextDisplay.TextAlignment.valueOf(value.toString().toUpperCase()));
                }
                catch (IllegalArgumentException ignore) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
                    return;
                }
            }

            case "background" -> {
                if (entity.getType() != EntityType.TEXT_DISPLAY) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditType", String.valueOf(id), player));
                    return;
                }

                if (value.toString().equals("?")) {
                    String message = MiniMessage.miniMessage().serialize(plugin.getConfigManager().getSuccessfully("edit.info", String.valueOf(id), player));
                    String infoValue = ((TextDisplay) entity).getBackgroundColor() == null ? "255,255,255" :
                            ((TextDisplay) entity).getBackgroundColor().getRed() + "," + ((TextDisplay) entity).getBackgroundColor().getGreen() + "," + ((TextDisplay) entity).getBackgroundColor().getBlue();
                    player.sendMessage(MiniMessage.miniMessage().deserialize(message
                            .replace("%type%", type)
                            .replace("%value%", infoValue)));
                    return;
                }

                String[] color = value.toString().split(",");
                if (color.length != 3) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
                    return;
                }

                try {
                    ((TextDisplay) entity).setBackgroundColor(Color.fromRGB(Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2])));
                }
                catch (NumberFormatException ignore) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
                    return;
                }
            }

            case "line_width" -> {
                if (entity.getType() != EntityType.TEXT_DISPLAY) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditType", String.valueOf(id), player));
                    return;
                }

                if (value.toString().equals("?")) {
                    String message = MiniMessage.miniMessage().serialize(plugin.getConfigManager().getSuccessfully("edit.info", String.valueOf(id), player));
                    player.sendMessage(MiniMessage.miniMessage().deserialize(message
                            .replace("%type%", type)
                            .replace("%value%", String.valueOf(((TextDisplay) entity).getLineWidth()))));
                    return;
                }

                try {
                    ((TextDisplay) entity).setLineWidth(Integer.parseInt(value.toString()));
                }
                catch (NumberFormatException ignore) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
                    return;
                }
            }

            case "see_through" -> {
                if (entity.getType() != EntityType.TEXT_DISPLAY) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditType", String.valueOf(id), player));
                    return;
                }

                if (value.toString().equals("?")) {
                    String message = MiniMessage.miniMessage().serialize(plugin.getConfigManager().getSuccessfully("edit.info", String.valueOf(id), player));
                    player.sendMessage(MiniMessage.miniMessage().deserialize(message
                            .replace("%type%", type)
                            .replace("%value%", String.valueOf(((TextDisplay) entity).isSeeThrough()))));
                    return;
                }

                try {
                    ((TextDisplay) entity).setSeeThrough(plugin.getUtils().parseBoolean(value.toString()));
                }
                catch (IllegalArgumentException ignore) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
                    return;
                }
            }

            case "text_opacity" -> {
                if (entity.getType() != EntityType.TEXT_DISPLAY) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditType", String.valueOf(id), player));
                    return;
                }

                if (value.toString().equals("?")) {
                    String message = MiniMessage.miniMessage().serialize(plugin.getConfigManager().getSuccessfully("edit.info", String.valueOf(id), player));
                    player.sendMessage(MiniMessage.miniMessage().deserialize(message
                            .replace("%type%", type)
                            .replace("%value%", String.valueOf(((TextDisplay) entity).getTextOpacity()))));
                    return;
                }

                try {
                    ((TextDisplay) entity).setTextOpacity(Byte.parseByte(value.toString()));
                }
                catch (IllegalArgumentException ignore) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
                    return;
                }
            }

            default -> {
                player.sendMessage(plugin.getConfigManager().getError("invalidEditType", String.valueOf(id), player));
                return;
            }
        }

        player.sendMessage(plugin.getConfigManager().getSuccessfully("edit.edit", String.valueOf(id), player));
    }



    private void teleport(Player player, String[] args) {
        if (!player.hasPermission("display.*") && !player.hasPermission("display.tp")) {
            player.sendMessage(plugin.getConfigManager().getError("unknownAction", null, player));
            return;
        }

        if (args.length > 7) {
            player.sendMessage(plugin.getConfigManager().getError("tooManyAgruments", null, player));
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

        Display entity = plugin.getDisplaysMap().get(id);

        if (!plugin.getDisplaysMap().containsKey(id) || entity.isDead()) {
            player.sendMessage(plugin.getConfigManager().getError("idDoesNotExist", String.valueOf(id), player));
            return;
        }

        if (args.length < 3) {
            player.sendMessage(plugin.getConfigManager().getError("notFoundTeleportType", String.valueOf(id), player));
            return;
        }

        switch (args[2].toLowerCase()) {
            case "pos" -> {
                if (args.length < 4) {
                    player.sendMessage(plugin.getConfigManager().getError("notFoundDimension", String.valueOf(id), player));
                    return;
                }

                World world = Bukkit.getWorld(args[3]);

                if (world == null) {
                    player.sendMessage(plugin.getConfigManager().getError("uknownDimension", String.valueOf(id), player));
                    return;
                }

                if (args.length < 7) {
                    player.sendMessage(plugin.getConfigManager().getError("notFoundCoords", String.valueOf(id), player));
                    return;
                }

                double x, y, z;

                try {
                    x = Double.parseDouble(args[4]);
                    y = Double.parseDouble(args[5]);
                    z = Double.parseDouble(args[6]);
                }
                catch (NumberFormatException ignore) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidCoords", String.valueOf(id), player));
                    return;
                }

                entity.teleport(new Location(world, x, y, z));

                player.sendMessage(plugin.getConfigManager().getSuccessfully("teleport.pos", String.valueOf(id), player));
            }

            case "here" -> {
                if (args.length > 3) {
                    player.sendMessage(plugin.getConfigManager().getError("tooManyAgruments", String.valueOf(id), player));
                    return;
                }

                Location location = player.getLocation();
                location.setYaw(0);
                location.setPitch(0);
                entity.teleport(location);

                player.sendMessage(plugin.getConfigManager().getSuccessfully("teleport.here", String.valueOf(id), player));
            }

            case "to" -> {
                if (args.length > 3) {
                    player.sendMessage(plugin.getConfigManager().getError("tooManyAgruments", String.valueOf(id), player));
                    return;
                }

                Location location = entity.getLocation();
                location.setYaw(player.getLocation().getYaw());
                location.setPitch(player.getLocation().getPitch());
                player.teleport(location);

                player.sendMessage(plugin.getConfigManager().getSuccessfully("teleport.to", String.valueOf(id), player));
            }

            default -> player.sendMessage(plugin.getConfigManager().getError("uknownTeleportType", String.valueOf(id), player));
        }
    }
}