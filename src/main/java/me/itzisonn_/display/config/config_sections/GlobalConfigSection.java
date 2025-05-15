package me.itzisonn_.display.config.config_sections;

import lombok.Getter;
import me.itzisonn_.display.config.ConfigManager;

@Getter
public class GlobalConfigSection extends AbstractConfigSection {
    private boolean isPapiEnabled = false;
    private int textUpdateInterval = 20;

    public GlobalConfigSection(ConfigManager configManager) {
        super(configManager, "");
    }

    @Override
    public void updateValues() {
        isPapiEnabled = configManager.getConfig().getBoolean("enablePapi", false);
        textUpdateInterval = configManager.getConfig().getInt("textUpdateInterval", 20);
    }
}
