package org.main.uneton;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
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
import org.main.uneton.ignore.Ignore;
import org.main.uneton.ignore.IgnoreListener;
import org.main.uneton.ignore.Ignorelist;
import org.main.uneton.admin.SetSpawn;
import org.main.uneton.combatlogger.Spawn;
import org.main.uneton.trash.TrashEvent;
import org.main.uneton.commands.*;
import org.main.uneton.events.*;
import org.main.uneton.ignore.Unignore;
import org.main.uneton.trash.Trash;

import java.util.*;

public class Combat extends JavaPlugin implements Listener {

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

        ShapedRecipe elytraRecipe = new ShapedRecipe(new NamespacedKey(this, "elytra_recipe"), notch_apple);
        elytraRecipe.shape("SDS", "LFL", "L L");
        elytraRecipe.setIngredient('S', Material.STRING);
        elytraRecipe.setIngredient('D', Material.DIAMOND);
        elytraRecipe.setIngredient('L', Material.LEATHER);
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
        getCommand("repair").setExecutor(new Repair());
        getCommand("rules").setExecutor(new Rules());
        getCommand("sign").setExecutor(new Sign());
        getCommand("sudo").setExecutor(new Sudo());

        // listeners
        Bukkit.getPluginManager().registerEvents(new Listeners(this), this);
        Bukkit.getPluginManager().registerEvents(new MessageHolder(), this);
        Bukkit.getPluginManager().registerEvents(new Deaths(), this);

        getCommand("freeze").setExecutor(new Freeze());
        Bukkit.getPluginManager().registerEvents(new FreezeListener(), this);

        getCommand("gm").setExecutor(new Gm());
        Bukkit.getPluginManager().registerEvents(new GmListener(), this);

        getCommand("ignore").setExecutor(new Ignore());
        getCommand("ignorelist").setExecutor(new Ignorelist());
        Bukkit.getPluginManager().registerEvents(new IgnoreListener(), this);
        getCommand("unignore").setExecutor(new Unignore());

        getCommand("trashcan").setExecutor(new Trash());
        Bukkit.getPluginManager().registerEvents(new TrashEvent(), this);




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


        ShapedRecipe coarseDirtRecipe = new ShapedRecipe(new NamespacedKey(this, "coarseDirtRecipe"), compDirt());
        coarseDirtRecipe.shape("DD", "DD");
        coarseDirtRecipe.setIngredient('D', Material.DIRT);
        Bukkit.addRecipe(coarseDirtRecipe);
    }

    private ItemStack compDirt() {
        ItemStack compDirt = new ItemStack(Material.COARSE_DIRT, 1);
        ItemMeta compDirtMeta = compDirt.getItemMeta();
        compDirtMeta.setDisplayName(ChatColor.RESET + "Compressed Dirt");
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

    public void onDisable() {
        // getLogger().info(String.format("Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
        // saveEconomy();
    }
}