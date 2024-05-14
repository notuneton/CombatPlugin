package org.main.uneton.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Repair implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        ItemStack heldRepair = player.getInventory().getItemInMainHand();
        if (heldRepair.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "You are not holding an item to repair!");
            return true;
        }
        if(heldRepair.getDurability() == 0) {
            player.sendMessage(ChatColor.RED + "This item is already maxed!");
            return true;
        }
        heldRepair.setDurability((short) 0);
        player.sendActionBar(ChatColor.GREEN + "Your item has been repaired.");
        return true;
    }
}
