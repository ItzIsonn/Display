package me.itzisonn_.display.subcommands.edit_types;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Set;

public class RotationEditType extends AbstractEditType {
    public RotationEditType(DisplayPlugin plugin) {
        super(plugin, "rotation", Set.of(EntityType.BLOCK_DISPLAY, EntityType.ITEM_DISPLAY, EntityType.TEXT_DISPLAY));
    }

    @Override
    public boolean onCommand(Player player, String value, Display entity, int id) {
        if (value.equals("?")) {
            Vector3f rotation = entity.getTransformation().getLeftRotation().getEulerAnglesXYZ(new Vector3f());

            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditInfo().getComponent(player,
                    Placeholder.parsed("id", String.valueOf(id)),
                    Placeholder.parsed("type", "rotation"),
                    Placeholder.parsed("value", round(Math.toDegrees(rotation.x)) + "," + round(Math.toDegrees(rotation.y)) + "," + round(Math.toDegrees(rotation.z)))));
            return false;
        }

        String[] rotation = value.split(",");
        if (rotation.length != 3) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
            return false;
        }

        try {
            Transformation oldTransformation = entity.getTransformation();

            entity.setTransformation(new Transformation(
                    oldTransformation.getTranslation(),
                    new Quaternionf()
                            .rotateAxis((float) Math.toRadians(Double.parseDouble(rotation[0])), 1, 0, 0)
                            .rotateAxis((float) Math.toRadians(Double.parseDouble(rotation[1])), 0, 1, 0)
                            .rotateAxis((float) Math.toRadians(Double.parseDouble(rotation[2])), 0, 0, 1),
                    oldTransformation.getScale(),
                    new Quaternionf()
            ));

            return true;
        }
        catch (NumberFormatException ignore) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
            return false;
        }
    }

    @Override
    public ArrayList<String> onTabComplete(EntityType type) {
        return Lists.newArrayList("<x>,<y>,<z>", "?");
    }

    private double round(double value) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}