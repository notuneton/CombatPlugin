package org.main.uneton.utils;

import org.bukkit.Bukkit;
import org.main.uneton.Combat;
import org.main.uneton.admin.*;
import org.main.uneton.combatlogger.CombatLog;
import org.main.uneton.combatlogger.SetSpawn;
import org.main.uneton.combatlogger.Spawn;
import org.main.uneton.commands.*;
import org.main.uneton.events.*;
import org.main.uneton.tabcomps.*;
import org.main.uneton.vanillaCmds.*;

public class RegistersUtils {

    private static Combat plugin;
    public RegistersUtils(Combat plugin) {
        RegistersUtils.plugin = plugin;
    }

    public static void registerCommands() {
        // Admin commands
        plugin.getCommand("cage").setExecutor(new Cage());
        plugin.getCommand("crash").setExecutor(new Crash());
        plugin.getCommand("freeze").setExecutor(new Freeze());
        plugin.getCommand("gm").setExecutor(new Gm());
        plugin.getCommand("cure").setExecutor(new Cure());
        plugin.getCommand("invsee").setExecutor(new Invsee());
        plugin.getCommand("launch").setExecutor(new Launch());
        plugin.getCommand("magictrick").setExecutor(new Magictrick());
        plugin.getCommand("slippery").setExecutor(new Slippery(plugin));
        plugin.getCommand("vanish").setExecutor(new Vanish(plugin));

        // Combat logger
        plugin.getCommand("setspawn").setExecutor(new SetSpawn(plugin));
        plugin.getCommand("spawn").setExecutor(new Spawn(plugin));

        // Other commands
        plugin.getCommand("blocklist").setExecutor(new BlockList());
        plugin.getCommand("block").setExecutor(new Blockplayer());
        plugin.getCommand("daily").setExecutor(new Daily());
        plugin.getCommand("enderchest").setExecutor(new Enderchest());
        plugin.getCommand("ping").setExecutor(new Ping());
        plugin.getCommand("puu").setExecutor(new Puu());
        plugin.getCommand("repair").setExecutor(new Repair());
        plugin.getCommand("rules").setExecutor(new Rules());
        plugin.getCommand("sign").setExecutor(new Sign());
        plugin.getCommand("trash").setExecutor(new Trash());
        plugin.getCommand("unblock").setExecutor(new Unblock());
        plugin.getCommand("guide").setExecutor(new Guide());
        plugin.getCommand("clear").setExecutor(new Clear());
        plugin.getCommand("msg").setExecutor(new Msg());
        plugin.getCommand("sudo").setExecutor(new Sudo());
        plugin.getCommand("time").setExecutor(new Time());
        plugin.getCommand("tp").setExecutor(new Tp());
    }

    public static void registerTabCompletes() {
        plugin.getCommand("daily").setTabCompleter(new DailyTabs());
        plugin.getCommand("enderchest").setTabCompleter(new EnderchestTabs());
        plugin.getCommand("guide").setTabCompleter(new GuideTabs());
        plugin.getCommand("puu").setTabCompleter(new PuuTabs());
        plugin.getCommand("repair").setTabCompleter(new RepairTabs());
        plugin.getCommand("rules").setTabCompleter(new RulesTabs());
        plugin.getCommand("sign").setTabCompleter(new SignTabs());
        plugin.getCommand("spawn").setTabCompleter(new SpawnTabs());
        plugin.getCommand("time").setTabCompleter(new TimeTabs());
        plugin.getCommand("tp").setTabCompleter(new TpTabs());
        plugin.getCommand("trash").setTabCompleter(new TrashTabs());
        plugin.getCommand("vanish").setTabCompleter(new VanishTabs());
    }

    public static void registerEventListeners() {
        Bukkit.getPluginManager().registerEvents(new CombatLog(plugin), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerDeaths(), plugin);
        Bukkit.getPluginManager().registerEvents(new BlockListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new FreezeListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new GmListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new Listeners(plugin), plugin);
        Bukkit.getPluginManager().registerEvents(new MagicStickEvent(), plugin);
        Bukkit.getPluginManager().registerEvents(new MessageHolder(), plugin);
        Bukkit.getPluginManager().registerEvents(new TrashEvent(), plugin);
    }
}
