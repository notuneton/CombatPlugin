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

        if (args.length == 1) {
            String usage = ColorUtils.colorize("&3>&b> &8+ &7usage: &f/repair ");
            player.sendMessage(usage);
            return true;
        }

        ItemStack heldRepair = player.getInventory().getItemInMainHand();
        if (heldRepair.getType() == Material.AIR) {
            String warn = ColorUtils.colorize("&4>&c> &x&2&E&2&E&2&E&l+ &7");
            player.sendMessage(warn + ColorUtils.colorize("&7You are not holding an item to repair!"));
            return true;
        }
        if (heldRepair.getDurability() == 0) {
            String warn = ColorUtils.colorize("&4>&c> &x&2&E&2&E&2&E&l+ &7");
            player.sendMessage(warn + ColorUtils.colorize("&7This item is already maxed!"));
            return true;
        }
        heldRepair.setDurability((short) 0);
        String success = ColorUtils.colorize("&2>&a> &x&2&E&2&E&2&E&l+ &7");
        player.sendMessage(success + ColorUtils.colorize("&7Your item has been repaired."));
        return true;
    }
}
