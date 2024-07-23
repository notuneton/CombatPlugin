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
import java.util.List;

public class MagicStickEvent implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
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
        if (!meta.hasDisplayName() || !meta.getDisplayName().equals("Magic Toy Stick")) {
            return false;
        }
        if (!meta.hasLore() || meta.getLore().size() != 2) {
            return false;
        }
        if (!meta.getLore().get(0).equals(ChatColor.GRAY + "Do not leave with an") ||
                !meta.getLore().get(1).equals(ChatColor.GRAY + "unsupervised magician.")) {
            return false;
        }
        if (!meta.hasEnchant(Enchantment.KNOCKBACK) || meta.getEnchantLevel(Enchantment.KNOCKBACK) != 3) {
            return false;
        }
        return meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS);
    }

    public void summonLineParticle(final Player player) {
        Location startLocation = player.getLocation();
        Vector direction = startLocation.getDirection().normalize();
        new BukkitRunnable() {
            private int ticks = 0;
            private final double trailLength = 40; // Length of the particle trail
            private final double spacing = 0.25; // Increase spacing to make particles move faster
            private final double lineDistance = 0.5; // Distance between the upper and lower particle lines
            private final float particleSize = 0.01f; // Size of the particles

            @Override
            public void run() {
                if (ticks > trailLength / spacing) {
                    this.cancel();
                    return;
                }
                double offset = ticks * spacing;
                Location loc1 = startLocation.clone().add(direction.clone().multiply(offset)).add(0, lineDistance, 0);
                player.getLocation().getWorld().spawnParticle(Particle.CLOUD, loc1, 0, 0, 0, 0, particleSize);
                Location loc2 = startLocation.clone().add(direction.clone().multiply(offset)).add(0, -lineDistance, 0);
                player.getLocation().getWorld().spawnParticle(Particle.CLOUD, loc2, 0, 0, 0, 0, particleSize);
                if (loc1.getBlock().getType() != Material.AIR || loc2.getBlock().getType() != Material.AIR) {
                    this.cancel();
                    return;
                }
                damageEntityes(player, loc1);
                damageEntityes(player, loc2);
                ticks++;
            }

            private void damageEntityes(Player player, Location location) {
                List<Entity> nearbyEntities = (List<Entity>) location.getWorld().getNearbyEntities(location, 0.5, 0.5, 0.5);
                for (Entity entity : nearbyEntities) {
                    if (entity instanceof LivingEntity && entity != player) {
                        ((LivingEntity) entity).damage(1000);
                        location.getWorld().strikeLightning(location);
                    }
                }
            }
        }.runTaskTimer(Combat.getInstance(), 0L, 1L);
    }
}