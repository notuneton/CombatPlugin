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

        if (args.length!= 2) {
            player.sendMessage(ChatColor.RED + "> /gamemode requires exactly 2 arguments: <gamemode> <player>");
            return true;
        }

        String mode = args[1].toUpperCase();

        switch (mode) {
            case "SURVIVAL":
                player.setGameMode(GameMode.SURVIVAL);
                break;
            case "CREATIVE":
                player.setGameMode(GameMode.CREATIVE);
                break;
            case "ADVENTURE":
                player.setGameMode(GameMode.ADVENTURE);
                break;
            case "SPECTATOR":
                player.setGameMode(GameMode.SPECTATOR);
                break;
            default:
                player.sendMessage(ChatColor.RED + "Error: Invalid gamemode. Valid options are: SURVIVAL, CREATIVE, ADVENTURE, SPECTATOR");
                return true;
        }

        if (player.getGameMode() == GameMode.valueOf(mode)) {
            player.sendMessage(ChatColor.BOLD.toString() +
                    ChatColor.DARK_AQUA + ">" +
                    ChatColor.AQUA + "> " + ChatColor.GRAY + "The selected player is already in the selected game mode" + ChatColor.DARK_GRAY + ".");
        } else {
            player.sendMessage(ChatColor.BOLD.toString() +
                    ChatColor.DARK_AQUA + ">" +
                    ChatColor.AQUA + "> " + ChatColor.GRAY + "Gamemode was set to " +
                    ChatColor.WHITE + mode +
                    ChatColor.GRAY + " successfully" + ChatColor.DARK_GRAY + ".");
        }

        return true;
    }

}
