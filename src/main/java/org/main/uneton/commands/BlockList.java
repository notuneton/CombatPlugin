package org.main.uneton.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

import java.util.Set;

import static org.main.uneton.commands.Blockplayer.getBlockedPlayers;

public class BlockList implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        Set<String> blockSet = getBlockedPlayers(player.getName());
        if (args.length == 0) {
            if (blockSet.isEmpty()) {
                player.sendMessage(ColorUtils.colorize("&c&lNOT FOUND! &7You don't have any blocked players."));
            }
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(ColorUtils.colorize("&c&lWHO?! &7Couldn't find a player with username "+ target.getName() +"!"));
            return true;
        }

        if (blockSet.isEmpty()) {
            player.sendMessage(ColorUtils.colorize("&7You haven't blocked anyone."));
        } else {
            player.sendMessage(ColorUtils.colorize("You are Blocked:"));
            for (String blocked : blockSet) {
                player.sendMessage(ColorUtils.colorize("- " + blocked));
            }
        }

        return true;
    }
}
