package com.space.boat.tools;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static com.space.boat.Boat.plugin;

public class ConfigManager {

    public static void createFile(String s) {
        File file = new File(plugin.getDataFolder(), s + ".yml");
        if (!file.exists()) {
            plugin.saveResource(s+ ".yml", false);
        }
    }

    public static FileConfiguration getConfig(String s) {
        File file = new File(plugin.getDataFolder(), s + ".yml");
        if (!file.exists()) {
            plugin.saveResource(s, false);
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public static void writeConfig(String s , String node, Object value) {
        FileConfiguration fileConfiguration = getConfig(s);
        fileConfiguration.set(node, value);
        try {
            fileConfiguration.save(new File(plugin.getDataFolder(), s + ".yml"));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
