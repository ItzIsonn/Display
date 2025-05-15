package me.itzisonn_.display.config.config_sections.messages.components;

import lombok.Getter;
import me.itzisonn_.display.config.config_sections.AbstractConfigSection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;

public abstract class ConfigMessage<T> {
    protected final AbstractConfigSection configSection;
    protected final String path;
    @Getter
    protected T value;

    public ConfigMessage(AbstractConfigSection configSection, String path) {
        this.configSection = configSection;
        this.path = path;
        value = null;
    }

    public abstract void updateValue();

    public Component getComponent(Player player, TagResolver... tagResolvers) {
        String componentValue = configSection.getConfigManager().getPlugin().parsePlaceholders(player, String.valueOf(value));

        MiniMessage miniMessage = configSection.getConfigManager().getPlugin().getMiniMessage();
        return miniMessage.deserialize(componentValue, tagResolvers);
    }

    public Component getComponent(Player player, String id) {
        if (id == null) return getComponent(player);
        else return getComponent(player, Placeholder.parsed("id", id));
    }

    public Component getComponent(Player player, int id) {
        return getComponent(player, String.valueOf(id));
    }

    public Component getComponent(String id) {
        return getComponent(null, id);
    }

    public Component getComponent(int id) {
        return getComponent(null, id);
    }

    public Component getComponent() {
        return getComponent(null);
    }
}
