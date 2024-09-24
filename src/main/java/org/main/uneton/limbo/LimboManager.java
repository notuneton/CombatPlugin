package org.main.uneton.limbo;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.main.uneton.utils.ColorUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LimboManager {

    private final JavaPlugin plugin;
    private final Location limboLocation;
    public LimboManager(JavaPlugin plugin, Location limboLocation) {
        this.plugin = plugin;
        this.limboLocation = limboLocation; // Initialize the field
        startInactivityCheckTask();
    }

    private final Map<UUID, Long> playerActivity = new HashMap<>();
    private final long inactivityThreshold = 5 * 60 * 1000; // 5 minutes in milliseconds

    public void updatePlayerActivity(Player player) {
        playerActivity.put(player.getUniqueId(), System.currentTimeMillis());
    }

    private void startInactivityCheckTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
                    UUID uuid = loopPlayer.getUniqueId();
                    long lastActivityTime = playerActivity.getOrDefault(uuid, currentTime);
                    if ((currentTime - lastActivityTime) >= inactivityThreshold) {
                        if (!loopPlayer.isInsideVehicle()) {  // Optional check: prevent teleporting if in a vehicle
                            sendPlayerToLimbo(loopPlayer);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void sendPlayerToLimbo(Player player) {
        player.teleport(limboLocation);
        player.sendMessage(ColorUtils.colorize("&cYou were spawned in Limbo."));
    }
}

