package org.main.uneton;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.main.uneton.admin.*;
import org.main.uneton.commands.BlockList;
import org.main.uneton.commands_vanilla.*;
import org.main.uneton.events.BlockListener;
import org.main.uneton.commands.Blockplayer;
import org.main.uneton.commands.Unblock;
import org.main.uneton.combatlogger.CombatLog;
import org.main.uneton.admin.Freeze;
import org.main.uneton.tabcomps.*;
import org.main.uneton.events.FreezeListener;
import org.main.uneton.admin.Gm;
import org.main.uneton.events.GmListener;
import org.main.uneton.combatlogger.SetSpawn;
import org.main.uneton.combatlogger.Spawn;
import org.main.uneton.commands_vanilla.Sudo;
import org.main.uneton.events.TrashEvent;
import org.main.uneton.commands.*;
import org.main.uneton.events.*;
import org.main.uneton.commands.Trash;
import org.main.uneton.events.MagicStick;
import org.main.uneton.utils.AfkCheckTask;
import org.main.uneton.utils.ColorUtils;

import java.util.*;
import static org.bukkit.Bukkit.getCommandMap;
import static org.main.uneton.combatlogger.CombatLog.combat_tagged;
import static org.main.uneton.utils.ScoreboardUtils.*;

public class Combat extends JavaPlugin implements Listener {
    
    private static Combat instance;
    public static HashMap<UUID, Integer> playTimes = new HashMap<>();
    public static final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private final Map<UUID, Long> lastMovementTime = new HashMap<>();
    private static final Map<String, Set<String>> blockedPlayers = new HashMap<>();
    public static Combat getInstance() {
        return instance;
    }
    public static Combat getInstance;
    public static HashMap<UUID, Double> economy = new HashMap<>();


    // private Economy vault;
    // private Config config = new Config(this, "economy");
    // private FileConfiguration fileConfig = config.getConfig();

