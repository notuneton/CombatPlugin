package org.main.uneton.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Thunderbolt implements CommandExecutor {

    private static final double chance_of = 1 / 8100000000.0;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        Random r = new Random();
        if (r.nextDouble() < chance_of) {
            Bukkit.broadcastMessage(ChatColor.BOLD + "Spawning 60 lightning bolts due to extremely rare chance!");
            for (int index = 0; index < 60; index++) {
                Bukkit.getWorlds().get(0).strikeLightning(player.getLocation());
            }
        }

        return true;
    }
}
