package me.itzisonn_.display.commands.edit_types;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

import java.util.ArrayList;

public class TranslationEditType extends AbstractEditType {
    public TranslationEditType(DisplayPlugin plugin) {
        super(plugin, "translation", 3);
    }

    @Override
    public boolean onCommand(Player player, DisplayData<?> displayData, String[] args) {
        Display entity = displayData.getDisplay();
        int id = displayData.getId();

        if (args.length == 1 && args[0].equals("?")) {
            Vector3f translation = entity.getTransformation().getTranslation();

            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditInfo().getComponent(player,
                    Placeholder.parsed("id", String.valueOf(id)),
                    Placeholder.parsed("type", "translation"),
                    Placeholder.parsed("value", translation.x + "," + translation.y + "," + translation.z)));
            return false;
        }

        try {
            Transformation oldTransformation = entity.getTransformation();

            entity.setTransformation(new Transformation(
                    new Vector3f(
                            Float.parseFloat(args[0]),
                            Float.parseFloat(args[1]),
                            Float.parseFloat(args[2])
                    ),
                    oldTransformation.getLeftRotation(),
                    oldTransformation.getScale(),
                    oldTransformation.getRightRotation()
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
            case 1 -> Lists.newArrayList("<x>", "?");
            case 2 -> {
                try {
                    Float.parseFloat(args[0]);
                    yield Lists.newArrayList("<y>");
                }
                catch (NumberFormatException ignore) {
                    yield new ArrayList<>();
                }
            }
            case 3 -> {
                try {
                    Float.parseFloat(args[0]);
                    Float.parseFloat(args[1]);
                    yield Lists.newArrayList("<z>");
                }
                catch (NumberFormatException ignore) {
                    yield new ArrayList<>();
                }
            }
            default -> new ArrayList<>();
        };
    }
}