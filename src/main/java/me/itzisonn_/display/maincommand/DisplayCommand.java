package me.itzisonn_.display.maincommand;

import me.itzisonn_.display.commands.*;
import me.itzisonn_.display.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class DisplayCommand extends AbstractCommand {
    private static Player player;



    @Override
    public void executeCommand(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            player = (Player) sender;

            if (player.hasPermission("display.command.*") || player.hasPermission("display.command.help") || player.hasPermission("display.command.reload") ||
                    player.hasPermission("display.command.create") || player.hasPermission("display.command.delete") || player.hasPermission("display.command.edit") ||
                    player.hasPermission("display.command.tpcoords") || player.hasPermission("display.command.tphere") || player.hasPermission("display.command.tpto") ||
                    player.hasPermission("display.command.changeid")) {

                if (!(args.length == 0)) {

                    if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("create") ||
                            args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("tpcoords") ||
                            args[0].equalsIgnoreCase("tphere") || args[0].equalsIgnoreCase("tpto") || args[0].equalsIgnoreCase("changeid")) {

                        switch (args[0]) {
                            case "help":
                                if (Help.checkHelp(args)) {
                                    Help.sendHelp();
                                }
                                break;

                            case "reload":
                                if (Reload.checkReload(args)) {
                                    Reload.reloadConfig();
                                }
                                break;

                            case "create":
                                if (Create.checkCreate(args)) {
                                    int id = Integer.parseInt(args[2]);
                                    EntityType type = EntityType.valueOf((args[1] + "_display").toUpperCase());
                                    Create.createObject(id, type);
                                }
                                break;

                            case "delete":
                                if (Delete.checkDelete(args)) {
                                    Delete.deleteObject(Integer.parseInt(args[1]));
                                }
                                break;

                            case "edit":
                                if (Edit.checkEditMain(args)) {
                                    int id = Integer.parseInt(args[1]);
                                    String editType = args[2].toLowerCase();

                                    switch (editType) {
                                        case "blocktype":
                                        case "itemtype":
                                            Edit.editMaterial(id, Material.getMaterial(args[3].toUpperCase()));
                                            break;

                                        case "glowing":
                                            Edit.editGlowing(id);
                                            break;

                                        case "scale":
                                            Edit.editScale(id, Float.parseFloat(args[3]), Float.parseFloat(args[4]), Float.parseFloat(args[5]));
                                            break;

                                        case "addline":
                                        case "setline":
                                            StringBuilder sb = new StringBuilder();
                                            for (int i = 4; i < args.length; i++) {
                                                sb.append(args[i]).append(" ");
                                            }
                                            sb.setLength(sb.length() - 1);
                                            sb.append("<reset>");
                                            String text = String.valueOf(sb).replaceAll("<br\\s*?>", "");

                                            Edit.editText(id, editType, Integer.parseInt(args[3]), text);
                                            break;
                                        case "removeline":
                                            Edit.editText(id, editType, Integer.parseInt(args[3]), null);
                                    }
                                }
                                break;

                            case "tpcoords":
                                if (Tpcoords.checkTpcoords(args)) {
                                    Location location = new Location(Bukkit.getWorld(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]));
                                    Tpcoords.tpCoords(Integer.parseInt(args[1]), location);
                                }
                                break;

                            case "tphere":
                                if (Tphere.checkTphere(args)) {
                                    Tphere.tpHere(Integer.parseInt(args[1]));
                                }
                                break;

                            case "tpto":
                                if (Tpto.checkTpto(args)) {
                                    Tpto.tpTo(Integer.parseInt(args[1]));
                                }
                                break;

                            case "changeid":
                                if (ChangeID.checkChangeID(args)) {
                                    ChangeID.changeID(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                                }
                                break;
                        }
                    }
                    else {
                        player.sendMessage(Config.getMsg("messages.errors.unknownAction", null));
                    }
                }
                else {
                    player.sendMessage(Config.getMsg("messages.errors.notFull", null));
                }
            }
            else {
                player.sendMessage(Config.getMsg("messages.errors.noPermission", null));
            }
        }
        else {
            sender.sendMessage(Config.getMsg("messages.errors.onlyPlayer", null));
        }
    }



    public DisplayCommand() { super("display"); }
    public static Player getPlayer() { return player; }
}