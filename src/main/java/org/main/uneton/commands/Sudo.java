package org.main.uneton.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class Sudo implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        // Corrected the syntax for checking if the sender is an instance of a player named "unetonn"
        Player player = (Player) sender;
        if (!allowedPlayers.contains(player.getName())) {
            player.sendMessage(ChatColor.BLUE + "\nAccess denied.\n " + ChatColor.WHITE + "You need admintrator privileges to complete this task. Go in Server console, then run command op player name so player get admintrator.\n\n");
            return true; // Stop executing further code
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "> /sudo <target> <command>");
            return true;
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendActionBar(ChatColor.DARK_RED + "That user is offline.");
            player.sendMessage(ChatColor.DARK_RED + "That user is offline.");
            return true;
        }

        if (args.length >= 2) {
            String user = args[0];
            Player cmd = Bukkit.getServer().getPlayer(user);
            if (cmd != null) {
                StringBuilder message = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    message.append(args[i]).append(" ");
                }
                cmd.chat(message.toString()); // Execute the command on the target player
                player.sendMessage(ChatColor.GRAY + "Executed command '" + ChatColor.GREEN + message + ChatColor.GRAY + "' on " + ChatColor.YELLOW + user);
            } else {
                player.sendMessage(ChatColor.RED + "Could not find player: " + user);
            }
        }
        return true;
    }

    private final List<String> allowedPlayers = Arrays.asList(
            "unetonn"
            // Add more usernames as needed
    );

}