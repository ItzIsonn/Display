package me.itzisonn_.display.subcommands;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
    @SuppressWarnings("deprecation")
    public void onCommand(Player player, String[] args) {
        if (args.length > 3) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getTooManyArguments().getComponent(player));
            return;
        }

        if (args.length < 1) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getNotFoundObjectType().getComponent(player));
            return;
        }

        if (!args[0].equalsIgnoreCase("block") && !args[0].equalsIgnoreCase("item") && !args[0].equalsIgnoreCase("text") && !args[0].equalsIgnoreCase("clone")) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getUnknownObjectType().getComponent(player));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getNotFoundId().getComponent(player));
            return;
        }

        int id;
        try {
            id = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException ignore) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidId().getComponent(player, args[1]));
            return;
        }


        if (args[0].equalsIgnoreCase("clone")) {
            if (plugin.getDisplaysMap().containsKey(id)) {
                player.sendMessage(plugin.getConfigManager().getErrorsSection().getIdAlreadyInUse().getComponent(player, id));
                return;
            }

            if (args.length < 3) {
                player.sendMessage(plugin.getConfigManager().getErrorsSection().getNotFoundId().getComponent(player));
                return;
            }

            int cloneId;
            try {
                cloneId = Integer.parseInt(args[2]);
            }
            catch (NumberFormatException ignore) {
                player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidId().getComponent(player, args[2]));
                return;
            }
            Display cloneEntity = plugin.getDisplaysMap().get(cloneId);

            if (!plugin.getDisplaysMap().containsKey(cloneId) || cloneEntity.isDead()) {
                player.sendMessage(plugin.getConfigManager().getErrorsSection().getIdDoesNotExist().getComponent(player, id));
                return;
            }

            Location location = player.getLocation();
            Display entity = null;

            switch (cloneEntity.getType()) {
                case BLOCK_DISPLAY -> {
                    location = new Location(location.getWorld(), location.getX() - 0.5, location.getY(), location.getZ() - 0.5);

                    entity = location.getWorld().spawn(location, BlockDisplay.class, blockDisplay -> {
                        blockDisplay.setBlock(((BlockDisplay) cloneEntity).getBlock());
                        blockDisplay.setGlowing(cloneEntity.isGlowing());
                        blockDisplay.setGlowColorOverride(cloneEntity.getGlowColorOverride());
                    });
                }

                case ITEM_DISPLAY -> {
                    location = new Location(location.getWorld(), location.getX(), location.getY() + 0.5, location.getZ());

                    entity = location.getWorld().spawn(location, ItemDisplay.class, itemDisplay -> {
                        itemDisplay.setItemStack(((ItemDisplay) cloneEntity).getItemStack());
                        itemDisplay.setGlowing(cloneEntity.isGlowing());
                        itemDisplay.setGlowColorOverride(cloneEntity.getGlowColorOverride());
                        itemDisplay.setItemDisplayTransform(((ItemDisplay) cloneEntity).getItemDisplayTransform());
                    });
                }

                case TEXT_DISPLAY -> {
                    location = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());

                    String cloneText = cloneEntity.getPersistentDataContainer().get(plugin.getNskDisplayText(), PersistentDataType.STRING);
                    if (cloneText == null) cloneText = plugin.getConfigManager().getDefaultValuesSection().getText();
                    String finalCloneText = cloneText;

                    entity = location.getWorld().spawn(location, TextDisplay.class, textDisplay -> {
                        PersistentDataContainer data = textDisplay.getPersistentDataContainer();
                        data.set(plugin.getNskDisplayText(), PersistentDataType.STRING, finalCloneText);
                        textDisplay.text(Component.text(finalCloneText));

                        textDisplay.setAlignment(((TextDisplay) cloneEntity).getAlignment());
                        textDisplay.setBackgroundColor(((TextDisplay) cloneEntity).getBackgroundColor());
                        textDisplay.setLineWidth(((TextDisplay) cloneEntity).getLineWidth());
                        textDisplay.setSeeThrough(((TextDisplay) cloneEntity).isSeeThrough());
                        textDisplay.setTextOpacity(((TextDisplay) cloneEntity).getTextOpacity());
                    });
                }
            }

            if (entity == null) {
                player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEntity().getComponent(player, id));
                return;
            }

            entity.setTransformation(cloneEntity.getTransformation());
            entity.setBillboard(cloneEntity.getBillboard());
            entity.setBrightness(cloneEntity.getBrightness());
            entity.setShadowRadius(cloneEntity.getShadowRadius());
            entity.setShadowStrength(cloneEntity.getShadowStrength());
            entity.setViewRange(cloneEntity.getViewRange());

            plugin.getDisplaysMap().put(id, entity);
            PersistentDataContainer data = entity.getPersistentDataContainer();
            data.set(plugin.getNskDisplayId(), PersistentDataType.INTEGER, id);

            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getCreateClone().getComponent(player, id));
            return;
        }

        if (args.length > 2) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getTooManyArguments().getComponent(player));
            return;
        }

        if (plugin.getDisplaysMap().containsKey(id)) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getIdAlreadyInUse().getComponent(player, id));
            return;
        }


        Display entity = null;
        Location location = player.getLocation();

        switch (args[0]) {
            case "block" -> {
                location = new Location(location.getWorld(), location.getX() - 0.5, location.getY(), location.getZ() - 0.5);

                entity = player.getWorld().spawn(location, BlockDisplay.class, blockDisplay ->
                        blockDisplay.setBlock(Bukkit.createBlockData(plugin.getConfigManager().getDefaultValuesSection().getBlock())));
            }

            case "item" -> {
                location = new Location(location.getWorld(), location.getX(), location.getY() + 0.5, location.getZ());

                entity = player.getWorld().spawn(location, ItemDisplay.class, itemDisplay ->
                        itemDisplay.setItemStack(new ItemStack(plugin.getConfigManager().getDefaultValuesSection().getItem())));
            }

            case "text" -> {
                location = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());

                entity = player.getWorld().spawn(location, TextDisplay.class, textDisplay -> {
                    PersistentDataContainer data = textDisplay.getPersistentDataContainer();

                    String text = plugin.getConfigManager().getDefaultValuesSection().getText();
                    data.set(plugin.getNskDisplayText(), PersistentDataType.STRING, text);
                    textDisplay.text(plugin.getMiniMessage().deserialize(text));
                });
            }
        }

        if (entity == null) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getUnknownObjectType().getComponent(player));
            return;
        }

        plugin.getDisplaysMap().put(id, entity);
        PersistentDataContainer data = entity.getPersistentDataContainer();
        data.set(plugin.getNskDisplayId(), PersistentDataType.INTEGER, id);

        player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getCreateNew().getComponent(player, id));
    }

    @Override
    public ArrayList<String> onTabComplete(Player player, String[] args) {
        if (args.length == 1) return Lists.newArrayList("block", "item", "text", "clone");
        if (args.length == 2) return Lists.newArrayList("<id>");
        if (args.length == 3 && args[0].equalsIgnoreCase("clone")) return getIDs();
        return new ArrayList<>();
    }
}
