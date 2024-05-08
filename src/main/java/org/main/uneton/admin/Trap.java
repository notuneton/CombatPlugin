package org.main.uneton.admin;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.Combat;

import java.util.HashMap;
import java.util.Map;

public class Trap implements CommandExecutor {

    private static final long TRAP_DURATION = 24*60*60*1000;
    private static final Map<Player, Long> trapTimer = new HashMap<>();
    private static Combat plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if(!player.hasPermission("combat.trap.sv")) {
            player.sendMessage(ChatColor.RED + "Permission Denied: You do not have permission to do this task.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /trap <player>");
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null || !target.isOnline()) {
                player.sendActionBar(ChatColor.DARK_RED + "That user is offline.");
                return true;
            }
            spawnTrap(player);
            trapTimer.put(player, System.currentTimeMillis()); // Record the time the trap was spawned
        }
        return true;
    }

    public static void spawnTrap(Player player) {
        Location loc = player.getLocation();
        Location bottomCorner = loc.clone().add(-1, -1, -1);
        PotionEffect mining_fatigue = new PotionEffect(PotionEffectType.SLOW_DIGGING, 1000000, 3);

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 4; y++) {
                for (int z = 0; z < 3; z++) {
                    if (((y == 1 || y == 2) && z == 1 & x == 1)) {  // empty space
                        continue;
                    }

                    if ((y == 0 || y == 3) && z == 1 & x == 1) {  // roof/bottom block(s)
                        bottomCorner.clone().add(x, y, z).getBlock().setType(Material.SPAWNER);
                        player.addPotionEffect(mining_fatigue);
                    } else { // Walls
                        bottomCorner.clone().add(x, y, z).getBlock().setType(Material.SPAWNER);
                        player.addPotionEffect(mining_fatigue);
                    }
                }
            }
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            removeTrap(player);
            trapTimer.remove(player);
        }, TRAP_DURATION);

        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (count < 5) { // spawn 5 cash pickup(s)
                    Location randomLoc = bottomCorner.clone().add(
                            Math.random() * 3,
                            Math.random() * 4,
                            Math.random() * 3
                    );
                    Item cash = player.getWorld().dropItem(randomLoc, new ItemStack(Material.EMERALD));
                    cash.setCustomName(ChatColor.GREEN + "$1000");
                    cash.setCustomNameVisible(true);
                    cash.setVelocity(randomLoc.getDirection().multiply(0.1));
                    cash.setPickupDelay(20);
                    count++;
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 20, 20);
    }

    private static void removeTrap(Player player) {
        Location loc = player.getLocation();
        Location bottomCorner = loc.clone().add(-1, -1, -1);
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 4; y++) {
                for (int z = 0; z < 3; z++) {
                    if (((y == 1 || y == 2) && z == 1 & x == 1)) {
                        continue;
                    }

                    if ((y == 0 || y == 3) && z == 1 & x == 1) {
                        bottomCorner.clone().add(x, y, z).getBlock().setType(Material.AIR);
                    } else { // Walls
                        bottomCorner.clone().add(x, y, z).getBlock().setType(Material.AIR);
                    }
                }
            }
        }
    }
}