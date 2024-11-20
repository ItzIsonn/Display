package me.itzisonn_.display.subcommands;

import com.google.common.collect.Lists;
import me.clip.placeholderapi.PlaceholderAPI;
import me.itzisonn_.display.DisplayPlugin;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Objects;

public class EditSubcommand extends AbstractSubcommand {
    public EditSubcommand(DisplayPlugin plugin) {
        super(plugin, "edit");
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onCommand(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(plugin.getConfigManager().getError("notFoundId", null, player));
            return;
        }

        int id;
        try {
            id = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException ignore) {
            player.sendMessage(plugin.getConfigManager().getError("invalidId", args[0], player));
            return;
        }

        Display entity = plugin.getDisplaysMap().get(id);

        if (!plugin.getDisplaysMap().containsKey(id) || entity.isDead()) {
            player.sendMessage(plugin.getConfigManager().getError("idDoesNotExist", String.valueOf(id), player));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(plugin.getConfigManager().getError("notFoundEditType", String.valueOf(id), player));
            return;
        }

        String type = args[1];

        if (args.length < 3) {
            player.sendMessage(plugin.getConfigManager().getError("notFoundEditValue", String.valueOf(id), player));
            return;
        }

        StringBuilder value = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
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

    @Override
    public ArrayList<String> onTabComplete(Player player, String[] args) {
        if (args.length == 1) return getIDs();

        EntityType type;
        try {
            Display entity = plugin.getDisplaysMap().get(Integer.parseInt(args[0]));
            if (entity == null) return new ArrayList<>();
            type = entity.getType();
        }
        catch (NumberFormatException ignore) {
            return new ArrayList<>();
        }

        if (args.length == 2) {
            ArrayList<String> list = Lists.newArrayList("id", "scale", "billboard", "brightness", "shadow", "view_range");
            switch (type) {
                case BLOCK_DISPLAY -> list.addAll(Lists.newArrayList("material", "glowing"));
                case ITEM_DISPLAY -> list.addAll(Lists.newArrayList("material", "glowing", "display_transform"));
                case TEXT_DISPLAY -> list.addAll(Lists.newArrayList("text", "alignment", "background", "line_width", "see_through", "text_opacity"));
            }
            return list;
        }

        if (args.length == 3) {
            return switch (args[1]) {
                case "id" -> Lists.newArrayList("<new_id>");
                case "scale" -> Lists.newArrayList("<x>,<y>,<z>", "?");
                case "billboard" -> {
                    ArrayList<String> list = plugin.getUtils().getBillBoardList();
                    list.add("?");
                    yield list;
                }
                case "brightness" -> Lists.newArrayList("<block>,<sky>", "?");
                case "shadow" -> Lists.newArrayList("<radius>,<strength>", "?");
                case "view_range" -> Lists.newArrayList("<range>", "?");
                case "material" -> {
                    if (type == EntityType.TEXT_DISPLAY) yield new ArrayList<>();

                    ArrayList<String> materialList;
                    if (type == EntityType.BLOCK_DISPLAY) materialList = plugin.getUtils().getBlockList();
                    else materialList = plugin.getUtils().getItemList();

                    materialList.add("?");
                    yield materialList;
                }
                case "glowing" -> {
                    if (type == EntityType.TEXT_DISPLAY) yield new ArrayList<>();
                    yield Lists.newArrayList("<r>,<g>,<b>", "off", "on", "?");
                }
                case "display_transform" -> {
                    if (type != EntityType.ITEM_DISPLAY) yield new ArrayList<>();
                    ArrayList<String> list = plugin.getUtils().getDisplayTransform();
                    list.add("?");
                    yield list;
                }
                case "text" -> {
                    if (type != EntityType.TEXT_DISPLAY) yield new ArrayList<>();
                    yield Lists.newArrayList("on", "off", "?");
                }
                case "alignment" -> {
                    if (type != EntityType.TEXT_DISPLAY) yield new ArrayList<>();
                    ArrayList<String> list = plugin.getUtils().getAlignment();
                    list.add("?");
                    yield list;
                }
                case "background" -> {
                    if (type != EntityType.TEXT_DISPLAY) yield new ArrayList<>();
                    yield Lists.newArrayList("<r>,<g>,<b>", "?");
                }
                case "line_width" -> {
                    if (type != EntityType.TEXT_DISPLAY) yield new ArrayList<>();
                    yield Lists.newArrayList("<width>", "?");
                }
                case "see_through" -> {
                    if (type != EntityType.TEXT_DISPLAY) yield new ArrayList<>();
                    yield Lists.newArrayList("true", "false", "?");
                }
                case "text_opacity" -> {
                    if (type != EntityType.TEXT_DISPLAY) yield new ArrayList<>();
                    yield Lists.newArrayList("<opacity>", "?");
                }
                default -> new ArrayList<>();
            };
        }
        return new ArrayList<>();
    }
}