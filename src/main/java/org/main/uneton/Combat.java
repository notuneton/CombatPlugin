package org.main.uneton;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.main.uneton.utils.*;

import java.util.*;

import static org.main.uneton.admin.Wipe.cancelPlayTimeRunnable;
import static org.main.uneton.utils.RegistersUtils.*;

public class Combat extends JavaPlugin implements Listener {

    public static final HashMap<UUID, Integer> playTimes = new HashMap<>();
    public static final HashMap<UUID, Long> cooldowns = new HashMap<>();
    public ConfigManager configManager;
    private static Combat instance;
    public static int playTimeTaskId;
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
        long delay = 20L;
        BukkitScheduler scheduler = Bukkit.getScheduler();
        Runnable runnable = () -> {
            for (Player loopedPlayer : Bukkit.getOnlinePlayers()) {
                UUID uuid = loopedPlayer.getUniqueId();
                int currentPlaytime = playTimes.getOrDefault(uuid, 0);
                playTimes.put(uuid, currentPlaytime + 1);
                ConfigManager.get().set("players-playtime." + uuid, playTimes.get(uuid));
            }
            ConfigManager.save();
        };
        playTimeTaskId = scheduler.runTaskTimer(this, runnable, 0L, delay).getTaskId();
    }

    @Override
    public void onDisable() {
        configManager = new ConfigManager(this);
        cancelPlayTimeRunnable();
        ConfigManager.saveAll();
    }
}