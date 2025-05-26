package me.itzisonn_.display.commands.edit_types;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Set;

public class BrightnessEditType extends AbstractMultipleEditType {
    public BrightnessEditType(DisplayPlugin plugin) {
        super(plugin, "brightness", Set.of(EntityType.BLOCK_DISPLAY, EntityType.ITEM_DISPLAY, EntityType.TEXT_DISPLAY));
    }

    @Override
    public boolean onCommand(Player player, String value, DisplayData<Display> displayData) {
        Display entity = displayData.getDisplay();
        int id = displayData.getId();

        if (value.equals("?")) {
            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditInfo().getComponent(player,
                    Placeholder.parsed("id", String.valueOf(id)),
                    Placeholder.parsed("type", "brightness"),
                    Placeholder.parsed("value", entity.getBrightness() == null ? "?" : entity.getBrightness().getBlockLight() + "," + entity.getBrightness().getSkyLight())));
            return false;
        }

        String[] brightness = value.split(",");
        if (brightness.length != 2) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
            return false;
        }

        try {
            entity.setBrightness(new Display.Brightness(Integer.parseInt(brightness[0]), Integer.parseInt(brightness[1])));
            return true;
        }
        catch (IllegalArgumentException ignore) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
            return false;
        }
    }

    @Override
    public ArrayList<String> onTabComplete(EntityType type) {
        return Lists.newArrayList("<block>,<sky>", "?");
    }
}