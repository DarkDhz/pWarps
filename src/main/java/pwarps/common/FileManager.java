package pwarps.common;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pwarps.main.Main;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class FileManager {
    private static final Main plugin = Main.getPlugin(Main.class);

    private FileConfiguration config = null;
    private File configFile = null;


    /*
     * CONFIG FILE
     */

    public FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }

        return config;
    }

    public void reloadConfig() {
        if (config == null) {
            configFile = new File(plugin.getDataFolder(), "config.yml");
        }

        config = YamlConfiguration.loadConfiguration(configFile);
        Reader defConfigStream;

        defConfigStream = new InputStreamReader(plugin.getResource("config.yml"), StandardCharsets.UTF_8);

        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        config.setDefaults(defConfig);
    }


    public void saveConfig() {
        if (config != null && configFile != null) {
            try {
                getConfig().save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveDefaultConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "config.yml");
        }

        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
    }
}
