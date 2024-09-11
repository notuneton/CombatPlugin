package org.main.uneton.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.main.uneton.Combat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlaytimeTracker {

    private final Combat plugin;
    private final Map<UUID, Integer> playTimes; // Track player playTimes in a map

    public PlaytimeTracker(Combat plugin) {
        this.plugin = plugin;
        this.playTimes = new HashMap<>();
    }

    public void startTracking() {
        loadPlayTimes(); // Load playTimes when the server starts
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    UUID uuid = player.getUniqueId();
                    String playerName = player.getName();
                    int currentPlayTime = playTimes.getOrDefault(uuid, 0);
                    playTimes.put(uuid, currentPlayTime + 1);
                    CustomConfigManager.get().set("players-playtime." + playerName, playTimes.get(uuid));
                }
                CustomConfigManager.save();
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public void loadPlayTimes() {
        FileConfiguration config = CustomConfigManager.get();
        if (config == null) {
            Bukkit.getLogger().warning("Failed to load playtime configuration. Configuration is null.");
            return;
        }
        ConfigurationSection section = config.getConfigurationSection("players-playtime");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                UUID uuid = UUID.fromString(key);
                int playtime = section.getInt(key);
                playTimes.put(uuid, playtime);
            }
        }
    }

    public void savePlayTimes() {
        for (UUID uuid : playTimes.keySet()) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            if (player.getName() != null) {
                CustomConfigManager.get().set("players-playtime." + player.getName(), playTimes.get(uuid));
            }
        }
        CustomConfigManager.save();
    }
}
