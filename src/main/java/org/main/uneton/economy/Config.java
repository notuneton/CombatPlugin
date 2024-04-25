package org.main.uneton.economy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Config {

    private final String name;
    private File file;
    private final JavaPlugin plugin;
    private FileConfiguration config;

    public Config(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name + ".yml";
    }
    public void load() {
        file = new File(plugin.getDataFolder(), name);
        if (!file.exists()) {
            plugin.saveResource(name, false);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }
    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Error saving config file: " + name);
            System.out.println(e.getMessage());
        }
    }
    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }
    public FileConfiguration getConfig() {
        return config;
    }

}
