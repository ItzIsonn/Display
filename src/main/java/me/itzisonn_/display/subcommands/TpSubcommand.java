package me.itzisonn_.display.subcommands;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getTooManyArguments().getComponent(player));
            return;
        }

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

        Display entity = plugin.getDisplaysMap().get(id);

        if (!plugin.getDisplaysMap().containsKey(id) || entity.isDead()) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getIdDoesNotExist().getComponent(player, id));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getNotFoundTeleportType().getComponent(player, id));
            return;
        }

        switch (args[1].toLowerCase()) {
            case "pos" -> {
                if (args.length < 3) {
                    player.sendMessage(plugin.getConfigManager().getErrorsSection().getNotFoundDimension().getComponent(player, id));
                    return;
                }

                World world = Bukkit.getWorld(args[2]);

                if (world == null) {
                    player.sendMessage(plugin.getConfigManager().getErrorsSection().getUnknownDimension().getComponent(player, id));
                    return;
                }

                if (args.length < 6) {
                    player.sendMessage(plugin.getConfigManager().getErrorsSection().getNotFoundCoords().getComponent(player, id));
                    return;
                }

                double x, y, z;

                try {
                    x = Double.parseDouble(args[3]);
                    y = Double.parseDouble(args[4]);
                    z = Double.parseDouble(args[5]);
                }
                catch (NumberFormatException ignore) {
                    player.sendMessage(plugin.getConfigManager().getErrorsSection().getInvalidCoords().getComponent(player, id));
                    return;
                }

                entity.teleport(new Location(world, x, y, z));

                player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getTeleportPos().getComponent(player,
                        Placeholder.parsed("id", String.valueOf(id)),
                        Placeholder.parsed("x", String.valueOf(x)),
                        Placeholder.parsed("y", String.valueOf(y)),
                        Placeholder.parsed("z", String.valueOf(z))));
            }

            case "here" -> {
                if (args.length > 2) {
                    player.sendMessage(plugin.getConfigManager().getErrorsSection().getTooManyArguments().getComponent(player, id));
                    return;
                }

                Location location = player.getLocation();
                location.setYaw(0);
                location.setPitch(0);
                entity.teleport(location);

                player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getTeleportHere().getComponent(player, id));
            }

            case "to" -> {
                if (args.length > 2) {
                    player.sendMessage(plugin.getConfigManager().getErrorsSection().getTooManyArguments().getComponent(player, id));
                    return;
                }

                Location location = entity.getLocation();
                location.setYaw(player.getLocation().getYaw());
                location.setPitch(player.getLocation().getPitch());
                player.teleport(location);

                player.sendMessage(plugin.getConfigManager().getSuccessfullySection().getTeleportTo().getComponent(player, id));
            }

            default -> player.sendMessage(plugin.getConfigManager().getErrorsSection().getUnknownTeleportType().getComponent(player, id));
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
