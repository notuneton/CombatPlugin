package org.main.uneton.admin;

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

        if(!player.hasPermission("combat.repair.use")) {
            player.sendMessage(ChatColor.RED + "Permission Denied: You do not have permission to do this task.");
            return true;
        }

        if(args.length == 0){
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

        if(args.length == 1 && "all".equals(args[0])) {
            ItemStack helmet = player.getInventory().getHelmet();
            ItemStack chestplate = player.getInventory().getChestplate();
            ItemStack leggings = player.getInventory().getLeggings();
            ItemStack boots = player.getInventory().getBoots();
            helmet.setDurability((short) 0);
            chestplate.setDurability((short) 0);
            leggings.setDurability((short) 0);
            boots.setDurability((short) 0);
            player.sendActionBar(ChatColor.GREEN + "Your armor has been repaired.");
        }

        return true;
    }
}
