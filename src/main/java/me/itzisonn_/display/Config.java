package me.itzisonn_.display;

import me.itzisonn_.display.maincommand.DisplayTab;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    private static FileConfiguration config = Display.getInstance().getConfig();



    public static Component getMsg(String path, String[] replaceTo) {
        String msg = null;

        if (path.equals("messages.errors.onlyPlayer")) {
            msg = config.getString(path, "<red>Команду может выполнять только игрок!");
        }
        if (path.equals("messages.errors.notFull")) {
            msg = config.getString(path, "{prefix} <red>Неполная команда! Список команд: {help}");
            msg = msg.replace("{help}", "/display help");
        }
        if (path.equals("messages.errors.unknownAction")) {
            msg = config.getString(path, "{prefix} <red>Неизвестный тип действия! Используйте: {subcommands}");
            msg = msg.replace("{subcommands}", String.valueOf(DisplayTab.mainTabComplete())).replace(", ", " | ");
            msg = msg.replace("{subcommands}", String.valueOf(DisplayTab.mainTabComplete())).replace("[", "").replace("]", "");
        }
        if (path.equals("messages.errors.tooManyArguments")) {
            msg = config.getString(path, "{prefix} <red>Слишком много аргументов!");
        }
        if (path.equals("messages.errors.noPermission")) {
            msg = config.getString(path, "{prefix} <red>У вас нет прав на выполнение данной команды!");
        }
        if (path.equals("messages.errors.notFoundObjectType")) {
            msg = config.getString(path, "{prefix} <red>Не найден тип объекта! Используйте: {types}");
            msg = msg.replace("{types}", "block | item | text");
        }
        if (path.equals("messages.errors.unknownObjectType")) {
            msg = config.getString(path, "{prefix} <red>Неизвестный тип объекта! Используйте: {types}");
            msg = msg.replace("{types}", "block | item | text");
        }
        if (path.equals("messages.errors.notFoundId")) {
            msg = config.getString(path, "{prefix} <red>Не найден ID объекта!");
        }
        if (path.equals("messages.errors.invalidId")) {
            msg = config.getString(path, "{prefix} <red>ID объекта должен быть числом!");
        }
        if (path.equals("messages.errors.idAlreadyInUse")) {
            msg = config.getString(path, "{prefix} <red>ID {id} уже используется!");
            msg = msg.replace("{id}", replaceTo[0]);
        }
        if (path.equals("messages.errors.idDoesNotExist")) {
            msg = config.getString(path, "{prefix} <red>Объекта с ID {id} не существует!");
            msg = msg.replace("{id}", replaceTo[0]);
        }
        if (path.equals("messages.errors.notFoundEditType")) {
            msg = config.getString(path, "{prefix} <red>Не найден тип редактирования! Используйте: {types}");
            if (replaceTo[0].equalsIgnoreCase("block")) msg = msg.replace("{types}", "blocktype | glowing | scale");
            if (replaceTo[0].equalsIgnoreCase("item")) msg = msg.replace("{types}", "itemtype | glowing | scale");
            if (replaceTo[0].equalsIgnoreCase("text")) msg = msg.replace("{types}", "addline | setline | removeline | scale");
        }
        if (path.equals("messages.errors.unknownEditType")) {
            msg = config.getString(path, "{prefix} <red>Неизвестный тип редактирования! Используйте: {types}");
            msg = msg.replace("{types}", "blocktype (itemtype) | glowing");
        }
        if (path.equals("messages.errors.notFoundBlock")) {
            msg = config.getString(path, "{prefix} <red>Не найден тип блока!");
        }
        if (path.equals("messages.errors.unknownBlock")) {
            msg = config.getString(path, "{prefix} <red>Неизвестный блок!");
        }
        if (path.equals("messages.errors.notFoundItem")) {
            msg = config.getString(path, "{prefix} <red>Не найден тип предмета!");
        }
        if (path.equals("messages.errors.unknownItem")) {
            msg = config.getString(path, "{prefix} <red>Неизвестный предмет!");
        }
        if (path.equals("messages.errors.notFoundLineNumber")) {
            msg = config.getString(path, "{prefix} <red>Не найден номер строки!");
        }
        if (path.equals("messages.errors.invalidLineNumber")) {
            msg = config.getString(path, "{prefix} <red>Номер строки должен быть числом!");
        }
        if (path.equals("messages.errors.unknownLineNumber")) {
            msg = config.getString(path, "{prefix} <red>Неизвестная строка!");
        }
        if (path.equals("messages.errors.oneLine")) {
            msg = config.getString(path, "{prefix} <red>Осталась последняя строка!");
        }
        if (path.equals("messages.errors.notFoundText")) {
            msg = config.getString(path, "{prefix} <red>Не найден текст!");
        }
        if (path.equals("messages.errors.notFoundScales")) {
            msg = config.getString(path, "{prefix} <red>Не найдены размеры!");
        }
        if (path.equals("messages.errors.invalidScales")) {
            msg = config.getString(path, "{prefix} <red>Неверный формат размеров!");
        }
        if (path.equals("messages.errors.notFoundDimension")) {
            msg = config.getString(path, "{prefix} <red>Не найдено измерение! Используйте: {types}");
            msg = msg.replace("{types}", "overworld | the_nether | the_end");
        }
        if (path.equals("messages.errors.unknownDimension")) {
            msg = config.getString(path, "{prefix} <red>Неизвестное измерение! Используйте: {types}");
            msg = msg.replace("{types}", "overworld | the_nether | the_end");
        }
        if (path.equals("messages.errors.notFoundCoords")) {
            msg = config.getString(path, "{prefix} <red>Не найдены координаты!");
        }
        if (path.equals("messages.errors.invalidCoords")) {
            msg = config.getString(path, "{prefix} <red>Неверный формат координат!");
        }

        if (path.equals("messages.successfully.reloaded")) {
            msg = config.getString(path, "{prefix} <green>Конфиг плагина успешно перезагружен!");
        }
        if (path.equals("messages.successfully.created")) {
            msg = config.getString(path, "{prefix} <green>Объект для отображения с ID {id} и типом {type} успешно создан!");
            msg = msg.replace("{id}", replaceTo[0]);
            msg = msg.replace("{type}", replaceTo[1]);
        }
        if (path.equals("messages.successfully.deleted")) {
            msg = config.getString(path, "{prefix} <green>Объект с ID {id} успешно удалён!");
            msg = msg.replace("{id}", replaceTo[0]);
        }
        if (path.equals("messages.successfully.edited")) {
            msg = config.getString(path, "{prefix} <green>У объекта с ID {id} успешно изменён параметр {edittype} на {value}<reset><green>!");
            msg = msg.replace("{id}", replaceTo[0]);
            msg = msg.replace("{edittype}", replaceTo[1]);
            msg = msg.replace("{value}", replaceTo[2]);
        }
        if (path.equals("messages.successfully.editedScale")) {
            msg = config.getString(path, "{prefix} <green>У объекта с ID {id} успешно изменён размер на {scalex}, {scaley}, {scalez}");
            msg = msg.replace("{id}", replaceTo[0]);
            msg = msg.replace("{scalex}", replaceTo[1]);
            msg = msg.replace("{scaley}", replaceTo[2]);
            msg = msg.replace("{scalez}", replaceTo[3]);
        }
        if (path.equals("messages.successfully.editedAddline")) {
            msg = config.getString(path, "{prefix} <green>Объекту с ID {id} успешно добавлена строка под номером {linenumber} с значением {text}<reset><green>!");
            msg = msg.replace("{id}", replaceTo[0]);
            msg = msg.replace("{linenumber}", replaceTo[1]);
            msg = msg.replace("{text}", replaceTo[2]);
        }
        if (path.equals("messages.successfully.editedSetline")) {
            msg = config.getString(path, "{prefix} <green>У объекта с ID {id} успешно изменена строка под номером {linenumber} на значение {text}<reset><green>!");
            msg = msg.replace("{id}", replaceTo[0]);
            msg = msg.replace("{linenumber}", replaceTo[1]);
            msg = msg.replace("{text}", replaceTo[2]);
        }
        if (path.equals("messages.successfully.editedRemoveline")) {
            msg = config.getString(path, "{prefix} <green>У объекта с ID {id} успешно удалена строка под номером {linenumber}<reset><green>!");
            msg = msg.replace("{id}", replaceTo[0]);
            msg = msg.replace("{linenumber}", replaceTo[1]);
        }
        if (path.equals("messages.successfully.teleportedCoords")) {
            msg = config.getString(path, "{prefix} <green>Объект с ID {id} успешно перемещён на координаты {x} {y} {z} в мире {world}!");
            msg = msg.replace("{id}", replaceTo[0]);
            msg = msg.replace("{x}", replaceTo[1]);
            msg = msg.replace("{y}", replaceTo[2]);
            msg = msg.replace("{z}", replaceTo[3]);
            msg = msg.replace("{world}", replaceTo[4]);
        }
        if (path.equals("messages.successfully.teleportedHere")) {
            msg = config.getString(path, "{prefix} <green>Объект с ID {id} успешно перемещён на ваши координаты!");
            msg = msg.replace("{id}", replaceTo[0]);
        }
        if (path.equals("messages.successfully.teleportedTo")) {
            msg = config.getString(path, "{prefix} <green>Вы были успешно перемещены на координаты объекта с ID {id}!");
            msg = msg.replace("{id}", replaceTo[0]);
        }
        if (path.equals("messages.successfully.changedID")) {
            msg = config.getString(path, "{prefix} <green>У объекта с ID {id} ID был изменен на {newid}!");
            msg = msg.replace("{id}", replaceTo[0]);
            msg = msg.replace("{newid}", replaceTo[1]);
        }


        if (msg != null && msg.contains("{prefix}")) {
            msg = msg.replace("{prefix}", config.getString("messages.prefix", "<#6d6d6d>[<gradient:#fdd134:#fb630e>Display</gradient><#6d6d6d>]<reset>"));
        }

        assert msg != null;
        return MiniMessage.miniMessage().deserialize(msg);
    }



    public static void reloadConfig() {
        config = Display.getInstance().getConfig();
    }

    public static int getValue(String path) {
        int value = 1;

        if (path.equals("textUpdateInterval")) {
            value = config.getInt(path, 1);
        }

        return value;
    }
}