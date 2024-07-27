package org.main.uneton.combatlogger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.Combat;
import org.main.uneton.utils.ColorUtils;

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

        if (!player.hasPermission("combat.setspawn.sv")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to run /" + command.getName() + ".");
            return true;
        }

        Location loc = player.getLocation();
        plugin.getConfig().set("spawn", loc);
        plugin.saveConfig();
        String success = ColorUtils.colorize("&2>&a> &x&2&E&2&E&2&E&l+ &7");
        player.sendMessage(success + ColorUtils.colorize("&aSuccessfully set the spawn to : X " + loc.getBlockX() + ", Y: " + loc.getBlockY() + ", X: " + loc.getBlockZ()));
        return true;



        /*
        plugin.getConfig().set("spawn.x", location.getX());
        plugin.getConfig().set("spawn.y", location.getY());
        plugin.getConfig().set("spawn.z", location.getZ());
        plugin.getConfig().set("spawn.worldName", location.getWorld().getName());
         */
    }
}
