package org.main.uneton;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.admin.*;
import org.main.uneton.combatlogger.CombatLog;
import org.main.uneton.frez.Freeze;
import org.main.uneton.frez.FreezeListener;
import org.main.uneton.gm.Gm;
import org.main.uneton.gm.GmListener;
import org.main.uneton.admin.SetSpawn;
import org.main.uneton.combatlogger.Spawn;
import org.main.uneton.trash.TrashEvent;
import org.main.uneton.commands.*;
import org.main.uneton.events.*;
import org.main.uneton.trash.Trash;

import java.util.*;

public class Combat extends JavaPlugin implements Listener {

    public HashMap<UUID, Integer> playTimes = new HashMap<>();
    private static Combat instance;
    public static Combat getInstance(){
        return instance;
    }

    public static Combat getInstance;
    public static HashMap<UUID, Double> economy = new HashMap<>();


    // private Economy vault;
    // private Config config = new Config(this, "economy");
    // private FileConfiguration fileConfig = config.getConfig();


    @Override
    public void onEnable() {

        ItemStack customTotem = new ItemStack(Material.TOTEM_OF_UNDYING, 1);
        ItemMeta customTotem_meta = customTotem.getItemMeta();
        customTotem_meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Another Heart");
        customTotem.setItemMeta(customTotem_meta);

        ShapedRecipe totemOfUndying = new ShapedRecipe(new NamespacedKey(this, "totemOfUndying"), customTotem);
        totemOfUndying.shape("HR");
        totemOfUndying.setIngredient('H', Material.HEART_OF_THE_SEA);
        totemOfUndying.setIngredient('R', Material.GOLD_INGOT);
        Bukkit.addRecipe(totemOfUndying);


        ItemStack notch_apple = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1);
        ItemMeta notch_apple_meta = notch_apple.getItemMeta();
        notch_apple_meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Notch Apple");
        notch_apple.setItemMeta(notch_apple_meta);

        ShapedRecipe godAppleRecipe = new ShapedRecipe(new NamespacedKey(this, "god_apple_recipe"), notch_apple);
        godAppleRecipe.shape("GGG", "GAG", "GGG");
        godAppleRecipe.setIngredient('G', Material.GOLD_BLOCK);
        godAppleRecipe.setIngredient('A', Material.APPLE);
        Bukkit.addRecipe(godAppleRecipe);


        ItemStack elytra = new ItemStack(Material.ELYTRA, 1);
        ItemMeta elytra_meta = elytra.getItemMeta();
        elytra_meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Elytra");
        elytra.setItemMeta(elytra_meta);

        ShapedRecipe elytraRecipe = new ShapedRecipe(new NamespacedKey(this, "elytra_recipe"), elytra);
        elytraRecipe.shape("SDS", "PFP", "P P");
        elytraRecipe.setIngredient('S', Material.STRING);
        elytraRecipe.setIngredient('D', Material.DIAMOND);
        elytraRecipe.setIngredient('P', Material.PHANTOM_MEMBRANE);
        elytraRecipe.setIngredient('F', Material.FEATHER);
        Bukkit.addRecipe(elytraRecipe);



        instance = this;
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        saveConfig();

        Bukkit.getPluginManager().registerEvents(this, this);

        // admin
        getCommand("crash").setExecutor(new Crash());
        getCommand("heal").setExecutor(new Heal());
        getCommand("invsee").setExecutor(new Invsee());
        getCommand("setspawn").setExecutor(new SetSpawn(this));
        getCommand("slippery").setExecutor(new Slippery());
        getCommand("trap").setExecutor(new Trap());

        // combatlogger
        Bukkit.getPluginManager().registerEvents(new CombatLog(this), this);
        getCommand("spawn").setExecutor(new Spawn(this));

        // commands
        getCommand("ec").setExecutor(new Ec()); 
        getCommand("guide").setExecutor(new Guide());
        getCommand("kys").setExecutor(new Kys());
        getCommand("ping").setExecutor(new Ping());
        getCommand("playtime").setExecutor(new Playtime(this));
        getCommand("puu").setExecutor(new Puu());
        getCommand("repair").setExecutor(new Repair());
        getCommand("rules").setExecutor(new Rules());
        getCommand("sign").setExecutor(new Sign());
        getCommand("sudo").setExecutor(new Sudo());

        // listeners
        Bukkit.getPluginManager().registerEvents(new Listeners(this), this);
        Bukkit.getPluginManager().registerEvents(new MessageHolder(), this);

        getCommand("freeze").setExecutor(new Freeze());
        Bukkit.getPluginManager().registerEvents(new FreezeListener(), this);

        getCommand("gm").setExecutor(new Gm());
        Bukkit.getPluginManager().registerEvents(new GmListener(), this);

        getCommand("trashcan").setExecutor(new Trash());
        Bukkit.getPluginManager().registerEvents(new TrashEvent(), this);


        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player1 : Bukkit.getOnlinePlayers()) {
                UUID uuid = player1.getUniqueId();
                int currentPlayTime = playTimes.getOrDefault(uuid, 0);
                playTimes.put(uuid, currentPlayTime + 1);
            }
        }, 0L, 20L); // 20 ticks = 1 second

        /*
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

    }

    public void onDisable() {
        // getLogger().info(String.format("Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
        // saveEconomy();
    }
}