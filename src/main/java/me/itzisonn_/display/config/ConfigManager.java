package me.itzisonn_.display.config;

import lombok.Getter;
import me.itzisonn_.display.DisplayPlugin;
import me.itzisonn_.display.config.config_sections.AbstractConfigSection;
import me.itzisonn_.display.config.config_sections.DefaultValuesConfigSection;
import me.itzisonn_.display.config.config_sections.GlobalConfigSection;
import me.itzisonn_.display.config.config_sections.messages.ErrorsMessagesConfigSection;
import me.itzisonn_.display.config.config_sections.messages.GlobalMessagesConfigSection;
import me.itzisonn_.display.config.config_sections.messages.ListMessagesConfigSection;
import me.itzisonn_.display.config.config_sections.messages.SuccessfullyMessagesConfigSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

@Getter
public class ConfigManager {
    private final DisplayPlugin plugin;
    private FileConfiguration config;

    private final GlobalConfigSection globalSection = new GlobalConfigSection(this);
    private final DefaultValuesConfigSection defaultValuesSection = new DefaultValuesConfigSection(this);

    private final GlobalMessagesConfigSection globalMessagesSection = new GlobalMessagesConfigSection(this);
    private final ListMessagesConfigSection listSection = new ListMessagesConfigSection(this);
    private final ErrorsMessagesConfigSection errorsSection = new ErrorsMessagesConfigSection(this);
    private final SuccessfullyMessagesConfigSection successfullySection = new SuccessfullyMessagesConfigSection(this);

    private final List<AbstractConfigSection> configSections = List.of(
            globalSection, defaultValuesSection,
            globalMessagesSection, listSection, errorsSection, successfullySection);



    public ConfigManager(DisplayPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }



    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();

        for (AbstractConfigSection configSection : configSections) {
            configSection.updateValues();
        }
    }
}