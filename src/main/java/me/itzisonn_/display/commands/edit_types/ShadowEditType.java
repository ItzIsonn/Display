package me.itzisonn_.display.commands.edit_types;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ShadowEditType extends AbstractEditType {
    public ShadowEditType(DisplayPlugin plugin) {
        super(plugin, "shadow", 2);
    }

    @Override
    public boolean onCommand(Player player, DisplayData<?> displayData, String[] args) {
        Display entity = displayData.getDisplay();
        int id = displayData.getId();

        if (args.length == 1 && args[0].equals("?")) {
            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditInfo().getComponent(player,
                    Placeholder.parsed("id", String.valueOf(id)),
                    Placeholder.parsed("type", "shadow"),
                    Placeholder.parsed("value", entity.getShadowRadius() + "," + entity.getShadowStrength())));
            return false;
        }

        try {
            float radius = Float.parseFloat(args[0]);
            float strength = Float.parseFloat(args[1]);
            entity.setShadowRadius(radius);
            entity.setShadowStrength(strength);
            return true;
        }
        catch (IllegalArgumentException | IndexOutOfBoundsException ignore) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
            return false;
        }
    }

    @Override
    public ArrayList<String> onTabComplete(Player player, DisplayData<?> displayData, String[] args) {
        return switch (args.length) {
            case 1 -> Lists.newArrayList("<radius>", "?");
            case 2 -> {
                try {
                    Float.parseFloat(args[0]);
                    yield Lists.newArrayList("<strength>");
                }
                catch (NumberFormatException ignore) {
                    yield new ArrayList<>();
                }
            }
            default -> new ArrayList<>();
        };
    }
}