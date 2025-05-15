package me.itzisonn_.display.config.config_sections.messages;

import lombok.Getter;
import me.itzisonn_.display.config.config_sections.messages.components.StringConfigMessage;
import me.itzisonn_.display.config.ConfigManager;
import me.itzisonn_.display.config.config_sections.AbstractConfigSection;

@Getter
public class ErrorsMessagesConfigSection extends AbstractConfigSection {
    private final StringConfigMessage onlyPlayer = new StringConfigMessage(this, "onlyPlayer");
    private final StringConfigMessage noPermission = new StringConfigMessage(this, "noPermission");
    private final StringConfigMessage notFull = new StringConfigMessage(this, "notFull");
    private final StringConfigMessage unknownAction = new StringConfigMessage(this, "unknownAction");
    private final StringConfigMessage tooManyArguments = new StringConfigMessage(this, "tooManyArguments");
    private final StringConfigMessage notFoundId = new StringConfigMessage(this, "notFoundId");
    private final StringConfigMessage invalidId = new StringConfigMessage(this, "invalidId");
    private final StringConfigMessage idAlreadyInUse = new StringConfigMessage(this, "idAlreadyInUse");
    private final StringConfigMessage idDoesNotExist = new StringConfigMessage(this, "idDoesNotExist");
    private final StringConfigMessage notFoundObjectType = new StringConfigMessage(this, "notFoundObjectType");
    private final StringConfigMessage unknownObjectType = new StringConfigMessage(this, "unknownObjectType");
    private final StringConfigMessage notFoundUuid = new StringConfigMessage(this, "notFoundUuid");
    private final StringConfigMessage invalidUuid = new StringConfigMessage(this, "invalidUuid");
    private final StringConfigMessage invalidEntity = new StringConfigMessage(this, "invalidEntity");
    private final StringConfigMessage entityAlreadyInUse = new StringConfigMessage(this, "entityAlreadyInUse");
    private final StringConfigMessage notFoundEditType = new StringConfigMessage(this, "notFoundEditType");
    private final StringConfigMessage invalidEditType = new StringConfigMessage(this, "invalidEditType");
    private final StringConfigMessage notFoundEditValue = new StringConfigMessage(this, "notFoundEditValue");
    private final StringConfigMessage invalidEditValue = new StringConfigMessage(this, "invalidEditValue");
    private final StringConfigMessage notFoundTeleportType = new StringConfigMessage(this, "notFoundTeleportType");
    private final StringConfigMessage unknownTeleportType = new StringConfigMessage(this, "unknownTeleportType");
    private final StringConfigMessage notFoundDimension = new StringConfigMessage(this, "notFoundDimension");
    private final StringConfigMessage unknownDimension = new StringConfigMessage(this, "unknownDimension");
    private final StringConfigMessage notFoundCoords = new StringConfigMessage(this, "notFoundCoords");
    private final StringConfigMessage invalidCoords = new StringConfigMessage(this, "invalidCoords");

    public ErrorsMessagesConfigSection(ConfigManager configManager) {
        super(configManager, "messages.errors");
    }

    @Override
    public void updateValues() {
        onlyPlayer.updateValue();
        noPermission.updateValue();
        notFull.updateValue();
        unknownAction.updateValue();
        tooManyArguments.updateValue();
        notFoundId.updateValue();
        invalidId.updateValue();
        idAlreadyInUse.updateValue();
        idDoesNotExist.updateValue();
        notFoundObjectType.updateValue();
        unknownObjectType.updateValue();
        notFoundUuid.updateValue();
        invalidUuid.updateValue();
        invalidEntity.updateValue();
        entityAlreadyInUse.updateValue();
        notFoundEditType.updateValue();
        invalidEditType.updateValue();
        notFoundEditValue.updateValue();
        invalidEditValue.updateValue();
        notFoundTeleportType.updateValue();
        unknownTeleportType.updateValue();
        notFoundDimension.updateValue();
        unknownDimension.updateValue();
        notFoundCoords.updateValue();
        invalidCoords.updateValue();
    }
}
