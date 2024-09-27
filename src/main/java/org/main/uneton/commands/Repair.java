package org.main.uneton.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

public class Repair implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        ItemStack heldRepair = player.getInventory().getItemInMainHand();
        if (heldRepair.getType() == Material.AIR) {
            player.sendMessage(ColorUtils.colorize("&c&lHEY! &7You are not holding an item to repair!"));
            return true;
        }
        if (heldRepair.getDurability() == 0) {
            player.sendMessage(ColorUtils.colorize("&c&lCAN'T! &7This item is already maxed!"));
            return true;
        }
        heldRepair.setDurability((short) 0);
        String success = ColorUtils.colorize("&2>&a> &x&8&8&8&3&A&4- &7");
        player.sendMessage(success + "Your item has been repaired.");
        return true;
    }
}
