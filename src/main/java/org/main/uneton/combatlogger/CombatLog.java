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
                    player.sendMessage(ColorUtils.colorize("&aYou are no longer in combat!"));
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