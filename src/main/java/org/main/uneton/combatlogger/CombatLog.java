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
                    String noLonger = "&8[i] &x&B&E&4&D&F&BY&x&B&B&5&4&F&Bo&x&B&8&5&C&F&Cu &x&B&5&6&3&F&Ca&x&B&3&6&B&F&Dr&x&B&0&7&2&F&De &x&A&D&7&A&F&Dn&x&A&A&8&1&F&Eo &x&A&7&8&8&F&El&x&A&4&9&0&F&Eo&x&A&1&9&7&F&Fn&x&9&F&9&6&F&Cg&x&9&C&8&B&F&6e&x&9&9&8&1&F&0r &x&9&6&7&6&E&Bi&x&9&3&6&B&E&5n &x&9&0&6&1&D&Fc&x&8&D&5&6&D&9o&x&8&A&4&C&D&3m&x&8&7&4&1&C&Eb&x&8&4&3&7&C&8a&x&8&1&2&C&C&2t";
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

            /*
            List<String> commands = Arrays.asList(
                    "tempban %player% 2 h combat logged out from the server"
            );
            int index = 0;
            String cmd = commands.get(index);
            cmd = cmd.replace("%player%", exited.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
             */
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