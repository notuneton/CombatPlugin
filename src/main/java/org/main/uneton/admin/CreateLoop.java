package org.main.uneton.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

import java.util.*;

import static org.main.uneton.utils.MessageHolder.perm;
import static org.main.uneton.utils.MessageHolder.success_bold;
import static org.main.uneton.utils.SoundsUtils.playCancerSound;

public class CreateLoop implements CommandExecutor, TabCompleter {

    private final JavaPlugin plugin;
    private final Map<UUID, BukkitRunnable> runningLoops = new HashMap<>();

    public CreateLoop(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (!player.hasPermission("combat.endlessloop.sv")) {
            player.sendMessage(ColorUtils.colorize(perm + command.getName()));
            playCancerSound(player);
            return true;
        }

        if (args.length > 2) {
            String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&8&8&8&3&A&4- &7/createloop <message to loop> <delay>");
            player.sendMessage(usage);
            return true;
        }

        if (args[0].equalsIgnoreCase("break_loop")) {
            stopLoop(player);
            return true;
        }

        String message = args[0];
        int delay;

        try {
            delay = Integer.parseInt(args[1]) * 20;
        } catch (NumberFormatException nfe) {
            player.sendMessage(ColorUtils.colorize("&c&lCAN'T! &7Please provide a valid number for delay!"));
            return true;
        }

        // no multiple task(s)!!
        if (runningLoops.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning("[CombatV3]: player: '" + sender.getName() + "' tried to add multiple loops!");
            player.sendMessage(ColorUtils.colorize("&x&5&B&5&B&5&B&l>&x&2&0&8&1&8&A&l>&x&3&6&D&D&E&E&l &x&8&8&8&3&A&4- &7You already have a loop running. Use /createloop break_loop to stop it."));
        }

        BukkitRunnable loopTask = new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage(ColorUtils.colorize("&a" + message));
            }
        };

        loopTask.runTaskTimer(plugin, 0, delay);
        return true;
    }

    public void stopLoop(Player player) {
        UUID playerID = player.getUniqueId();
        if (runningLoops.containsKey(playerID)) {
            runningLoops.get(playerID).cancel();
            runningLoops.remove(playerID);
            player.sendMessage(ColorUtils.colorize(success_bold + "The loop was break."));
        } else {
            player.sendMessage(ColorUtils.colorize("&cYou don't have a loop running."));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("break_loop");
        }
        return Collections.emptyList();
    }
}
