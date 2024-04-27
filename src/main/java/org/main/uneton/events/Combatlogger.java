package org.main.uneton.events;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Combatlogger implements Listener {

    private final JavaPlugin plugin;

    // Player 1 -> player,player2,3,4,5, ...
    private static final Map<Player, List<Player>> combatlogged = new HashMap<>(); // Functio
    public static final Map<Player, Long> combatCooldown = new HashMap<>(); // Animaatio

    public Combatlogger(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        combatTask();
    }

    private void combatTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            List<Player> toRemove = new ArrayList<>();
            combatCooldown.keySet().forEach(player -> {
                Long endTime = combatCooldown.get(player);
                if (endTime < System.currentTimeMillis()) {
                    toRemove.add(player);
                    player.sendMessage(ChatColor.GRAY + "You are no longer in combat.");
                }
                if (combatCooldown.containsKey(player)) {
                    player.sendActionBar(ChatColor.GRAY + "Combat: " + ChatColor.DARK_AQUA + (endTime - System.currentTimeMillis()) / 1000);
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
    public void onEnderPearl(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();
        if (projectile.getType() == EntityType.ENDER_PEARL) {
            Player quitter = (Player) projectile.getShooter();
            if (combatCooldown.containsKey(quitter)) {
                event.setCancelled(true);
                quitter.sendActionBar(ChatColor.RED + "You are not allowed to use that while combatlog!");
            }
        }
    }

    @EventHandler
    public void onElytra(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (combatCooldown.containsKey(player)) {
            event.setCancelled(true);
            player.sendActionBar(ChatColor.RED + "You are not allowed to do that while combatlog!");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player quitter = event.getPlayer();
        if (combatCooldown.containsKey(quitter)) {
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
        combatCooldown.put(player, System.currentTimeMillis() + 21000);
        combatCooldown.put(player2, System.currentTimeMillis() + 21000);
        // Lisää pelaajan 1 targetteigin pelaajan 2
        addToCombatList(player, player2);
        addToCombatList(player2, player);
    }

    private void addToCombatList(Player player, Player target) {
        List<Player> targets = combatlogged.getOrDefault(player, new ArrayList<>());
        if (!targets.contains(target)) {
            targets.add(target);
            combatlogged.put(player, targets);
        }
    }

    private void endCombat(Player player) {
        combatCooldown.remove(player);
        combatlogged.remove(player);
        removeFromTargetLists(player);
    }

    private void removeFromTargetLists(Player target) {
        // Poistetaan leavaaja jokaisen targetin target listasta
        // Jos leavaaja on targeting vika pelaaja sen cooldown resettaantuu
        for (Map.Entry<Player, List<Player>> entry : combatlogged.entrySet()) {
            Player player = entry.getKey();
            List<Player> targets = entry.getValue();
            targets.remove(target);
            // Jos leavaaja on targeting vika pelaaja sen cooldown resettaantuu
            if (targets.isEmpty()) {
                combatCooldown.remove(player);
            }
            combatlogged.put(player, targets);
        }

        /*
        combat.forEach((player, targets) -> {
            targets.remove(target);
            if (targets.isEmpty()) {
                combatCooldown.remove(player);
            }
            combat.put(target, targets);
        });*/
    }
}
