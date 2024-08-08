package org.main.uneton.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

public class Prototype implements CommandExecutor {

    private final Plugin plugin;
    public Prototype(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        Location spawnLoc = plugin.getConfig().getLocation("spawn");
        if (spawnLoc != null) {
            String success = ColorUtils.colorize("&3>&b> &8+ &7");
            player.sendMessage(success+ ColorUtils.colorize("&3>&b> &8+ &7Moved to &fspawn&7."));
            player.teleport(spawnLoc);
        } else {
            player.sendMessage("");
        }

        return false;
    }
}
