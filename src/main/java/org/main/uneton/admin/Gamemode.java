package org.main.uneton.admin;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Gamemode implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (args.length != 2) {
            player.sendMessage(ChatColor.RED + "/gamemode requires exactly 2 arguments: <gamemode> <player>");
            return true;
        }

        String target = args[0];
        String mode = args[1].toUpperCase();
        if (!target.equals(player.getName())) {
            player.sendMessage(ChatColor.RED + "You cannot change another player's gamemode.");
            return true;
        }

        try {
            GameMode gameMode = GameMode.valueOf(mode);
            player.setGameMode(gameMode);
            player.sendMessage(ChatColor.BOLD.toString() +
                    ChatColor.AQUA + ">" + ChatColor.GRAY + "Gamemode was set to " + ChatColor.WHITE + gameMode +
                    ChatColor.GRAY + " successfully" + ChatColor.DARK_GRAY + ".");
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + "Error: " + ChatColor.DARK_RED + "Invalid gamemode. Valid options are: SURVIVAL, CREATIVE, ADVENTURE, SPECTATOR.");
        }

        return true;
    }
}
