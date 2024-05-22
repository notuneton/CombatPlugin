package org.main.uneton.ignore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class Ignore implements CommandExecutor {

    public static Set<String> ignoredPlayers = new HashSet<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "> /ignore <player>");
            return true;
        }

        Player user = Bukkit.getServer().getPlayer(args[0]);
        if (user == null || !user.isOnline()) {
            player.sendActionBar(ChatColor.DARK_RED + "That user is offline.");
            return true;
        }

        String target = args[0];
        if (ignoredPlayers.add(target)) {
            player.sendMessage(ChatColor.YELLOW + "Successfully ignored player " + player.getName());
        } else {
            player.sendMessage(ChatColor.YELLOW + "Player is already ignored");
        }

        return true;
    }
}