package org.main.uneton;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.main.uneton.limbo.LimboManager;
import org.main.uneton.limbo.PlayerActivityListener;
import org.main.uneton.utils.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.bukkit.Bukkit.getPlayer;
import static org.main.uneton.utils.RegistersUtils.*;

public class Combat extends JavaPlugin implements Listener {

    public static HashMap<UUID, Integer> playTimes = new HashMap<>();
    public static final HashMap<UUID, Long> cooldowns = new HashMap<>();
    public static final Map<UUID, Long> lastMovementTime = new HashMap<>();
    private static final Map<String, Set<String>> blockedPlayers = new HashMap<>();
    private ProtocolManager protocolManager;
    private ConfigManager configManager;
    private LimboManager limboManager;
    private static Combat instance;
    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }
    public static Combat getInstance() {
        return instance;
    }
    public static String[] perm = {ColorUtils.colorize("&c&lCAN'T! &cMmh.. Seems like you do not have permission to run /")};
    long viive = 20L;


    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, this);
        Location limboLocation = new Location(Bukkit.getWorld("world"), 32, 1, -47);
        limboManager = new LimboManager(this, limboLocation);
        Bukkit.getPluginManager().registerEvents(new PlayerActivityListener(limboManager), this);

        configManager = new ConfigManager(this);
        ConfigManager.setup(this);
        ConfigManager.loadAll();


        BukkitScheduler scheduler = Bukkit.getScheduler();
        Runnable runnable = () -> {
            for (Player loop_player : Bukkit.getOnlinePlayers()) {
                UUID uuid = loop_player.getUniqueId();
                int currentPlaytime = playTimes.getOrDefault(uuid, 0);
                playTimes.put(uuid, currentPlaytime + 1);
                ConfigManager.get().set("players-playtime." + uuid.toString(), playTimes.get(uuid));
            }
            ConfigManager.save();
        };
        scheduler.runTaskTimer(this, runnable, 0L, viive);

        saveDefaultConfig();
        saveConfig();
        RecipeManager.createElytraRecipe();
        RecipeManager.createEnchantedAppleRecipe();
        RecipeManager.createChainableArmorRecipe();

        new RegistersUtils(this);
        registerCommands();
        registerTabCompletes();
        registerEventListeners();
    }

    @Override
    public void onDisable() {
        configManager = new ConfigManager(this);
        configManager.saveAll();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}


