package org.main.uneton.economy;
/*

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.main.uneton.Combat;

import java.util.logging.Level;

public class VaultHook {
    

    public static Economy hook(Combat plugin) {

        plugin.getLogger().info("Hooking economy...");
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().log(Level.WARNING, "Vault not found, Economy features disabled.");
            return null;
        } else {
            Economy vault = plugin.getVault();
            if (vault != null) {
                plugin.getLogger().info("Economy is already hooked");
                return vault;
            } else {
                plugin.getServer().getServicesManager().register(Economy.class, new EcoImpl(), plugin, ServicePriority.Highest);
                RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
                if (rsp != null) {
                    vault = rsp.getProvider();
                    plugin.getLogger().info("Economy hooked! (" + vault.getName() + ")");
                    return vault;
                }
            }
        }
        return null;
    }


}
*/