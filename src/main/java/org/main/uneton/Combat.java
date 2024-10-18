package org.main.uneton;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.*;

import java.util.*;
import static org.main.uneton.utils.RegistersUtils.*;

public class Combat extends JavaPlugin implements Listener {

    public static final HashMap<UUID, Integer> playTimes = new HashMap<>();
    public static final HashMap<UUID, Long> cooldowns = new HashMap<>();
    public ConfigManager configManager;
    private static Combat instance;
    public static int playTimeTaskId;
    private BukkitAudiences adventure;
    public static Combat getInstance() {
        return instance;
    }

    public @NotNull BukkitAudiences adventure() {
        if (this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    public static final String messageDebug = ColorUtils.colorize("&aSuccess!");

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

        RegistersUtils registersUtils = new RegistersUtils(this);
        registersUtils.registerCommands();
        registersUtils.registerTabCompletes();
        registersUtils.registerEventListeners();
    }

    public void playTimeRunnable() {
        long delay = 20L;
        BukkitScheduler scheduler = Bukkit.getScheduler();
        Runnable runnable = () -> {
            for (Player loop_player : Bukkit.getOnlinePlayers()) {
                UUID uuid = loop_player.getUniqueId();
                int currentPlaytime = playTimes.getOrDefault(uuid, 0);
                playTimes.put(uuid, currentPlaytime + 1);
                ConfigManager.get().set("player-playtime." + uuid, playTimes.get(uuid));
            }
            ConfigManager.save();
        };
        scheduler.runTaskTimer(this, runnable, 0L, delay);
    }

    @Override
    public void onDisable() {
        configManager = new ConfigManager(this);
        cancelPlayTimeRunnable();
        ConfigManager.saveAll();
    }

    public static void cancelPlayTimeRunnable() {
        Bukkit.getScheduler().cancelTask(playTimeTaskId);
    }

    public static void wipePlaytime(Player player) {
        UUID uuid = player.getUniqueId();
        playTimes.remove(uuid);
        ConfigManager.get().set("player-playtime." + uuid, null);
        ConfigManager.save();
    }
}