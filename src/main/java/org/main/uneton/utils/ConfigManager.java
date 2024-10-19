package org.main.uneton.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.main.uneton.Combat;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ConfigManager {

    private static Combat plugin;
    private static FileConfiguration config;
    private static File configFile;
    public static Map<UUID, Integer> playTimes = new HashMap<>();
    public static final Map<UUID, Integer> kills = new HashMap<>();
    public static final Map<UUID, Integer> someCoins = new HashMap<>();
    public static final Map<UUID, Integer> deaths = new HashMap<>();

    public ConfigManager(Combat plugin) {
        ConfigManager.plugin = plugin;
    }

    // Sets up the configuration file
    public static void setup(Combat plugin) {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        reload();
    }

    public static void reload() {
        if (configFile == null) {
            throw new IllegalArgumentException("Configuration file cannot be null");
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }
    public static FileConfiguration get() {
        return config;
    }
    public static void save() {
        if (config == null || configFile == null) {
            throw new IllegalArgumentException("File or configuration cannot be null. Ensure the configuration is properly initialized.");
        }
        try {
            config.save(configFile);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void saveAll() {
        FileConfiguration config = ConfigManager.get();

        // Collect all unique UUIDs from player data
        Set<UUID> uuids = new HashSet<>();
        uuids.addAll(playTimes.keySet());
        uuids.addAll(kills.keySet());
        uuids.addAll(deaths.keySet());
        uuids.addAll(someCoins.keySet());

        // Save player data to configuration
        for (UUID uuid : uuids) {
            savePlayerData(config, uuid);
        }
        saveCombatName(config);
        saveJoinMessage(config);
        saveMobDrop(config);
        saveSpawnLocation(config);
        ConfigManager.save();
    }

    private static void savePlayerData(FileConfiguration config, UUID uuid) {
        if (playTimes.containsKey(uuid)) {
            config.set("player-playtime." + uuid.toString(), playTimes.get(uuid));
        }
        if (kills.containsKey(uuid)) {
            config.set("player-kills." + uuid.toString(), kills.get(uuid));
        }
        if (deaths.containsKey(uuid)) {
            config.set("player-deaths." + uuid.toString(), deaths.get(uuid));
        }
        if (someCoins.containsKey(uuid)) {
            config.set("coins." + uuid.toString(), someCoins.get(uuid));
        }
    }

    private static void saveCombatName(FileConfiguration config) {
        String combatColor = config.getString("combat-name");
        if (combatColor != null) {
            config.set("combat-name", combatColor);
        }
    }

    private static void saveJoinMessage(FileConfiguration config) {
        String joinMessage = config.getString("join-message");
        config.set("join-message", joinMessage);
    }

    private static void saveMobDrop(FileConfiguration config) {
        boolean enable = plugin.getConfig().getBoolean("enable-mob-drops");
        config.set("enable-mob-drops", enable);
    }

    private static void saveSpawnLocation(FileConfiguration config) {
        Location spawnLocation = ConfigManager.getSpawnLocation();
        if (spawnLocation != null && spawnLocation.getWorld() != null) {
            config.set("spawn-location.world", spawnLocation.getWorld().getName());
            config.set("spawn-location.x", spawnLocation.getX());
            config.set("spawn-location.y", spawnLocation.getY());
            config.set("spawn-location.z", spawnLocation.getZ());
            config.set("spawn-location.yaw", spawnLocation.getYaw());
            config.set("spawn-location.pitch", spawnLocation.getPitch());
        } else {
            Bukkit.getLogger().warning("[CombatV3]: Spawn location or world is null. Skipping save.");
        }
    }

    public static void loadAll() {
        FileConfiguration config = ConfigManager.get();
        if (config == null) {
            Bukkit.getLogger().warning("[CombatV3]: Failed to load configuration. Configuration is null.");
            return;
        }

        loadPlayerData(config, "player-playtime", playTimes);
        loadPlayerData(config, "player-kills", kills);
        loadPlayerData(config, "player-deaths", deaths);
        loadPlayerData(config, "coins", someCoins);
    }

    private static void loadPlayerData(FileConfiguration config, String sectionName, Map<UUID, Integer> dataMap) {
        ConfigurationSection section = config.getConfigurationSection(sectionName);
        if (section != null) {
            for (String key : section.getKeys(false)) {
                UUID uuid = UUID.fromString(key);
                int value = section.getInt(key);
                dataMap.put(uuid, value);
            }
        }
    }

    public static Location getSpawnLocation() {
        FileConfiguration config = ConfigManager.get();
        if (config == null) {
            Bukkit.getLogger().warning("[CombatV3]: Configuration is null.");
            return null;
        }
        String worldName = config.getString("spawn-location.world");
        if (worldName == null) {
            Bukkit.getLogger().warning("[CombatV3]: World name is null.");
            return null;
        }
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            Bukkit.getLogger().warning("[CombatV3]: World not found: " + worldName);
            return null;
        }

        double x = config.getDouble("spawn-location.x");
        double y = config.getDouble("spawn-location.y");
        double z = config.getDouble("spawn-location.z");
        float yaw = (float) config.getDouble("spawn-location.yaw");
        float pitch = (float) config.getDouble("spawn-location.pitch");
        return new Location(world, x, y, z, yaw, pitch);
    }

    public static String formatPlaytime(int hours, int minutes, int seconds) {
        if (seconds >= 60) {
            minutes += seconds / 60;
            seconds %= 60;
        }

        if (minutes >= 60) {
            hours += minutes / 60;
            minutes %= 60;
        }
        return String.format("&fTotal-Played &e%dh %dm &8%ds", hours, minutes, seconds);
    }

    public static void addKill(UUID player_uuid) {
        int currentKills = kills.getOrDefault(player_uuid, 0);
        kills.put(player_uuid, currentKills + 1);
    }

    public static void addDeath(UUID player_uuid) {
        int currentDeaths = deaths.getOrDefault(player_uuid, 0);
        deaths.put(player_uuid, currentDeaths + 1);
    }
}