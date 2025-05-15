package me.itzisonn_.display.config.config_sections;

import lombok.Getter;
import me.itzisonn_.display.config.ConfigManager;

@Getter
public abstract class AbstractConfigSection {
    protected final ConfigManager configManager;
    protected final String sectionPath;

    public AbstractConfigSection(ConfigManager configManager, String sectionPath) {
        this.configManager = configManager;
        this.sectionPath = sectionPath;
    }

    public String getFullPath(String path) {
        if (sectionPath.isBlank()) return path;
        else return sectionPath + "." + path;
    }

    public abstract void updateValues();
}
