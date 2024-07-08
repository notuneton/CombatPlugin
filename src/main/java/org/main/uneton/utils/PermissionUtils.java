package org.main.uneton.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class PermissionUtils {

    public static void messageIfNotPermitted(CommandSender sender, String perm) {
        if (!sender.hasPermission(perm)) {
            sender.sendMessage(ChatColor.BLUE + "Access denied.\n " + ChatColor.WHITE + "You need admintrator privileges to complete this task. Go in Server console, then run command op player name so player get admintrator.");
        }
    }
}
