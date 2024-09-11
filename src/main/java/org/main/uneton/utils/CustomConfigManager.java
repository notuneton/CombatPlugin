package org.main.uneton.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.main.uneton.Combat;

import java.io.File;
import java.io.IOException;

public class CustomConfigManager {

    private static File file;
    private static FileConfiguration customfile;

    public CustomConfigManager() {
    }

    public static void setup() {
        file = new File(Combat.getPlugin(Combat.class).getDataFolder(), "config.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException var1) {
                throw new RuntimeException(var1);
            }
        }

        customfile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return customfile;
    }

    public static void save() {
        try {
            customfile.save(file);
        } catch (IOException var1) {
            Bukkit.getLogger().warning("Couldn't save the file!");
        }

    }

    public static void reload() {
        customfile = YamlConfiguration.loadConfiguration(file);
    }

}
