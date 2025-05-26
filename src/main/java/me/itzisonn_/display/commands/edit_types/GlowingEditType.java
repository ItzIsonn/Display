package me.itzisonn_.display.commands.edit_types;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Color;
import org.bukkit.entity.*;

import java.util.ArrayList;

public class GlowingEditType extends AbstractEditType {
    public GlowingEditType(DisplayPlugin plugin) {
        super(plugin, "glowing", 3, EntityType.BLOCK_DISPLAY, EntityType.ITEM_DISPLAY);
    }

    @Override
    public boolean onCommand(Player player, DisplayData<?> displayData, String[] args) {
        Display entity = displayData.getDisplay();
        int id = displayData.getId();

        if (args.length == 1) {
            if (args[0].equals("?")) {
                String infoValue = entity.getGlowColorOverride() == null ? "255,255,255" :
                        entity.getGlowColorOverride().getRed() + "," + entity.getGlowColorOverride().getGreen() + "," + entity.getGlowColorOverride().getBlue();

                player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditInfo().getComponent(player,
                        Placeholder.parsed("id", String.valueOf(id)),
                        Placeholder.parsed("type", "glowing"),
                        Placeholder.parsed("value", entity.isGlowing() + ";" + infoValue)));
                return false;
            }
            else if (args[0].equalsIgnoreCase("on")) entity.setGlowing(true);
            else if (args[0].equalsIgnoreCase("off")) entity.setGlowing(false);
            else {
                player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
                return false;
            }

            return true;
        }

        try {
            entity.setGlowColorOverride(Color.fromRGB(
                    Integer.parseInt(args[0]),
                    Integer.parseInt(args[1]),
                    Integer.parseInt(args[2])
            ));
        }
        catch (NumberFormatException | IndexOutOfBoundsException ignore) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
            return false;
        }

        return true;
    }

    @Override
    public ArrayList<String> onTabComplete(Player player, DisplayData<?> displayData, String[] args) {
        return switch (args.length) {
            case 1 -> Lists.newArrayList("<r>", "on", "off", "?");
            case 2 -> {
                try {
                    Integer.parseInt(args[0]);
                    yield Lists.newArrayList("<g>");
                }
                catch (NumberFormatException ignore) {
                    yield new ArrayList<>();
                }
            }
            case 3 -> {
                try {
                    Integer.parseInt(args[0]);
                    Integer.parseInt(args[1]);
                    yield Lists.newArrayList("<b>");
                }
                catch (NumberFormatException ignore) {
                    yield new ArrayList<>();
                }
            }
            default -> new ArrayList<>();
        };
    }
}