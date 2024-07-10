package org.main.uneton.combatlogger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.main.uneton.utils.ColorUtils;

import java.util.*;

public class CombatLog implements Listener {

    private final JavaPlugin plugin;
    private static final Map<Player, List<Player>> isInCombat = new HashMap<>();
    public static final Map<Player, Long> combat_tagged = new HashMap<>();

    public CombatLog(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        combatTask();
    }

    private void combatTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            List<Player> toRemove = new ArrayList<>();
            combat_tagged.keySet().forEach(player -> {
                Long endTime = combat_tagged.get(player);
                if (endTime < System.currentTimeMillis()) {
                    toRemove.add(player);
                    String noLonger = "&8&l[i] &x&5&7&B&3&2&5Y&x&5&8&B&3&2&5o&x&5&8&B&3&2&6u &x&5&9&B&3&2&6a&x&5&9&B&3&2&7r&x&5&A&B&3&2&7e &x&5&A&B&3&2&8n&x&5&B&B&3&2&8o &x&5&C&B&3&2&9l&x&5&C&B&3&2&9o&x&5&D&B&3&2&An&x&5&D&B&3&2&Ag&x&5&E&B&3&2&Be&x&5&E&B&3&2&Br &x&5&F&B&3&2&Ci&x&6&0&B&3&2&Cn &x&6&0&B&3&2&Cc&x&6&1&B&3&2&Do&x&6&1&B&3&2&Dm&x&6&2&B&3&2&Eb&x&6&2&B&3&2&Ea&x&6&3&B&3&2&Ft &x&6&4&B&3&2&Ft&x&6&4&B&3&3&0a&x&6&5&B&3&3&0g&x&6&5&B&3&3&1g&x&6&6&B&3&3&1e&x&6&6&B&3&3&2d&x&6&7&B&3&3&2!";
                    player.sendMessage(ColorUtils.colorize(noLonger));
                }
                if (combat_tagged.containsKey(player)) {
                    player.sendActionBar(ChatColor.GRAY + "Combat: " + ChatColor.DARK_AQUA + (endTime - System.currentTimeMillis()) / 1000);
                }
            });
            toRemove.forEach(this::endCombat);
        }, 0, 20L);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        if (event.getDamager() instanceof Player attacker && event.getEntity() instanceof Player victim) {
            startCombat(attacker, victim);
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player exited = event.getPlayer();
        if (combat_tagged.containsKey(exited)) {
            exited.setHealth(0);
            endCombat(exited);
        }
    }


     /*
     List<String> commands = Arrays.asList(
            "tempban %player% 2 h combat logged out from the server"
     );
     int index = 0;
     String cmd = commands.get(index);
     cmd = cmd.replace("%player%", exited.getName());
     Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
     */

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        endCombat(victim);
    }

    @EventHandler
    public void onElytra(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (combat_tagged.containsKey(player)) {
            event.setCancelled(true);
        }
    }

    private void startCombat(Player player, Player target) {
        combat_tagged.put(player, System.currentTimeMillis() + 21000);
        combat_tagged.put(target, System.currentTimeMillis() + 21000);
        addToCombatList(player, target);
        addToCombatList(target, player);
    }

    private void endCombat(Player player) {
        combat_tagged.remove(player);
        isInCombat.remove(player);
        removeFromTargetLists(player);
    }

    private void addToCombatList(Player player, Player target) {
        List<Player> targets = isInCombat.getOrDefault(player, new ArrayList<>());
        if (!targets.contains(target)) {
            targets.add(target);
            isInCombat.put(player, targets);
        }
    }

    private void removeFromTargetLists(Player target) {
        for (Map.Entry<Player, List<Player>> entry : isInCombat.entrySet()) {
            Player player = entry.getKey();
            List<Player> targets = entry.getValue();
            targets.remove(target);
            if (targets.isEmpty()) {
                combat_tagged.remove(player);
            }
            isInCombat.put(player, targets);
        }
    }
}