    private ProtocolManager protocolManager;
    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, this);
        createElytraRecipe();
        createOldEnchantedAppleRecipe();
        startTitleScheduler(); // titlen päivitys
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player user : Bukkit.getOnlinePlayers()) {
                    UUID uuid = user.getUniqueId();
                    int currentPlayTime = playTimes.getOrDefault(uuid, 0);
                    playTimes.put(uuid, currentPlayTime + 1);
                    instance.getConfig().set("seconds." + uuid, playTimes.get(uuid));
                }
                instance.saveConfig();
            }
        }.runTaskTimer(this, 0L, 20L);

        getConfig().options().copyDefaults();
        saveDefaultConfig();
        saveConfig();


        // admin
        getCommand("cage").setExecutor(new Cage());
        getCommand("crash").setExecutor(new Crash());
        getCommand("freeze").setExecutor(new Freeze());
        getCommand("gm").setExecutor(new Gm());
        getCommand("cure").setExecutor(new Cure());
        getCommand("invsee").setExecutor(new Invsee());
        getCommand("launch").setExecutor(new Launch());
        getCommand("magictrick").setExecutor(new Magictrick());
        getCommand("slippery").setExecutor(new Slippery(this));
        getCommand("vanish").setExecutor(new Vanish(this));

        // combatlogger
        Bukkit.getPluginManager().registerEvents(new CombatLog(this), this);
        getCommand("setspawn").setExecutor(new SetSpawn(this));
        getCommand("spawn").setExecutor(new Spawn(this));

        // commands
        getCommand("blocklist").setExecutor(new BlockList());
        getCommand("block").setExecutor(new Blockplayer());
        getCommand("daily").setExecutor(new Daily());
        getCommand("enderchest").setExecutor(new Enderchest());
        getCommand("ping").setExecutor(new Ping());
        getCommand("puu").setExecutor(new Puu());
        getCommand("repair").setExecutor(new Repair());
        getCommand("rules").setExecutor(new Rules());
        getCommand("sign").setExecutor(new Sign());
        getCommand("trash").setExecutor(new Trash());
        getCommand("unblock").setExecutor(new Unblock());

        // tabcompleters
        getCommand("daily").setTabCompleter(new DailyTabs());
        getCommand("enderchest").setTabCompleter(new EnderchestTabs());
        getCommand("guide").setTabCompleter(new GuideTabs());
        getCommand("puu").setTabCompleter(new PuuTabs());
        getCommand("repair").setTabCompleter(new RepairTabs());
        getCommand("rules").setTabCompleter(new RulesTabs());
        getCommand("sign").setTabCompleter(new SignTabs());
        getCommand("spawn").setTabCompleter(new SpawnTabs());
        getCommand("time").setTabCompleter(new TimeTabs());
        getCommand("tp").setTabCompleter(new TpTabs());
        getCommand("trash").setTabCompleter(new TrashTabs());
        getCommand("vanish").setTabCompleter(new VanishTabs());

        // vanilla
        getCommand("guide").setExecutor(new Guide());
        getCommand("clear").setExecutor(new Clear());
        getCommand("msg").setExecutor(new Msg());
        getCommand("op").setExecutor(new Op());
        getCommand("sudo").setExecutor(new Sudo());
        getCommand("time").setExecutor(new Time());
        getCommand("tp").setExecutor(new Tp());

        // listeners
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
        Bukkit.getPluginManager().registerEvents(new FreezeListener(), this);
        Bukkit.getPluginManager().registerEvents(new GmListener(), this);
        Bukkit.getPluginManager().registerEvents(new Listeners(this), this);
        Bukkit.getPluginManager().registerEvents(new MagicStick(), this);
        Bukkit.getPluginManager().registerEvents(new MessageHolder(), this);
        Bukkit.getPluginManager().registerEvents(new TrashEvent(), this);

        getServer().getPluginManager().registerEvents(new PlayerAfkMove(this), this);
        new AfkCheckTask().runTaskTimer(this, 0, 20);
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
            Bukkit.broadcastMessage(ColorUtils.colorize(player.getName() + " was kicked for inactivity."));
        }
    }

    private void createOldEnchantedAppleRecipe() {
        ItemStack notch_apple = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1);
        ItemMeta notch_apple_meta = notch_apple.getItemMeta();
        if (notch_apple_meta != null) {
            notch_apple_meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Enchanted Apple");
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
            elytraMeta.setDisplayName(ColorUtils.colorize("&eElytra's"));
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

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!kills.containsKey(uuid)) {
            int countsKilled = getConfig().getInt("kills." + uuid, 0);
            kills.put(uuid, countsKilled);
        }
        if (!deaths.containsKey(uuid)) {
            int countsDeaths = getConfig().getInt("deaths." + uuid, 0);
            deaths.put(uuid, countsDeaths);
        }
        if (!playTimes.containsKey(uuid)) {
            int playtimeSeconds = getConfig().getInt("playtime." + uuid, 0);
            playTimes.put(uuid, playtimeSeconds);
        }
        startUpdatingScoreboard(player, this);
    }

    public static boolean doesCommandExist(String commandName) {
        CommandMap commandMap = getCommandMap();
        if (commandMap != null) {
            Command command = commandMap.getCommand(commandName);
            return command != null;
        }
        return false;
    }

    @Override
    public void onDisable() {
        for (UUID uuid : playTimes.keySet()) {
            getConfig().set("deaths." + uuid, kills.get(uuid));
            getConfig().set("kills." + uuid, deaths.get(uuid));
            getConfig().set("playtime." + uuid, playTimes.get(uuid));
        }
        saveConfig();
    }
}




























/*
        getLogger().info(String.format("Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
        saveEconomy();



        this.vault = VaultHook.hook(this);
        loadEconomy(); // todo better error handling if vault cannot be hooked
        loadData();

        Config c = new Config(Combat.getInstance(), "data_config");
        c.load();
        c.getConfig().set("sd", "lol");
        c.save();
        double number = 1234567890123456789012345678901234567890.0;
        System.out.println(formatNumber(number));

    }


    private void loadEconomy() {
        if (fileConfig.isConfigurationSection("balances")) {
            for (String playerUuid : fileConfig.getConfigurationSection("balances").getKeys(false)) {
                double balance = fileConfig.getDouble("balances." + playerUuid);
                UUID uuid = UUID.fromString(playerUuid);
                economy.put(uuid, balance);
            }
        }
    }

    public void saveEconomy() {
        for (Map.Entry<UUID, Double> entry : economy.entrySet()) {
            UUID uuid = entry.getKey();
            double balance = entry.getValue();
            fileConfig.set("balances." + uuid, balance);
        }
        config.save();
        config.reload();
    }


    public static Economy hook(Combat plugin) {
        plugin.getLogger().info("Hooking economy...");
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().log(Level.WARNING, "Vault not found, Economy features disabled.");
            return null;
        } else {
            plugin.getServer().getServicesManager().register(Economy.class, new EcoImpl(), plugin, ServicePriority.Highest);
            RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                Economy vault;
                vault = rsp.getProvider();
                plugin.getLogger().info("Economy hooked! (" + vault.getName() + ")");
                return vault;
            }
        }
        return null;
    }
    public Economy getVault() {
        return vault;
    }
*/