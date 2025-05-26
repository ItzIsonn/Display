package me.itzisonn_.display.commands;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.UUID;

public class LoadCommand extends AbstractCommand {
    public LoadCommand(DisplayPlugin plugin) {
        super(plugin, "load");
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length > 2) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getTooManyArguments().getComponent(player));
            return;
        }

        if (args.length < 1) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getNotFoundUuid().getComponent(player));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getNotFoundId().getComponent(player));
            return;
        }

        int id;
        try {
            id = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException ignore) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidId().getComponent(player, args[1]));
            return;
        }

        if (plugin.getDisplayManager().has(id)) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getIdAlreadyInUse().getComponent(player, id));
            return;
        }

        Display display;
        try {
            Entity entity = Bukkit.getEntity(UUID.fromString(args[0]));
            if (entity instanceof Display displayEntity) display = displayEntity;
            else {
                player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidEntity().getComponent(player, id));
                return;
            }
        }
        catch (IllegalArgumentException ignore) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidUuid().getComponent(player, id));
            return;
        }

        if (plugin.getDisplayManager().has(display.getUniqueId())) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getEntityAlreadyInUse().getComponent(player, id));
            return;
        }

        plugin.getDisplayManager().add(id, display);
        PersistentDataContainer data = display.getPersistentDataContainer();
        data.set(plugin.getNskDisplayId(), PersistentDataType.INTEGER, id);

        player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getLoad().getComponent(player,
                Placeholder.parsed("id", String.valueOf(id)),
                Placeholder.parsed("uuid", args[0])));
    }

    @Override
    public ArrayList<String> onTabComplete(Player player, String[] args) {
        if (args.length == 1) {
            ArrayList<String> list = Lists.newArrayList("<uuid>");
            Display target = getTarget(player);
            if (target != null) list.add(target.getUniqueId().toString());
            return list;
        }

        if (args.length == 2) return Lists.newArrayList("<id>");
        return new ArrayList<>();
    }



    private static Display getTarget(Player player) {
        Vector step = player.getEyeLocation().clone().getDirection();
        Location checkingLocation = player.getEyeLocation().clone();

        int checkDistance = 10;
        while (checkDistance >= 0) {
            for (Entity entity : checkingLocation.getWorld().getNearbyEntities(checkingLocation, 0.5, 0.5, 0.5)) {
                if (entity instanceof Display display) return display;
            }

            checkingLocation.add(step);
            checkDistance--;
        }

        return null;
    }
}
