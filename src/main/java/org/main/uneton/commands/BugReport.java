package org.main.uneton.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

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
            String usage = ColorUtils.colorize("&3>&b> &8+ &7usage: &f/bugreport <description>");
            player.sendMessage(usage);
            return true;
        }

        String description = String.join(" ", args);
        String success = ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&2&0&8&1&8&A&l>&x&3&6&D&D&E&E&l> ");
        player.sendMessage(success + ColorUtils.colorize("&7Thank you for submitting a bug report!"));

        // Log the bug report
        plugin.getLogger().info(player.getName() + " submitted a bug report: " + description);
        return true;
    }
}
