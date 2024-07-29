package org.main.uneton;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.main.uneton.admin.*;
import org.main.uneton.combatlogger.CombatLog;
import org.main.uneton.admin.Freeze;
import org.main.uneton.comvanilla.Msg;
import org.main.uneton.comvanilla.Tp;
import org.main.uneton.events.FreezeListener;
import org.main.uneton.admin.Gm;
import org.main.uneton.events.GmListener;
import org.main.uneton.combatlogger.SetSpawn;
import org.main.uneton.combatlogger.Spawn;
import org.main.uneton.commands.Sudo;
import org.main.uneton.events.TrashEvent;
import org.main.uneton.commands.*;
import org.main.uneton.events.*;
import org.main.uneton.commands.Trash;
import org.main.uneton.events.MagicStickEvent;

import java.util.*;
import static org.bukkit.Bukkit.getCommandMap;
import static org.main.uneton.utils.ScoreboardUtils.startUpdatingScoreboard;

public class Combat extends JavaPlugin implements Listener {
    
    private static Combat instance;
    public static HashMap<UUID, Integer> playTimes = new HashMap<>();
    public static final HashMap<UUID, Long> cooldowns = new HashMap<>();
    public static Combat getInstance() {
        return instance;
    }
    public static Combat getInstance;
    public static HashMap<UUID, Double> economy = new HashMap<>();


    // private Economy vault;
    // private Config config = new Config(this, "economy");
    // private FileConfiguration fileConfig = config.getConfig();


    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, this);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player user : Bukkit.getOnlinePlayers()) {
                    UUID uuid = user.getUniqueId();
                    int currentPlayTime = playTimes.getOrDefault(uuid, 0);
                    playTimes.put(uuid, currentPlayTime + 1);
                    instance.getConfig().set("seconds." + uuid, playTimes.get(uuid));
                }
            }
        }.runTaskTimer(this, 0L, 20L);


        getConfig().options().copyDefaults();
        saveDefaultConfig();
        saveConfig();

        // admin
        getCommand("cage").setExecutor(new Cage());
        getCommand("crash").setExecutor(new Crash());
        getCommand("flyspeed").setExecutor(new FlySped());
        getCommand("freeze").setExecutor(new Freeze());
        getCommand("gm").setExecutor(new Gm());
        getCommand("cure").setExecutor(new Cure());
        getCommand("invsee").setExecutor(new Invsee());
        getCommand("launch").setExecutor(new Launch());
        getCommand("givestick").setExecutor(new GiveMagicStick());
        getCommand("slippery").setExecutor(new Slippery(this));
        getCommand("vanish").setExecutor(new Vanish(this));
        getCommand("wipeprofile").setExecutor(new Wipeprofile(this));

        // combatlogger
        Bukkit.getPluginManager().registerEvents(new CombatLog(this), this);
        getCommand("setspawn").setExecutor(new SetSpawn(this));
        getCommand("spawn").setExecutor(new Spawn(this));

        // commands
        getCommand("bugreport").setExecutor(new BugReport(this));
        getCommand("enderchest").setExecutor(new Ec());
        getCommand("guide").setExecutor(new Guide());
        getCommand("ping").setExecutor(new Ping());
        getCommand("playtime").setExecutor(new Playtime(this));
        getCommand("puu").setExecutor(new Puu());
        getCommand("repair").setExecutor(new Repair());
        getCommand("rules").setExecutor(new Rules());
        getCommand("sign").setExecutor(new Sign());
        getCommand("sudo").setExecutor(new Sudo());
        getCommand("trash").setExecutor(new Trash());

        // vanilla
        getCommand("tp").setExecutor(new Tp());
        getCommand("msg").setExecutor(new Msg());

        // listeners
        Bukkit.getPluginManager().registerEvents(new FreezeListener(), this);
        Bukkit.getPluginManager().registerEvents(new GmListener(), this);
        Bukkit.getPluginManager().registerEvents(new Listeners(this), this);
        Bukkit.getPluginManager().registerEvents(new TrashEvent(), this);
        Bukkit.getPluginManager().registerEvents(new MagicStickEvent(), this);

        Bukkit.getPluginManager().registerEvents(this, this);
        ShapedRecipe coarseDirtRecipe = new ShapedRecipe(new NamespacedKey(this, "coarseDirtRecipe"), compDirt());
        coarseDirtRecipe.shape("DD", "DD");
        coarseDirtRecipe.setIngredient('D', Material.DIRT);
        Bukkit.addRecipe(coarseDirtRecipe);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        // Load playtime for the player
        if (!playTimes.containsKey(uuid)) {
            int playtimeSeconds = getConfig().getInt("playtime." + uuid, 0);
            playTimes.put(uuid, playtimeSeconds);
        }

        // Ensure the scoreboard updates for the player
        startUpdatingScoreboard(player, this);
    }


    private ItemStack compDirt() {
        ItemStack compDirt = new ItemStack(Material.COARSE_DIRT, 1);
        ItemMeta compDirtMeta = compDirt.getItemMeta();
        compDirtMeta.setDisplayName(ChatColor.YELLOW + "Compressed Dirt");
        compDirt.setItemMeta(compDirtMeta);
        return compDirt;
    }

    @EventHandler
    public void onBlockBroken(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getType() == compDirt().getType()) {
            event.setDropItems(false);
            Location loc = block.getLocation();
            ItemStack dirt = new ItemStack(Material.DIRT, 9); // Create an ItemStack of 9 dirt blocks
            Item dropped = loc.getWorld().dropItemNaturally(loc, dirt);
        }

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
        // Save all playtimes to the configuration when the plugin is disabled
        for (UUID uuid : playTimes.keySet()) {
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