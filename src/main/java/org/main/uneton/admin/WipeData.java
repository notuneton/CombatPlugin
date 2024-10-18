package org.main.uneton.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;
import org.main.uneton.utils.ConfigManager;

import java.util.UUID;

import static org.main.uneton.Combat.wipePlaytime;
import static org.main.uneton.utils.ConfigManager.*;
import static org.main.uneton.utils.MessageHolder.perm;
import static org.main.uneton.utils.MessageHolder.unknown;
import static org.main.uneton.utils.SoundsUtils.playCancerSound;

public class WipeData implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (!player.hasPermission("combat.wipe.sv")) {
            player.sendMessage(ColorUtils.colorize(perm + command.getName()));
            playCancerSound(player);
            return true;
        }

        if (args.length == 0) {
            String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&8&8&8&3&A&4- &7/wipeprofile <player> ");
            player.sendMessage(usage);
            return true;
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(unknown);
            return true;
        }

        UUID uuid = target.getUniqueId();
        stopTrackingPlayTime(target);

        wipePlaytime(target);
        kills.remove(uuid);
        deaths.remove(uuid);
        someCoins.remove(uuid);
        ConfigManager.get().set("player-kills." + uuid, 0);
        ConfigManager.get().set("player-deaths." + uuid, 0);
        ConfigManager.get().set("coins." + uuid, 0);
        target.getActivePotionEffects().clear();
        for (ItemStack item : target.getInventory().getContents()) {
            if (item != null) {
                target.getInventory().removeItem(item);
            }
        }

        target.sendMessage(ColorUtils.colorize("&f[IMPORTANT] &cYour UUID has been wiped by &e" + player.getName() + "&c."));
        player.sendMessage(ColorUtils.colorize("&eSuccessfully wiped uuid of player &c" + target.getName() + "&e."));

        player.sendMessage(ColorUtils.colorize("&aProfile successfully wiped!"));
        ConfigManager.save();
        return true;
    }

    private void stopTrackingPlayTime(Player player) {
        UUID uuid = player.getUniqueId();
        playTimes.remove(uuid);
        ConfigManager.get().set("player-playtime." + uuid, 0);
        ConfigManager.save();
    }
}
