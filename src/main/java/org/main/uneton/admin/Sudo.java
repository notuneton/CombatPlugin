package org.main.uneton.admin;

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
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (args.length >= 2) {
            String user = args[0];
            Player sudomsg = Bukkit.getServer().getPlayer(user);

            if (sudomsg != null) {
                StringBuilder message = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    message.append(args[i]).append(" ");
                }
                sudomsg.chat(message.toString());
                player.sendMessage(ChatColor.GREEN + "Made " + user + " execute " + message);
            } else {
                player.sendActionBar(ChatColor.RED + "That player does not exist.");
            }

        } else {
            player.sendMessage(ChatColor.RED + "usage: /sudo <player> <text>");
        }

        return true;
    }

}
