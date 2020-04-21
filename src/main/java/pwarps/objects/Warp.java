package pwarps.objects;

/**
 * @author blaze
 */

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

public class Warp implements Serializable {
    private final String name;
    private String desc;
    private Location loc;

    static public enum State {PUBLIC, PRIVATE}

    ;
    private State currentState;
    ItemStack item;

    /**
     * @param l Coords of warp
     */

    public Warp(Location l) {
        this.loc = l;
        this.name = "Not set";
        this.desc = "Cambia la descripción con /pw edit!";
        this.currentState = State.PUBLIC;
        item = new ItemStack(Material.COMPASS);
    }

    /**
     * @param l    Coords of Warp
     * @param name Name of Warp
     */
    public Warp(Location l, String name) {
        this.loc = l;
        this.currentState = State.PUBLIC;
        this.name = name;
        this.desc = "Cambia la descripción con /pw edit!";
        item = new ItemStack(Material.COMPASS);
    }

    /**
     * @param l    Coords of Warp
     * @param name Name of Warp
     * @param desc Description of Warp
     */
    public Warp(Location l, String name, String desc) {
        this.loc = l;
        this.currentState = State.PUBLIC;
        this.name = name;
        this.desc = desc;
        item = new ItemStack(Material.COMPASS);
    }

    /**
     * @param l    Coords of Warp
     * @param name Name of Warp
     * @param desc Description of Warp
     * @param i    Item of Warp
     */
    public Warp(Location l, String name, String desc, ItemStack i) {
        this.loc = l;
        this.currentState = State.PUBLIC;
        this.name = name;
        this.desc = desc;
        item = i;
    }

    /**
     * @param l    Coords of Warp
     * @param name Name of Warp
     * @param desc Description of Warp
     * @param i    Item of Warp
     * @param e    State of Warp
     */
    public Warp(Location l, String name, String desc, ItemStack i, State e) {
        this.loc = l;
        this.currentState = e;
        this.name = name;
        this.desc = desc;
        item = i;
    }

    /**
     * @param e New State for Warp
     */
    public void setState(State e) {
        currentState = e;
    }

    /**
     * @param l New Location for Warp
     */
    public void setLocation(Location l) {
        loc = l;
    }

    /**
     * @param desc Set Description for Warp
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @param i Set ItemStack for Warp menu
     */
    public void setItem(ItemStack i) {
        this.item = i;
    }

    /**
     * @return Return Warp's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return Return Warp's Location
     */
    public Location getLocation() {
        return this.loc;
    }

    /**
     * @return Return Warp's Description
     */
    public String getDesc() {
        return this.desc;
    }

    /**
     * @return Return Warp's State
     */
    public State getState() {
        return this.currentState;
    }

    /**
     * @return Returns Warp Slot ItemStack
     */
    public ItemStack getItem() {
        return this.item;
    }

    /**
     * @return Class to String
     */
    @Override
    public String toString() {
        return name;
    }
}
