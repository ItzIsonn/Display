package me.itzisonn_.display;

import me.itzisonn_.display.subcommands.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DisplayCommand implements CommandExecutor, TabCompleter {
    private final DisplayPlugin plugin;
    private final Set<AbstractSubcommand> subcommands;

    public DisplayCommand(DisplayPlugin plugin) {
        PluginCommand pluginCommand = plugin.getCommand("display");
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
        }

        this.plugin = plugin;
        subcommands = Set.of(
                new HelpSubcommand(plugin),
                new ReloadSubcommand(plugin),
                new CreateSubcommand(plugin),
                new LoadSubcommand(plugin),
                new DeleteSubcommand(plugin),
                new ListSubcommand(plugin),
                new EditSubcommand(plugin),
                new TpSubcommand(plugin));
    }



    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getConfigManager().getErrorsSection().getOnlyPlayer().getComponent());
            return true;
        }

        if (!player.hasPermission("display.*")) {
            for (AbstractSubcommand subcommand : subcommands) {
                if (player.hasPermission("display." + subcommand.getName())) break;
            }

            player.sendMessage(plugin.getConfigManager().getErrorsSection().getNoPermission().getComponent(player));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(plugin.getConfigManager().getErrorsSection().getNotFull().getComponent(player));
            return true;
        }

        for (AbstractSubcommand subcommand : subcommands) {
            if (args[0].equalsIgnoreCase(subcommand.getName())) {
                if (!player.hasPermission("display.*") && !player.hasPermission("display." + subcommand.getName())) {
                    player.sendMessage(plugin.getConfigManager().getErrorsSection().getUnknownAction().getComponent(player));
                    return true;
                }
                subcommand.onCommand(player, Arrays.copyOfRange(args, 1, args.length));
                return true;
            }
        }

        player.sendMessage(plugin.getConfigManager().getErrorsSection().getUnknownAction().getComponent(player));
        return true;
    }



    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String str, @NotNull String[] args) {
        List<String> tabComplete = tabComplete(sender, args);

        String last = args[args.length - 1];
        List<String> result = new ArrayList<>();

        for (String arg : tabComplete) {
            if (arg.toLowerCase().startsWith(last.toLowerCase())) result.add(arg);
        }

        return result;
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return new ArrayList<>();

        if (args.length == 1) {
            List<String> arrayList = new ArrayList<>();

            for (AbstractSubcommand subcommand : subcommands) {
                if (player.hasPermission("display." + subcommand.getName()) || player.hasPermission("display.*"))
                    arrayList.add(subcommand.getName());
            }

            return arrayList;
        }

        for (AbstractSubcommand subcommand : subcommands) {
            if (args[0].equalsIgnoreCase(subcommand.getName())) {
                if (player.hasPermission("display." + subcommand.getName()) || player.hasPermission("display.*"))
                    return subcommand.onTabComplete(player, Arrays.copyOfRange(args, 1, args.length));
                return new ArrayList<>();
            }
        }

        return new ArrayList<>();
    }
}