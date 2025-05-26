package me.itzisonn_.display.commands.edit_types;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.manager.DisplayData;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Set;

public class ScaleEditType extends AbstractMultipleEditType {
    public ScaleEditType(DisplayPlugin plugin) {
        super(plugin, "scale", Set.of(EntityType.BLOCK_DISPLAY, EntityType.ITEM_DISPLAY, EntityType.TEXT_DISPLAY));
    }

    @Override
    public boolean onCommand(Player player, String value, DisplayData<Display> displayData) {
        Display entity = displayData.getDisplay();
        int id = displayData.getId();

        if (value.equals("?")) {
            Vector3f scale = entity.getTransformation().getScale();

            player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getEditInfo().getComponent(player,
                    Placeholder.parsed("id", String.valueOf(id)),
                    Placeholder.parsed("type", "scale"),
                    Placeholder.parsed("value", scale.x + "," + scale.y + "," + scale.z)));
            return false;
        }

        String[] scale = value.split(",");
        if (scale.length != 3) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEditValue().getComponent(player, id));
            return false;
        }

        try {
            Transformation oldTransformation = entity.getTransformation();

            entity.setTransformation(new Transformation(
                    oldTransformation.getTranslation(),
                    oldTransformation.getLeftRotation(),
                    new Vector3f(
                            Float.parseFloat(scale[0]),
                            Float.parseFloat(scale[1]),
                            Float.parseFloat(scale[2])
                    ),
                    oldTransformation.getRightRotation()
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
}