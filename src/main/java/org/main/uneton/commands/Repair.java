package org.main.uneton.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static org.main.uneton.utils.MessageHolder.*;

public class Repair implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        ItemStack heldRepair = player.getInventory().getItemInMainHand();
        if (heldRepair.getType() == Material.AIR) {
            player.sendMessage(hey_repair_1);
            return true;
        }
        if (heldRepair.getDurability() == 0) {
            player.sendMessage(repair_already_maxed);
            return true;
        }
        heldRepair.setDurability((short) 0);
        player.sendMessage(repaired);
        return true;
    }
}
