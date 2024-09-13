package org.main.uneton.combatlogger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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
                    player.sendMessage(ColorUtils.colorize("&x&5&3&9&F&A&6Y&x&5&9&A&4&A&9o&x&5&E&A&8&A&Bu &x&6&4&A&D&A&Ea&x&6&9&B&1&B&0r&x&6&F&B&6&B&3e &x&7&5&B&A&B&6n&x&7&A&B&F&B&8o &x&8&0&C&4&B&Bl&x&8&6&C&8&B&Eo&x&8&B&C&D&C&0n&x&9&1&D&1&C&3g&x&9&6&D&6&C&5e&x&9&C&D&A&C&8r &x&A&2&D&F&C&Bi&x&A&7&E&4&C&Dn &x&A&D&E&8&D&0c&x&B&3&E&D&D&3o&x&B&8&F&1&D&5m&x&B&E&F&6&D&8b&x&C&3&F&A&D&Aa&x&C&9&F&F&D&Dt"));
                }
                if (combat_tagged.containsKey(player)) {
                    player.sendActionBar(ColorUtils.colorize("&7Combat: &3" + (endTime - System.currentTimeMillis()) / 1000));
                    player.sendActionBar(ColorUtils.colorize("&x&5&3&9&F&A&6Y&x&5&9&A&4&A&9o&x&5&E&A&8&A&Bu &x&6&4&A&D&A&Ea&x&6&9&B&1&B&0r&x&6&F&B&6&B&3e &x&7&5&B&A&B&6n&x&7&A&B&F&B&8o &x&8&0&C&4&B&Bl&x&8&6&C&8&B&Eo&x&8&B&C&D&C&0n&x&9&1&D&1&C&3g&x&9&6&D&6&C&5e&x&9&C&D&A&C&8r &x&A&2&D&F&C&Bi&x&A&7&E&4&C&Dn &x&A&D&E&8&D&0c&x&B&3&E&D&D&3o&x&B&8&F&1&D&5m&x&B&E&F&6&D&8b&x&C&3&F&A&D&Aa&x&C&9&F&F&D&Dt"));
                }
            });
            toRemove.forEach(this::endCombatTag);
        }, 0, 20L);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player attacker && event.getEntity() instanceof Player victim) {
            startCombat(attacker, victim);
        }
    }
    
    @EventHandler
    @Deprecated
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player exited = event.getPlayer();
        if (combat_tagged.containsKey(exited)) {
            Bukkit.broadcastMessage(ColorUtils.colorize("&c" + exited.getName() + " was killed while disconnected"));
            exited.setHealth(0);
            endCombatTag(exited);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        endCombatTag(victim);
    }

    private void startCombat(Player player, Player target) {
        combat_tagged.put(player, System.currentTimeMillis() + 21000);
        combat_tagged.put(target, System.currentTimeMillis() + 21000);
        addToCombatList(player, target);
        addToCombatList(target, player);
    }

    private void endCombatTag(Player player) {
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