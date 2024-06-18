package org.main.uneton.ignore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static org.main.uneton.ignore.Ignore.getIgnoredPlayers;

public class Unignore implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "> /unignore <player>");
            return true;
        }

        String targetName = args[0];
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null || !target.isOnline()) {
            player.sendActionBar(ChatColor.DARK_RED + "That user is offline.");
            return true;
        }

        Set<String> ignoredSet = getIgnoredPlayers(player.getName());
        if (ignoredSet.remove(targetName)) {
            player.sendMessage(ChatColor.YELLOW + "Successfully unignored player " + targetName);
        } else {
            player.sendMessage(ChatColor.RED + "Player " + targetName + " is not ignored");
        }

        return true;
    }
}
