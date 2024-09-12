package org.main.uneton.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.main.uneton.Combat;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ConfigManager {

    private final Combat plugin;
    private static FileConfiguration config;
    private static File configFile;
    public static Map<UUID, Integer> playTimes = null; // Track player playTimes in a map
    public static final Map<UUID, Integer> kills = new HashMap<>();
    public static final Map<UUID, Integer> deaths = new HashMap<>();

    public ConfigManager(Combat plugin) {
        this.plugin = plugin;
        playTimes = new HashMap<>();
    }
    public static void setup(Combat plugin) {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", true); // Copy default file
        }
        reload();
    }
    public static void reload() {
        if (configFile == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }
    public static FileConfiguration get() {
        return config;
    }
    public static void save() {
        if (config == null || configFile == null) {
            throw new IllegalArgumentException("File or configuration cannot be null");
        }
        try {
            config.save(configFile);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void saveAllData() {
        FileConfiguration config = ConfigManager.get();
        // Save players data
        Set<UUID> alluuids = new HashSet<>();
        alluuids.addAll(playTimes.keySet());
        alluuids.addAll(kills.keySet());
        alluuids.addAll(deaths.keySet());
        for (UUID uuid : alluuids) {
            if (playTimes.containsKey(uuid)) {
                config.set("players-playtime." + uuid.toString(), playTimes.get(uuid));
            }
            if (kills.containsKey(uuid)) {
                config.set("kills." + uuid.toString(), kills.get(uuid));
            }
            if (deaths.containsKey(uuid)) {
                config.set("deaths." + uuid.toString(), deaths.get(uuid));
            }
        }
        String joinMessage = plugin.getConfig().getString("join-message");
        config.set("join-message", joinMessage);
        ConfigManager.save();
    }

    public static void loadAllData() {
        FileConfiguration config = ConfigManager.get();
        if (config == null) {
            Bukkit.getLogger().warning("Failed to load configuration. Configuration is null.");
            return;
        }

        ConfigurationSection playtimeSection = config.getConfigurationSection("players-playtime");
        if (playtimeSection != null) {
            for (String key : playtimeSection.getKeys(false)) {
                UUID uuid = UUID.fromString(key);
                int playtime = playtimeSection.getInt(key);
                playTimes.put(uuid, playtime);
            }
        }

        ConfigurationSection killsSection = config.getConfigurationSection("kills");
        if (killsSection != null) {
            for (String key : killsSection.getKeys(false)) {
                UUID uuid = UUID.fromString(key);
                int killsCount = killsSection.getInt(key);
                kills.put(uuid, killsCount);
            }
        }

        ConfigurationSection deathsSection = config.getConfigurationSection("deaths");
        if (deathsSection != null) {
            for (String key : deathsSection.getKeys(false)) {
                UUID uuid = UUID.fromString(key);
                int deathsCount = deathsSection.getInt(key);
                deaths.put(uuid, deathsCount);
            }
        }
    }

    public static String formatPlaytime(int hours, int minutes, int seconds) {
        if (seconds >= 60) {
            hours += minutes / 60;
            seconds %= 60;
        }
        if (minutes >= 60) {
            hours += minutes / 60;
            minutes %= 60;
        }
        return String.format("  &fPlaytime: &e%dh %dm %ds", hours, minutes, seconds);
    }

    public static void addKill(UUID player_uuid) {
        kills.put(player_uuid, kills.getOrDefault(player_uuid, 0) + 1);
    }
    public static void addDeath(UUID player_uuid) {
        deaths.put(player_uuid, deaths.getOrDefault(player_uuid, 0) + 1);
    }
}
