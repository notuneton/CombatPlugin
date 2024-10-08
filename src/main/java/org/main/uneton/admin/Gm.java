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

import static org.main.uneton.utils.MessageHolder.perm;
import static org.main.uneton.utils.MessageHolder.unknown;
import static org.main.uneton.utils.SoundsUtils.playCancerSound;

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
            player.sendMessage(ColorUtils.colorize(perm + command.getName()));
            playCancerSound(player);
            return true;
        }

        if (args.length == 0) {
            String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&8&8&8&3&A&4- &7&x&A&B&A&B&A&B/&x&A&B&A&B&A&Bg&x&A&B&A&B&A&Bm &x&A&B&A&B&A&B<&x&A&B&A&B&A&Bp&x&A&B&A&B&A&Bl&x&A&B&A&B&A&Ba&x&A&B&A&B&A&By&x&A&B&A&B&A&Be&x&A&B&A&B&A&Br&x&A&B&A&B&A&B>");
            player.sendMessage(usage);
            return true;
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(unknown);
            return true;
        }

        if (args.length == 1) {
            if (gm_list.contains(target)) {
                gm_list.remove(target);
                player.sendMessage(ColorUtils.colorize("&aYou are not longer in Godmode."));
                cancelTask();
            } else {
                gm_list.add(target);
                sendGodModePacked(player);
            }
        }
        return true;
    }

    private void sendGodModePacked(Player player) {
        task = Bukkit.getScheduler().runTaskTimer(Combat.getInstance(), () -> {
            player.sendActionBar(ColorUtils.colorize("&fYou are currently in &9Godmode."));
        }, 0, 1);
    }

    public void cancelTask() {
        if (task != null) {
            task.cancel();
        }
    }
}
