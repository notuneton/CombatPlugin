package org.main.uneton.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

import static org.main.uneton.utils.MessageHolder.perm;
import static org.main.uneton.utils.MessageHolder.unknown;
import static org.main.uneton.utils.SoundsUtils.playCancerSound;

public class Invsee implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (!player.hasPermission("combat.invsee.sv")) {
            player.sendMessage(ColorUtils.colorize(perm + command.getName()));
            playCancerSound(player);
            return true;
        }

        if (args.length == 0) {
            String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&8&8&8&3&A&4- &7&x&A&B&A&B&A&B/&x&A&B&A&B&A&Bi&x&A&B&A&B&A&Bn&x&A&B&A&B&A&Bv&x&A&B&A&B&A&Bs&x&A&B&A&B&A&Be&x&A&B&A&B&A&Be &x&A&B&A&B&A&B<&x&A&B&A&B&A&Bp&x&A&B&A&B&A&Bl&x&A&B&A&B&A&Ba&x&A&B&A&B&A&By&x&A&B&A&B&A&Be&x&A&B&A&B&A&Br&x&A&B&A&B&A&B>");
            player.sendMessage(usage);
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null || !target.isOnline()) {
                player.sendMessage(unknown);
                return true;
            }
            Inventory inventory = target.getInventory();
            player.openInventory(inventory);
        }
        return true;
    }
}
