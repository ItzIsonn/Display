package me.itzisonn_.display.config.config_sections.messages;

import lombok.Getter;
import me.itzisonn_.display.config.ConfigManager;
import me.itzisonn_.display.config.config_sections.messages.components.ComponentConfigMessage;
import me.itzisonn_.display.config.config_sections.AbstractConfigSection;
import me.itzisonn_.display.config.config_sections.messages.components.StringConfigMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;

@Getter
public class GlobalMessagesConfigSection extends AbstractConfigSection {
    private final ComponentConfigMessage prefix = new ComponentConfigMessage(this, "prefix");
    private List<Component> info = List.of(Component.text("?" + getFullPath("info") + "?"));
    private final StringConfigMessage editingText = new StringConfigMessage(this, "editingText");

    public GlobalMessagesConfigSection(ConfigManager configManager) {
        super(configManager, "messages");
    }

    @Override
    public void updateValues() {
        MiniMessage miniMessage = configManager.getPlugin().getMiniMessage();

        prefix.updateValue();
        info = configManager.getConfig().getStringList(getFullPath("info")).stream().map(miniMessage::deserialize).toList();
        editingText.updateValue();
    }
}
