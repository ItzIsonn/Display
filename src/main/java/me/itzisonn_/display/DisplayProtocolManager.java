package me.itzisonn_.display;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.entity.Player;

import java.util.List;

public class DisplayProtocolManager {
    private final ProtocolManager protocolManager;

    public DisplayProtocolManager() {
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    public void updateFakeText(Player player, int entityId, Component component) {
        updateFakeText(player, entityId, GsonComponentSerializer.gson().serialize(component));
    }

    public void updateFakeText(Player player, int entityId, String json) {
        PacketContainer fakeText = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        fakeText.getIntegers().write(0, entityId);

        WrappedDataValue dataValue = WrappedDataValue.fromWrappedValue(
                23,
                WrappedDataWatcher.Registry.getChatComponentSerializer(),
                WrappedChatComponent.fromJson(json));

        fakeText.getDataValueCollectionModifier().write(0, List.of(dataValue));

        protocolManager.sendServerPacket(player, fakeText);
    }
}
