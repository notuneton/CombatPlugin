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

public class Vanish implements CommandExecutor {

    private static Combat plugin;
    public Vanish(Combat plugin) {
        Vanish.plugin = plugin;
    }
    BukkitTask task;

    private final Set<Player> vanishedPlayers = new HashSet<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (!player.hasPermission("combat.vanish.sv")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to run " + command.getName() + ".");
            return true;
        }

        if (args.length == 1) {
            String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l+ &x&A&B&A&B&A&B/&x&A&B&A&B&A&Bv&x&A&B&A&B&A&Ba&x&A&B&A&B&A&Bn&x&A&B&A&B&A&Bi&x&A&B&A&B&A&Bs&x&A&B&A&B&A&Bh &x&A&B&A&B&A&B");
            player.sendMessage(usage);
            return true;
        }

        if (vanishedPlayers.contains(player)) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.showPlayer(plugin, player);
            }
            vanishedPlayers.remove(player);
            String success = ColorUtils.colorize("&x&5&B&5&B&5&B&l>&x&2&0&8&1&8&A&l>&x&3&6&D&D&E&E&l> &x&2&E&2&E&2&E&l+ &7");
            player.sendMessage(success + ColorUtils.colorize("&fYou are not longer in &cVANISHED"));
            cancelTask();
        } else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.hidePlayer(plugin, player);
            }
            vanishedPlayers.add(player);
            sendVanishPacked(player);
        }

        return true;
    }

    private void sendVanishPacked(Player player) {
        task = Bukkit.getScheduler().runTaskTimer(Combat.getInstance(), () -> {
            player.sendActionBar(ColorUtils.colorize("&fYou are currently &cVANISHED"));
        }, 0, 1);
    }

    public void cancelTask() {
        if (task != null) {
            task.cancel();
        }
    }
}
