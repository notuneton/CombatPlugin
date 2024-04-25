package org.main.uneton.economy;

import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.main.uneton.Combat;

public class VaultHook {

    private Combat plugin = Combat.getInstance();
    private Economy provider;

    public void Hook() {
        this.provider = plugin.impl;
        Bukkit.getServicesManager().register(Economy.class, this.provider, this.plugin, ServicePriority.Normal);
        Bukkit.getConsoleSender().sendMessage(Component.text("Vault hooked into " + this.plugin.getName()));
    }

    public void unHook(){
        Bukkit.getServicesManager().unregister(Economy.class, this.provider);
        Bukkit.getConsoleSender().sendMessage(Component.text("Vault unhooked from " + this.plugin.getName()));
    }
}
