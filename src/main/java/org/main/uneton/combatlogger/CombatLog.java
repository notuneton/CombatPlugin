package org.main.uneton.combatlogger;

import org.bukkit.Bukkit;
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
                    String warn = ColorUtils.colorize("&8[i]  &x&6&5&F&B&2&8Y&x&6&4&F&B&2&Co&x&6&4&F&B&3&1u &x&6&3&F&C&3&5a&x&6&3&F&C&3&9r&x&6&2&F&C&3&Ee &x&6&2&F&C&4&2n&x&6&1&F&C&4&6o &x&6&0&F&D&4&Bl&x&6&0&F&D&4&Fo&x&5&F&F&D&5&3n&x&5&F&F&D&5&8g&x&5&E&F&D&5&Ce&x&5&E&F&D&6&0r &x&5&D&F&E&6&5i&x&5&C&F&E&6&9n &x&5&C&F&E&6&Dc&x&5&B&F&E&7&2o&x&5&B&F&E&7&6m&x&5&A&F&F&7&Ab&x&5&A&F&F&7&Fa&x&5&9&F&F&8&3t");
                    player.sendMessage(ColorUtils.colorize(warn));
                }
                if (combat_tagged.containsKey(player)) {
                    player.sendActionBar(ColorUtils.colorize("&7Combat: &3"+ (endTime - System.currentTimeMillis()) / 1000));
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
        combat_tagged.put(player, System.currentTimeMillis() + 30000);
        combat_tagged.put(target, System.currentTimeMillis() + 30000);
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