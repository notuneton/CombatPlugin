package org.main.uneton.frez;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class Freeze implements CommandExecutor {

    public static Set<Player> freeze_list = new HashSet<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (!player.hasPermission("combat.freeze.sv")) {
            player.sendMessage(ChatColor.RED + "Permission Denied: You do not have permission to do this task.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "> /freeze <player>");
            return true;
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendActionBar(ChatColor.DARK_RED + "That user is offline.");
            return true;
        }

        if (args.length == 1) {
            if (freeze_list.contains(target)) {
                freeze_list.remove(target);
            } else {
                freeze_list.add(target);
            }
        }

        return true;
    }
}
