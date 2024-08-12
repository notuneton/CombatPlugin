package org.main.uneton.block;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

import java.util.Set;

import static org.main.uneton.block.Blockplayer.getBlockedPlayers;

public class Unblock implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "> /unblock <player>");
            return true;
        }

        String targetName = args[0];
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null || !target.isOnline()) {
            String warn = ColorUtils.colorize("&4>&c> &8+ 7");
            player.sendActionBar(warn + "That player does not exist.");
            return true;
        }

        Set<String> blockSet = getBlockedPlayers(player.getName());
        if (blockSet.remove(targetName)) {
            player.sendMessage(ChatColor.YELLOW + "Successfully unblock player " + targetName);
        } else {
            player.sendMessage(ChatColor.RED + "Player " + targetName + " is not block");
        }

        return true;
    }
}
