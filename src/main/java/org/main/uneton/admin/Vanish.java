package org.main.uneton.admin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.Combat;
import org.main.uneton.utils.ColorUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.main.uneton.utils.SoundsUtils.playCancerSound;

public class Vanish implements CommandExecutor {

    private static Combat plugin;
    private final ProtocolManager protocolManager;

    public Vanish(Combat plugin) {
        Vanish.plugin = plugin;
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }
    BukkitTask task;
    private final Set<Player> vanishedPlayers = new HashSet<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (!player.hasPermission("combat.vanish.sv")) {
            player.sendMessage(ColorUtils.colorize("&c&lCAN'T! &7You do not have permission to run /" + command.getName() + "."));
            playCancerSound(player);
            return true;
        }

        if (args.length == 1) {
            String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&A&B&A&B&A&B/&x&A&B&A&B&A&Bv&x&A&B&A&B&A&Ba&x&A&B&A&B&A&Bn&x&A&B&A&B&A&Bi&x&A&B&A&B&A&Bs&x&A&B&A&B&A&Bh &x&A&B&A&B&A&B");
            player.sendMessage(usage);
            return true;
        }

        if (vanishedPlayers.contains(player)) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.showPlayer(plugin, player);
            }
            vanishedPlayers.remove(player);
            Bukkit.broadcastMessage(ColorUtils.colorize("&8" + " [" + "&a" + "-" + "&8" + "] " + "&7" + player.getName()));
            cancelTask();
        } else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.hidePlayer(plugin, player);
            }
            Bukkit.broadcastMessage(ColorUtils.colorize("&8" + " [" + "&c" + "-" + "&8" + "] " + "&7" + player.getName()));
            vanishedPlayers.add(player);
            sendVanishPacked(player);
        }

        return true;
    }

    private void sendVanishPacked(Player player) {
        task = Bukkit.getScheduler().runTaskTimer(Combat.getInstance(), () -> {
            player.sendActionBar(ColorUtils.colorize("&fYou are currently &cVANISHED"));
        }, 0, 1);
    }

    public void cancelTask() {
        if (task != null) {
            task.cancel();
        }
    }
}
