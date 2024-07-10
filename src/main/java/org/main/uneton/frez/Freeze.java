package org.main.uneton.frez;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

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
            player.sendMessage(ChatColor.RED + "You do not have permission to run /" + command.getName() + ".");
            return true;
        }

        if (args.length == 0) {
            String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l- &7Usage: \"&x&A&B&A&B&A&B/&x&A&B&A&B&A&Bi&x&A&B&A&B&A&Bn&x&A&B&A&B&A&Bv&x&A&B&A&B&A&Bs&x&A&B&A&B&A&Be&x&A&B&A&B&A&Be &x&A&B&A&B&A&B<&x&A&B&A&B&A&Bp&x&A&B&A&B&A&Bl&x&A&B&A&B&A&Ba&x&A&B&A&B&A&By&x&A&B&A&B&A&Be&x&A&B&A&B&A&Br&x&A&B&A&B&A&B> &7/freeze <player>\"\n");
            player.sendMessage(usage);
            return true;
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendActionBar(ChatColor.DARK_RED + "That user is offline.");
            player.sendMessage(ChatColor.DARK_RED + "That user is offline.");
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
