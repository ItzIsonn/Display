package me.itzisonn_.display.config.config_sections.messages;

import lombok.Getter;
import me.itzisonn_.display.config.ConfigManager;
import me.itzisonn_.display.config.config_sections.AbstractConfigSection;
import me.itzisonn_.display.config.config_sections.messages.components.StringConfigMessage;

@Getter
public class SuccessfullyMessagesConfigSection extends AbstractConfigSection {
    private final StringConfigMessage reload = new StringConfigMessage(this, "reload");
    private final StringConfigMessage createNew = new StringConfigMessage(this, "create.new");
    private final StringConfigMessage createClone = new StringConfigMessage(this, "create.clone");
    private final StringConfigMessage load = new StringConfigMessage(this, "load");
    private final StringConfigMessage deleteId = new StringConfigMessage(this, "delete.id");
    private final StringConfigMessage deleteAll = new StringConfigMessage(this, "delete.all");
    private final StringConfigMessage editEdit = new StringConfigMessage(this, "edit.edit");
    private final StringConfigMessage editInfo = new StringConfigMessage(this, "edit.info");
    private final StringConfigMessage teleportPos = new StringConfigMessage(this, "teleport.pos");
    private final StringConfigMessage teleportHere = new StringConfigMessage(this, "teleport.here");
    private final StringConfigMessage teleportTo = new StringConfigMessage(this, "teleport.to");

    public SuccessfullyMessagesConfigSection(ConfigManager configManager) {
        super(configManager, "messages.successfully");
    }

    @Override
    public void updateValues() {
        reload.updateValue();
        createNew.updateValue();
        createClone.updateValue();
        load.updateValue();
        deleteId.updateValue();
        deleteAll.updateValue();
        editEdit.updateValue();
        editInfo.updateValue();
        teleportPos.updateValue();
        teleportHere.updateValue();
        teleportTo.updateValue();
    }
}
