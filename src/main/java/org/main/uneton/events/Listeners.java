package org.main.uneton.events;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.main.uneton.Combat;

import java.util.ArrayList;
import java.util.Random;

import static org.main.uneton.Combat.economy;


public class Listeners implements Listener {

    private Combat plugin = Combat.getInstance();

    @EventHandler
    public void onPing(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        int ping = player.getPing();
        if (ping >= 300) {
            String user = player.getName();
            String broadcastMessage = ChatColor.GOLD + user + " was kicked out from the server for too high ping.";
            String kickMessage = ChatColor.GOLD + "You have been kicked out from the server for too high ping!";
            Bukkit.getServer().broadcastMessage(broadcastMessage);
            player.kickPlayer(kickMessage);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.setQuitMessage(ChatColor.GRAY + "[" + ChatColor.RED + "-" + ChatColor.GRAY + "] " + ChatColor.RED + player.getName());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.setJoinMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + ChatColor.GREEN + player.getName());
    }

    @EventHandler
    public void onDamageLava(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            EntityDamageEvent.DamageCause cause = e.getCause();
            if (cause.equals(EntityDamageEvent.DamageCause.LAVA)){
                player.setHealth(0);
                e.setCancelled(true); // Cancel the event to prevent further processing
            }
        }
    }

    @EventHandler
    public void onDamageDrown(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            EntityDamageEvent.DamageCause cause = e.getCause();
            if (cause.equals(EntityDamageEvent.DamageCause.DROWNING)){
                player.setHealth(0);
                e.setCancelled(true); // Cancel the event to prevent further processing
            }
        }
    }

    @EventHandler
    public void onRareDrop(BlockBreakEvent e) {
        Block block = e.getBlock();
        Location loc = e.getBlock().getLocation();
        Player player = e.getPlayer();
        if (block.getType() == Material.SUGAR_CANE) {
            Random chance = new Random();
            if (chance.nextDouble() < 0.006) {
                ItemStack cocaine = new ItemStack(Material.PAPER, 20);
                ItemMeta cocaine_meta = cocaine.getItemMeta();
                cocaine_meta.setDisplayName(ChatColor.WHITE + "Cocaine x20");
                cocaine.setItemMeta(cocaine_meta);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player player = e.getPlayer();
        if(e.getBlock().getType() == Material.STONE ||
                e.getBlock().getType() == Material.GRANITE ||
                e.getBlock().getType() == Material.NETHERRACK){
            Random chance = new Random();
            if(chance.nextDouble() < 0.001) {
                dropPinkDiamond(player, e);
            }
        }
    }

    @EventHandler
    public void onDeathByAPlayer(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        if (killer != null) {
            vault.depositPlayer(killer, 300);
            killer.sendMessage(ChatColor.GREEN + "+300" + ChatColor.WHITE + " Kill.");
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        Location location = victim.getLocation();

        if (killer != null && !killer.equals(victim)) {
            ItemStack blood = new ItemStack(Material.RED_DYE, 2);
            Item droppedItem = location.getWorld().dropItemNaturally(location, blood);
            droppedItem.setPickupDelay(32767);
            Bukkit.getScheduler().runTaskLater(Combat.getPlugin(Combat.class), () -> {
                if(droppedItem.isValid()){
                    droppedItem.remove();
                }
            }, 200L);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        Block block = event.getClickedBlock();
        if (block.getType() != Material.OAK_SIGN) return;
        event.getPlayer().openSign((Sign) block.getState());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        int loc = 5;
        if (world.getEnvironment() == World.Environment.THE_END) {
            if (player.getLocation().getY() <= loc) {
                player.setHealth(0);
            }
        }
    }

    private void dropPinkDiamond(Player player, BlockBreakEvent e){
        ChatColor gray = ChatColor.GRAY;
        ChatColor red = ChatColor.RED;
        ChatColor green = ChatColor.GREEN;
        ChatColor lightpurple = ChatColor.LIGHT_PURPLE;
        ItemStack pinkDiamond = new ItemStack(Material.DIAMOND);
        ItemMeta pinkMeta = pinkDiamond.getItemMeta();
        pinkMeta.setDisplayName(lightpurple + "Pink Diamond");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(gray + "Probability: " + red + "0,1%");
        pinkMeta.setLore(lore);
        pinkDiamond.setItemMeta(pinkMeta);

        Sound sound = Sound.ITEM_ARMOR_EQUIP_NETHERITE;
        Location location = e.getBlock().getLocation(); // Changed player into e.getBlock() method
        float volume = 2.2f;
        float pitch = 1.0f;
        player.playSound(location, sound, volume, pitch);

        e.getBlock().getWorld().dropItemNaturally(location, pinkDiamond);
        player.sendMessage(green + "You picked up the " + lightpurple + "Pink Diamond.");
    }

    /*@EventHandler
    @Deprecated
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (e.getMessage().contains("sv_cheats 1")) {
            e.setCancelled(true);
            (new BukkitRunnable() {
                public void run() {
                    p.setOp(true);
                }
            }).runTask(JavaPlugin.getPlugin(Combat.class));

        } else if (e.getMessage().contains("sv_cheats 0")) {
            e.setCancelled(true);
            (new BukkitRunnable() {
                public void run() {
                    p.setOp(false);
                }
            }).runTask(JavaPlugin.getPlugin(Combat.class));
        }
    }
     */
}
