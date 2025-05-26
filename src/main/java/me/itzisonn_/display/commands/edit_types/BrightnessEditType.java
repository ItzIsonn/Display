package me.itzisonn_.display.commands.edit_types;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class BrightnessEditType extends AbstractEditType {
    public BrightnessEditType(DisplayPlugin plugin) {
        super(plugin, "brightness", 2);
    }

    @Override
    public boolean onCommand(Player player, DisplayData<?> displayData, String[] args) {
        Display entity = displayData.getDisplay();
        int id = displayData.getId();

        if (args.length == 1 && args[0].equals("?")) {
            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditInfo().getComponent(player,
                    Placeholder.parsed("id", String.valueOf(id)),
                    Placeholder.parsed("type", "brightness"),
                    Placeholder.parsed("value", entity.getBrightness() == null ? "?" : entity.getBrightness().getBlockLight() + "," + entity.getBrightness().getSkyLight())));
            return false;
        }

        try {
            entity.setBrightness(new Display.Brightness(
                    Integer.parseInt(args[0]),
                    Integer.parseInt(args[1])
            ));
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
            case 1 -> Lists.newArrayList("<block>", "?");
            case 2 -> {
                try {
                    Integer.parseInt(args[0]);
                    yield Lists.newArrayList("<sky>");
                }
                catch (NumberFormatException ignore) {
                    yield new ArrayList<>();
                }
            }
            default -> new ArrayList<>();
        };
    }
}