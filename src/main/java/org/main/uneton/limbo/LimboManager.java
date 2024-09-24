package org.main.uneton.limbo;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.main.uneton.utils.ColorUtils;
import org.main.uneton.utils.ConfigManager;

import java.util.*;

public class LimboManager {

    private final JavaPlugin plugin;
    private Location limboLocation;
    public LimboManager(JavaPlugin plugin, Location limboLocation) {
        this.plugin = plugin;
        this.limboLocation = limboLocation; // Initialize the field
        startInactivityCheckTask();
    }
    private final Map<UUID, Long> playerActivity = new HashMap<>();
    private final Set<UUID> playersInLimbo = new HashSet<>();
    private final Map<UUID, Long> lastRewardTime = new HashMap<>();
    private final long inactivityThreshold = 3 * 60 * 1000; // in milliseconds
    private final long rewardInterval = 3 * 60 * 1000;

    public void updatePlayerActivity(Player player) {
        playerActivity.put(player.getUniqueId(), System.currentTimeMillis());
        lastRewardTime.remove(player.getUniqueId());
    }

    private void startInactivityCheckTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
                    UUID uuid = loopPlayer.getUniqueId();
                    long lastActivityTime = playerActivity.getOrDefault(uuid, currentTime);

                    // Tarkista onko pelaaja epäaktiivinen JA ei ole jo limbo-tilassa
                    if ((currentTime - lastActivityTime) >= inactivityThreshold && !playersInLimbo.contains(uuid)) {
                        sendPlayerToLimbo(loopPlayer);
                    }

                    // Jos pelaaja on limbo-tilassa, tarkista palkkion antaminen
                    if (playersInLimbo.contains(uuid)) {
                        giveRewardIfIntervalPassed(loopPlayer, currentTime);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void sendPlayerToLimbo(Player player) {
        limboLocation = ConfigManager.getSpawnLocation();
        assert limboLocation != null;
        player.teleport(limboLocation);
        UUID uuid = player.getUniqueId();
        playersInLimbo.add(uuid);

        player.sendMessage(ColorUtils.colorize("&cAn exception occurred in your connection, so you have been routed to limbo!"));
        player.sendMessage(ColorUtils.colorize("&cYou were spawned in Limbo."));
    }

    private void giveRewardIfIntervalPassed(Player player, long currentTime) {
        UUID uuid = player.getUniqueId();
        long lastReward = lastRewardTime.getOrDefault(uuid, 0L);
        if (currentTime - lastReward >= rewardInterval) {
            lastRewardTime.put(uuid, currentTime);
            player.sendMessage(ColorUtils.colorize("&6You received &f20 coins&6!"));
            ConfigManager.addSomeCoins(uuid, 20);
        }
    }
}

