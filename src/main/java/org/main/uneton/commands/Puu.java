package org.main.uneton.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Puu implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        spawnTreeAhead(player);
        return true;
    }

    private void spawnTreeAhead(Player player) {
        Location location = player.getLocation();
        Vector direction = location.getDirection(); // Get the direction the player is facing
        Location treeLocation = location.add(direction); // Move the location one block in that direction

        treeLocation.getWorld().generateTree(treeLocation, TreeType.TREE);
    }
}
