package org.main.uneton.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.main.uneton.utils.ColorUtils;

import java.util.Objects;

import static org.main.uneton.combatlogger.CombatLog.combat_tagged;

public class Deaths implements Listener {

    @EventHandler
    @Deprecated
    public void onPlayerSuffocate(PlayerDeathEvent event) {
        Player victim = event.getPlayer();
        if (event.getEntity().getLastDamageCause() != null) {
            return;
        }
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
            event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " swallowed too big an apple... Or just noclip in a wall"));
        }
    }

    @EventHandler
    @Deprecated
    public void onPlayerFall(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        if (event.getEntity().getLastDamageCause() != null) {
            return;
        }
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " believed he could fly, but couldn't.."));
            if (Math.random() < 0.2) {
                event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " thought he could fly, What an autist.."));
            }
        }
    }

    @EventHandler
    @Deprecated
    public void onPlayerVoid(PlayerDeathEvent event) {
        Player victim = event.getPlayer();
        if (event.getEntity().getLastDamageCause() != null) {
            return;
        }
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.VOID) {
            event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " tried to break the laws of physics"));
            if (Math.random() < 0.5) {
                event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " was caught escaping the map"));
            }
        }
    }

    @EventHandler
    @Deprecated
    public void onPlayerSuicide(PlayerDeathEvent event) {
        Player victim = event.getPlayer();
        if (event.getEntity().getLastDamageCause() != null) {
            return;
        }
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.SUICIDE) {
            event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " made an excuse for dying."));
        }
    }

    @EventHandler
    @Deprecated
    public void onPlayerDrown(PlayerDeathEvent event) {
        Player victim = event.getPlayer();
        if (event.getEntity().getLastDamageCause() != null) {
            return;
        }
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.DROWNING) {
            event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " drowned unconscious and didn't make it to the surface fast enough.."));
        }
    }

    @EventHandler
    @Deprecated
    public void onPlayerFreeze(PlayerDeathEvent event) {
        Player victim = event.getPlayer();
        if (event.getEntity().getLastDamageCause() != null) {
            return;
        }
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FREEZE) {
            event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " succumbed to the icy embrace of winter"));
        }
    }

    @EventHandler
    @Deprecated
    public void onPlayerContact(PlayerDeathEvent event) {
        Player victim = event.getPlayer();
        if (event.getEntity().getLastDamageCause() != null) {
            return;
        }
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.CONTACT) {
            event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " hugged something spiky a little too tight"));
        }
    }

    @EventHandler
    @Deprecated
    public void onPlayerHitAWallFlying(PlayerDeathEvent event) {
        Player victim = event.getPlayer();
        if (event.getEntity().getLastDamageCause() != null) {
            return;
        }
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FLY_INTO_WALL) {
            event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " was not aware of physics"));
            if (Math.random() < 0.6) {
                event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " tried to 9/11 or something.."));
            }
        }
    }

    @EventHandler
    @Deprecated
    public void onPlayerMagmaBurn(PlayerDeathEvent event) {
        Player victim = event.getPlayer();
        if (event.getEntity().getLastDamageCause() != null) {
            return;
        }
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.HOT_FLOOR) {
            event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " melted his legs into a bones"));
        }
    }

    @EventHandler
    @Deprecated
    public void onPlayerFire(PlayerDeathEvent event){
        Player victim = event.getPlayer();
        if (event.getEntity().getLastDamageCause() != null) {
            return;
        }
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE) { // normal fire
            event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " ended tried to be a human torch but ended up as ashes"));
        }
    }

    @EventHandler
    @Deprecated
    public void onPlayerBurn(PlayerDeathEvent event){
        Player victim = event.getPlayer();
        if (event.getEntity().getLastDamageCause() != null) {
            return;
        }
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) { // in flames
            event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " hallucinated and jumped into the flames"));
            if (Math.random() < 0.4) {
                event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " obviously isn't fireproof"));
            } else if (Math.random() < 0.4) {
                event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " didn't get to water in time"));
            }
        }
    }

    @EventHandler
    @Deprecated
    public void onPlayerStarve(PlayerDeathEvent event) {
        Player victim = event.getPlayer();
        if (event.getEntity().getLastDamageCause() != null) {
            return;
        }
        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.STARVATION) {
            event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " couldn't afford food"));
        }
    }
}
