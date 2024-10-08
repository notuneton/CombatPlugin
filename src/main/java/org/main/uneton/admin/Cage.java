package org.main.uneton.admin;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.Combat;
import org.main.uneton.utils.ColorUtils;

import static org.main.uneton.utils.MessageHolder.*;
import static org.main.uneton.utils.SoundsUtils.playCancerSound;

public class Cage implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (!player.hasPermission("combat.cage.sv")) {
            player.sendMessage(ColorUtils.colorize(perm + command.getName()));
            playCancerSound(player);
            return true;
        }

        if (args.length == 0) {
            String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&8&8&8&3&A&4- &7&x&A&B&A&B&A&B/&x&A&B&A&B&A&Bc&x&A&B&A&B&A&Ba&x&A&B&A&B&A&Bg&x&A&B&A&B&A&Be &x&A&B&A&B&A&B<&x&A&B&A&B&A&Bp&x&A&B&A&B&A&Bl&x&A&B&A&B&A&Ba&x&A&B&A&B&A&By&x&A&B&A&B&A&Be&x&A&B&A&B&A&Br&x&A&B&A&B&A&B>");
            player.sendMessage(usage);
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null || !target.isOnline()) {
                player.sendMessage(unknown);
                return true;
            }

            spawnTrap(target);
        }
        return true;
    }

    public static void spawnTrap(Player player) {
        Location loc = player.getLocation();
        Location bottomCorner = loc.clone();
        player.setGameMode(GameMode.ADVENTURE);

        player.sendMessage(ColorUtils.colorize("&x&5&B&5&B&5&B&l>&x&2&0&8&1&8&A&l>&x&3&6&D&D&E&E&l> &7You were trapped by " + ChatColor.UNDERLINE + ChatColor.WHITE + player.getName() + "&7!"));

        // Set spawner blocks only at the outer edges
        for (int x = 0; x <= 4; x++) {
            for (int y = 0; y <= 4; y++) {
                for (int z = 0; z <= 4; z++) {
                    if (x == 0 || x == 4 || y == 0 || y == 4 || z == 0 || z == 4) {
                        bottomCorner.clone().add(x, y, z).getBlock().setType(Material.SPAWNER);
                    }
                }
            }
        }

        Location middleLocation = bottomCorner.clone().add(2, 3, 2);
        player.teleport(middleLocation);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline() && !player.isDead()) {
                    player.setGameMode(GameMode.SURVIVAL);
                    player.sendMessage(ColorUtils.colorize("&aYou have been released from the trap!"));
                    removeTrapBox(bottomCorner);
                }
            }
        }.runTaskLater(Combat.getInstance(), 1200); // 1 minute
    }

    private static void removeTrapBox(Location bottomCorner) {
        for (int x = 0; x <= 4; x++) {
            for (int y = 0; y <= 4; y++) {
                for (int z = 0; z <= 4; z++) {
                    if (x == 0 || x == 4 || y == 0 || y == 4 || z == 0 || z == 4) {
                        bottomCorner.clone().add(x, y, z).getBlock().setType(Material.AIR);
                    }
                }
            }
        }
    }
}

