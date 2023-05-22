package me.itzisonn_.display;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {
    private static final FileConfiguration config = Display.getInstance().getConfig();

    public static String getMsg(String path, String replaceTo) {
        String msg = null;

        if (path.equals("messages.all.onlyPlayerConsole")) {
            msg = config.getString(path, "&cКоманду может выполнять только игрок!");
        }
        if (path.equals("messages.all.notFull")) {
            msg = config.getString(path, "{prefix} &cНеполная команда! Список команд: {help}");
            msg = msg.replace("{help}", "/display help");
        }
        if (path.equals("messages.all.unknownAction")) {
            msg = config.getString(path, "{prefix} &cНеизвестный тип действия! Используйте: {subcommands}");
            msg = msg.replace("{subcommands}", "help | create | delete | tphere | tpcoords");
        }
        if (path.equals("messages.all.tooManyArguments")) {
            msg = config.getString(path, "{prefix} &cСлишком много аргументов!");
        }

        if (path.equals("messages.createCommand.notFoundObjectType")) {
            msg = config.getString(path, "{prefix} &cНе найден тип объекта! Используйте: {types}");
            msg = msg.replace("{types}", "block | item");
        }
        if (path.equals("messages.createCommand.unknownObjectType")) {
            msg = config.getString(path, "{prefix} &cНеизвестный тип объекта! Используйте: {types}");
            msg = msg.replace("{types}", "block | item");
        }
        if (path.equals("messages.createCommand.notFoundId")) {
            msg = config.getString(path, "{prefix} &cНе найден ID объекта! Используйте любое натуральное число, не использовавшееся раньше");
        }
        if (path.equals("messages.createCommand.invalidId")) {
            msg = config.getString(path, "{prefix} &cID объекта должен быть числом! Используйте любое натуральное число, не использовавшееся раньше");
        }
        if (path.equals("messages.createCommand.idAlreadyInUse")) {
            msg = config.getString(path, "{prefix} &cID {id} уже используется! Попробуйте другой");
            msg = msg.replace("{id}", replaceTo);
        }
        if (path.equals("messages.createCommand.successfullyCreated")) {
            msg = config.getString(path, "{prefix} &aОбъект для отображения с ID {id} успешно создан!");
            msg = msg.replace("{id}", replaceTo);
        }

        if (path.equals("messages.editCommand.notFoundEditType")) {
            msg = config.getString(path, "{prefix} &cНе найден тип редактирования! Используйте: {types}");
            msg = msg.replace("{types}", "blocktype (itemtype) | glowing");
        }
        if (path.equals("messages.editCommand.unknownEditType")) {
            msg = config.getString(path, "{prefix} &cНеизвестный тип редактирования! Используйте: {types}");
            msg = msg.replace("{types}", "blocktype (itemtype) | glowing");
        }
        if (path.equals("messages.editCommand.notFoundBlock")) {
            msg = config.getString(path, "{prefix} &cНе найден тип блока! Используйте существующий блок");
        }
        if (path.equals("messages.editCommand.unknownBlock")) {
            msg = config.getString(path, "{prefix} &cНеизвестный блок! Используйте существующий блок");
        }
        if (path.equals("messages.editCommand.notFoundItem")) {
            msg = config.getString(path, "{prefix} &cНе найден тип предмета! Используйте существующий предмет");
        }
        if (path.equals("messages.editCommand.unknownItem")) {
            msg = config.getString(path, "{prefix} &cНеизвестный предмет! Используйте существующий предмет");
        }
        if (path.equals("messages.editCommand.successfullyEdited")) {
            msg = config.getString(path, "{prefix} &aУ объекта с ID {id} успешно изменён параметр {edittype} на {value}");
            msg = msg.replace("{id}", replaceTo);
            msg = msg.replace("{edittype}", Checks.getEdittype());
            msg = msg.replace("{value}", Checks.getValue());
        }

        if (path.equals("messages.tpcoordsCommand.notFoundDimension")) {
            msg = config.getString(path, "{prefix} &cНе найдено измерение! Используйте: {types}");
            msg = msg.replace("{types}", "overworld | the_nether | the_end");
        }
        if (path.equals("messages.tpcoordsCommand.unknownDimension")) {
            msg = config.getString(path, "{prefix} &cНеизвестное измерение! Используйте: {types}");
            msg = msg.replace("{types}", "overworld | the_nether | the_end");
        }
        if (path.equals("messages.tpcoordsCommand.notFoundCoords")) {
            msg = config.getString(path, "{prefix} &cНе найдены координаты! Используйте формат Double");
        }
        if (path.equals("messages.tpcoordsCommand.invalidCoords")) {
            msg = config.getString(path, "{prefix} &cНеверный формат координат! Используйте формат Double");
        }
        if (path.equals("messages.tpcoordsCommand.successfullyTpcoords")) {
            msg = config.getString(path, "{prefix} &aОбъект с ID {id} успешно перемещён на координаты {x} {y} {z} в мире {world}");
            msg = msg.replace("{id}", replaceTo);
            msg = msg.replace("{x}", Checks.getX());
            msg = msg.replace("{y}", Checks.getY());
            msg = msg.replace("{z}", Checks.getZ());
            msg = msg.replace("{world}", Checks.getWorld());
        }

        if (path.equals("messages.other.notFoundId")) {
            msg = config.getString(path, "{prefix} &cНе найден ID объекта! Используйте существующий ID объекта");
        }
        if (path.equals("messages.other.invalidId")) {
            msg = config.getString(path, "{prefix} &cID объекта должен быть числом! Используйте существующий ID объекта");
        }
        if (path.equals("messages.other.idDoesNotExist")) {
            msg = config.getString(path, "{prefix} &cОбъекта с ID {id} не существует!");
            msg = msg.replace("{id}", replaceTo);
        }
        if (path.equals("messages.other.successfullyDeleted")) {
            msg = config.getString(path, "{prefix} &aОбъект с ID {id} успешно удалён!");
            msg = msg.replace("{id}", replaceTo);
        }
        if (path.equals("messages.other.successfullyTphere")) {
            msg = config.getString(path, "{prefix} &aОбъект с ID {id} успешно перемещён на ваши координаты!");
            msg = msg.replace("{id}", replaceTo);
        }
        if (path.equals("messages.other.successfullyTpto")) {
            msg = config.getString(path, "{prefix} &aВы были успешно перемещены на координаты объекта с ID {id}!");
            msg = msg.replace("{id}", replaceTo);
        }

        if (msg != null && msg.contains("{prefix}")) {
            msg = msg.replace("{prefix}", config.getString("messages.prefix", "&8[&#fdd134D&#fdbf2ei&#fcac27s&#fc9a21p&#fc881bl&#fb7514a&#fb630ey&8]"));
        }

        msg = useHex(msg);
        return msg;
    }



    private static String useHex(String message) {
        Pattern pattern = Pattern.compile("(#[a-fA-F0-9]{6})");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();
            for (char c : ch) {
                builder.append("&").append(c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message).replace('&', '§');
    }
}