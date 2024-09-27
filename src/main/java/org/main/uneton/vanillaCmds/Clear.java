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

import java.util.List;

import static org.main.uneton.Combat.perm;
import static org.main.uneton.utils.SoundsUtils.playCancerSound;

public class Clear implements CommandExecutor {

    public static String success = ColorUtils.colorize("&3>&b> &x&8&8&8&3&A&4- &7");

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

        switch (args.length) {
            case 1:
                String firstArg = args[0];
                if (firstArg.equals("20") || firstArg.equals("10")) {
                    int radius = Integer.parseInt(firstArg);
                    clearGroundItems(player, radius);
                } else {
                    Player target = Bukkit.getServer().getPlayer(firstArg);
                    if (target == null || !target.isOnline()) {
                        player.sendMessage(ColorUtils.colorize("&c&lWHO?! &7Couldn't find a player with username " + firstArg + "!"));
                    } else {
                        clearInventory(player, target);
                    }
                }
                return true;

            default:
                player.sendMessage(ColorUtils.colorize("&cI'm not sure what you meant by /" + command.getName() + ". Use /clear <player> <radius> or /clear <radius>."));
                return true;
        }
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
        player.sendMessage(success + ColorUtils.colorize("Cleared &e" + nearbyItems.size() + " &7ground items within a &f" + radius + " &7block radius."));
    }

    private void clearInventory(Player player, Player target) {
        int amount = 0;
        for (ItemStack items : target.getInventory().getContents()) {
            if (items != null) {
                amount += items.getAmount();
            }
        }

        String itemLabel = amount == 1 ? "item" : "item(s)";
        player.sendMessage(success + ColorUtils.colorize(player.getName() + " &7has successfully cleared '&e" + target.getName() + "'s&7' inventory of &f" + amount + " " + itemLabel + "&7."));
        target.getInventory().clear();
        target.getActivePotionEffects().clear();
    }
}
