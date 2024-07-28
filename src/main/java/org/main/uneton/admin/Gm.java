package org.main.uneton.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.Combat;
import org.main.uneton.utils.ColorUtils;

import java.util.HashSet;
import java.util.Set;

public class Gm implements CommandExecutor {

    public static Set<Player> gm_list = new HashSet<>();
    private BukkitTask task;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (!player.hasPermission("combat.gm.sv")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to run " + command.getName() + ".");
            return true;
        }

        if (args.length == 0) {
            String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l+ &7&x&A&B&A&B&A&B/&x&A&B&A&B&A&Bg&x&A&B&A&B&A&Bm &x&A&B&A&B&A&B<&x&A&B&A&B&A&Bp&x&A&B&A&B&A&Bl&x&A&B&A&B&A&Ba&x&A&B&A&B&A&By&x&A&B&A&B&A&Be&x&A&B&A&B&A&Br&x&A&B&A&B&A&B>");
            player.sendMessage(usage);
            return true;
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            String warn = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l+ ");
            player.sendMessage(warn + ColorUtils.colorize("&7That player does not exist."));
            return true;
        }

        if (args.length == 1) {
            if (gm_list.contains(target)) {
                gm_list.remove(target);
                player.sendMessage(ColorUtils.colorize("&aYou are not longer in Godmode."));
                cancelTask();
            } else {
                gm_list.add(target);
                sendGodPacked(player);
            }
        }
        return true;
    }

    private void sendGodPacked(Player player) {
        task = Bukkit.getScheduler().runTaskTimer(Combat.getInstance(), () -> {
            player.sendActionBar(ColorUtils.colorize("You are currently in Godmode."));
        }, 0, 1);
    }

    public void cancelTask() {
        if (task != null) {
            task.cancel();
        }
    }
}
