package org.main.uneton.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Sudo implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        ChatColor h = ChatColor.GRAY;
        ChatColor v = ChatColor.GREEN;
        ChatColor vv = ChatColor.DARK_GREEN;
        Player player = (Player) sender;

        if(args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /sudo <target> <message>");
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendActionBar(ChatColor.DARK_RED + "That user is offline.");
            return true;
        }

        if (args.length >= 2) {
            String user = args[0];
            Player cmd = Bukkit.getServer().getPlayer(user);

            if (cmd != null) {
                StringBuilder message = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    message.append(args[i]).append(" ");
                }

                cmd.chat(message.toString());
                //TODO message
            }
        }
        return true;
    }
}