package me.itzisonn_.display.maincommand;

import me.itzisonn_.display.Display;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractCommand implements CommandExecutor {
    public AbstractCommand(String command) {
        PluginCommand pluginCommand = Display.getInstance().getCommand(command);

        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
        }
    }

    public abstract void executeCommand(CommandSender sender, String label, String[] args);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        executeCommand(sender, label, args);
        return true;
    }
}