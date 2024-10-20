package org.main.uneton.vanillaCmds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

import java.util.List;

import static org.main.uneton.utils.MessageHolder.*;
import static org.main.uneton.utils.SoundsUtils.playCancerSound;

public class Clear implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (!player.hasPermission("combat.clear.sv")) {
            player.sendMessage(ColorUtils.colorize(perm + command.getName()));
            playCancerSound(player);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(unknown_syntax);
            return true;
        }

        String targetType = args[0];
        if (targetType.equals("living_entities") || targetType.equals("ground_items")) {
            int radius = 10; // Default radius
            if (args.length > 1) {
                try {
                    radius = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage(ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &7Invalid radius! Please enter a valid number."));
                    return true;
                }
            }

            if (targetType.equals("living_entities")) {
                clearGroundMobs(player, radius);
            } else {
                clearGroundItems(player, radius);
            }
            return true;
        }

        Player target = Bukkit.getServer().getPlayer(targetType);
        if (target == null || !target.isOnline()) {
            player.sendMessage(unknown);
            return true;
        } else {
            clearPlayerOwnInventory(player, target);
        }
        return true;
    }

    private void clearGroundItems(Player player, int radius) {
        Location loc_player = player.getLocation();
        List<Entity> nearbyItems = player.getWorld()
                .getNearbyEntities(loc_player, radius, radius, radius).stream()
                .filter(entity -> entity instanceof Item)
                .toList();

        for (Entity entity : nearbyItems) {
            entity.remove();
        }
        player.sendMessage(getSuccess + ColorUtils.colorize("Cleared &e" + nearbyItems.size() + " &7ground items within a &f" + radius + " &7block radius."));
    }

    private void clearGroundMobs(Player player, int radius) {
        Location loc_player = player.getLocation();
        List<Entity> nearbyMobs = player.getWorld()
                .getNearbyEntities(loc_player, radius, radius, radius).stream()
                .filter(entity -> entity instanceof Mob)
                .toList();

        for (Entity entity : nearbyMobs) {
            entity.remove();
        }
        player.sendMessage(getSuccess + ColorUtils.colorize("Removed &e" + nearbyMobs.size() + " &7living mobs within a &f" + radius + " &7block radius."));
    }

    private void clearPlayerOwnInventory(Player player, Player target) {
        int amount = 0;
        for (ItemStack items : target.getInventory().getContents()) {
            if (items != null) {
                amount += items.getAmount();
            }
        }

        String itemLabel = amount == 1 ? "item" : "item(s)";
        Bukkit.broadcastMessage(getSuccess + ColorUtils.colorize("&f"+ player.getName() + " &7has successfully cleared '&c" + target.getName() + " s&7' inventory of &f" + amount + " " + itemLabel + "&7."));
        target.getInventory().clear();
        target.getActivePotionEffects().clear();
    }
}
