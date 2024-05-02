package org.main.uneton;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.main.uneton.admin.*;
import org.main.uneton.combatlogger.CombatLog;
import org.main.uneton.economy.EcoImpl;
import org.main.uneton.gm.Gm;
import org.main.uneton.gm.GmListener;
import org.main.uneton.ignore.Ignore;
import org.main.uneton.ignore.IgnoreListener;
import org.main.uneton.ignore.Ignorelist;
import org.main.uneton.admin.SetSpawn;
import org.main.uneton.combatlogger.Spawn;
import org.main.uneton.suicide.Suicide;
import org.main.uneton.suicide.SuicideEvent;
import org.main.uneton.trash.TrashEvent;
import org.main.uneton.commands.*;
import org.main.uneton.events.*;
import org.main.uneton.ignore.Unignore;
import org.main.uneton.trash.Trash;

import java.util.*;
import java.util.logging.Level;


public class Combat extends JavaPlugin implements Listener {

    private static Combat instance;
    public static Combat getInstance(){
        return instance;
    }

    public static HashMap<UUID, Double> economy = new HashMap<>();
    public static Combat getInstance;

    private Economy vault;
    //private Config config = new Config(this, "economy");
    //private FileConfiguration fileConfig = config.getConfig();

    public HashMap<UUID, Integer> playTimes = new HashMap<>();
    private HashMap<UUID, Integer> killsMap = new HashMap<>();
    private HashMap<UUID, Integer> deathsMap = new HashMap<>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        Player player = e.getEntity();
        UUID playerUUID = player.getUniqueId();
        deathsMap.put(playerUUID, deathsMap.getOrDefault(playerUUID, 0) +1);
        saveData();
    }
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        if(e.getEntity().getKiller() != null){
            if (!(e.getEntity() instanceof Player)) {
                return;
            }
            Entity victim = e.getEntity();
            Player killer = e.getEntity().getKiller();
            UUID killerUUID = killer.getUniqueId();
            killsMap.put(killerUUID, killsMap.getOrDefault(killerUUID, 0)+ 1);
            saveData();
        }
    }
    private void loadData(){
        FileConfiguration config = getConfig();
        for(String uuid : config.getKeys(false)){
            killsMap.put(UUID.fromString(uuid), config.getInt(uuid + ".kills"));
            deathsMap.put(UUID.fromString(uuid), config.getInt(uuid + ".deaths"));
        }
        saveConfig();
    }

    private void saveData() {
        FileConfiguration config = getConfig();
        config.getKeys(false).forEach(config::isSet);
        for (UUID uuid : killsMap.keySet()) {
            // Get the player's name from the UUID
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            String user = offlinePlayer.getName();
            if (user != null) {
                config.set(user + ".kills", killsMap.get(uuid));
                config.set(user + ".deaths", deathsMap.get(uuid));
            }
        }
        saveConfig();
    }


    @Override
    public void onEnable() {
        instance = this;
        getConfig().options().copyDefaults();
        // saveDefaultConfig();
        saveConfig();


        // admin
        getCommand("clearchat").setExecutor(new ClearChat());
        getCommand("crash").setExecutor(new Crash());
        getCommand("heal").setExecutor(new Heal());
        getCommand("invsee").setExecutor(new Invsee());
        getCommand("repair").setExecutor(new Repair());
        getCommand("setspawn").setExecutor(new SetSpawn(this));
        getCommand("slippery").setExecutor(new Slippery());
        getCommand("trap").setExecutor(new Trap());

        // combatlogger
        Bukkit.getPluginManager().registerEvents(new CombatLog(this), this);
        getCommand("spawn").setExecutor(new Spawn(this));

        // commands
        getCommand("enderchest").setExecutor(new Enderchest());
        getCommand("guide").setExecutor(new Guide());
        getCommand("playtime").setExecutor(new Playtime(this));
        getCommand("rules").setExecutor(new Rules());
        getCommand("sign").setExecutor(new Sign());
        getCommand("sudo").setExecutor(new Sudo());

        // listeners
        Bukkit.getPluginManager().registerEvents(new Listeners(this), this);
        Bukkit.getPluginManager().registerEvents(new MessageHolder(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeaths(), this);

        getCommand("gm").setExecutor(new Gm());
        Bukkit.getPluginManager().registerEvents(new GmListener(), this);

        getCommand("ignore").setExecutor(new Ignore());
        getCommand("ignorelist").setExecutor(new Ignorelist());
        Bukkit.getPluginManager().registerEvents(new IgnoreListener(), this);
        getCommand("unignore").setExecutor(new Unignore());

        getCommand("suicide").setExecutor(new Suicide());
        Bukkit.getPluginManager().registerEvents(new SuicideEvent(), this);

        getCommand("disposal").setExecutor(new Trash());
        Bukkit.getPluginManager().registerEvents(new TrashEvent(), this);


        //TODO Schedule a repeating task that runs every second
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player user : Bukkit.getOnlinePlayers()) {
                UUID uuid = user.getUniqueId();
                int currentPlayTime = playTimes.getOrDefault(uuid, 0);
                playTimes.put(uuid, currentPlayTime + 1);
            }
        }, 0L, 20L); // 20 ticks = 1 second







        //this.vault = VaultHook.hook(this);
        //loadEconomy(); // todo better error handling if vault cannot be hooked
        //loadData();

        /*Config c = new Config(Combat.getInstance(), "data_config");
        c.load();
        c.getConfig().set("sd", "lol");
        c.save();
        double number = 1234567890123456789012345678901234567890.0;
        System.out.println(formatNumber(number));
        */
    }

    /*private void loadEconomy() {
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
    */
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

    public void onDisable() {
        getLogger().info(String.format("Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
        //saveEconomy();
    }

}
