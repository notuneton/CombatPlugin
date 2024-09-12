package org.main.uneton;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.main.uneton.utils.*;
import org.main.uneton.events.*;

import java.util.*;

import static org.bukkit.Bukkit.getPlayer;
import static org.main.uneton.combatlogger.CombatLog.combat_tagged;
import static org.main.uneton.utils.RegistersUtils.*;

public class Combat extends JavaPlugin implements Listener {
    
    private static Combat instance;
    public static HashMap<UUID, Integer> playTimes = new HashMap<>();
    public static final HashMap<UUID, Long> cooldowns = new HashMap<>();
    public static final Map<UUID, Long> lastMovementTime = new HashMap<>();
    private static final Map<String, Set<String>> blockedPlayers = new HashMap<>();
    public static Combat getInstance() {
        return instance;
    }
    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }
    private ProtocolManager protocolManager;
    private ConfigManager configManager;
    long viive = 20L;


    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, this);
        configManager = new ConfigManager(this);
        ConfigManager.setup(this);
        ConfigManager.loadAllData();
        saveDefaultConfig();

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

        saveConfig();
        RecipeManager.createElytraRecipe();
        RecipeManager.createEnchantedAppleRecipe();

        new RegistersUtils(this);
        registerCommands();
        registerTabCompletes();
        registerEventListeners();

        getServer().getPluginManager().registerEvents(new PlayerAfkMove(this), this);
        new AfkCheckTask().runTaskTimer(this, 0, 20);
    }

    @Override
    public void onDisable() {
        configManager = new ConfigManager(this);
        configManager.saveAllData();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public static void updatePlayerActivity(Player player) {
        lastMovementTime.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public static long getLastActivityTime(Player player) {
        return lastMovementTime.getOrDefault(player.getUniqueId(), 0L);
    }

    public void kickPlayerForAFK(Player player) {
        if (player.hasPermission("combat.bypass.afkkick") || combat_tagged.containsKey(player)) {
            return;
        }

        Location afk_location = this.getConfig().getLocation("spawn-location");

        player.sendMessage(ColorUtils.colorize("&cAn exception occurred in your connection, so you have been routed to &espawn&c!"));
        player.sendMessage(ColorUtils.colorize("&cYou were spawned in &eStart-zone&c!"));
        player.sendMessage("\n");
        player.teleport(afk_location);
        Bukkit.broadcastMessage(ColorUtils.colorize("&ca " + player.getName() + " was kicked for inactivity."));
    }
}



