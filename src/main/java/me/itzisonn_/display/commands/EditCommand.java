package me.itzisonn_.display.commands;

import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import me.itzisonn_.display.commands.edit_types.*;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class EditCommand extends AbstractCommand {
    private final Set<AbstractEditType> editTypes;

    public EditCommand(DisplayPlugin plugin) {
        super(plugin, "edit");

        editTypes = Set.of(
                new IdEditType(plugin),
                new ScaleEditType(plugin),
                new RotationEditType(plugin),
                new TranslationEditType(plugin),
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
                new TextOpacityEditType(plugin)
        );
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getNotFoundId().getComponent(player));
            return;
        }

        int id;
        try {
            id = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException ignore) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidId().getComponent(player, args[0]));
            return;
        }

        DisplayData<?> displayData = plugin.getDisplayManager().get(id);
        if (displayData == null) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getIdDoesNotExist().getComponent(player, id));
            return;
        }

        Display entity = displayData.getDisplay();
        if (entity.isDead()) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getIdDoesNotExist().getComponent(player, id));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getNotFoundEditType().getComponent(player, id));
            return;
        }

        String type = args[1].toLowerCase();

        if (args.length < 3) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getNotFoundEditValue().getComponent(player, id));
            return;
        }

        for (AbstractEditType editType : editTypes) {
            if (!type.equals(editType.getName())) continue;

            if (!editType.getEntityTypes().contains(entity.getType())) {
                player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditType().getComponent(player, id));
                return;
            }

            String[] editTypeArgs = Arrays.copyOfRange(args, 2, args.length);
            if (editTypeArgs.length > editType.getMaxArgs()) {
                player.sendMessage(plugin.getConfigManager().getErrorsSection().getTooManyArguments().getComponent(player, id));
                return;
            }

            boolean shouldSendMessage = editType.onCommand(player, displayData, editTypeArgs);
            if (shouldSendMessage) {
                player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditEdit().getComponent(player, id));
            }
            return;
        }

        player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditType().getComponent(player, id));
    }

    @Override
    public ArrayList<String> onTabComplete(Player player, String[] args) {
        if (args.length == 1) return getIDs();

        EntityType type;
        DisplayData<?> displayData;
        try {
            displayData = plugin.getDisplayManager().get(Integer.parseInt(args[0]));
            if (displayData == null) return new ArrayList<>();

            type = displayData.getDisplay().getType();
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



        for (AbstractEditType editType : editTypes) {
            if (args[1].equalsIgnoreCase(editType.getName())) {
                if (!editType.getEntityTypes().contains(type)) return new ArrayList<>();

                String[] editTypeArgs = Arrays.copyOfRange(args, 2, args.length);
                if (editTypeArgs.length > editType.getMaxArgs()) return new ArrayList<>();

                return editType.onTabComplete(player, displayData, editTypeArgs);
            }
        }

        return new ArrayList<>();
    }
}