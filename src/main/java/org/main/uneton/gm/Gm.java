package org.main.uneton.gm;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class Gm implements CommandExecutor {

    public static Set<Player> gm_players = new HashSet<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "usage: /gm <player>");
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target != null) {
                if (gm_players.contains(target)){
                    gm_players.remove(target);
                    target.sendMessage(ChatColor.GREEN + player.getName() + " is no longer in god mode.");
                } else {
                    gm_players.add(target);
                    target.sendMessage(ChatColor.GREEN + player.getName() + " is now in god mode.");
                }
            }
        }
        return true;
    }
}
