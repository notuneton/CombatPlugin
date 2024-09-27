package org.main.uneton.vanillaCmds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

import java.util.Arrays;
import java.util.List;

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

        switch (args.length) {
            case 0:
                String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &7/clear <player> ");
                player.sendMessage(usage);
                return true;
            case 1:
                try {
                    int radius = Integer.parseInt(args[0]);
                    clearGroundItems(player, radius);
                } catch (NumberFormatException exception) {
                    player.sendMessage(ColorUtils.colorize("&c&lHEYO! &7Please provide a valid number."));
                }
                return true;
            case 2:
                Player target = Bukkit.getServer().getPlayer(args[0]);
                if (target == null || !target.isOnline()) {
                    player.sendMessage(ColorUtils.colorize("&c&lWHO?! &7Couldn't find a player with username "+ target.getName() +"!"));
                    return true;
                }

                clearInventory(player, target);
                return true;

            default:
                player.sendMessage(ColorUtils.colorize("&cI'm not sure what you meant by "+ command.getName() +"! Use /clear <player> <radius> or /clear <radius>."));
                return true;
        }
    }

    private void clearGroundItems(Player player, int radius) {
        Location loc_player = player.getLocation();
        List<Entity> nearby_items = player.getWorld()
                .getNearbyEntities(loc_player, radius, radius, radius).stream()
                .filter(entity -> entity instanceof Item)
                .toList();

        for (Entity entity : nearby_items) {
            entity.remove();
        }
        player.sendMessage(success + ColorUtils.colorize("Cleared &e" + nearby_items.size() + " &7ground items within a &f" + radius + " &7block radius."));
    }

    private void clearInventory(Player player, Player target) {
        int amount = 0;
        for (ItemStack items : target.getInventory().getContents()) {
            if (items != null) {
                amount += items.getAmount();
            }
        }

        String itemLabel = amount == 1 ? "item" : "item(s)";
        player.sendMessage(success + ColorUtils.colorize(player.getName() + " &7has successfully cleared '&e" + target.getName() + "&7' inventory of &f" + amount + " " + itemLabel + "&7."));
        target.getInventory().clear();
        target.getActivePotionEffects().clear();
    }
}
