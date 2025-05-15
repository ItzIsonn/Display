package me.itzisonn_.display.config.config_sections.messages.components;

import me.itzisonn_.display.config.ConfigManager;
import me.itzisonn_.display.config.config_sections.AbstractConfigSection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;

public class ComponentConfigMessage extends ConfigMessage<Component> {
    public ComponentConfigMessage(AbstractConfigSection configSection, String path) {
        super(configSection, path);
        value = Component.text("?" + configSection.getFullPath(path) + "?");
    }

    @Override
    public void updateValue() {
        ConfigManager configManager = configSection.getConfigManager();
        value = configManager.getPlugin().getMiniMessage().deserialize(configManager.getConfig().getString(configSection.getFullPath(path), "?" + configSection.getFullPath(path) + "?"));
    }

    @Override
    public Component getComponent(Player player, TagResolver... tagResolvers) {
        return value;
    }
}
