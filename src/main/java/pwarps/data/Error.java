package pwarps.data;

import pwarps.main.Main;

import java.util.logging.Level;

public class Error {
    public static void close(Main plugin, Exception ex) {
        plugin.getLogger().log(Level.SEVERE, "§c§lLOG> §FError al cortar la conexion con la base de datos", ex);
    }
}
