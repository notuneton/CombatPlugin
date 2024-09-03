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
                    String warn = "&x&E&1&2&1&4&Ay&x&E&1&2&1&4&Ao&x&E&1&2&1&4&Au &x&E&1&2&1&4&Aa&x&E&1&2&1&4&Ar&x&E&1&2&1&4&Ae &x&E&1&2&1&4&An&x&E&1&2&1&4&Ao &x&E&1&2&1&4&Al&x&E&1&2&1&4&Ao&x&E&1&2&1&4&An&x&E&1&2&1&4&Ag&x&E&1&2&1&4&Ae&x&E&1&2&1&4&Ar &x&E&1&2&1&4&Ai&x&E&1&2&1&4&An &x&E&1&2&1&4&Ac&x&E&1&2&1&4&Ao&x&E&1&2&1&4&Am&x&E&1&2&1&4&Ab&x&E&1&2&1&4&Aa&x&E&1&2&1&4&At";
                    player.sendMessage(ColorUtils.colorize(warn));
                }
                if (combat_tagged.containsKey(player)) {
                    String str = ColorUtils.colorize("&7Combat: &3" + (endTime - System.currentTimeMillis()) / 1000);
                    player.sendActionBar(str);
                }
            });
            toRemove.forEach(this::endCombatTag);
        }, 0, 20L);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player attacker && event.getEntity() instanceof Player victim) {
            startCombat(attacker, victim);
            victim.sendMessage(ColorUtils.colorize("&7You are combat-logged by " + attacker.getName()));
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player exited = event.getPlayer();
        if (combat_tagged.containsKey(exited)) {
            Bukkit.broadcastMessage(ColorUtils.colorize(exited.getName() + " was killed while disconnected!"));
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
        combat_tagged.put(player, System.currentTimeMillis() + 20000);
        combat_tagged.put(target, System.currentTimeMillis() + 20000);
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