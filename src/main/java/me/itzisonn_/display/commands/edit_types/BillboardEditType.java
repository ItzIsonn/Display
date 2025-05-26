package me.itzisonn_.display.commands.edit_types;

import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class BillboardEditType extends AbstractEditType {
    public BillboardEditType(DisplayPlugin plugin) {
        super(plugin, "billboard", 1);
    }

    @Override
    public boolean onCommand(Player player, DisplayData<?> displayData, String[] args) {
        Display entity = displayData.getDisplay();
        int id = displayData.getId();

        if (args[0].equals("?")) {
            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditInfo().getComponent(player,
                    Placeholder.parsed("id", String.valueOf(id)),
                    Placeholder.parsed("type", "billboard"),
                    Placeholder.parsed("value", entity.getBillboard().name())));
            return false;
        }

        try {
            entity.setBillboard(Display.Billboard.valueOf(args[0].toUpperCase()));
            return true;
        }
        catch (IllegalArgumentException ignore) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
            return false;
        }
    }

    @Override
    public ArrayList<String> onTabComplete(Player player, DisplayData<?> displayData, String[] args) {
        ArrayList<String> list = getBillBoardList();
        list.add("?");
        return list;
    }



    private static ArrayList<String> getBillBoardList() {
        ArrayList<String> list = new ArrayList<>();
        for (Display.Billboard billboard : Display.Billboard.values()) {
            list.add(billboard.name().toLowerCase());
        }
        return list;
    }
}