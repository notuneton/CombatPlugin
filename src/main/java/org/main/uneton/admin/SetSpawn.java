package org.main.uneton.admin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.Combat;

public class SetSpawn implements CommandExecutor {

    private final Combat plugin;

    public SetSpawn(Combat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if(!player.hasPermission("combat.setspawn.sv")) {
            player.sendMessage(ChatColor.RED + "Permission Denied: You do not have permission to do this task.");
            return true;
        }

        Location location = player.getLocation();
        plugin.getConfig().set("spawn", location);
        plugin.saveConfig();
        player.sendMessage(ChatColor.GREEN + "Successfully set the spawn to : X " + location.getBlockX() + ", Y: " + location.getBlockY() + ", X: " + location.getBlockZ());


        /*
        plugin.getConfig().set("spawn.x", location.getX());
        plugin.getConfig().set("spawn.y", location.getY());
        plugin.getConfig().set("spawn.z", location.getZ());
        plugin.getConfig().set("spawn.worldName", location.getWorld().getName());
         */
        return true;
    }
}
