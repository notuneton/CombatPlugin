package org.main.uneton.combatlogger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.Combat;
import org.main.uneton.utils.ColorUtils;

import static org.main.uneton.utils.SoundsUtils.playCancerSound;

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
            player.sendMessage(ColorUtils.colorize("&c&lCAN'T! &7You do not have permission to run /" + command.getName() + "."));
            playCancerSound(player);
            return true;
        }

        Location location = player.getLocation();
        plugin.getConfig().set("spawn", location);
        plugin.saveConfig();
        String success = ColorUtils.colorize("&2>&a> &8+ &7");
        player.sendMessage(success + ColorUtils.colorize("&7Successfully set the &aspawn&7 to : X " + location.getBlockX() + ", Y: " + location.getBlockY() + ", X: " + location.getBlockZ()));
        return true;
    }
}
