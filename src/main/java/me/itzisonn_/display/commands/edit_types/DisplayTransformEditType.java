package me.itzisonn_.display.commands.edit_types;

import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class DisplayTransformEditType extends AbstractEditType {
    public DisplayTransformEditType(DisplayPlugin plugin) {
        super(plugin, "display_transform", 1, EntityType.ITEM_DISPLAY);
    }

    @Override
    public boolean onCommand(Player player, DisplayData<?> displayData, String[] args) {
        if (!(displayData.getDisplay() instanceof ItemDisplay entity)) return true;
        int id = displayData.getId();

        if (args[0].equals("?")) {
            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditInfo().getComponent(player,
                    Placeholder.parsed("id", String.valueOf(id)),
                    Placeholder.parsed("type", "display_transform"),
                    Placeholder.parsed("value", entity.getItemDisplayTransform().name())));
            return false;
        }

        try {
            entity.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.valueOf(args[0].toUpperCase()));
            return true;
        }
        catch (IllegalArgumentException ignore) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
            return false;
        }
    }

    @Override
    public ArrayList<String> onTabComplete(Player player, DisplayData<?> displayData, String[] args) {
        ArrayList<String> list = getDisplayTransform();
        list.add("?");
        return list;
    }



    private static ArrayList<String> getDisplayTransform() {
        ArrayList<String> list = new ArrayList<>();
        for (ItemDisplay.ItemDisplayTransform displayTransform : ItemDisplay.ItemDisplayTransform.values()) {
            list.add(displayTransform.name().toLowerCase());
        }
        return list;
    }
}