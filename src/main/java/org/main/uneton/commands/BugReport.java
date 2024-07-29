package org.main.uneton.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

import java.util.UUID;

import static org.main.uneton.Combat.cooldowns;

public class BugReport implements CommandExecutor {

    private final JavaPlugin plugin;
    public BugReport(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (args.length == 0) {
            String usage = ColorUtils.colorize("&3>&b> &8+ &7usage: &f/bugreport <description> ");
            player.sendMessage(usage);
            return true;
        }

        UUID playerUUID = player.getUniqueId();
        if (isOnCooldown(playerUUID, 60)) {
            long timeLeft = getTimeLeft(playerUUID, 60);
            player.sendMessage(ColorUtils.colorize("&cYou must wait " + timeLeft + "&c seconds before using this command again."));
        } else {
            updateCooldown(playerUUID);
            String description = String.join(" ", args);
            String success = ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&2&0&8&1&8&A&l>&x&3&6&D&D&E&E&l> ");
            player.sendMessage(success + ColorUtils.colorize("&7Thank you for submitting a bug report!"));

            // Log the bug report
            plugin.getLogger().info(player.getName() + " submitted a bug report: " + description);
        }
        return true;
    }

    private boolean isOnCooldown(UUID playerUUID, int cooldownSeconds) {
        if (cooldowns.containsKey(playerUUID)) {
            long lastUsed = cooldowns.get(playerUUID);
            long currentTime = System.currentTimeMillis();
            long cooldownTime = cooldownSeconds * 1000L; // Convert to milliseconds

            return (currentTime - lastUsed) < cooldownTime;
        }
        return false;
    }
    private long getTimeLeft(UUID playerUUID, int cooldownSeconds) {
        if (cooldowns.containsKey(playerUUID)) {
            long lastUsed = cooldowns.get(playerUUID);
            long currentTime = System.currentTimeMillis();
            long cooldownTime = cooldownSeconds * 1000L;

            return (cooldownTime - (currentTime - lastUsed)) / 1000; // Convert back to seconds
        }
        return 0;
    }
    private void updateCooldown(UUID playerUUID) {
        cooldowns.put(playerUUID, System.currentTimeMillis());
    }
}
