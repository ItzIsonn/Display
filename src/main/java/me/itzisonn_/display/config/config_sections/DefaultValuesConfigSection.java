package me.itzisonn_.display.config.config_sections;

import lombok.Getter;
import me.itzisonn_.display.config.ConfigManager;
import org.bukkit.Material;

import java.util.logging.Level;

@Getter
public class DefaultValuesConfigSection extends AbstractConfigSection {
    private Material block = Material.STONE;
    private Material item = Material.APPLE;
    private String text = "Текст";

    public DefaultValuesConfigSection(ConfigManager configManager) {
        super(configManager, "defaultValues");
    }

    @Override
    public void updateValues() {
        Material blockMaterial = getMaterial("block");
        if (blockMaterial != null && blockMaterial.isBlock()) block = blockMaterial;

        Material itemMaterial = getMaterial("item");
        if (itemMaterial != null && itemMaterial.isItem()) item = itemMaterial;

        text = configManager.getConfig().getString(getFullPath("text"), "Текст");
    }

    private Material getMaterial(String path) {
        try {
            return Material.valueOf(configManager.getConfig().getString(getFullPath(path), "STONE").toUpperCase());
        }
        catch (IllegalArgumentException e) {
            configManager.getPlugin().getLogger().log(Level.SEVERE, "Invalid value found for " + path);
        }

        return null;
    }
}
