//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.space.boat.tools;

import com.space.boat.Boat;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {
    public ConfigManager() {
    }

    public static void createFile(String s) {
        File file = new File(Boat.plugin.getDataFolder(), s + ".yml");
        if (!file.exists()) {
            Boat.plugin.saveResource(s + ".yml", false);
        }

    }

    public static FileConfiguration getConfig(String s) {
        File file = new File(Boat.plugin.getDataFolder(), s + ".yml");
        if (!file.exists()) {
            Boat.plugin.saveResource(s, false);
        }

        return YamlConfiguration.loadConfiguration(file);
    }

    public static void writeConfig(String s, String node, Object value) {
        FileConfiguration fileConfiguration = getConfig(s);
        fileConfiguration.set(node, value);

        try {
            fileConfiguration.save(new File(Boat.plugin.getDataFolder(), s + ".yml"));
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }
}
