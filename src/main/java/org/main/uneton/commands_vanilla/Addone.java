package org.main.uneton.commands_vanilla;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static org.main.uneton.utils.ScoreboardUtils.counters;

public class Addone implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        Player player = (Player) sender;
        UUID id = player.getUniqueId();

        player.sendMessage("Your counter has been incremented!");
        counters.put(id, counters.getOrDefault(id, 0) + 1);

        return true;
    }
}
