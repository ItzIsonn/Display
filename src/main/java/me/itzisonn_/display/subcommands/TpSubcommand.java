package me.itzisonn_.display.subcommands;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class TpSubcommand extends AbstractSubcommand {
    public TpSubcommand(DisplayPlugin plugin) {
        super(plugin, "tp");
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length > 6) {
            player.sendMessage(plugin.getConfigManager().getError("tooManyArguments", null, player));
            return;
        }

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
            player.sendMessage(plugin.getConfigManager().getError("notFoundTeleportType", String.valueOf(id), player));
            return;
        }

        switch (args[1].toLowerCase()) {
            case "pos" -> {
                if (args.length < 3) {
                    player.sendMessage(plugin.getConfigManager().getError("notFoundDimension", String.valueOf(id), player));
                    return;
                }

                World world = Bukkit.getWorld(args[2]);

                if (world == null) {
                    player.sendMessage(plugin.getConfigManager().getError("unknownDimension", String.valueOf(id), player));
                    return;
                }

                if (args.length < 6) {
                    player.sendMessage(plugin.getConfigManager().getError("notFoundCoords", String.valueOf(id), player));
                    return;
                }

                double x, y, z;

                try {
                    x = Double.parseDouble(args[3]);
                    y = Double.parseDouble(args[4]);
                    z = Double.parseDouble(args[5]);
                }
                catch (NumberFormatException ignore) {
                    player.sendMessage(plugin.getConfigManager().getError("invalidCoords", String.valueOf(id), player));
                    return;
                }

                entity.teleport(new Location(world, x, y, z));

                player.sendMessage(plugin.getConfigManager().getSuccessfully("teleport.pos", String.valueOf(id), player));
            }

            case "here" -> {
                if (args.length > 2) {
                    player.sendMessage(plugin.getConfigManager().getError("tooManyArguments", String.valueOf(id), player));
                    return;
                }

                Location location = player.getLocation();
                location.setYaw(0);
                location.setPitch(0);
                entity.teleport(location);

                player.sendMessage(plugin.getConfigManager().getSuccessfully("teleport.here", String.valueOf(id), player));
            }

            case "to" -> {
                if (args.length > 2) {
                    player.sendMessage(plugin.getConfigManager().getError("tooManyArguments", String.valueOf(id), player));
                    return;
                }

                Location location = entity.getLocation();
                location.setYaw(player.getLocation().getYaw());
                location.setPitch(player.getLocation().getPitch());
                player.teleport(location);

                player.sendMessage(plugin.getConfigManager().getSuccessfully("teleport.to", String.valueOf(id), player));
            }

            default -> player.sendMessage(plugin.getConfigManager().getError("unknownTeleportType", String.valueOf(id), player));
        }
    }

    @Override
    public ArrayList<String> onTabComplete(Player player, String[] args) {
        if (args.length == 1) return getIDs();
        if (args.length == 2) return Lists.newArrayList("pos", "here", "to");

        if (args[1].equalsIgnoreCase("pos")) {
            if (args.length == 3) {
                ArrayList<String> worlds = new ArrayList<>();
                Bukkit.getWorlds().forEach(world -> worlds.add(world.getName()));
                return worlds;
            }
            if (args.length == 4) return Lists.newArrayList("<x>");
            if (args.length == 5) return Lists.newArrayList("<y>");
            if (args.length == 6) return Lists.newArrayList("<z>");
        }
        return new ArrayList<>();
    }
}
