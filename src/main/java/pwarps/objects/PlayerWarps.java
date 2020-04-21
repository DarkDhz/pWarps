package pwarps.objects;

import org.bukkit.Location;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author blaze
 */

public class PlayerWarps implements Serializable {

    private ArrayList<Warp> warps;

    public PlayerWarps() {
        warps = new ArrayList<Warp>();
    }

    /**
     * @param w Init with list
     */
    public PlayerWarps(ArrayList<Warp> w) {
        warps = w;
    }

    /**
     * @return Returns Warp List
     */
    public ArrayList<Warp> getList() {
        return this.warps;
    }

    /**
     * @param loc  Coords of player
     * @param name Name for the warp
     */
    public void addWarp(Location loc, String name) throws Exception {
        if (!existWarp(name)) {
            warps.add(new Warp(loc, name));
        } else {
            throw new Exception("¡Este warp ya existe!");
        }
    }

    public void addObjectWarp(Warp w) throws Exception {
        if (!existWarp(w.getName())) {
            warps.add(w);
        } else {
            throw new Exception("¡Este warp ya existe!");
        }
    }

    /**
     * @param name Name of warp to delete
     * @throws Exception Warp doesn't exist
     */
    public void removeWarp(String name) throws Exception {
        if (existWarp(name)) {
            warps.remove(getWarp(name));
        } else {
            throw new Exception("¡Este warp no existe!");
        }
    }

    /**
     * @param name Name of Warp
     * @return Return Warp Object
     */
    public Warp getWarp(String name) {
        for (Warp wp : warps) {
            if (wp.getName().equalsIgnoreCase(name)) {
                return wp;
            }
        }
        return null;
    }

    /**
     * @param id ID of Warp
     * @return Warp fund by ID
     * @throws Exception Warp not exist
     */
    public Warp getByID(int id) throws Exception {
        if (id >= warps.size()) {
            throw new Exception("¡Not Found!");
        } else {
            return warps.get(id);
        }
    }

    /**
     * @param n Warp Name
     * @return True if exist
     */
    public boolean existWarp(String n) {
        for (Warp warp : warps) {
            if (warp.getName().equalsIgnoreCase(n)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return Class toString
     */
    public String showInfo() {
        String toReturn = "";
        for (Warp wp : warps) {
            toReturn = toReturn + wp.toString() + "\n";
        }
        return toReturn;
    }

    /**
     * @return Class to String
     */
    @Override
    public String toString() {
        return warps.toString();
    }


}
