package me.itzisonn_.display.config.config_sections.messages.components;

import me.itzisonn_.display.config.config_sections.AbstractConfigSection;

public class StringConfigMessage extends ConfigMessage<String> {
    public StringConfigMessage(AbstractConfigSection configSection, String path) {
        super(configSection, path);
        value = "?" + configSection.getFullPath(path) + "?";
    }

    @Override
    public void updateValue() {
        value = configSection.getConfigManager().getConfig().getString(configSection.getFullPath(path), "?" + configSection.getFullPath(path) + "?");
    }
}
