package org.main.uneton.events;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.main.uneton.Combat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MagicStickEvent implements Listener {

    private static final Set<Block> explosionLocations = new HashSet<>();

    private final double trailLength = 100; // Hiukkaspolun pituus suhde
    public static final int explosionPower = 8; // räjähdyksen voima
    private final double spacing = 1.0; // Lisää välilyöntejä saadaksesi hiukkaset liikkumaan nopeammin
    private final double lineDistance = 0.4; // Ylemmän ja alemman hiukkasviivan välinen etäisyys
    private final float particleSize = 0.5f; // Hiukkasten koko

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

    @Deprecated
    private static boolean isMagicToyStick(ItemStack item) {
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
        Location startLocation = player.getLocation().add(0, player.getEyeHeight(), 0);
        Vector direction = startLocation.getDirection().normalize();
        new BukkitRunnable() {
            private int localTicks = 0;
            @Override
            public void run() {
                if (localTicks > trailLength / spacing) {
                    this.cancel();
                    return;
                }
                double offset = localTicks * spacing;
                Location loc1 = startLocation.clone().add(direction.clone().multiply(offset)).add(0, lineDistance, 0);
                Location loc2 = startLocation.clone().add(direction.clone().multiply(offset)).add(0, -lineDistance, 0);
                player.getWorld().spawnParticle(Particle.CLOUD, loc1, 0, 0, 0, 0, particleSize);
                player.getWorld().spawnParticle(Particle.CLOUD, loc2, 0, 0, 0, 0, particleSize);

                if (loc2.getBlock().getType() != Material.AIR) {
                    explode(loc2);
                    this.cancel();
                    return;
                }
                if (loc1.getBlock().getType() != Material.AIR) {
                    explode(loc1);
                    this.cancel();
                    return;
                }
                localTicks++;
            }
        }.runTaskTimer(Combat.getInstance(), 0L, 1L);
    }

    public static void explode(Location location) {
        World w = location.getWorld();
        explosionLocations.add(location.getBlock());
        w.createExplosion(location, explosionPower, false, true);
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent e) {
        Block explosionPoint = e.getBlock();
        Location location = explosionPoint.getLocation();
        World w = location.getWorld();
        if (w == null) return;
        if (explosionLocations.contains(explosionPoint)) {
            explosionLocations.remove(explosionPoint);
        } else {
            return;
        }
        e.blockList().forEach(b -> {
            for (int i = 0; i < 5; i++) {
                Location bloc = b.getLocation();
                Vector direction = bloc.toVector().subtract(location.toVector()).normalize();

                double randomFactor = 1.1;
                double force = 1.2;
                Vector velocity = direction.multiply(force).add(new Vector(
                        (Math.random() - 0.5) * randomFactor,
                        Math.random() * 0.5,
                        (Math.random() - 0.5) * randomFactor
                ));
                if (!Double.isFinite(velocity.getX()) || !Double.isFinite(velocity.getY()) || !Double.isFinite(velocity.getZ())) {
                    velocity = new Vector(0, 0, 0);
                }

                FallingBlock fallingBlock = w.spawn(b.getLocation(), FallingBlock.class);
                fallingBlock.setVelocity(velocity);
                b.getDrops().clear();
            }
        });
    }
}