package org.main.uneton.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Slippery implements CommandExecutor {

    private final JavaPlugin plugin;
    private final Map<UUID, BukkitTask> playerTasks = new HashMap<>();

    public Slippery(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (!player.hasPermission("combat.slippery.sv")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to run /" + command.getName() + ".");
            return true;
        }
    
        if (args.length == 0) {
            String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l- &7Usage: &x&A&B&A&B&A&B/&x&A&B&A&B&A&Bs&x&A&B&A&B&A&Bl&x&A&B&A&B&A&Bi&x&A&B&A&B&A&Bp&x&A&B&A&B&A&Bp&x&A&B&A&B&A&Be&x&A&B&A&B&A&Br&x&A&B&A&B&A&By &x&A&B&A&B&A&B<&x&A&B&A&B&A&Bp&x&A&B&A&B&A&Bl&x&A&B&A&B&A&Ba&x&A&B&A&B&A&By&x&A&B&A&B&A&Be&x&A&B&A&B&A&Br&x&A&B&A&B&A&B>");
            player.sendMessage(usage);
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendActionBar(ChatColor.DARK_RED + "That user is offline.");
            player.sendMessage(ChatColor.DARK_RED + "That user is offline.");
            return true;
        }

        UUID targetUUID = target.getUniqueId();
        if (playerTasks.containsKey(targetUUID)) {
            //todo Cancel the existing task if it exists
            playerTasks.get(targetUUID).cancel();
            playerTasks.remove(targetUUID);
            String warn = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l- ");
            player.sendMessage(warn + ChatColor.GRAY + "Stopped dropping items from " + ChatColor.DARK_AQUA + target.getName() + "'s" + ChatColor.GRAY + " inventory.");

        } else {
            //todo Start a new task
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    for (ItemStack invitems : target.getInventory().getContents()) {
                        if (invitems != null) {
                            dropItems(target, invitems);
                        }
                    }
                }
            }.runTaskTimer(plugin, 0L, 60L); // Schedule the task every 60 ticks (3 seconds)
            playerTasks.put(targetUUID, task);
            String success = ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&2&0&8&1&8&A&l>&x&3&6&D&D&E&E&l> ");
            player.sendMessage(success + ChatColor.GRAY + "Started dropping items from " + target.getName() + "'s inventory every 3 seconds.");
        }
        return true;
    }

    private void dropItems(Player target, ItemStack invitems) {
        target.getWorld().dropItemNaturally(target.getLocation(), invitems);
        target.getInventory().remove(invitems);
    }
}
