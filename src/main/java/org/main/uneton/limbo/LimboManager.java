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
    private final Map<UUID, Boolean> notifiedPlayers = new HashMap<>(); // To track if a player has been notified
    private final long inactivityThreshold = 3 * 60 * 1000; // in milliseconds

    public void updatePlayerActivity(Player player) {
        UUID uuid = player.getUniqueId();
        playerActivity.put(uuid, System.currentTimeMillis());
        notifiedPlayers.put(uuid, false); // Reset notification status when player moves

        // Remove player from limbo if they are currently in it
        if (playersInLimbo.contains(uuid)) {
            playersInLimbo.remove(uuid);
        }
    }

    private void startInactivityCheckTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
                    UUID uuid = loopPlayer.getUniqueId();
                    long lastActivityTime = playerActivity.getOrDefault(uuid, currentTime);

                    // Check for inactivity
                    if ((currentTime - lastActivityTime) >= inactivityThreshold && !playersInLimbo.contains(uuid)) {
                        sendPlayerToLimbo(loopPlayer);
                    }

                    // Check if player is in limbo
                    if (playersInLimbo.contains(uuid)) {
                        // Send messages only if not already notified
                        if (!notifiedPlayers.getOrDefault(uuid, false)) {
                            notifyPlayerInLimbo(loopPlayer);
                            notifiedPlayers.put(uuid, true); // Mark as notified
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public void removePlayerFromLimbo(Player player) {
        // Poista pelaaja limbo-listasta
        // Oletetaan, että sinulla on lista, joka pitää kirjaa limboon sijoitetuista pelaajista
        if (playersInLimbo.contains(player)) {
            playersInLimbo.remove(player);
        }
    }


    private void sendPlayerToLimbo(Player player) {
        limboLocation = ConfigManager.getSpawnLocation();
        assert limboLocation != null;
        player.teleport(limboLocation);
        UUID uuid = player.getUniqueId();
        playersInLimbo.add(uuid);
        notifyPlayerInLimbo(player);
    }

    private void notifyPlayerInLimbo(Player player) {
        player.sendMessage(ColorUtils.colorize("&cAn exception occurred in your connection, so you have been routed to limbo!"));
        player.sendMessage(ColorUtils.colorize("&cYou were spawned in Limbo."));
    }
}

