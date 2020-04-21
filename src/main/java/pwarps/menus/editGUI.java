package pwarps.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pwarps.main.Main;
import pwarps.objects.Warp;
import pwarps.objects.Warp.State;

import java.util.ArrayList;

/**
 * @author blaze
 */
public class editGUI {
    private static final Main plugin = Main.getPlugin(Main.class);
    public static String title = "§4§l» §8Edit: §b";
    private Warp w;

    /**
     * @param p    Player who open's the inventory
     * @param name Name of Warp
     * @return Return inventory
     */
    public Inventory getGUI(Player p, String name) {
        Inventory inv = Bukkit.createInventory(p, 9, title + name);
        w = plugin.playerwarps.get(p.getName()).getWarp(name);

        inv.setItem(0, Visible(w.getState()));
        inv.setItem(3, setDesc(w.getDesc()));
        inv.setItem(5, setItem(w.getItem()));
        inv.setItem(8, close());

        return inv;
    }

    /**
     * @param s WARP description
     * @return Return custom ItemStack
     */
    public static ItemStack Visible(State s) {
        ItemStack item = null;
        switch (s) {
            case PUBLIC:
                item = new ItemStack(Material.WOOL, 1, (short) 5);
                break;
            case PRIVATE:
                item = new ItemStack(Material.BARRIER);
                break;

        }

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§4§l» §eCambiar Visibilidad");
        ArrayList<String> list = new ArrayList<String>();
        list.add("§1");
        switch (s) {
            case PUBLIC:
                list.add("§fActual: §6Publico");
                break;
            case PRIVATE:
                list.add("§fActual: §cPrivado");
                break;
        }
        list.add("§2");
        list.add("§e§lClic para cambiar!");
        meta.setLore(list);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * @param desc WARP deacription
     * @return Return custom ItemStack
     */
    private static ItemStack setDesc(String desc) {
        ItemStack item = new ItemStack(Material.BOOK_AND_QUILL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§4§l» §eCambiar Descripción");
        ArrayList<String> list = new ArrayList<String>();
        list.add("§1");
        list.add("§eDescripción Actual:");
        list.add("§f" + desc);
        list.add("§2");
        meta.setLore(list);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * @return Return custom ItemStack
     */
    private static ItemStack close() {
        ItemStack item = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§4§l» §eSalir");
        ArrayList<String> list = new ArrayList<String>();
        list.add("§1");
        list.add("§c§l¡Clic para salir!");
        list.add("§2");
        meta.setLore(list);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * @param is Warp's ItemStack
     * @return Return custom ItemStack
     */
    private static ItemStack setItem(ItemStack is) {
        ItemStack item = is;
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§4§l» §eCambiar Item");
        ArrayList<String> list = new ArrayList<String>();
        list.add("§1");
        list.add("§eItem Actual:");
        list.add("§f" + is.getType().toString());
        list.add("§2");
        meta.setLore(list);
        item.setItemMeta(meta);
        return item;
    }


}
