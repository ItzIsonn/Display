package me.itzisonn_.display.maincommand;

import me.clip.placeholderapi.PlaceholderAPI;
import me.itzisonn_.display.Display;
import me.itzisonn_.display.Utils;
import me.itzisonn_.display.Config;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;

public class DisplayCommand extends AbstractCommand {
    private final Display display;
    private final Utils utils;
    private final Config config;
    private final HashMap<Integer, Entity> displays;

    public DisplayCommand(Display display, Utils utils, Config config) {
        super("display", display);

        this.display = display;
        this.utils = utils;
        this.config = config;
        
        displays = display.displays;
    }

    private Player player;



    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(config.getMsg("messages.errors.onlyPlayer", null));
            return;
        }

        player = (Player) sender;

        if (!player.hasPermission("display.*") && !player.hasPermission("display.help") && !player.hasPermission("display.reload") &&
            !player.hasPermission("display.create") && !player.hasPermission("display.delete") && !player.hasPermission("display.edit") &&
            !player.hasPermission("display.tp") && /*!player.hasPermission("display.info") && !player.hasPermission("display.list") &&*/
            !player.hasPermission("display.clone")) {
            player.sendMessage(config.getMsg("messages.errors.noPermission", null));
            return;
        }

        if (args.length == 0) {
            player.sendMessage(config.getMsg("messages.errors.notFull", null));
            return;
        }



        switch (args[0].toLowerCase()) {
            case "help" -> sendHelp(args);
            case "reload" -> reloadConfig(args);
            case "create" -> createObject(args);
            case "delete" -> deleteObject(args);
            case "edit" -> edit(args);
            //case "info" -> showInfo(player, args);
            //case "list" -> showList(player, args);
            case "tp" -> teleport(args);
            case "clone" -> clone(args);
            default -> player.sendMessage(config.getMsg("messages.errors.unknownAction", null));
        }
    }





    private void sendHelp(String[] args) {
        if (!player.hasPermission("display.*") && !player.hasPermission("display.help")) {
            player.sendMessage(config.getMsg("messages.errors.unknownAction", null));
            return;
        }

        if (args.length > 1) {
            player.sendMessage(config.getMsg("messages.errors.tooManyArguments", null));
            return;
        }



        String prefix = MiniMessage.miniMessage().serialize(config.getMsg("messages.prefix", null));
        player.sendMessage(MiniMessage.miniMessage().deserialize(prefix + " <#e173fd>Помощь по команде /display"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#adf3fd>/display help <#8e8e8e>- <#d0d0d0>Показать помощь"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#adf3fd>/display reload <#8e8e8e>- <#d0d0d0>Перезагрузить конфиг"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#adf3fd>/display create [block | item | text] <id> <#8e8e8e>- <#d0d0d0>Создать объект для отображения"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#adf3fd>/display delete [<id> | all] <#8e8e8e>- <#d0d0d0>Удалить объект (или все объекты) с указанным ID"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#adf3fd>/display edit <id> <type>:<value>;<type>:<value> <#8e8e8e>- <#d0d0d0>Изменить параметры у объекта с указанным ID"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("  <#c0c4fd>• id:<newid> <#8e8e8e>- <#d0d0d0>Изменить ID объекта на другой"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("  <#c0c4fd>• scale:<x>,<y>,<z> <#8e8e8e>- <#d0d0d0>Изменить размеры объекта на указанные"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("  <#c0c4fd>• material:<value> <#8e8e8e>- <#d0d0d0>Изменить материал. Только для block и item"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("  <#c0c4fd>• glowing:[true | false] <#8e8e8e>- <#d0d0d0>Изменить подсветку. Только для block и item"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("  <#c0c4fd>• text:[true | false] <#8e8e8e>- <#d0d0d0>Начать редактирование текста через чат. Только для text"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#adf3fd>/display tp [pos | here | to] <id> [<world> <x> <y> <z> только для pos] <#8e8e8e>- <#d0d0d0>Телепортировать объект/телепортироваться к объекту с указанным ID"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#adf3fd>/display clone <id> <cloneid> <#8e8e8e>- <#d0d0d0>Клонировать объект"));
    }



    private void reloadConfig(String[] args) {
        if (!player.hasPermission("display.*") && !player.hasPermission("display.reload")) {
            player.sendMessage(config.getMsg("messages.errors.unknownAction", null));
            return;
        }

        if (args.length > 1) {
            player.sendMessage(config.getMsg("messages.errors.tooManyArguments", null));
            return;
        }



        display.reloadConfig();
        config.reloadConfig();
        utils.scheduler.cancel();
        utils.startTextUpdating();

        player.sendMessage(config.getMsg("messages.successfully.reloaded", null));
    }



    private void createObject(String[] args) {
        if (!player.hasPermission("display.*") && !player.hasPermission("display.create")) {
            player.sendMessage(config.getMsg("messages.errors.unknownAction", null));
            return;
        }

        if (args.length > 3) {
            player.sendMessage(config.getMsg("messages.errors.tooManyArguments", null));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(config.getMsg("messages.errors.notFoundObjectType", null));
            return;
        }

        if (!args[1].equalsIgnoreCase("block") && !args[1].equalsIgnoreCase("item") && !args[1].equalsIgnoreCase("text")) {
            player.sendMessage(config.getMsg("messages.errors.unknownObjectType", null));
            return;
        }

        if (args.length < 3) {
            player.sendMessage(config.getMsg("messages.errors.notFoundId", null));
            return;
        }

        if (!utils.isInt(args[2])) {
            player.sendMessage(config.getMsg("messages.errors.invalidId", null));
            return;
        }

        int id = Integer.parseInt(args[2]);

        if (displays.containsKey(id)) {
            player.sendMessage(config.getMsg("messages.errors.idAlreadyInUse", null));
            return;
        }



        Entity entity = null;
        EntityType type = EntityType.valueOf((args[1] + "_display").toUpperCase());
        Location location = player.getLocation();

        switch (type) {
            case BLOCK_DISPLAY -> {
                location = new Location(location.getWorld(), location.getX() - 0.5, location.getY(), location.getZ() - 0.5);
                entity = player.getWorld().spawnEntity(location, EntityType.BLOCK_DISPLAY);
                ((BlockDisplay) entity).setBlock(Bukkit.createBlockData(Material.STONE));
            }

            case ITEM_DISPLAY -> {
                location = new Location(location.getWorld(), location.getX(), location.getY() + 0.5, location.getZ());
                entity = player.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
                ((ItemDisplay) entity).setItemStack(new ItemStack(Material.APPLE));
            }

            case TEXT_DISPLAY -> {
                location = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
                entity = player.getWorld().spawnEntity(location, EntityType.TEXT_DISPLAY);
                PersistentDataContainer data = entity.getPersistentDataContainer();
                data.set(display.displayText_nsk, PersistentDataType.STRING, "Text");
                ((TextDisplay) entity).text(Component.text("Text"));
            }
        }

        assert entity != null;
        displays.put(id, entity);
        PersistentDataContainer data = entity.getPersistentDataContainer();
        data.set(display.displayID_nsk, PersistentDataType.INTEGER, id);

        player.sendMessage(config.getMsg("messages.successfully.created", String.valueOf(id)));
    }



    private void deleteObject(String[] args) {
        if (!player.hasPermission("display.*") && !player.hasPermission("display.delete")) {
            player.sendMessage(config.getMsg("messages.errors.unknownAction", null));
            return;
        }

        if (args.length > 2) {
            player.sendMessage(config.getMsg("messages.errors.tooManyArguments", null));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(config.getMsg("messages.errors.notFoundId", null));
            return;
        }

        if (!utils.isInt(args[1]) && !args[1].equalsIgnoreCase("all")) {
            player.sendMessage(config.getMsg("messages.errors.invalidId", null));
            return;
        }



        if (utils.isInt(args[1])) {
            int id = Integer.parseInt(args[1]);
            Entity entity = displays.get(id);

            if (!displays.containsKey(id) || entity.isDead()) {
                player.sendMessage(config.getMsg("messages.errors.idDoesNotExist", null));
                return;
            }


            entity.remove();
            displays.remove(id);

            player.sendMessage(config.getMsg("messages.successfully.deleted.id", String.valueOf(id)));
        }
        if (args[1].equalsIgnoreCase("all")) {
            for (int id : displays.keySet()) {
                Entity entity = displays.get(id);
                entity.remove();
            }
            displays.clear();

            player.sendMessage(config.getMsg("messages.successfully.deleted.all", null));
        }
    }
    
    
    
    private void edit(String[] args) {
        if (!player.hasPermission("display.*") && !player.hasPermission("display.edit")) {
            player.sendMessage(config.getMsg("messages.errors.unknownAction", null));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(config.getMsg("messages.errors.notFoundId", null));
            return;
        }

        if (!utils.isInt(args[1])) {
            player.sendMessage(config.getMsg("messages.errors.invalidId", null));
            return;
        }

        int id = Integer.parseInt(args[1]);
        Entity entity = displays.get(id);

        if (!displays.containsKey(id) || entity.isDead()) {
            player.sendMessage(config.getMsg("messages.errors.idDoesNotExist", null));
            return;
        }

        EntityType type = entity.getType();

        if (args.length < 3) {
            player.sendMessage(config.getMsg("messages.errors.notFoundEditFormat", null));
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            sb.append(args[i]);
        }

        if (String.valueOf(sb).startsWith(";") || String.valueOf(sb).endsWith(";")) {
            player.sendMessage(config.getMsg("messages.errors.invalidEditFormat", null));
            return;
        }

        boolean isFormat = true;
        String[] edits = String.valueOf(sb).toLowerCase().split(";");

        for (String editArg : edits) {
            if (editArg.isEmpty()) {
                player.sendMessage(config.getMsg("messages.errors.invalidEditFormat", null));
                return;
            }

            String[] editArgSplit = editArg.split(":");

            if (editArgSplit.length != 2 || StringUtils.countMatches(editArg, ":") > 1 || editArgSplit[0].isEmpty() || editArgSplit[1].isEmpty()) {
                player.sendMessage(config.getMsg("messages.errors.invalidEditFormat", null));
                return;
            }

            String editType = editArgSplit[0];
            String editValue = editArgSplit[1];

            switch (type) {
                case BLOCK_DISPLAY -> {
                    switch (editType) {
                        case "id" -> {
                            if (utils.isInt(editValue)) {
                                if (displays.containsKey(Integer.parseInt(editValue))) isFormat = false;
                            }
                            else isFormat = false;
                        }

                        case "glowing" -> {
                            if (!utils.isBoolean(editValue)) isFormat = false;
                        }

                        case "material" -> {
                            Material material = Material.getMaterial(editValue.toUpperCase());
                            if (material == null || !material.isBlock() || material == Material.AIR || material == Material.CAVE_AIR || material == Material.BARRIER) isFormat = false;
                        }

                        case "scale" -> {
                            String[] xyz = editValue.split(",");

                            if (xyz.length != 3 || StringUtils.countMatches(editValue, ",") > 2) {
                                player.sendMessage(config.getMsg("messages.errors.invalidEditFormat", null));
                                return;
                            }

                            if (!utils.isFloat(xyz[0]) || !utils.isFloat(xyz[1]) || !utils.isFloat(xyz[2])) isFormat = false;
                        }

                        default -> isFormat = false;
                    }
                }

                case ITEM_DISPLAY -> {
                    switch (editType) {
                        case "id" -> {
                            if (utils.isInt(editValue)) {
                                if (displays.containsKey(Integer.parseInt(editValue))) isFormat = false;
                            }
                            else isFormat = false;
                        }

                        case "glowing" -> {
                            if (!utils.isBoolean(editValue)) isFormat = false;
                        }

                        case "material" -> {
                            Material material = Material.getMaterial(editValue.toUpperCase());
                            if (material == null || !material.isItem() || material == Material.AIR) isFormat = false;
                        }

                        case "scale" -> {
                            String[] xyz = editValue.split(",");

                            if (xyz.length != 3) isFormat = false;

                            String x = xyz[0];
                            String y = xyz[1];
                            String z = xyz[2];

                            if (!utils.isFloat(x) || !utils.isFloat(y) || !utils.isFloat(z)) isFormat = false;
                        }

                        default -> isFormat = false;
                    }
                }

                case TEXT_DISPLAY ->  {
                    switch (editType) {
                        case "id" -> {
                            if (utils.isInt(editValue)) {
                                if (displays.containsKey(Integer.parseInt(editValue))) isFormat = false;
                            }
                            else isFormat = false;
                        }

                        case "scale" -> {
                            String[] xyz = editValue.split(",");

                            if (xyz.length != 3) isFormat = false;

                            String x = xyz[0];
                            String y = xyz[1];
                            String z = xyz[2];

                            if (!utils.isFloat(x) || !utils.isFloat(y) || !utils.isFloat(z)) isFormat = false;
                        }

                        case "text" -> {
                            if (!utils.isBoolean(editValue)) isFormat = false;
                        }

                        default -> isFormat = false;
                    }
                }
            }
        }

        if (!isFormat) {
            player.sendMessage(config.getMsg("messages.errors.invalidEditFormat", null));
            return;
        }



        for (String editArg : edits) {
            String[] editArgSplit = editArg.split(":");

            String editType = editArgSplit[0];
            String editValue = editArgSplit[1];

            switch (editType) {
                case "id" -> {
                    displays.remove(id);
                    displays.put(Integer.parseInt(editValue), entity);

                    PersistentDataContainer data = entity.getPersistentDataContainer();
                    data.set(display.displayID_nsk, PersistentDataType.INTEGER, Integer.parseInt(editValue));
                }

                case "glowing" -> {
                    boolean glow = Boolean.parseBoolean(editValue);
                    entity.setGlowing(glow);
                }

                case "material" -> {
                    Material material = Material.getMaterial(editValue.toUpperCase());

                    assert material != null;
                    if (type == EntityType.BLOCK_DISPLAY) {
                        ((BlockDisplay) entity).setBlock(Bukkit.createBlockData(material));
                    }
                    else if (type == EntityType.ITEM_DISPLAY) {
                        ((ItemDisplay) entity).setItemStack(new ItemStack(material));
                    }
                }

                case "scale" -> {
                    float x = Float.parseFloat(editValue.split(",")[0]);
                    float y = Float.parseFloat(editValue.split(",")[1]);
                    float z = Float.parseFloat(editValue.split(",")[2]);

                    Transformation transformation = new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(x, y, z), new Quaternionf());
                    ((org.bukkit.entity.Display) entity).setTransformation(transformation);
                }

                case "text" -> {
                    boolean isText = editValue.equals("true");

                    if (isText) {
                        display.editText.put(player, id);

                        PersistentDataContainer data = entity.getPersistentDataContainer();
                        String text = data.get(display.displayText_nsk, PersistentDataType.STRING);

                        if (display.isHookedPapi) {
                            assert text != null;
                            player.sendMessage(MiniMessage.miniMessage().deserialize("<click:suggest_command:'" + text + "'><hover:show_text:'" + PlaceholderAPI.setPlaceholders(null, text) + "'><italic><dark_gray>Нажмите, чтобы получить текущий текст"));
                        }
                        else {
                            player.sendMessage(MiniMessage.miniMessage().deserialize("<click:suggest_command:'" + text + "'><hover:show_text:'" + text + "'><italic><dark_gray>Нажмите, чтобы получить текущий текст"));
                        }

                    }
                    else display.editText.remove(player);
                }
            }
        }
        
        player.sendMessage(config.getMsg("messages.successfully.edited", String.valueOf(id)));
    }



    /*public void showInfo(Player player, String[] args) {
        if (!player.hasPermission("display.*") && !player.hasPermission("display.info")) {
            player.sendMessage(config.getMsg("messages.errors.unknownAction", null));
            return;
        }

        if (args.length > 2) {
            player.sendMessage(config.getMsg("messages.errors.tooManyArguments", null));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(config.getMsg("messages.errors.notFoundId", null));
            return;
        }

        if (!utils.isInt(args[1])) {
            player.sendMessage(config.getMsg("messages.errors.invalidId", null));
            return;
        }

        int id = Integer.parseInt(args[1]);
        Entity entity = displays.get(id);

        if (!displays.containsKey(id) || entity.isDead()) {
            player.sendMessage(config.getMsg("messages.errors.idDoesNotExist", null));
            return;
        }



        String isGlowing = "<yellow>Не доступна  ";
        Vector3f scale = ((org.bukkit.entity.Display) entity).getTransformation().getScale();

        if (entity.getType() == EntityType.BLOCK_DISPLAY || entity.getType() == EntityType.ITEM_DISPLAY) {
            if (entity.isGlowing()) {
                isGlowing = "<green>Включена  ";
            }
            else {
                isGlowing = "<red>Выключена  ";
            }
        }

        Inventory inventory = Bukkit.createInventory(null, 27, MiniMessage.miniMessage().deserialize(" » Инфо об объекте"));

        ItemStack main = new ItemStack(Material.ENDER_EYE);
        ItemMeta mainMeta = main.getItemMeta();
        mainMeta.displayName(Component.text(""));
        java.util.List<Component> mainLore = new ArrayList<>();
        mainLore.add(MiniMessage.miniMessage().deserialize("  <i:false><grey>» <gradient:#fdd134:#fb630e>Основное</gradient>  "));
        mainLore.add(Component.text(""));
        mainLore.add(MiniMessage.miniMessage().deserialize("  <i:false><grey>Тип: <#fdd134>" + entity.getType() + "  "));
        mainLore.add(MiniMessage.miniMessage().deserialize("  <i:false><grey>ID: <#fdd134>" + id + "  "));
        mainLore.add(Component.text(""));
        mainMeta.lore(mainLore);
        main.setItemMeta(mainMeta);

        ItemStack location = new ItemStack(Material.COMPASS);
        ItemMeta locationMeta = location.getItemMeta();
        locationMeta.displayName(Component.text(""));
        java.util.List<Component> locationLore = new ArrayList<>();
        locationLore.add(MiniMessage.miniMessage().deserialize("  <i:false><grey>» <gradient:#fdd134:#fb630e>Позиция</gradient>  "));
        locationLore.add(Component.text(""));
        locationLore.add(MiniMessage.miniMessage().deserialize("  <i:false><grey>X: <#fdd134>" + String.format("%.3f", entity.getLocation().getX()) + "  "));
        locationLore.add(MiniMessage.miniMessage().deserialize("  <i:false><grey>Y: <#fdd134>" + String.format("%.3f", entity.getLocation().getY()) + "  "));
        locationLore.add(MiniMessage.miniMessage().deserialize("  <i:false><grey>Z: <#fdd134>" + String.format("%.3f", entity.getLocation().getZ()) + "  "));
        locationLore.add(MiniMessage.miniMessage().deserialize("  <i:false><grey>Мир: <#fdd134>" + entity.getLocation().getWorld().getName() + "  "));
        locationLore.add(Component.text(""));
        locationMeta.lore(locationLore);
        location.setItemMeta(locationMeta);

        ItemStack other = new ItemStack(Material.PRISMARINE_SHARD);
        ItemMeta otherMeta = other.getItemMeta();
        otherMeta.displayName(Component.text(""));
        java.util.List<Component> otherLore = new ArrayList<>();
        otherLore.add(MiniMessage.miniMessage().deserialize("  <i:false><grey>» <gradient:#fdd134:#fb630e>Другое</gradient>  "));
        otherLore.add(Component.text(""));
        otherLore.add(MiniMessage.miniMessage().deserialize("  <i:false><grey>Подсветка: <#fdd134>" + isGlowing));
        otherLore.add(MiniMessage.miniMessage().deserialize("  <i:false><grey>Размеры: <#fdd134>" + scale.x + ", " + scale.y + ", " + scale.z));
        otherLore.add(Component.text(""));
        otherMeta.lore(otherLore);
        other.setItemMeta(otherMeta);

        ItemStack decoration = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta decorationMeta = decoration.getItemMeta();
        decorationMeta.displayName(Component.text(""));
        decoration.setItemMeta(decorationMeta);

        ItemStack exit = new ItemStack(Material.SPRUCE_DOOR);
        ItemMeta exitMeta = exit.getItemMeta();
        exitMeta.displayName(Component.text(""));
        java.util.List<Component> exitLore = new ArrayList<>();
        exitLore.add(MiniMessage.miniMessage().deserialize("  <i:false><grey>» <gradient:#ff0000:#b40000>Выйти</gradient>  "));
        exitLore.add(Component.text(""));
        exitMeta.lore(exitLore);
        NamespacedKey namespacedKey = new NamespacedKey(display, "displayItem");
        PersistentDataContainer data = exitMeta.getPersistentDataContainer();
        data.set(namespacedKey, PersistentDataType.STRING, "exit");
        exit.setItemMeta(exitMeta);

        inventory.setItem(11, main);
        inventory.setItem(13, location);
        inventory.setItem(15, other);
        inventory.setItem(26, exit);
        inventory.setItem(0, decoration);
        inventory.setItem(8, decoration);
        inventory.setItem(9, decoration);
        inventory.setItem(17, decoration);
        inventory.setItem(18, decoration);


        player.openInventory(inventory);
    }*/



    /*public void showList(Player player, String[] args) {
        if (!player.hasPermission("display.*") && !player.hasPermission("display.list")) {
            player.sendMessage(config.getMsg("messages.errors.unknownAction", null));
            return;
        }

        if (args.length > 1) {
            player.sendMessage(config.getMsg("messages.errors.tooManyArguments", null));
            return;
        }



        Inventory inventory = Bukkit.createInventory(null, 45, MiniMessage.miniMessage().deserialize(" » Список объектов"));

        ItemStack decoration = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta decorationMeta = decoration.getItemMeta();
        decorationMeta.displayName(Component.text(""));
        decoration.setItemMeta(decorationMeta);

        inventory.setItem(0, decoration);
        inventory.setItem(8, decoration);
        inventory.setItem(9, decoration);
        inventory.setItem(17, decoration);
        inventory.setItem(18, decoration);
        inventory.setItem(26, decoration);
        inventory.setItem(27, decoration);
        inventory.setItem(35, decoration);
        inventory.setItem(36, decoration);
        inventory.setItem(44, decoration);

        for (int id : displays.keySet()) {
            Entity entity = displays.get(id);

            ItemStack object = new ItemStack(Material.PAPER);
            ItemMeta objectMeta = object.getItemMeta();
            objectMeta.displayName(Component.text(""));
            java.util.List<Component> objectLore = new ArrayList<>();
            objectLore.add(MiniMessage.miniMessage().deserialize("  <i:false><grey>» <gradient:#fdd134:#fb630e>Объект</gradient>  "));
            objectLore.add(Component.text(""));
            objectLore.add(MiniMessage.miniMessage().deserialize("  <i:false><grey>Тип: <#fdd134>" + entity.getType() + "  "));
            objectLore.add(MiniMessage.miniMessage().deserialize("  <i:false><grey>ID: <#fdd134>" + id + "  "));
            objectLore.add(Component.text(""));
            objectLore.add(MiniMessage.miniMessage().deserialize("  <i:false><dark_green>• <green>Нажмите, чтобы посмотреть  "));
            objectLore.add(Component.text(""));
            objectMeta.lore(objectLore);
            NamespacedKey namespacedKey = new NamespacedKey(display, "displayItem");
            PersistentDataContainer data = objectMeta.getPersistentDataContainer();
            data.set(namespacedKey, PersistentDataType.STRING, String.valueOf(id));
            object.setItemMeta(objectMeta);

            for (int i = 2; i < 43; i++) {
                if (inventory.getItem(i) == null && i != 7 && i != 10 && i != 16 && i != 19 && i != 25 && i != 28 && i != 34 && i != 37) {
                    inventory.setItem(i, object);
                    break;
                }
            }
        }

        player.openInventory(inventory);
    }*/



    private void teleport(String[] args) {
        if (!player.hasPermission("display.*") && !player.hasPermission("display.tp")) {
            player.sendMessage(config.getMsg("messages.errors.unknownAction", null));
            return;
        }

        if (args.length > 7) {
            player.sendMessage(config.getMsg("messages.errors.tooManyArguments", null));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(config.getMsg("messages.errors.notFoundId", null));
            return;
        }

        if (!utils.isInt(args[1])) {
            player.sendMessage(config.getMsg("messages.errors.invalidId", null));
            return;
        }

        int id = Integer.parseInt(args[1]);
        Entity entity = displays.get(id);

        if (!displays.containsKey(id) || entity.isDead()) {
            player.sendMessage(config.getMsg("messages.errors.idDoesNotExist", null));
            return;
        }

        if (args.length < 3) {
            player.sendMessage(config.getMsg("messages.errors.notFoundTeleportType", null));
            return;
        }

        switch (args[2].toLowerCase()) {
            case "pos" -> {
                if (args.length < 4) {
                    player.sendMessage(config.getMsg("messages.errors.notFoundDimension", null));
                    return;
                }

                if (!args[3].equalsIgnoreCase("overworld") && !args[3].equalsIgnoreCase("the_nether") && !args[3].equalsIgnoreCase("the_end")) {
                    player.sendMessage(config.getMsg("messages.errors.unknownDimension", null));
                    return;
                }

                if (args.length < 7) {
                    player.sendMessage(config.getMsg("messages.errors.notFoundCoords", null));
                    return;
                }

                if (!utils.isDouble(args[4]) || !utils.isDouble(args[5]) || !utils.isDouble(args[6])) {
                    player.sendMessage(config.getMsg("messages.errors.invalidCoords", null));
                    return;
                }
            }

            case "here", "to" -> {
                if (args.length > 3) {
                    player.sendMessage(config.getMsg("messages.errors.tooManyArguments", null));
                    return;
                }
            }

            default -> {
                player.sendMessage(config.getMsg("messages.errors.unknownTeleportType", null));
                return;
            }
        }



        switch(args[2].toLowerCase()) {
            case "pos" -> {
                Location location = new Location(Bukkit.getWorld(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]), Double.parseDouble(args[6]));

                if (entity.getType() == EntityType.ITEM_DISPLAY) {
                    location = new Location(location.getWorld(), location.getX() + 0.5, location.getY() + 0.5, location.getZ() + 0.5);
                }
                else if (entity.getType() == EntityType.TEXT_DISPLAY) {
                    location = new Location(location.getWorld(), location.getX() + 0.5, location.getY(), location.getZ() + 0.5);
                }

                entity.teleport(location);

                player.sendMessage(config.getMsg("messages.successfully.teleported.pos", String.valueOf(id)));
            }

            case "here" -> {
                Location location = player.getLocation();

                if (entity.getType() == EntityType.BLOCK_DISPLAY) {
                    location = new Location(location.getWorld(), location.getX() - 0.5, location.getY(), location.getZ() - 0.5);
                }
                else if (entity.getType() == EntityType.ITEM_DISPLAY) {
                    location = new Location(location.getWorld(), location.getX(), location.getY() + 0.5, location.getZ());
                }

                entity.teleport(location);

                player.sendMessage(config.getMsg("messages.successfully.teleported.here", String.valueOf(id)));
            }

            case "to" -> {
                Location location = entity.getLocation();

                if (entity.getType() == EntityType.BLOCK_DISPLAY) {
                    location = new Location(location.getWorld(), location.getX() + 0.5, location.getY(), location.getZ() + 0.5);
                }
                else if (entity.getType() == EntityType.ITEM_DISPLAY) {
                    location = new Location(location.getWorld(), location.getX(), location.getY() - 0.5, location.getZ());
                }

                player.teleport(location);

                player.sendMessage(config.getMsg("messages.successfully.teleported.to", String.valueOf(id)));
            }
        }
    }



    private void clone(String[] args) {
        if (!player.hasPermission("display.*") && !player.hasPermission("display.clone")) {
            player.sendMessage(config.getMsg("messages.errors.unknownAction", null));
            return;
        }

        if (args.length > 3) {
            player.sendMessage(config.getMsg("messages.errors.tooManyArguments", null));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(config.getMsg("messages.errors.notFoundId", null));
            return;
        }

        if (!utils.isInt(args[1])) {
            player.sendMessage(config.getMsg("messages.errors.invalidId", null));
            return;
        }

        int cloneID = Integer.parseInt(args[1]);
        Entity cloneEntity = displays.get(cloneID);

        if (!displays.containsKey(cloneID) || cloneEntity.isDead()) {
            player.sendMessage(config.getMsg("messages.errors.idDoesNotExist", null));
            return;
        }

        if (args.length < 3) {
            player.sendMessage(config.getMsg("messages.errors.notFoundId", null));
            return;
        }

        if (!utils.isInt(args[2])) {
            player.sendMessage(config.getMsg("messages.errors.invalidId", null));
            return;
        }

        int id = Integer.parseInt(args[2]);

        if (displays.containsKey(id)) {
            player.sendMessage(config.getMsg("messages.errors.idAlreadyInUse", null));
            return;
        }



        Location location = player.getLocation();

        Entity entity = null;

        switch (cloneEntity.getType()) {
            case BLOCK_DISPLAY -> {
                location = new Location(location.getWorld(), location.getX() - 0.5, location.getY(), location.getZ() - 0.5);
                entity = location.getWorld().spawnEntity(location, EntityType.BLOCK_DISPLAY);
                ((BlockDisplay) entity).setBlock(((BlockDisplay) cloneEntity).getBlock());
            }

            case ITEM_DISPLAY -> {
                location = new Location(location.getWorld(), location.getX(), location.getY() + 0.5, location.getZ());
                entity = location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
                ((ItemDisplay) entity).setItemStack(((ItemDisplay) cloneEntity).getItemStack());
            }

            case TEXT_DISPLAY -> {
                location = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
                entity = location.getWorld().spawnEntity(location, EntityType.TEXT_DISPLAY);
                PersistentDataContainer data = entity.getPersistentDataContainer();
                data.set(display.displayText_nsk, PersistentDataType.STRING, "Text");
                ((TextDisplay) entity).text(Component.text("Text"));
            }
        }

        assert entity != null;
        entity.setGlowing(cloneEntity.isGlowing());
        ((org.bukkit.entity.Display) entity).setTransformation(((org.bukkit.entity.Display) cloneEntity).getTransformation());

        displays.put(id, entity);
        PersistentDataContainer data = entity.getPersistentDataContainer();
        data.set(display.displayID_nsk, PersistentDataType.INTEGER, id);

        player.sendMessage(config.getMsg("messages.successfully.cloned", String.valueOf(id)));
    }
}