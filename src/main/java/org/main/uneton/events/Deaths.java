package org.main.uneton.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Random;

public class Deaths implements Listener {

    // works fine

    @EventHandler
    public void onPlayerSuffocate(PlayerDeathEvent event){
        Player victim = event.getPlayer();
        ChatColor red = ChatColor.RED;
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.SUFFOCATION){
            event.setDeathMessage(red + victim.getName() + " went into backrooms...");
        }
    }

    @EventHandler
    public void onPlayerFall(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        ChatColor red = ChatColor.RED;
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setDeathMessage(red + victim.getName() + " believed they could fly... but couldn't");
        }
    }

    @EventHandler
    public void onPlayerVoid(PlayerDeathEvent event) {
        Player victim = event.getPlayer();
        ChatColor red = ChatColor.RED;
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.VOID){
            event.setDeathMessage(red + victim.getName() + " was caught escaping the map");
        }
    }

    @EventHandler
    public void onPlayerDrown(PlayerDeathEvent event) {
        Player victim = event.getPlayer();
        ChatColor red = ChatColor.RED;
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.DROWNING){
            event.setDeathMessage(red + victim.getName() + " didn't make it to the surface fast enough");
        }
    }

    @EventHandler
    public void onPlayerFreeze(PlayerDeathEvent event) {
        Player victim = event.getPlayer();
        ChatColor red = ChatColor.RED;
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FREEZE){
            event.setDeathMessage(red + victim.getName() + " succumbed to the icy embrace of winter");
        }
    }

    @EventHandler
    public void onPlayerLightning(PlayerDeathEvent event) {
        Player victim = event.getPlayer();
        ChatColor red = ChatColor.RED;
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.LIGHTNING){
            event.setDeathMessage(red + "A bolt from the heavens strikes down into " + victim.getName());
        }
    }

    @EventHandler
    public void onPlayerContact(PlayerDeathEvent event){
        Player victim = event.getPlayer();
        ChatColor red = ChatColor.RED;
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.CONTACT){
            event.setDeathMessage(red + victim.getName() + " hugged something spiky a little too tight");
        }
    }

    @EventHandler
    public void onPlayerHitAWallFlying(PlayerDeathEvent event) {
        Player victim = event.getPlayer();
        ChatColor red = ChatColor.RED;
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FLY_INTO_WALL) {
            event.setDeathMessage(red + victim.getName() + " tried to 9/11 or something..");
            Random chance = new Random();
            if (chance.nextDouble() < 0.40) {
                event.setDeathMessage(red + victim.getName() + " was not aware of physics");
            }
        }
    }

    @EventHandler
    public void onPlayerMagmaBurn(PlayerDeathEvent event) {
        Player victim = event.getPlayer();
        ChatColor red = ChatColor.RED;
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.HOT_FLOOR) { // magma
            event.setDeathMessage(red + victim.getName() + " melted his legs into a bones");
        }
    }

    @EventHandler
    public void onPlayerFire(PlayerDeathEvent event){
        Player victim = event.getPlayer();
        ChatColor red = ChatColor.RED;
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE){ // normal fire
            event.setDeathMessage(red + victim.getName() + " ended tried to be a human torch but ended up as ashes");
        }
    }

    @EventHandler
    public void onPlayerBurn(PlayerDeathEvent event){
        Player victim = event.getPlayer();
        ChatColor red = ChatColor.RED;
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE_TICK){ // in flames
            event.setDeathMessage(red + victim.getName() + " hallucinated and jumped into the flames");
            Random chance = new Random();
            if (chance.nextDouble() < 0.40) {
                event.setDeathMessage(red + victim.getName() + " obviously isn't fireproof");
            } else if (chance.nextDouble() < 0.30){
                event.setDeathMessage(red + victim.getName() + " didn't get to water in time");
            }
        }
    }

    @EventHandler
    public void onPlayerStarve(PlayerDeathEvent event) {
        Player victim = event.getPlayer();
        ChatColor red = ChatColor.RED;
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.STARVATION){
            event.setDeathMessage(red + victim.getName() + " couldn't afford food");
        }
    }


    // end

}
