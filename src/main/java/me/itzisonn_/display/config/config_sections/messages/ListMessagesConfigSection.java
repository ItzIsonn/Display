package me.itzisonn_.display.config.config_sections.messages;

import lombok.Getter;
import me.itzisonn_.display.config.ConfigManager;
import me.itzisonn_.display.config.config_sections.messages.components.ComponentConfigMessage;
import me.itzisonn_.display.config.config_sections.AbstractConfigSection;
import me.itzisonn_.display.config.config_sections.messages.components.StringConfigMessage;

@Getter
public class ListMessagesConfigSection extends AbstractConfigSection {
    private final ComponentConfigMessage title = new ComponentConfigMessage(this, "title");
    private final StringConfigMessage format = new StringConfigMessage(this, "format");
    private final ComponentConfigMessage empty = new ComponentConfigMessage(this, "empty");

    public ListMessagesConfigSection(ConfigManager configManager) {
        super(configManager, "messages.list");
    }

    @Override
    public void updateValues() {
        title.updateValue();
        format.updateValue();
        empty.updateValue();
    }
}
