package org.main.uneton.stick;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
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
import java.util.ArrayList;

public class MagicStickEvent implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if (player.getInventory().contains(hasMagicToyStick())) {
                double k = 1.0;

                for (int i = 0; i < k; i++) {
                    startParticleTrails(player);
                }
            }
        }
    }

    public void startParticleTrails(final Player player) {
        Location startLocation = player.getLocation();
        Vector direction = startLocation.getDirection().normalize();
        new BukkitRunnable() {
            private int ticks = 0;
            private final double trailLength = 40;
            private final double spacing = 0.2;
            private final double lineDistance = 0.5;

            @Override
            public void run() {
                if (ticks > trailLength / spacing) {
                    this.cancel();
                    return;
                }
                double offset = ticks * spacing;
                Location loc1 = startLocation.clone().add(direction.clone().multiply(offset)).add(lineDistance, 0, 0);
                player.getWorld().spawnParticle(Particle.CLOUD, loc1, 0, 0, 0, 0, 0);
                Location loc2 = startLocation.clone().add(direction.clone().multiply(offset)).add(-lineDistance, 0, 0);
                player.getWorld().spawnParticle(Particle.CLOUD, loc2, 0, 0, 0, 0, 0);
                ticks++;
            }
        }.runTaskTimer(Combat.getInstance(), 0L, 1L);
    }

    private Material hasMagicToyStick() {
        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta meta = stick.getItemMeta();
        meta.setDisplayName("Magic Toy Stick");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+ "Do not leave with an");
        lore.add(ChatColor.GRAY+ "unsupervised magician.");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.KNOCKBACK, 3, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        stick.setItemMeta(meta);
        return stick.getType();
    }
}