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

import java.util.ArrayList;
import java.util.Set;

public class ScaleEditType extends AbstractEditType {
    public ScaleEditType(DisplayPlugin plugin) {
        super(plugin, "scale", Set.of(EntityType.BLOCK_DISPLAY, EntityType.ITEM_DISPLAY, EntityType.TEXT_DISPLAY));
    }

    @Override
    public boolean onCommand(Player player, String value, Display entity, int id) {
        if (value.equals("?")) {
            player.sendMessage(plugin.getConfigManager().getSuccessfully("edit.info", String.valueOf(id), player,
                    Placeholder.parsed("type", "scale"),
                    Placeholder.parsed("value", entity.getTransformation().getScale().x + "," + entity.getTransformation().getScale().y + "," + entity.getTransformation().getScale().z)));
            return false;
        }

        String[] scale = value.split(",");
        if (scale.length != 3) {
            player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
            return false;
        }

        try {
            entity.setTransformation(new Transformation(new Vector3f(), new Quaternionf(),
                    new Vector3f(Float.parseFloat(scale[0]), Float.parseFloat(scale[1]), Float.parseFloat(scale[2])), new Quaternionf()));
            return true;
        }
        catch (NumberFormatException ignore) {
            player.sendMessage(plugin.getConfigManager().getError("invalidEditValue", String.valueOf(id), player));
            return false;
        }
    }

    @Override
    public ArrayList<String> onTabComplete(EntityType type) {
        return Lists.newArrayList("<x>,<y>,<z>", "?");
    }
}