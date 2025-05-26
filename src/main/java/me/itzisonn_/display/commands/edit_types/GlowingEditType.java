package me.itzisonn_.display.commands.edit_types;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Color;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.Set;

public class GlowingEditType extends AbstractMultipleEditType {
    public GlowingEditType(DisplayPlugin plugin) {
        super(plugin, "glowing", Set.of(EntityType.BLOCK_DISPLAY, EntityType.ITEM_DISPLAY));
    }

    @Override
    public boolean onCommand(Player player, String value, DisplayData<Display> displayData) {
        Display entity = displayData.getDisplay();
        int id = displayData.getId();

        if (value.equals("?")) {
            String infoValue = entity.getGlowColorOverride() == null ? "255,255,255" :
                    entity.getGlowColorOverride().getRed() + "," + entity.getGlowColorOverride().getGreen() + "," + entity.getGlowColorOverride().getBlue();

            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditInfo().getComponent(player,
                    Placeholder.parsed("id", String.valueOf(id)),
                    Placeholder.parsed("type", "glowing"),
                    Placeholder.parsed("value", entity.isGlowing() + ";" + infoValue)));
            return false;
        }

        if (value.equalsIgnoreCase("on")) entity.setGlowing(true);
        else if (value.equalsIgnoreCase("off")) entity.setGlowing(false);
        else {
            String[] color = value.split(",");
            if (color.length != 3) {
                player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
                return false;
            }

            try {
                entity.setGlowColorOverride(Color.fromRGB(Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2])));
            }
            catch (NumberFormatException ignore) {
                player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
                return false;
            }
        }

        return true;
    }

    @Override
    public ArrayList<String> onTabComplete(EntityType type) {
        return Lists.newArrayList("<r>,<g>,<b>", "off", "on", "?");
    }
}