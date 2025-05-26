package me.itzisonn_.display.commands.edit_types;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class RotationEditType extends AbstractEditType {
    public RotationEditType(DisplayPlugin plugin) {
        super(plugin, "rotation", 3);
    }

    @Override
    public boolean onCommand(Player player, DisplayData<?> displayData, String[] args) {
        Display entity = displayData.getDisplay();
        int id = displayData.getId();

        if (args.length == 1 && args[0].equals("?")) {
            Vector3f rotation = entity.getTransformation().getLeftRotation().getEulerAnglesXYZ(new Vector3f());

            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditInfo().getComponent(player,
                    Placeholder.parsed("id", String.valueOf(id)),
                    Placeholder.parsed("type", "rotation"),
                    Placeholder.parsed("value", round(Math.toDegrees(rotation.x)) + "," + round(Math.toDegrees(rotation.y)) + "," + round(Math.toDegrees(rotation.z)))));
            return false;
        }

        try {
            Transformation oldTransformation = entity.getTransformation();

            entity.setTransformation(new Transformation(
                    oldTransformation.getTranslation(),
                    new Quaternionf()
                            .rotateAxis((float) Math.toRadians(Double.parseDouble(args[0])), 1, 0, 0)
                            .rotateAxis((float) Math.toRadians(Double.parseDouble(args[1])), 0, 1, 0)
                            .rotateAxis((float) Math.toRadians(Double.parseDouble(args[2])), 0, 0, 1),
                    oldTransformation.getScale(),
                    new Quaternionf()
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
                    Double.parseDouble(args[0]);
                    yield Lists.newArrayList("<y>");
                }
                catch (NumberFormatException ignore) {
                    yield new ArrayList<>();
                }
            }
            case 3 -> {
                try {
                    Double.parseDouble(args[0]);
                    Double.parseDouble(args[1]);
                    yield Lists.newArrayList("<z>");
                }
                catch (NumberFormatException ignore) {
                    yield new ArrayList<>();
                }
            }
            default -> new ArrayList<>();
        };
    }

    private double round(double value) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}