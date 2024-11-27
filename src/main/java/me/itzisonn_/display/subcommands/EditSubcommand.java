package me.itzisonn_.display.subcommands;

import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.subcommands.edit_types.*;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.Set;

public class EditSubcommand extends AbstractSubcommand {
    private final Set<AbstractEditType> editTypes;

    public EditSubcommand(DisplayPlugin plugin) {
        super(plugin, "edit");

        editTypes = Set.of(
                new IdEditType(plugin),
                new ScaleEditType(plugin),
                new BillboardEditType(plugin),
                new BrightnessEditType(plugin),
                new ShadowEditType(plugin),
                new ViewRangeEditType(plugin),
                new MaterialEditType(plugin),
                new GlowingEditType(plugin),
                new DisplayTransformEditType(plugin),
                new TextEditType(plugin),
                new AlignmentEditType(plugin),
                new BackgroundEditType(plugin),
                new LineWidthEditType(plugin),
                new SeeThroughEditType(plugin),
                new TextOpacityEditType(plugin));
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(plugin.getConfigManager().getError("notFoundId", null, player));
            return;
        }

        int id;
        try {
            id = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException ignore) {
            player.sendMessage(plugin.getConfigManager().getError("invalidId", args[0], player));
            return;
        }

        Display entity = plugin.getDisplaysMap().get(id);

        if (!plugin.getDisplaysMap().containsKey(id) || entity.isDead()) {
            player.sendMessage(plugin.getConfigManager().getError("idDoesNotExist", String.valueOf(id), player));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(plugin.getConfigManager().getError("notFoundEditType", String.valueOf(id), player));
            return;
        }

        String type = args[1].toLowerCase();

        if (args.length < 3) {
            player.sendMessage(plugin.getConfigManager().getError("notFoundEditValue", String.valueOf(id), player));
            return;
        }

        StringBuilder valueBuilder = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            valueBuilder.append(args[i]);
        }
        String value = valueBuilder.toString();

        for (AbstractEditType editType : editTypes) {
            if (type.equals(editType.getName())) {
                if (!editType.getEntityTypes().contains(entity.getType())) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidEditType", String.valueOf(id), player));
                    return;
                }
                boolean shouldSendMessage = editType.onCommand(player, value, entity, id);
                if (shouldSendMessage) player.sendMessage(plugin.getConfigManager().getSuccessfully("edit.edit", String.valueOf(id), player));
                return;
            }
        }
    }

    @Override
    public ArrayList<String> onTabComplete(Player player, String[] args) {
        if (args.length == 1) return getIDs();

        EntityType type;
        try {
            Display entity = plugin.getDisplaysMap().get(Integer.parseInt(args[0]));
            if (entity == null) return new ArrayList<>();
            type = entity.getType();
        }
        catch (NumberFormatException ignore) {
            return new ArrayList<>();
        }

        if (args.length == 2) {
            ArrayList<String> arrayList = new ArrayList<>();
            for (AbstractEditType editType : editTypes) {
                if (editType.getEntityTypes().contains(type)) arrayList.add(editType.getName());
            }
            return arrayList;
        }

        if (args.length == 3) {
            for (AbstractEditType editType : editTypes) {
                if (args[1].equalsIgnoreCase(editType.getName())) {
                    if (!editType.getEntityTypes().contains(type)) return new ArrayList<>();
                    return editType.onTabComplete(type);
                }
            }
        }

        return new ArrayList<>();
    }
}