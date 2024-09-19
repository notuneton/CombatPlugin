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
    public static Map<UUID, Integer> playTimes = null;
    public static final Map<UUID, Integer> kills = new HashMap<>();
    public static final Map<UUID, Integer> entity_kills = new HashMap<>();
    public static final Map<UUID, Integer> deaths = new HashMap<>();

    public ConfigManager(Combat plugin) {
        this.plugin = plugin;
        playTimes = new HashMap<>();
    }
    public static void setup(Combat plugin) {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false); // Copy default file
        }
        reload(); //this class method
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
            throw new IllegalArgumentException("File or configuration cannot be null. Ensure the configuration is properly initialized.");
        }
        try {
            config.save(configFile);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void saveAll() {
        FileConfiguration config = ConfigManager.get();
        Set<UUID> alluuids = new HashSet<>();
        alluuids.addAll(playTimes.keySet());
        alluuids.addAll(kills.keySet());
        alluuids.addAll(deaths.keySet());

        for (UUID uuid : alluuids) {
            if (playTimes.containsKey(uuid)) {
                config.set("players-playtimes." + uuid.toString(), playTimes.get(uuid));
            }
            if (kills.containsKey(uuid)) {
                config.set("player-kills." + uuid.toString(), kills.get(uuid));
            }
            if (deaths.containsKey(uuid)) {
                config.set("player-deaths." + uuid.toString(), deaths.get(uuid));
            }
        }

        String combatColor = plugin.getConfig().getString("combat-name");
        if (combatColor != null) {
            config.set("combat-name", combatColor);
        }

        Location spawnLocation = ConfigManager.getSpawnLocation();
        if (spawnLocation != null) {
            config.set("spawn-location.world", spawnLocation.getWorld().getName());
            config.set("spawn-location.x", spawnLocation.getX());
            config.set("spawn-location.y", spawnLocation.getY());
            config.set("spawn-location.z", spawnLocation.getZ());
            config.set("spawn-location.yaw", spawnLocation.getYaw());
            config.set("spawn-location.pitch", spawnLocation.getPitch());
        } else {
            Bukkit.getLogger().warning("[CombatV3]: Spawn location is null. Skipping save.");
        }

        String joinMessage = plugin.getConfig().getString("join-message");
        config.set("join-message", joinMessage);
        ConfigManager.save();
    }

    public static void loadAll() {
        FileConfiguration config = ConfigManager.get();
        if (config == null) {
            Bukkit.getLogger().warning("[CombatV3]: Failed to load configuration. Configuration is null.");
            return;
        }

        ConfigurationSection playtimeSection = config.getConfigurationSection("players-playtimes");
        if (playtimeSection != null) {
            for (String key : playtimeSection.getKeys(false)) {
                UUID uuid = UUID.fromString(key);
                int playtime = playtimeSection.getInt(key);
                playTimes.put(uuid, playtime);
            }
        }
        ConfigurationSection killsSection = config.getConfigurationSection("player-kills");
        if (killsSection != null) {
            for (String key : killsSection.getKeys(false)) {
                UUID uuid = UUID.fromString(key);
                int killsCount = killsSection.getInt(key);
                kills.put(uuid, killsCount);
            }
        }
        ConfigurationSection deathsSection = config.getConfigurationSection("player-deaths");
        if (deathsSection != null) {
            for (String key : deathsSection.getKeys(false)) {
                UUID uuid = UUID.fromString(key);
                int deathsCount = deathsSection.getInt(key);
                deaths.put(uuid, deathsCount);
            }
        }

        String worldName = config.getString("spawn-location.world");
        if (worldName != null) {
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                double x = config.getDouble("spawn-location.x");
                double y = config.getDouble("spawn-location.y");
                double z = config.getDouble("spawn-location.z");
                float yaw = (float) config.getDouble("spawn-location.yaw");
                float pitch = (float) config.getDouble("spawn-location.pitch");

                Location spawn_location = new Location(world, x, y, z, yaw, pitch);
                config.set("spawn-location", spawn_location);
            } else {
                Bukkit.getLogger().warning("[CombatV3]: World not found: " + worldName);
            }
        } else {
            Bukkit.getLogger().warning("[CombatV3]: World name in spawn-location is null.");
        }
    }

    public static Location getSpawnLocation() {
        FileConfiguration config = ConfigManager.get();
        if (config == null) {
            Bukkit.getLogger().warning("[CombatV3]: configuration is null.");
            return null;
        }

        String worldName = config.getString("spawn-location.world");
        if (worldName == null) {
            Bukkit.getLogger().warning("[CombatV3]: world name is null.");
            return null;
        }

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            Bukkit.getLogger().warning("[CombatV3]: world not found: " + worldName);
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
