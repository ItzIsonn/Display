package me.itzisonn_.display.commands;

import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import me.itzisonn_.display.commands.edit_types.*;
import org.bukkit.entity.*;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Set;

public class EditCommand extends AbstractCommand {
    private final Set<AbstractEditType<?>> editTypes;

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
    @SuppressWarnings("unchecked")
    public void onCommand(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getNotFoundUuid().getComponent(player));
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

        StringBuilder valueBuilder = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            valueBuilder.append(args[i]);
        }
        String value = valueBuilder.toString();

        for (AbstractEditType<?> editType : editTypes) {
            if (!type.equals(editType.getName())) continue;

            if (editType instanceof AbstractMultipleEditType multipleEditType) {
                if (!multipleEditType.getEntityTypes().contains(entity.getType())) {
                    player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditType().getComponent(player, id));
                    return;
                }
            }
            else {
                if (!isOkay(editType, displayData)) {
                    player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditType().getComponent(player, id));
                    return;
                }
            }

            Class<?> genericClass;
            if (editType instanceof AbstractMultipleEditType) genericClass = (Class<?>) ((ParameterizedType) editType.getClass().getSuperclass().getGenericSuperclass()).getActualTypeArguments()[0];
            else genericClass = (Class<?>) ((ParameterizedType) editType.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

            boolean shouldSendMessage;
            if (genericClass.isAssignableFrom(BlockDisplay.class)) {
                AbstractEditType<BlockDisplay> blockDisplayEditType = (AbstractEditType<BlockDisplay>) editType;
                DisplayData<BlockDisplay> blockDisplayData = (DisplayData<BlockDisplay>) displayData;
                shouldSendMessage = blockDisplayEditType.onCommand(player, value, blockDisplayData);
            }
            else if (genericClass.isAssignableFrom(ItemDisplay.class)) {
                AbstractEditType<ItemDisplay> itemDisplayEditType = (AbstractEditType<ItemDisplay>) editType;
                DisplayData<ItemDisplay> itemDisplayData = (DisplayData<ItemDisplay>) displayData;
                shouldSendMessage = itemDisplayEditType.onCommand(player, value, itemDisplayData);
            }
            else if (genericClass.isAssignableFrom(TextDisplay.class)) {
                AbstractEditType<TextDisplay> textDisplayEditType = (AbstractEditType<TextDisplay>) editType;
                DisplayData<TextDisplay> textDisplayData = (DisplayData<TextDisplay>) displayData;
                shouldSendMessage = textDisplayEditType.onCommand(player, value, textDisplayData);
            }
            else {
                AbstractEditType<Display> displayEditType = (AbstractEditType<Display>) editType;
                DisplayData<Display> defaultDisplayData = (DisplayData<Display>) displayData;
                shouldSendMessage = displayEditType.onCommand(player, value, defaultDisplayData);
            }

            if (shouldSendMessage) {
                player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditEdit().getComponent(player, id));
            }
            return;
        }
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
            for (AbstractEditType<?> editType : editTypes) {
                if (editType instanceof AbstractMultipleEditType multipleEditType) {
                    if (multipleEditType.getEntityTypes().contains(type)) arrayList.add(editType.getName());
                }
                else {
                    if (isOkay(editType, displayData)) arrayList.add(editType.getName());
                }
            }
            return arrayList;
        }

        if (args.length == 3) {
            for (AbstractEditType<?> editType : editTypes) {
                if (args[1].equalsIgnoreCase(editType.getName())) {
                    if (editType instanceof AbstractMultipleEditType multipleEditType) {
                        if (multipleEditType.getEntityTypes().contains(type)) return editType.onTabComplete(type);
                    }
                    else {
                        if (isOkay(editType, displayData)) return editType.onTabComplete(type);
                    }
                    return new ArrayList<>();
                }
            }
        }

        return new ArrayList<>();
    }

    private boolean isOkay(AbstractEditType<?> editType, DisplayData<?> displayData) {
        Class<?> editTypeGeneric = (Class<?>) ((ParameterizedType) editType.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Class<?> displayDataGeneric = displayData.getDisplay().getClass();

        return editTypeGeneric.isAssignableFrom(displayDataGeneric);
    }
}