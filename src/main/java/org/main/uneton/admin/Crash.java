package org.main.uneton.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

import static org.main.uneton.utils.SoundsUtils.playCancerSound;

public class Crash implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (!player.hasPermission("combat.crash.sv")) {
            player.sendMessage(ColorUtils.colorize("&c&lCAN'T! &7You do not have permission to run /" + command.getName() + "."));
            playCancerSound(player);
            return true;
        }

        if (args.length == 0) {
            String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l+ &7&x&A&B&A&B&A&B/&x&A&B&A&B&A&Bc&x&A&B&A&B&A&Br&x&A&B&A&B&A&Ba&x&A&B&A&B&A&Bs&x&A&B&A&B&A&Bh &x&A&B&A&B&A&B<&x&A&B&A&B&A&Bp&x&A&B&A&B&A&Bl&x&A&B&A&B&A&Ba&x&A&B&A&B&A&By&x&A&B&A&B&A&Be&x&A&B&A&B&A&Br&x&A&B&A&B&A&B>");
            player.sendMessage(usage);
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null || !target.isOnline()) {
                String warn = ColorUtils.colorize("&4>&c> &8+ &7");
                player.sendActionBar(warn + "That player does not exist.");
                return true;
            }

            player.spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), Integer.MAX_VALUE);
            String success = ColorUtils.colorize("&x&5&B&5&B&5&B&l>&x&2&0&8&1&8&A&l>&x&3&6&D&D&E&E&l> &7");
            player.sendMessage(success + "You have crashed " + ChatColor.UNDERLINE+ ColorUtils.colorize("&a" +target.getName() + "&7" + "."));
        }
        return true;
    }
}