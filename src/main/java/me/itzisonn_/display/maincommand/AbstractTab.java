package me.itzisonn_.display.maincommand;

import me.itzisonn_.display.Display;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTab implements TabCompleter {
    public AbstractTab(String command, Display display) {
        PluginCommand pluginCommand = display.getCommand(command);

        if (pluginCommand != null) {
            pluginCommand.setTabCompleter(this);
        }
    }

    public ArrayList<String> tabComplete(CommandSender sender, String[] args) { return null; }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String str, @NotNull String[] args) {
        return filter(tabComplete(sender, args), args);
    }

    private List<String> filter(List<String> list, String[] args) {
        if (list == null) return null;
        String last = args[args.length - 1];
        List<String> result = new ArrayList<>();

        for (String arg : list) {
            if (arg.toLowerCase().startsWith(last.toLowerCase())) result.add(arg);
        }

        return result;
    }
}
