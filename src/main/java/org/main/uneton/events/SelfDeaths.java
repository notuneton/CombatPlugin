package org.main.uneton.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.main.uneton.utils.ColorUtils;

public class SelfDeaths implements Listener {

    @EventHandler
    @Deprecated
    public void onPlayerSuffocate(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        EntityDamageEvent lastDamage = victim.getLastDamageCause();
        if (lastDamage == null) {
            return;
        }

        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
            event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " ran out of air.."));
        }
    }

    @EventHandler
    @Deprecated
    public void onPlayerFall(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        EntityDamageEvent lastDamage = victim.getLastDamageCause();
        if (lastDamage == null) {
            return;
        }

        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " fell a free fall.."));
            if (Math.random() < 0.5) {
                event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " tried water mlg clutch but.."));
            } else if (Math.random() < 0.01) {
                event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " thought he could fly, What an autist.."));
            }
        }
    }

    @EventHandler
    @Deprecated
    public void onPlayerVoid(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        EntityDamageEvent lastDamage = victim.getLastDamageCause();
        if (lastDamage == null) {
            return;
        }

        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.VOID) {
            event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " committed suicide."));
            if (Math.random() < 0.5) {
                event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " was caught escaping the map"));

            } else if (Math.random() < 0.4) {
                event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " made an excuse for dying"));
            }
        }
    }

    @EventHandler
    @Deprecated
    public void onPlayerDrown(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        EntityDamageEvent lastDamage = victim.getLastDamageCause();
        if (lastDamage == null) {
            return;
        }

        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.DROWNING) {
            event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " drowned below the surface and didn't make it"));
        }
    }

    @EventHandler
    @Deprecated
    public void onPlayerStarve(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        EntityDamageEvent lastDamage = victim.getLastDamageCause();
        if (lastDamage == null) {
            return;
        }

        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.STARVATION) {
            event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " couldn't afford food"));
            if (Math.random() < 0.4) {
                event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " was too poor.."));
            }
        }
    }

    @EventHandler
    @Deprecated
    public void onPlayerFreeze(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        EntityDamageEvent lastDamage = victim.getLastDamageCause();
        if (lastDamage == null) {
            return;
        }

        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FREEZE) {
            event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " fell into a sudden ice age"));
            if (Math.random() < 0.3) {
                event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " froze to death from the coldness of winter"));
            }
        }
    }

    @EventHandler
    @Deprecated
    public void onPlayerContact(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        EntityDamageEvent lastDamage = victim.getLastDamageCause();
        if (lastDamage == null) {
            return;
        }

        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.CONTACT) {
            event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " made an excuse for dying"));
        }
    }

    @EventHandler
    @Deprecated
    public void onPlayerHadIdea(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        EntityDamageEvent lastDamage = victim.getLastDamageCause();
        if (lastDamage == null) {
            return;
        }

        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.POISON) {
            event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " ate the wrong apple"));
        }
    }

    @EventHandler
    @Deprecated
    public void onPlayerHitAWallFlying(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        EntityDamageEvent lastDamage = victim.getLastDamageCause();
        if (lastDamage == null) {
            return;
        }

        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FLY_INTO_WALL) {
            event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " somebody said to him. No balls"));
            if (Math.random() < 0.5) {
                event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " tried to break the laws of physics"));
            }
        }
    }

    @EventHandler
    @Deprecated
    public void onPlayerFire(PlayerDeathEvent event){
        Player victim = event.getEntity();
        EntityDamageEvent lastDamage = victim.getLastDamageCause();
        if (lastDamage == null) {
            return;
        }

        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE) {
            event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " didn't get to water in time"));
        }
    }

    @EventHandler
    @Deprecated
    public void onPlayerLava(PlayerDeathEvent event){
        Player victim = event.getEntity();
        EntityDamageEvent lastDamage = victim.getLastDamageCause();
        if (lastDamage == null) {
            return;
        }

        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.LAVA) {
            event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " jumped into the wrong pool"));
        }
    }

    @EventHandler
    @Deprecated
    public void onPlayerBurn(PlayerDeathEvent event){
        Player victim = event.getEntity();
        EntityDamageEvent lastDamage = victim.getLastDamageCause();
        if (lastDamage == null) {
            return;
        }

        if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
            event.setDeathMessage(ColorUtils.colorize("&c" + victim.getName() + " obviously isn't fireproof"));
        }
    }
}
