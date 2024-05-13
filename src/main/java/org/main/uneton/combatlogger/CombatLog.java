package org.main.uneton.combatlogger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

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
                    player.sendActionBar(ChatColor.GREEN + "You are no longer in combat.");
                    player.sendMessage(ChatColor.GREEN + "You are no longer in combat.");
                }
                if (combat_tagged.containsKey(player)) {
                    player.sendActionBar(ChatColor.WHITE + "Combat: " + ChatColor.DARK_RED + (endTime - System.currentTimeMillis()) / 1000);
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
    public void onElytra(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (combat_tagged.containsKey(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onShield(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (combat_tagged.containsKey(player)) {
            if (event.getHand() == EquipmentSlot.OFF_HAND) {
                ItemStack item = player.getInventory().getItemInOffHand();
                if (item.getType() == Material.SHIELD) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player quitter = event.getPlayer();
        if (combat_tagged.containsKey(quitter)) {
            quitter.setHealth(0);
            endCombat(quitter);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        endCombat(player);
    }


    private void startCombat(Player player, Player player2) {
        combat_tagged.put(player, System.currentTimeMillis() + 21000);
        combat_tagged.put(player2, System.currentTimeMillis() + 21000);
        addToCombatList(player, player2);
        addToCombatList(player2, player);
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

        /*
        combat.forEach((player, targets) -> {
            targets.remove(target);
            if (targets.isEmpty()) {
                combat_tagged.remove(player);
            }
            combat.put(target, targets);
        });*/
    }
}