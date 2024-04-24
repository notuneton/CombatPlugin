package org.main.uneton.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SaveClear implements CommandExecutor {

    Map<UUID, ItemStack[]> items = new HashMap<>();
    Map<UUID, ItemStack[]> armor = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        if(args.length == 0){
            player.sendMessage(ChatColor.RED + "usage: /saveclear <player>");
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getServer().getPlayer(args[0]);
            if (target == null || !target.isOnline() || isInventoryEmpty(target.getInventory())) {
                player.sendMessage(ChatColor.RED + "Cannot save-clear the player's inventory because it is empty.");
                return true;
            }

            storeInventoryItems(target);
            player.sendMessage(ChatColor.GREEN + "The inventory of " + ChatColor.GOLD + target.getName() + ChatColor.GREEN + " has been saved and cleared.");
            return true;
        }

        if (args.length == 2 && "undo".equals(args[1])) {
            Player target = Bukkit.getServer().getPlayer(args[0]);
            if (target == null || !target.isOnline()) {
                player.sendActionBar(ChatColor.RED + "That player does not exist.");
                return true;
            }
            restoreInventory(target);
            player.sendMessage(ChatColor.GREEN + "The inventory of " + ChatColor.GOLD + target.getName() + ChatColor.GREEN + " has been restored.");
            return true;
        }

        return true;
    }

    public void storeInventoryItems(Player player){
        UUID uuid = player.getUniqueId();

        ItemStack[] contents = player.getInventory().getContents();
        ItemStack[] armorContents = player.getInventory().getArmorContents();

        items.put(uuid, contents); // Tallenna sisältö
        armor.put(uuid, armorContents); // Tallenna varusteet

        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
    }

    public void restoreInventory(Player player){
        UUID uuid = player.getUniqueId();

        ItemStack[] contents = items.get(uuid);
        ItemStack[] armorContents = armor.get(uuid);

        if(contents != null){
            player.getInventory().setContents(contents); // Palauta sisältö
        } else {
            player.getInventory().clear();
        }

        if(armorContents != null){
            player.getInventory().setArmorContents(armorContents); // Palauta varusteet
        } else {
            player.getInventory().setHelmet(null);
            player.getInventory().setChestplate(null);
            player.getInventory().setLeggings(null);
            player.getInventory().setBoots(null);
        }
    }

    private boolean isInventoryEmpty(PlayerInventory inventory){
        for(ItemStack item : inventory.getContents()){
            if(item != null && item.getType() != Material.AIR) {
                return false;
            }
        }
        return true;
    }
}

