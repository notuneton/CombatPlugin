package org.main.uneton;

import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.main.uneton.utils.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.bukkit.Bukkit.getPlayer;
import static org.main.uneton.utils.ConfigManager.playTimes;
import static org.main.uneton.utils.RegistersUtils.*;

public class Combat extends JavaPlugin implements Listener {

    public static final HashMap<UUID, Integer> playTimes = new HashMap<>();
    public static final HashMap<UUID, Long> cooldowns = new HashMap<>();
    public static final Map<UUID, Long> lastMovementTime = new HashMap<>();
    private static final Map<String, Set<String>> blockedPlayers = new HashMap<>();
    private ConfigManager configManager;
    private static Combat instance;
    private static int playTimeTaskId;
    public static Combat getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, this);

        configManager = new ConfigManager(this);
        ConfigManager.setup(this);
        ConfigManager.loadAll();
        playTimeRunnable();

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

    public void playTimeRunnable() {
        long every_time = 20L;
        BukkitScheduler scheduler = Bukkit.getScheduler();
        Runnable runnable = () -> {
            for (Player loopedPlayer : Bukkit.getOnlinePlayers()) {
                UUID uuid = loopedPlayer.getUniqueId();
                int currentPlaytime = playTimes.getOrDefault(uuid, 0);
                playTimes.put(uuid, currentPlaytime + 1);
                ConfigManager.get().set("players-playtime." + uuid.toString(), playTimes.get(uuid));
            }
            ConfigManager.save();
        };
        scheduler.runTaskTimer(this, runnable, 0L, every_time);
    }

    public static void cancelPlayTimeRunnable() {
        Bukkit.getScheduler().cancelTask(playTimeTaskId); // Cancel the task using the stored ID
    }

    public static void wipePlayTime(Player player) {
        UUID uuid = player.getUniqueId();
        playTimes.remove(uuid); // Remove from playTimes map
        ConfigManager.get().set("players-playtime." + uuid, null);
        player.sendMessage("null playtime sout");

        player.sendMessage(ColorUtils.colorize("&cYour data has been wiped."));
        ConfigManager.save(); // Save the config changes
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


