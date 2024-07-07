package org.main.uneton.gm;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.Combat;

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
            return false;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "> /gm <player>");
            return true;
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendActionBar(ChatColor.DARK_RED + "That user is offline.");
            player.sendMessage(ChatColor.DARK_RED + "That user is offline.");
            return true;
        }

        if (args.length == 1) {
            if (gm_list.contains(target)) {
                gm_list.remove(target);
                player.sendMessage(ChatColor.WHITE + "You are not" +ChatColor.RED + " longer " + ChatColor.WHITE + "in" + ChatColor.BLUE + " Godmode.");
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
            player.sendActionBar(ChatColor.WHITE + "You are currently in " + ChatColor.BLUE + "Godmode.");
        }, 0, 1);
    }

    public void cancelTask() {
        if (task != null) {
            task.cancel();
        }
    }
}
