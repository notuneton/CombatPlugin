package org.main.uneton.limbo;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.main.uneton.utils.ColorUtils;
import org.main.uneton.utils.ConfigManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LimboManager {

    private final JavaPlugin plugin;
    private Location limboLocation;
    public LimboManager(JavaPlugin plugin, Location limboLocation) {
        this.plugin = plugin;
        this.limboLocation = limboLocation; // Initialize the field
        startInactivityCheckTask();
    }

    private final Map<UUID, Long> playerActivity = new HashMap<>();
    private final long inactivityThreshold = 3 * 60 * 1000; // in milliseconds

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
                        sendPlayerToLimbo(loopPlayer);
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
        player.sendMessage(ColorUtils.colorize("&6You received &f60 coins for being teleported to Limbo!"));
        ConfigManager.addSomeCoins(uuid, 60);
        player.sendMessage(ColorUtils.colorize("&cAn exception occurred in your connection, so you have been routed to limbo!"));
        player.sendMessage(ColorUtils.colorize("&cYou were spawned in Limbo."));
    }
}

