package pwarps.data;

import org.bukkit.Bukkit;
import pwarps.main.Main;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class SQLite extends Database {

    public static Main plugin = Main.getPlugin(Main.class);

    private final String dbname;

    public SQLite(Main instance) {
        super(instance);
        dbname = "core";
    }

    String warps
            = "CREATE TABLE IF NOT EXISTS `" + warps_table + "` (`user` TEXT, `location` TEXT, `name` TEXT, `desc` TEXT, `item` TEXT, `state` TEXT)";

    public Connection getSQLConnection() {
        File dataFolder = new File(plugin.getDataFolder() + File.separator + "database", dbname + ".db");
        if (!dataFolder.exists()) {
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Error de escritura: " + dbname + ".db");
            }
        }
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "Excepcion al iniciar database: ", ex);
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "¡Se requiere la libreria de SQLite!");
        }
        return null;
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
                Bukkit.getConsoleSender().sendMessage("§c§lLOG> §FConexion §aSQLite §fcerrada correctamente");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void load() {
        connection = getSQLConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(warps);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
