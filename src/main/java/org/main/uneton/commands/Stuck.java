package org.main.uneton.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class Stuck implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if(command.getName().equalsIgnoreCase("stuck")) {
            player.teleport(player.getWorld().getSpawnLocation());
            return true;
        }

        return false;
    }
}
