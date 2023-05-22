package me.itzisonn_.display;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {
    private static final FileConfiguration config = Display.getInstance().getConfig();

    public static String getMsg(String path, String replaceTo) {
        String msg = null;

        if (path.equals("messages.errors.onlyPlayerConsole")) {
            msg = config.getString(path, "&cКоманду может выполнять только игрок!");
        }
        if (path.equals("messages.errors.notFull")) {
            msg = config.getString(path, "{prefix} &cНеполная команда! Список команд: {help}");
            msg = msg.replace("{help}", "/display help");
        }
        if (path.equals("messages.errors.unknownAction")) {
            msg = config.getString(path, "{prefix} &cНеизвестный тип действия! Используйте: {subcommands}");
            msg = msg.replace("{subcommands}", "help | create | delete | edit | tphere | tpcoords | tpto | changeid");
        }
        if (path.equals("messages.errors.tooManyArguments")) {
            msg = config.getString(path, "{prefix} &cСлишком много аргументов!");
        }
        if (path.equals("messages.errors.notFoundObjectType")) {
            msg = config.getString(path, "{prefix} &cНе найден тип объекта! Используйте: {types}");
            msg = msg.replace("{types}", "block | item");
        }
        if (path.equals("messages.errors.unknownObjectType")) {
            msg = config.getString(path, "{prefix} &cНеизвестный тип объекта! Используйте: {types}");
            msg = msg.replace("{types}", "block | item");
        }
        if (path.equals("messages.errors.notFoundId")) {
            msg = config.getString(path, "{prefix} &cНе найден ID объекта!");
        }
        if (path.equals("messages.errors.invalidId")) {
            msg = config.getString(path, "{prefix} &cID объекта должен быть числом!");
        }
        if (path.equals("messages.errors.idAlreadyInUse")) {
            msg = config.getString(path, "{prefix} &cID {id} уже используется!");
            msg = msg.replace("{id}", replaceTo);
        }
        if (path.equals("messages.errors.idDoesNotExist")) {
            msg = config.getString(path, "{prefix} &cОбъекта с ID {id} не существует!");
            msg = msg.replace("{id}", replaceTo);
        }
        if (path.equals("messages.errors.notFoundEditType")) {
            msg = config.getString(path, "{prefix} &cНе найден тип редактирования! Используйте: {types}");
            msg = msg.replace("{types}", "blocktype (itemtype) | glowing");
        }
        if (path.equals("messages.errors.unknownEditType")) {
            msg = config.getString(path, "{prefix} &cНеизвестный тип редактирования! Используйте: {types}");
            msg = msg.replace("{types}", "blocktype (itemtype) | glowing");
        }
        if (path.equals("messages.errors.notFoundBlock")) {
            msg = config.getString(path, "{prefix} &cНе найден тип блока!");
        }
        if (path.equals("messages.errors.unknownBlock")) {
            msg = config.getString(path, "{prefix} &cНеизвестный блок!");
        }
        if (path.equals("messages.errors.notFoundItem")) {
            msg = config.getString(path, "{prefix} &cНе найден тип предмета!");
        }
        if (path.equals("messages.errors.unknownItem")) {
            msg = config.getString(path, "{prefix} &cНеизвестный предмет!");
        }
        if (path.equals("messages.errors.notFoundDimension")) {
            msg = config.getString(path, "{prefix} &cНе найдено измерение! Используйте: {types}");
            msg = msg.replace("{types}", "overworld | the_nether | the_end");
        }
        if (path.equals("messages.errors.unknownDimension")) {
            msg = config.getString(path, "{prefix} &cНеизвестное измерение! Используйте: {types}");
            msg = msg.replace("{types}", "overworld | the_nether | the_end");
        }
        if (path.equals("messages.errors.notFoundCoords")) {
            msg = config.getString(path, "{prefix} &cНе найдены координаты!");
        }
        if (path.equals("messages.errors.invalidCoords")) {
            msg = config.getString(path, "{prefix} &cНеверный формат координат!");
        }


        if (path.equals("messages.successfully.created")) {
            msg = config.getString(path, "{prefix} &aОбъект для отображения с ID {id} успешно создан!");
            msg = msg.replace("{id}", replaceTo);
        }
        if (path.equals("messages.successfully.deleted")) {
            msg = config.getString(path, "{prefix} &aОбъект с ID {id} успешно удалён!");
            msg = msg.replace("{id}", replaceTo);
        }
        if (path.equals("messages.successfully.edited")) {
            msg = config.getString(path, "{prefix} &aУ объекта с ID {id} успешно изменён параметр {edittype} на {value}!");
            msg = msg.replace("{id}", replaceTo);
            msg = msg.replace("{edittype}", Checks.getEdittype());
            msg = msg.replace("{value}", Checks.getValue());
        }
        if (path.equals("messages.successfully.teleportedCoords")) {
            msg = config.getString(path, "{prefix} &aОбъект с ID {id} успешно перемещён на координаты {x} {y} {z} в мире {world}!");
            msg = msg.replace("{id}", replaceTo);
            msg = msg.replace("{x}", Checks.getX());
            msg = msg.replace("{y}", Checks.getY());
            msg = msg.replace("{z}", Checks.getZ());
            msg = msg.replace("{world}", Checks.getWorld());
        }
        if (path.equals("messages.successfully.teleportedHere")) {
            msg = config.getString(path, "{prefix} &aОбъект с ID {id} успешно перемещён на ваши координаты!");
            msg = msg.replace("{id}", replaceTo);
        }
        if (path.equals("messages.successfully.teleportedTo")) {
            msg = config.getString(path, "{prefix} &aВы были успешно перемещены на координаты объекта с ID {id}!");
            msg = msg.replace("{id}", replaceTo);
        }
        if (path.equals("messages.successfully.changedID")) {
            msg = config.getString(path, "{prefix} &aУ объекта с ID {id} ID был изменен на {newid}!");
            msg = msg.replace("{id}", replaceTo);
            msg = msg.replace("{newid}", Checks.getNewid());
        }


        if (msg != null && msg.contains("{prefix}")) {
            msg = msg.replace("{prefix}", config.getString("messages.prefix", "&#6d6d6d[&#fdd134D&#fdbf2ei&#fcac27s&#fc9a21p&#fc881bl&#fb7514a&#fb630ey&#6d6d6d]&r"));
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