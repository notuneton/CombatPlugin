package org.main.uneton.magicStick;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
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
                System.out.println("[CombatV3] Magic Toy Stick was used!");

                double k = 10.0;
                for (int i = 0; i < k; i++) {
                    System.out.println("tes2");
                    spawnWhiteParticles(player);

                }
            }
        }
    }

    private void spawnWhiteParticles(Player player) {
        Location startLocation = player.getEyeLocation();
        Vector directionVector = player.getLocation().getDirection();
        new BukkitRunnable() {
            int distance = 0;
            final int maxDistance = 15;
            @Override
            public void run() {
                if (distance > maxDistance) {
                    cancel();
                    return;
                }

                Location currentLocation = startLocation.clone().add(directionVector.clone().multiply(distance));
                player.getWorld().spawnParticle(Particle.CLOUD, currentLocation, 100);
                distance++;

            }
        }.runTaskTimer(Combat.getInstance, 0L, 1L); // Run every tick
    }

    public ItemStack hasMagicToyStick() {
        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta meta = stick.getItemMeta();
        meta.setDisplayName("Magic Toy Stick");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Do not leave with an");
        lore.add(ChatColor.GRAY + "unsupervised magician.");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.KNOCKBACK, 5, true);
        stick.setItemMeta(meta);
        return stick;
    }
}
