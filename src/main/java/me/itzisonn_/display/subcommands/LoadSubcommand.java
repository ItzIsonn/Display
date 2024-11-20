package me.itzisonn_.display.subcommands;

import com.google.common.collect.Lists;
import me.itzisonn_.display.DisplayPlugin;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.UUID;

public class LoadSubcommand extends AbstractSubcommand {
    public LoadSubcommand(DisplayPlugin plugin) {
        super(plugin, "load");
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length > 2) {
            player.sendMessage(plugin.getConfigManager().getError("tooManyArguments", null, player));
            return;
        }

        if (args.length < 1) {
            player.sendMessage(plugin.getConfigManager().getError("notFoundUuid", null, player));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(plugin.getConfigManager().getError("notFoundId", null, player));
            return;
        }

        int id;
        try {
            id = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException ignore) {
            player.sendMessage(plugin.getConfigManager().getError("invalidId", args[1], player));
            return;
        }

        if (plugin.getDisplaysMap().containsKey(id)) {
            player.sendMessage(plugin.getConfigManager().getError("idAlreadyInUse", String.valueOf(id), player));
            return;
        }

        Display display;
        try {
            Entity entity = Bukkit.getEntity(UUID.fromString(args[1]));
            if (entity instanceof Display) display = (Display) entity;
            else {
                player.sendMessage(plugin.getConfigManager().getError("invalidEntity", String.valueOf(id), player));
                return;
            }
        }
        catch (IllegalArgumentException ignore) {
            player.sendMessage(plugin.getConfigManager().getError("invalidUuid", String.valueOf(id), player));
            return;
        }

        if (plugin.getDisplaysMap().containsValue(display)) {
            player.sendMessage(plugin.getConfigManager().getError("entityAlreadyInUse", String.valueOf(id), player));
            return;
        }

        plugin.getDisplaysMap().put(id, display);
        PersistentDataContainer data = display.getPersistentDataContainer();
        data.set(plugin.getNskDisplayId(), PersistentDataType.INTEGER, id);

        String message = MiniMessage.miniMessage().serialize(plugin.getConfigManager().getSuccessfully("load", String.valueOf(id), player));
        player.sendMessage(MiniMessage.miniMessage().deserialize(message
                .replace("%uuid%", args[0])));
    }

    @Override
    public ArrayList<String> onTabComplete(Player player, String[] args) {
        if (args.length == 1) {
            ArrayList<String> list = Lists.newArrayList("<uuid>");
            Display target = plugin.getUtils().getTarget(player, 10);
            if (target != null) list.add(target.getUniqueId().toString());
            return list;
        }
        if (args.length == 2) return Lists.newArrayList("<id>");
        return new ArrayList<>();
    }
}
