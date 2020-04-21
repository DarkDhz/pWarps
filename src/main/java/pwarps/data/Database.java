package pwarps.data;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pwarps.main.Main;
import pwarps.objects.PlayerWarps;
import pwarps.objects.Warp;
import pwarps.objects.Warp.State;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public abstract class Database {
    Main plugin;
    Connection connection;
    public String warps_table = "warps";

    public Database(Main instance) {
        plugin = instance;
    }

    public abstract void load();

    public abstract Connection getSQLConnection();


    public void savePlayerData(String p, Warp pw) {
        try {
            PreparedStatement st = connection.prepareStatement("INSERT INTO " + warps_table + " VALUES (?,?,?,?,?,?)");

            st.setString(1, p);
            st.setString(2, serializeLocation(pw.getLocation()));
            st.setString(3, pw.getName());
            st.setString(4, pw.getDesc());
            st.setString(5, serializeItem(pw.getItem()));
            st.setString(6, serializeState(pw.getState()));
            st.executeUpdate();
            st.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private String serializeState(State s) {
        String toReturn = "";
        switch (s) {
            case PUBLIC:
                toReturn = "public";
                break;
            case PRIVATE:
                toReturn = "private";
                break;
            default:
                break;
        }
        return toReturn;
    }

    private State getState(String s) {
        if (s == null) {
            return null;
        }
        if (s.equalsIgnoreCase("private")) {
            return State.PRIVATE;
        } else {
            return State.PUBLIC;
        }
    }

    private String serializeLocation(Location loc) {
        return loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";" + loc.getPitch();
    }

    private Location getLocation(String loc) {
        if (loc == null) {
            return null;
        }

        String[] a = loc.split(";");
        String world = a[0];
        double x = Double.valueOf(a[1]);
        double y = Double.valueOf(a[2]);
        double z = Double.valueOf(a[3]);
        float yaw = Float.valueOf(a[4]);
        float pitch = Float.valueOf(a[5]);

        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    private String serializeItem(ItemStack m) {
        return m.getType().toString() + ";" + m.getData().getData();

    }

    private ItemStack getItem(String i) {
        String[] ex = i.split(";");
        return new ItemStack(Material.getMaterial(ex[0]), 1, Short.valueOf(ex[1]));
    }

    public void deletePlayerData(String p, Warp wp) {
        try {
            PreparedStatement st = connection.prepareStatement("DELETE FROM " + warps_table + " WHERE user=? AND name=?");

            st.setString(1, p);
            st.setString(2, wp.getName());
            st.executeUpdate();
            st.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateWarpState(String p, Warp wp, State e) {
        try {
            PreparedStatement st = connection.prepareStatement("UPDATE " + warps_table + " SET state = '" + serializeState(e) + "' WHERE user=? AND name=?");

            st.setString(1, p);
            st.setString(2, wp.getName());
            st.executeUpdate();
            st.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void updateWarpDesc(String p, Warp wp, String desc) {
        try {
            PreparedStatement st = connection.prepareStatement("UPDATE " + warps_table + " SET desc = '" + desc + "' WHERE user=? AND name=?");


            st.setString(1, p);
            st.setString(2, wp.getName());
            st.executeUpdate();
            st.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void updateWarpItem(String p, Warp wp, ItemStack item) {
        try {
            PreparedStatement st = connection.prepareStatement("UPDATE " + warps_table + " SET item = '" + serializeItem(item) + "' WHERE user=? AND name=?");

            st.setString(1, p);
            st.setString(2, wp.getName());
            st.executeUpdate();
            st.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void updateWarpLocation(String p, Warp wp, Location loc) {
        try {
            PreparedStatement st = connection.prepareStatement("UPDATE " + warps_table + " SET location='" + serializeLocation(loc) + "'? WHERE user=? AND name=?");

            st.setString(1, p);
            st.setString(2, wp.getName());
            st.executeUpdate();
            st.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public PlayerWarps getPlayerData(String p) {
        ArrayList<Warp> warps = new ArrayList<Warp>();
        try {
            PreparedStatement st = connection.prepareStatement("SELECT name, location, desc, item, state FROM " + warps_table + " WHERE user=?");

            st.setString(1, p);
            ResultSet result = st.executeQuery();
            while (result.next()) {
                String name = result.getString(1);
                String location = result.getString(2);
                String desc = result.getString(3);
                String item = result.getString(4);
                String state = result.getString(5);
                warps.add(new Warp(getLocation(location), name, desc, getItem(item), getState(state)));
            }
            close(st, result);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return new PlayerWarps(warps);
    }

    public boolean playerWarpsExist(String p) {
        try {
            PreparedStatement st = connection.prepareStatement("SELECT name FROM " + warps_table + " WHERE user=?");

            st.setString(1, p);
            ResultSet result = st.executeQuery();
            if (result.next()) {
                close(st, result);
                return true;
            }
            close(st, result);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void close(PreparedStatement ps, ResultSet rs) {
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
            Error.close(plugin, ex);
        }
    }

    public ArrayList<Warp> getWarpList(String p) {
        ArrayList<Warp> warps = new ArrayList<Warp>();
        try {
            PreparedStatement st = connection.prepareStatement("SELECT name, location, desc, item, state FROM " + warps_table + " WHERE user=?");

            st.setString(1, p);
            ResultSet result = st.executeQuery();
            while (result.next()) {
                String name = result.getString(1);
                String location = result.getString(2);
                String desc = result.getString(3);
                String item = result.getString(4);
                String state = result.getString(5);
                warps.add(new Warp(getLocation(location), name, desc, getItem(item), getState(state)));
            }
            close(st, result);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return warps;
    }
}
