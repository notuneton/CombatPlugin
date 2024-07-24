package org.main.uneton.events;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.main.uneton.Combat;
import org.main.uneton.utils.ColorUtils;

import java.util.List;

public class MagicStickEvent implements Listener {

    private int ticks = 0;
    private final double trailLength = 40; // Hiukkaspolun pituus suhde
    private final double spacing = 0.8; // Lisää välilyöntejä saadaksesi hiukkaset liikkumaan nopeammin (normaali 0,25)
    private final int countofParticles = 100;
    private final double lineDistance = 0.5; // Ylemmän ja alemman hiukkasviivan välinen etäisyys
    private final float particleSize = 0.001f; // Hiukkasten koko

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        if (!player.hasPermission("combat.magicstick.sv")) {
            player.sendActionBar(ColorUtils.colorize("&cYou do not have permission to use that!"));
            return;
        }
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            ItemStack heldItem = player.getInventory().getItemInMainHand();
            if (isMagicToyStick(heldItem)) {
                double k = 1.0;
                for (int i = 0; i < k; i++) {
                    summonLineParticle(player);
                }
            }
        }
    }

    private boolean isMagicToyStick(ItemStack item) {
        if (item == null || item.getType() != Material.STICK) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }
        return meta.hasDisplayName() && meta.getDisplayName().equals("Magic Toy Stick") &&
                meta.hasLore() && meta.getLore().size() == 2 &&
                meta.getLore().get(0).equals(ChatColor.GRAY + "Do not leave with an") &&
                meta.getLore().get(1).equals(ChatColor.GRAY + "unsupervised magician.") &&
                meta.hasEnchant(Enchantment.KNOCKBACK) && meta.getEnchantLevel(Enchantment.KNOCKBACK) == 3 &&
                meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS);
    }



    public void summonLineParticle(final Player player) {
        Location startLocation = player.getLocation();
        Vector direction = startLocation.getDirection().normalize();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (ticks > trailLength / spacing) {
                    this.cancel();
                    return;
                }
                double offset = ticks * spacing;
                Location upperLocation = startLocation.clone().add(direction.clone().multiply(offset)).add(0, lineDistance, particleSize);
                Location lowerLocation = startLocation.clone().add(direction.clone().multiply(offset)).add(0, -lineDistance, particleSize);

                spawnParticle(player, upperLocation);
                spawnParticle(player, lowerLocation);

                if (upperLocation.getBlock().getType() != Material.AIR || lowerLocation.getBlock().getType() != Material.AIR) {

                    strikeLightningAtLocations(upperLocation, lowerLocation);

                    this.cancel();
                    return;
                }

                damageEntities(player, upperLocation);
                damageEntities(player, lowerLocation);
                ticks++;
            }

            private void strikeLightningAtLocations(Location... locations) {
                for (Location location : locations) {
                    for (int i = 0; i < 100; i++) {
                        location.getWorld().strikeLightning(location);
                    }
                }
            }

            private void spawnParticle(Player player, Location location) {
                player.getLocation().getWorld().spawnParticle(Particle.CLOUD, location, countofParticles, 0, 0, 0, particleSize);
            }

            private void damageEntities(Player player, Location location) {
                List<Entity> nearbyEntities = (List<Entity>) location.getWorld().getNearbyEntities(location, 0.5, 0.5, 0.5);
                for (Entity entity : nearbyEntities) {
                    if (entity instanceof LivingEntity && entity != player) {
                        ((LivingEntity) entity).damage(1000);
                        for (int index = 0; index < 100; index++) {
                            location.getWorld().strikeLightning(location);
                        }
                    }
                }
            }
        }.runTaskTimer(Combat.getInstance(), 0L, 1L);
    }
}