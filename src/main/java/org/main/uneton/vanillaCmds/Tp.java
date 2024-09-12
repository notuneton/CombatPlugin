package org.main.uneton.vanillaCmds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

import static org.main.uneton.utils.SoundsUtils.playCancerSound;

public class Tp implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (!player.hasPermission("combat.tp.sv")) {
            player.sendMessage(ColorUtils.colorize("&c&lCAN'T! &7You do not have permission to run /" + command.getName() + "."));
            playCancerSound(player);
            return true;
        }

        if (args.length == 0) {
            String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &8+ &x&A&B&A&B&A&B/&x&A&B&A&B&A&Bt&x&A&B&A&B&A&Bp &x&A&B&A&B&A&B<&x&A&B&A&B&A&Bp&x&A&B&A&B&A&Bl&x&A&B&A&B&A&Ba&x&A&B&A&B&A&By&x&A&B&A&B&A&Be&x&A&B&A&B&A&Br &x&A&B&A&B&A&B<&x&A&B&A&B&A&Bt&x&A&B&A&B&A&Ba&x&A&B&A&B&A&Br&x&A&B&A&B&A&Bg&x&A&B&A&B&A&Be&x&A&B&A&B&A&Bt> &7| &x&A&B&A&B&A&B/&x&A&B&A&B&A&Bt&x&A&B&A&B&A&Bp &x&A&B&A&B&A&B<&x&A&B&A&B&A&Bx&x&A&B&A&B&A&By&x&A&B&A&B&A&B> &x&A&B&A&B&A&By &x&A&B&A&B&A&B<&x&A&B&A&B&A&Bz&x&A&B&A&B&A&B>");
            player.sendMessage(usage);
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null || !target.isOnline()) {
                String warn = ColorUtils.colorize("&4>&c> &8+ &7");
                player.sendMessage(warn + "That player does not exist.");
                return true;
            }
            player.teleport(target);
            player.sendMessage(ColorUtils.colorize("&x&5&B&5&B&5&B&l>&x&1&C&7&A&1&7&l>&x&3&3&D&D&2&A&l> &8+ &7Teleporting. Whoosh..."));
        }

        if (args.length == 2) {
            Player fromPlayer = Bukkit.getPlayer(args[0]);
            Player toPlayer = Bukkit.getPlayer(args[1]);

            assert toPlayer != null;
            assert fromPlayer != null;
            fromPlayer.teleport(toPlayer);
            player.sendMessage(ColorUtils.colorize("&x&5&B&5&B&5&B&l>&x&1&C&7&A&1&7&l>&x&3&3&D&D&2&A&l> &8+ &7Teleporting. Whoosh..."));
            toPlayer.sendMessage(ColorUtils.colorize("&x&5&B&5&B&5&B&l>&x&1&C&7&A&1&7&l>&x&3&3&D&D&2&A&l> &8+ &7'" + fromPlayer.getName() + "' has been teleported to you!"));
        }
        return true;
    }
}
