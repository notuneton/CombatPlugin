package org.main.uneton.ignore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static org.main.uneton.ignore.Ignore.ignoredPlayers;

public class Unignore implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED +"usage: /unignore <player>");
            return true;
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendActionBar(ChatColor.RED + "That player does not exist.");
            return true;
        }

        if (ignoredPlayers.remove(target)) {
            target.sendMessage(ChatColor.YELLOW + "Successfully un-ignored player");
        } else {
            target.sendMessage(ChatColor.RED + "Player is not ignored");
        }

        return true;
    }
}
