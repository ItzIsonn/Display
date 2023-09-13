package me.itzisonn_.display;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class Config {
    private final Display display;

    private FileConfiguration config;
    public final HashMap<String, String> configValues = new HashMap<>();

    public Config(Display display) {
        this.display = display;
        this.config = display.getConfig();
    }



    private void updateValues() {
        configValues.clear();

        configValues.put("messages.prefix", config.getString("messages.prefix", "<gradient:#adf3fd:#e173fd>Display</gradient> <#6d6d6d>|<reset>"));

        configValues.put("messages.errors.onlyPlayer", config.getString("messages.errors.onlyPlayer", "<red>Команду может выполнять только игрок!"));
        configValues.put("messages.errors.noPermission", config.getString("messages.errors.noPermission", "{prefix} <red>У вас нет прав на выполнение данной команды!"));
        configValues.put("messages.errors.notFull", config.getString("messages.errors.notFull", "{prefix} <red>Неполная команда! Список команд: /display help"));
        configValues.put("messages.errors.unknownAction", config.getString("messages.errors.unknownAction", "{prefix} <red>Неизвестный тип действия!"));
        configValues.put("messages.errors.tooManyArguments", config.getString("messages.errors.tooManyArguments", "{prefix} <red>Слишком много аргументов!"));
        configValues.put("messages.errors.notFoundId", config.getString("messages.errors.notFoundId", "{prefix} <red>Не найден ID объекта"));
        configValues.put("messages.errors.invalidId", config.getString("messages.errors.invalidId", "{prefix} <red>ID объекта должен быть числом!"));
        configValues.put("messages.errors.idAlreadyInUse", config.getString("messages.errors.idAlreadyInUse", "{prefix} <red>Этот ID уже используется!"));
        configValues.put("messages.errors.idDoesNotExist", config.getString("messages.errors.idDoesNotExist", "{prefix} <red>Объекта с этим ID не существует!"));
        configValues.put("messages.errors.notFoundObjectType", config.getString("messages.errors.notFoundObjectType", "{prefix} <red>Не найден тип объекта!"));
        configValues.put("messages.errors.unknownObjectType", config.getString("messages.errors.unknownObjectType", "{prefix} <red>Неизвестный тип объекта!"));
        configValues.put("messages.errors.notFoundEditFormat", config.getString("messages.errors.notFoundEditFormat", "{prefix} <red>Не найден формат редактирования!"));
        configValues.put("messages.errors.invalidEditFormat", config.getString("messages.errors.invalidEditFormat", "{prefix} <red>Ошибка в формате редактирования!"));
        configValues.put("messages.errors.notFoundTeleportType", config.getString("messages.errors.notFoundTeleportType", "{prefix} <red>Не найден тип телепорта!"));
        configValues.put("messages.errors.unknownTeleportType", config.getString("messages.errors.unknownTeleportType", "{prefix} <red>Неизвестный тип телепорта!"));
        configValues.put("messages.errors.notFoundDimension", config.getString("messages.errors.notFoundDimension", "{prefix} <red>Не найдено измерение!"));
        configValues.put("messages.errors.unknownDimension", config.getString("messages.errors.unknownDimension", "{prefix} <red>Неизвестное измерение!"));
        configValues.put("messages.errors.notFoundCoords", config.getString("messages.errors.notFoundCoords", "{prefix} <red>Не найдены координаты!"));
        configValues.put("messages.errors.invalidCoords", config.getString("messages.errors.invalidCoords", "{prefix} <red>Неверный формат координат!"));

        configValues.put("messages.successfully.reloaded", config.getString("messages.successfully.reloaded", "{prefix} <green>Конфиг плагина успешно перезагружен!"));
        configValues.put("messages.successfully.created", config.getString("messages.successfully.created", "{prefix} <green>Новый объект для отображения успешно создан!"));
        configValues.put("messages.successfully.deleted.id", config.getString("messages.successfully.deleted.id", "{prefix} <green>Объект с ID {id} успешно удалён!"));
        configValues.put("messages.successfully.deleted.all", config.getString("messages.successfully.deleted.all", "{prefix} <green>Все объекты успешно удалёны!"));
        configValues.put("messages.successfully.edited", config.getString("messages.successfully.edited", "{prefix} <green>Объект с ID {id} успешно отредактирован!"));
        configValues.put("messages.successfully.teleported.pos", config.getString("messages.successfully.teleported.pos", "{prefix} <green>Объект с ID {id} успешно перемещён на указанные координаты!"));
        configValues.put("messages.successfully.teleported.here", config.getString("messages.successfully.teleported.here", "{prefix} <green>Объект с ID {id} успешно перемещён к вам!"));
        configValues.put("messages.successfully.teleported.to", config.getString("messages.successfully.teleported.to", "{prefix} <green>Вы успешно переместились к объекту с ID {id}!"));
        configValues.put("messages.successfully.cloned", config.getString("messages.successfully.cloned", "{prefix} <green>Объект с ID {id} успешно клонирован"));
    }

    public Component getMsg(String path, String replaceID) {
        String message = configValues.get(path);

        if (path.startsWith("messages.successfully.") &&
            !path.equals("messages.successfully.reloaded") && !path.equals("messages.successfully.deleted.all")) message = message.replace("{id}", replaceID);
        if (message.contains("{prefix}")) message = message.replace("{prefix}", configValues.get("messages.prefix"));

        return MiniMessage.miniMessage().deserialize(message);
    }



    public void reloadConfig() {
        display.saveDefaultConfig();
        config = display.getConfig();

        updateValues();
    }

    public int getValue(String path) {
        int value = 1;

        if (path.equals("textUpdateInterval")) {
            value = config.getInt(path, 1);
        }

        return value;
    }
}