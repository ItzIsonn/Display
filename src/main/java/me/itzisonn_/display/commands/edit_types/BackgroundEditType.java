package me.itzisonn_.display.commands.edit_types;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Color;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.util.ArrayList;

public class BackgroundEditType extends AbstractEditType {
    public BackgroundEditType(DisplayPlugin plugin) {
        super(plugin, "background", 3, EntityType.TEXT_DISPLAY);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(Player player, DisplayData<?> displayData, String[] args) {
        if (!(displayData.getDisplay() instanceof TextDisplay entity)) return true;
        int id = displayData.getId();

        if (args.length == 1 && args[0].equals("?")) {
            String infoValue = entity.getBackgroundColor() == null ? "255,255,255" :
                    entity.getBackgroundColor().getRed() + "," + entity.getBackgroundColor().getGreen() + "," + entity.getBackgroundColor().getBlue();
            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditInfo().getComponent(player,
                    Placeholder.parsed("id", String.valueOf(id)),
                    Placeholder.parsed("type", "background"),
                    Placeholder.parsed("value", infoValue)));
            return false;
        }

        try {
            entity.setBackgroundColor(Color.fromRGB(
                    Integer.parseInt(args[0]),
                    Integer.parseInt(args[1]),
                    Integer.parseInt(args[2])
            ));
            return true;
        }
        catch (NumberFormatException | IndexOutOfBoundsException ignore) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
            return false;
        }
    }

    @Override
    public ArrayList<String> onTabComplete(Player player, DisplayData<?> displayData, String[] args) {
        return switch (args.length) {
            case 1 -> Lists.newArrayList("<r>", "?");
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