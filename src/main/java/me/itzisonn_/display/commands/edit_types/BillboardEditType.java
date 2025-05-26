package me.itzisonn_.display.commands.edit_types;

import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.Utils;
import me.itzisonn_.display.manager.DisplayData;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Set;

public class BillboardEditType extends AbstractMultipleEditType {
    public BillboardEditType(DisplayPlugin plugin) {
        super(plugin, "billboard", Set.of(EntityType.BLOCK_DISPLAY, EntityType.ITEM_DISPLAY, EntityType.TEXT_DISPLAY));
    }

    @Override
    public boolean onCommand(Player player, String value, DisplayData<Display> displayData) {
        Display entity = displayData.getDisplay();
        int id = displayData.getId();

        if (value.equals("?")) {
            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditInfo().getComponent(player,
                    Placeholder.parsed("id", String.valueOf(id)),
                    Placeholder.parsed("type", "billboard"),
                    Placeholder.parsed("value", entity.getBillboard().name())));
            return false;
        }

        try {
            entity.setBillboard(Display.Billboard.valueOf(value.toUpperCase()));
            return true;
        }
        catch (IllegalArgumentException ignore) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
            return false;
        }
    }

    @Override
    public ArrayList<String> onTabComplete(EntityType type) {
        ArrayList<String> list = Utils.getBillBoardList();
        list.add("?");
        return list;
    }
}