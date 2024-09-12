package org.main.uneton;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.main.uneton.utils.ConfigManager;
import org.main.uneton.utils.RegistersUtils;
import org.main.uneton.events.*;
import org.main.uneton.utils.AfkCheckTask;
import org.main.uneton.utils.ColorUtils;

import java.util.*;
import static org.bukkit.Bukkit.getCommandMap;
import static org.bukkit.Bukkit.getPlayer;
import static org.main.uneton.combatlogger.CombatLog.combat_tagged;
import static org.main.uneton.utils.ConfigManager.*;
import static org.main.uneton.utils.RegistersUtils.*;

public class Combat extends JavaPlugin implements Listener {
    
    private static Combat instance;
    public static HashMap<UUID, Integer> playTimes = new HashMap<>();
    public static final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private final Map<UUID, Long> lastMovementTime = new HashMap<>();
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

        BukkitScheduler scheduler = Bukkit.getScheduler();
        Runnable runnable = () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                UUID uuid = player.getUniqueId();
                int currentPlaytime = playTimes.getOrDefault(uuid, 0);
                playTimes.put(uuid, currentPlaytime + 1);
                ConfigManager.get().set("players-playtime." + uuid, playTimes.get(uuid));
            }
            ConfigManager.save();
        };
        scheduler.runTaskTimer(this, runnable, 0L, viive);


        saveDefaultConfig();
        saveConfig();

        createElytraRecipe();
        createEnchantedAppleRecipe();

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

    public void updatePlayerActivity(Player player) {
        lastMovementTime.put(player.getUniqueId(), System.currentTimeMillis());
    }
    public long getLastActivityTime(Player player) {
        return lastMovementTime.getOrDefault(player.getUniqueId(), 0L);
    }

    public void kickPlayerForAFK(Player player) {
        if (player.hasPermission("combat.bypass.afkkick")) {
            return;
        }
        if (combat_tagged.containsKey(player)) {
            return;
        } else {
            Location afk_location = this.getConfig().getLocation("spawn");
            player.sendMessage(ColorUtils.colorize("&cAn exception occurred in your connection, so you have been routed to &espawn&c!"));
            player.teleport(afk_location);
            Bukkit.broadcastMessage(ColorUtils.colorize("&ca " + player.getName() + " was kicked for inactivity."));
        }
    }

    private void createEnchantedAppleRecipe() {
        ItemStack notch_apple = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1);
        ItemMeta notch_apple_meta = notch_apple.getItemMeta();
        if (notch_apple_meta != null) {
            notch_apple_meta.setDisplayName(ColorUtils.colorize("&cEnchanted Apple"));
            notch_apple.setItemMeta(notch_apple_meta);
        }

        ShapedRecipe godAppleRecipe = new ShapedRecipe(new NamespacedKey(Combat.instance, "god_apple_recipe"), notch_apple);
        godAppleRecipe.shape("GGG", "GAG", "GGG");
        godAppleRecipe.setIngredient('G', Material.GOLD_BLOCK);
        godAppleRecipe.setIngredient('A', Material.APPLE);
        Bukkit.addRecipe(godAppleRecipe);
    }

    private void createElytraRecipe() {
        ItemStack elytra = new ItemStack(Material.ELYTRA, 1);
        ItemMeta elytraMeta = elytra.getItemMeta();
        if (elytraMeta != null) {
            elytraMeta.setDisplayName(ColorUtils.colorize("&Elytra's"));
            elytra.setItemMeta(elytraMeta);
        }
        NamespacedKey key = new NamespacedKey(instance, "elytra_recipe");
        ShapedRecipe elytraRecipe = new ShapedRecipe(key, elytra);
        elytraRecipe.shape("PDP", "P P", "F F");
        elytraRecipe.setIngredient('F', Material.FEATHER);
        elytraRecipe.setIngredient('P', Material.PHANTOM_MEMBRANE);
        elytraRecipe.setIngredient('D', Material.DIAMOND);
        Bukkit.addRecipe(elytraRecipe);
    }

    public static boolean doesCommandExist(String commandName) {
        CommandMap commandMap = getCommandMap();
        if (commandMap != null) {
            Command command = commandMap.getCommand(commandName);
            return command != null;
        }
        return false;
    }
}
