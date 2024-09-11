package org.main.uneton.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.main.uneton.Combat;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        for (UUID uuid : playTimes.keySet()) {
            config.set("players-playtime." + uuid.toString(), playTimes.get(uuid));
        }
        for (UUID uuid : kills.keySet()) {
            config.set("kills." + uuid.toString(), kills.get(uuid));
        }
        for (UUID uuid : deaths.keySet()) {
            config.set("deaths." + uuid.toString(), deaths.get(uuid));
        }
        ConfigManager.save();
    }

    public static void loadAllData() {
        FileConfiguration config = ConfigManager.get();
        if (config == null) {
            Bukkit.getLogger().warning("Failed to load configuration. Configuration is null.");
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
