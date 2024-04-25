package org.main.uneton;

import org.bukkit.*;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.main.uneton.admin.*;
import org.main.uneton.gm.Gm;
import org.main.uneton.gm.GmListener;
import org.main.uneton.ignore.Ignore;
import org.main.uneton.ignore.IgnoreListener;
import org.main.uneton.ignore.Ignorelist;
import org.main.uneton.suicide.Suicide;
import org.main.uneton.suicide.SuicideClickEvent;
import org.main.uneton.trash.TrashClickEvent;
import org.main.uneton.commands.*;
import org.main.uneton.events.*;
import org.main.uneton.ignore.Unignore;
import org.main.uneton.trash.Trash;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class Combat extends JavaPlugin implements Listener {


    private static Combat instance;
    public static Combat getInstance(){
        return instance;
    }
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
        Bukkit.getPluginManager().registerEvents(this, this);
        loadData();


        // Schedule a repeating task that runs every second & FROM PLAYER TIME CLASS
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player user : Bukkit.getOnlinePlayers()) {
                UUID uuid = user.getUniqueId();
                int currentPlayTime = playTimes.getOrDefault(uuid, 0);
                playTimes.put(uuid, currentPlayTime + 1);
            }
        }, 0L, 20L); // 20 ticks = 1 second


        // admin
        getCommand("crash").setExecutor(new Crash());
        getCommand("gm").setExecutor(new Gm());
        getCommand("heal").setExecutor(new Heal());
        getCommand("invsee").setExecutor(new Invsee());
        getCommand("repair").setExecutor(new Repair());
        getCommand("slippery").setExecutor(new Slippery());
        getCommand("sudo").setExecutor(new Sudo());
        getCommand("trapcage").setExecutor(new Trap());

        // commands
        getCommand("enderchest").setExecutor(new Enderchest());
        getCommand("guide").setExecutor(new Guide());
        getCommand("playtime").setExecutor(new Playtime(this));
        getCommand("rules").setExecutor(new Rules());
        getCommand("stuck").setExecutor(new Stuck());

        // listeners
        Bukkit.getPluginManager().registerEvents(new Combatlogger(this), this);
        // Bukkit.getPluginManager().registerEvents(new Deaths(), this);
        Bukkit.getPluginManager().registerEvents(new ForceOp(), this);
        Bukkit.getPluginManager().registerEvents(new Listeners(), this);
        Bukkit.getPluginManager().registerEvents(new MessageHolder(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeaths(), this);

        getCommand("gm").setExecutor(new Gm());
        Bukkit.getPluginManager().registerEvents(new GmListener(), this);

        getCommand("ignore").setExecutor(new Ignore());
        getCommand("ignorelist").setExecutor(new Ignorelist());
        getCommand("unignore").setExecutor(new Unignore());
        Bukkit.getPluginManager().registerEvents(new IgnoreListener(), this);

        getCommand("suicide").setExecutor(new Suicide());
        Bukkit.getPluginManager().registerEvents(new SuicideClickEvent(), this);

        getCommand("disposal").setExecutor(new Trash());
        Bukkit.getPluginManager().registerEvents(new TrashClickEvent(), this);



        new BukkitRunnable() {
            @Override
            public void run() {
                String dayName = printDay();
                Bukkit.getLogger().info("[CombatV2] " +dayName);
            }
        }.runTaskTimer(this, 0L, 576000L);

    }



    private String printDay() {
        // Get the current day and return its name
        LocalDate date = LocalDate.now();
        DayOfWeek dayname = date.getDayOfWeek();
        return "Today is " + dayname.name();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
