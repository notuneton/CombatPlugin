package org.main.uneton.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Random;

public class PlayerDeaths implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player attacker = victim.getKiller();
        ChatColor red = ChatColor.RED;

        if (attacker != null && attacker.getType() == EntityType.PLAYER) {
            Random random = new Random();
            int chance = random.nextInt(100); // Generate a random number

            if (chance < 10) {
                event.setDeathMessage(red + victim.getName() + " took the L to " + attacker.getName());
            } else if (chance < 20) {
                event.setDeathMessage(red + victim.getName() + " got rekt by " + attacker.getName());
            } else if (chance < 30) {
                event.setDeathMessage(red + victim.getName() + " was oinked down by " + attacker.getName());
            } else if (chance < 40) {
                event.setDeathMessage(red + victim.getName() + " was hunted down by " + attacker.getName());
            } else if (chance < 50) {
                event.setDeathMessage(red + victim.getName() + " got clapped by " + attacker.getName());
            } else if (chance < 60) {
                event.setDeathMessage(red + victim.getName() + " got eliminated by " + attacker.getName());
            } else if (chance < 70) {
                event.setDeathMessage(red + victim.getName() + " was destroyed by " + attacker.getName());
            } else if (chance < 80) {
                event.setDeathMessage(red + victim.getName() + " was yeeted by " + attacker.getName());
            } else {
                event.setDeathMessage(red + attacker.getName() + " ruined brutally " + victim.getName());
            }
        }
    }
}
