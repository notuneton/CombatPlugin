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
            long currentTime = System.currentTimeMillis();
            combat_tagged.keySet().forEach(player -> {
                Long endTime = combat_tagged.get(player);
                if (endTime < currentTime) {
                    toRemove.add(player);
                    player.sendMessage(ColorUtils.colorize("&x&9&1&C&B&F&BY&x&8&F&C&6&F&Bo&x&8&D&C&2&F&Bu &x&8&B&B&D&F&Ca&x&8&9&B&9&F&Cr&x&8&7&B&4&F&Ce &x&8&5&A&F&F&Cn&x&8&3&A&B&F&Co &x&8&1&A&6&F&Dl&x&7&F&A&1&F&Do&x&7&D&9&D&F&Dn&x&7&C&9&8&F&Dg&x&7&A&9&4&F&De&x&7&8&8&F&F&Dr &x&7&6&8&A&F&Ei&x&7&4&8&6&F&En &x&7&2&8&1&F&Ec&x&7&0&7&C&F&Eo&x&6&E&7&8&F&Em&x&6&C&7&3&F&Fb&x&6&A&6&F&F&Fa&x&6&8&6&A&F&Ft"));
                }
                if (combat_tagged.containsKey(player)) {
                    String combatColor = plugin.getConfig().getString("combat-name");
                    if (combatColor != null) {
                        long remainingTime = (endTime - currentTime) / 1000;
                        player.sendActionBar(ColorUtils.colorize(combatColor + remainingTime));
                    }
                    plugin.saveConfig();
                }
            });
            toRemove.forEach(this::endCombat);
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
            endCombat(exited);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        endCombat(victim);
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