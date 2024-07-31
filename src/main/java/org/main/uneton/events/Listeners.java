package org.main.uneton.events;

import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.Combat;
import org.main.uneton.utils.ColorUtils;
import org.main.uneton.utils.ScoreboardUtils;
import org.main.uneton.utils.Tab;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.bukkit.Bukkit.getCommandMap;
import static org.bukkit.Bukkit.getPlayer;
import static org.main.uneton.Combat.doesCommandExist;
import static org.main.uneton.utils.ScoreboardUtils.*;

public class Listeners implements Listener {

    private static Combat plugin;
    private Economy vault;

    public Listeners(Combat plugin) {
        this.plugin = plugin;
        // this.vault = plugin.getVault();
    }

    @EventHandler
    public void onPing(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        int ping = player.getPing();
        if (ping >= 300) {
            String user = player.getName();
            String kickMessage = ChatColor.GOLD + "You have been kicked out from the server for too high ping!";
            player.kickPlayer(kickMessage);
        }
    }

    @EventHandler
    public void onChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        if (message.contains(":123:")) {
            message = message.replace(":123:", ColorUtils.colorize("&a1&r&e2&r&c3"));
            event.setMessage(message);
        }
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().split(" ")[0].substring(1);
        Player player = event.getPlayer();
        if (!doesCommandExist(command)) {
            String warn = ColorUtils.colorize("&4>&c> &x&2&E&2&E&2&E&l- &7");
            player.sendMessage(warn + "The command /"+command + " does not exist.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCommandPreprocessPl(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().split(" ")[0].substring(1);
        Player player = event.getPlayer();
        if (!player.hasPermission("bukkit.command.plugins")) {
            String warn = ColorUtils.colorize("&4>&c> &x&2&E&2&E&2&E&l- &7");
            player.sendMessage(warn + "The command /pl does not exist.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player attacker = victim.getKiller();
        if (attacker != null && attacker instanceof Player) {
            UUID attackerUUID = attacker.getUniqueId();
            UUID victimUUID = victim.getUniqueId();

            ScoreboardUtils.addKill(attackerUUID);
            ScoreboardUtils.addDeath(victimUUID);

            ScoreboardUtils.updateScoreboard(attacker);
            ScoreboardUtils.updateScoreboard(victim);
        }
    }
    @EventHandler
    public void onPlayerSelfDeath(PlayerDeathEvent event) {
        Player diedPlayer = event.getPlayer();
        if (diedPlayer != null && diedPlayer instanceof Player) {
            UUID attackerUUID = diedPlayer.getUniqueId();
            ScoreboardUtils.addSelfDeath(attackerUUID);
            ScoreboardUtils.updateScoreboard(diedPlayer);
        }
    }


    @EventHandler
    public void onJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Tab.updateTab();
        // player.sendMessage(ColorUtils.colorize("&3>&b> &8+ &7Siirryttiin palvelimelle &fmain&7."));
        // String join = ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&2&0&8&1&8&A&l>&x&3&6&D&D&E&E&l>");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        e.setJoinMessage(now.format(formatter) + ChatColor.DARK_GRAY + " [" + ChatColor.GREEN + "+" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + player.getName());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Tab.updateTab();
        updateScoreboard(player);
        // String quit = ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&2&0&8&1&8&A&l>&x&3&6&D&D&E&E&l>");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        e.setQuitMessage(now.format(formatter) + ChatColor.DARK_GRAY + " [" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + player.getName());
    }

    @EventHandler
    public void onPlyaerDeath(PlayerDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            e.setKeepInventory(true);
            e.getDrops().clear();
        }
    }

    @EventHandler
    public void onDropOnDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        Location location = victim.getLocation();

        if (killer != null && !killer.equals(victim)) {
            ItemStack blood = new ItemStack(Material.RED_DYE, 3);
            Item dropped = location.getWorld().dropItemNaturally(location, blood);
            dropped.setPickupDelay(32767);
            Bukkit.getScheduler().runTaskLater(Combat.getPlugin(Combat.class), () -> {
                if (dropped.isValid()) {
                    dropped.remove();
                }
            }, 200L);
        }
    }

    @EventHandler
    public void onPlayerInteractSign(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        Block block = event.getClickedBlock();
        if (block.getType() != Material.OAK_SIGN) return;
        event.getPlayer().openSign((Sign) block.getState());
    }

    @EventHandler
    public void onLootBox(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        Location loc = e.getBlock().getLocation();

        if (e.getBlock().getType() == Material.POLISHED_DIORITE) {
            Random chance = new Random();
            if (Math.random() < 0.2) {
                int index = chance.nextInt(blocksList.length);
                ItemStack dropped_item = blocksList[index];
                block.getWorld().dropItemNaturally(loc, dropped_item);

            } else if (Math.random() < 0.8) {
                e.setDropItems(false);
            }
        }
    }

    private final ItemStack[] blocksList = new ItemStack[]{
            new ItemStack(Material.EMERALD),
            new ItemStack(Material.AMETHYST_SHARD),
            new ItemStack(Material.IRON_NUGGET),
            new ItemStack(Material.GOLD_NUGGET),
            new ItemStack(Material.COPPER_INGOT),
            new ItemStack(Material.STRING)
    };

    @EventHandler
    public void onShearSheep(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Location loc = player.getLocation();
        if (event.getRightClicked() instanceof Sheep) {
            if (player.getInventory().getItemInMainHand().getType() == Material.SHEARS) {
                Random chance = new Random();
                if (chance.nextDouble() < 0.001) {
                    woolDrop(player, loc);
                }
            }
        }
    }

    private void woolDrop(Player player, Location loc) {
        ItemStack bluegem = new ItemStack(Material.LAPIS_LAZULI, 1);
        ItemMeta bluegem_meta = bluegem.getItemMeta();
        bluegem_meta.setDisplayName(ChatColor.BLUE + "Blue gem color");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(ChatColor.GREEN + "Probability: " + ChatColor.YELLOW + "0.001%");
        bluegem_meta.setLore(lore);
        bluegem.setItemMeta(bluegem_meta);

        player.getWorld().dropItemNaturally(loc, bluegem);
    }
}