package org.main.uneton.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PermissionUtils {

    public static void messageIfNotPermitted(Player player, String perm) {
        if (!player.hasPermission(perm)) {
            player.sendMessage(ChatColor.BLUE + "Access denied.\n " + ChatColor.WHITE + "You need admintrator privileges to complete this task. Go in Server console, then run command op player name so player get admintrator.");
        }
    }
}
