package me.itzisonn_.display.maincommand;

import me.itzisonn_.display.Display;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractCommand implements CommandExecutor {
    public AbstractCommand(String command, Display display) {
        PluginCommand pluginCommand = display.getCommand(command);

        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
        }
    }

    public abstract void execute(CommandSender sender, String[] args);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        execute(sender, args);
        return true;
    }
}