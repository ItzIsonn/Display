package me.itzisonn_.display.commands;

import me.itzisonn_.display.Config;
import me.itzisonn_.display.maincommand.DisplayCommand;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

public class Help {
    private static final Player player = DisplayCommand.getPlayer();



    public static void sendHelp() {
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#6d6d6d>[<gradient:#fdd134:#fb630e>Display</gradient><#6d6d6d>] <#fc881b>Помощь по команде /display"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#fdd134>/display help <dark_grey>- <#a5a5a5>Показать помощь"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#fdd134>/display reload <dark_grey>- <#a5a5a5>Перезагрузить конфиг"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#fdd134>/display create [block | item | text] <id> <dark_grey>- <#a5a5a5>Создать объект для отображения"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#fdd134>/display delete <id> <dark_grey>- <#a5a5a5>Удалить объект с указанным ID"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#fdd134>/display edit <id> ... <dark_grey>- <#a5a5a5>Изменить параметры у объекта с указанным ID"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("  <#fcac27>• [blocktype | itemtype] <type> <dark_grey>- <#a5a5a5>Изменить тип блока/предмета. Только для block и item"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("  <#fcac27>• glowing <dark_grey>- <#a5a5a5>Изменить подсветку на противоположное значение. Только для block и item"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("  <#fcac27>• scale <scalex> <scaley> <scalez> <dark_grey>- <#a5a5a5>Изменить размеры объекта на указанные"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("  <#fcac27>• addline <linenumber> <text> <dark_grey>- <#a5a5a5>Добавить строку в указанной позиции. Только для text"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("  <#fcac27>• setline <linenumber> <text> <dark_grey>- <#a5a5a5>Изменить строку с указанной позицией. Только для text"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("  <#fcac27>• removeline <linenumber> <dark_grey>- <#a5a5a5>Удалить строку с указанной позицией. Только для text"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#fdd134>/display tpcoords <id> <world> <x> <y> <z> <dark_grey>- <#a5a5a5>Переместить объект с указанным ID на указанные координаты"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#fdd134>/display tphere <id> <dark_grey>- <#a5a5a5>Переместить объект с указанным ID на координаты исполнителя"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#fdd134>/display tpto <id> <dark_grey>- <#a5a5a5>Переместить исполнителя на координаты объекта с указанным ID"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("<#fdd134>/display changeid <id> <newid> <dark_grey>- <#a5a5a5>Изменить ID у объекта с указанным ID на новый"));
    }



    public static boolean checkHelp(String[] args) {
        if (player.hasPermission("display.command.help") || player.hasPermission("display.command.*")) {
            if (args.length == 1) {
                return true;
            }
            else {
                player.sendMessage(Config.getMsg("messages.errors.tooManyArguments", null));
                return false;
            }
        }
        else {
            player.sendMessage(Config.getMsg("messages.errors.unknownAction", null));
            return false;
        }
    }
}