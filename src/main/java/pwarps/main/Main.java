package pwarps.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pwarps.common.FileManager;
import pwarps.data.Database;
import pwarps.data.SQLite;
import pwarps.objects.PlayerWarps;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author blaze
 */
public class Main extends JavaPlugin {

    public HashMap<String, PlayerWarps> playerwarps = new HashMap<>();
    public HashMap<String, String> editing_chat = new HashMap<>();
    public HashMap<String, String> editing_object = new HashMap<>();
    public ArrayList<String> timer = new ArrayList<>();

    private Database db;
    private SQLite sql;
    private FileManager fm;

    public String serverFormat = "§8[§b§l»§r§8] ";
    public String noPermissionMessage = serverFormat + "§c¡No tienes permisos para ejecutar este comando!";

    @Override
    public void onEnable() {
        createFolder();

        fm = new FileManager();
        fm.saveDefaultConfig();

        db = new SQLite(this);
        db.load();

        loadCommands();
        loadEvents();

    }


    @Override
    public void onDisable() {
        sql = new SQLite(this);
        sql.closeConnection();
    }

    /**
     * @return Returns plugin Database
     */
    public Database getPluginDatabase() {
        return db;
    }

    public void loadCommands() {
        getCommand("pwarp").setExecutor(new pwarps.commands.c_pwarp());
    }

    public void loadEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new pwarps.events.joinEvent(), this);
        pm.registerEvents(new pwarps.events.clickEvent(), this);
        pm.registerEvents(new pwarps.events.chatEvent(), this);
        pm.registerEvents(new pwarps.events.playerInteract(), this);
    }

    public void createFolder() {
        File userdata = new File(getDataFolder() + File.separator + "database");
        if (!userdata.exists()) {
            userdata.mkdirs();
            getLogger().info("§c§lLOG> §fGenerado §euserdata §ffolder");
            getLogger().info("§c§lLOG> §eUserdata §fgenerada correctamente");
        }
    }

    public String rep(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public List<String> repList(List<String> messages) {
        List<String> newList = new ArrayList<>();
        for (String line : messages) {
            newList.add(rep(line));
        }

        return newList;
    }

    /**
     * @return Return plugin's FileManager
     */
    public FileManager getFM() {
        return fm;
    }

    /**
     * @param name Name for Item
     * @param m    Material for Item
     * @param lore Lore for Item
     * @return Return custom ItemStack
     */
    public static ItemStack createItem(String name, Material m, ArrayList<String> lore) {
        ItemStack item = new ItemStack(m);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

}
