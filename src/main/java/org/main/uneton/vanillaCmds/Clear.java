package org.main.uneton.vanillaCmds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

import java.util.Arrays;

import static org.main.uneton.Combat.perm;
import static org.main.uneton.utils.SoundsUtils.playCancerSound;

public class Clear implements CommandExecutor {

    public static String success = ColorUtils.colorize("&3>&b> &8+ &7");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (!player.hasPermission("combat.clear.sv")) {
            player.sendMessage(ColorUtils.colorize(Arrays.toString(perm) + command.getName()));
            playCancerSound(player);
            return true;
        }

        if (args.length == 0) {
            String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &8+ &7/clear <player> ");
            player.sendMessage(usage);
            return true;
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(ColorUtils.colorize("&c&lWHO?! &7Couldn't find a player with username "+ target.getName() +"!"));
            return true;
        }

        int amount = 0;
        for (ItemStack items : target.getInventory().getContents()) {
            if (items != null) {
                amount += items.getAmount();
            }
        }
        String itemLabel = amount == 1 ? "item" : "item(s)";
        player.sendMessage(success + ColorUtils.colorize(player.getName() + " &7has successfully cleared &7'&e" + target.getName() + "&7' inventory of &a" + amount + " " + itemLabel+ "&7."));
        target.getInventory().clear();
        target.getActivePotionEffects().clear();
        return true;
    }
}
