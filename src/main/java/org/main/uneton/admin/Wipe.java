package org.main.uneton.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.Combat;
import org.main.uneton.utils.ColorUtils;
import org.main.uneton.utils.ConfigManager;

import java.util.Arrays;
import java.util.UUID;

import static org.main.uneton.Combat.perm;
import static org.main.uneton.utils.ConfigManager.*;
import static org.main.uneton.utils.SoundsUtils.playCancerSound;

public class Wipe implements CommandExecutor {

    public Wipe(Combat plugin) {
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if (!player.hasPermission("combat.wipe.sv")) {
            player.sendMessage(ColorUtils.colorize(Arrays.toString(perm) + command.getName()));
            playCancerSound(player);
            return true;
        }

        if (args.length == 0) {
            String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &7/wipeprofile <player> ");
            player.sendMessage(usage);
            return true;
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(ColorUtils.colorize("&c&lWHO?! &7Couldn't find a player with username "+ target.getName() +"!"));
            return true;
        }

        UUID uuid = target.getUniqueId();
        kills.remove(uuid);
        deaths.remove(uuid);
        Combat.playTimes.remove(uuid);
        ConfigManager.get().set("player-kills." + uuid, 0);
        ConfigManager.get().set("player-deaths." + uuid, 0);
        ConfigManager.get().set("players-playtime." + uuid, 0);
        target.getActivePotionEffects().clear();
        for (ItemStack item : target.getInventory().getContents()) {
            if (item != null) {
                target.getInventory().removeItem(item);
            }
        }
        ConfigManager.save();
        target.sendMessage(ColorUtils.colorize("&f[IMPORTANT] &cYour uuid has been wiped by &e" + player.getName() + "&c."));
        player.sendMessage(ColorUtils.colorize("&eSuccessfully wiped uuid player of &c" + target.getName() + "&e."));
        return true;
    }
}
