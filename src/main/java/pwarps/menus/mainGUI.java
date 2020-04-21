package pwarps.menus;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pwarps.main.Main;

public class mainGUI {
    private static final Main plugin = Main.getPlugin(Main.class);
    public static String title = "§4§l»§r §8PlayerWarps Principal";
    public static String warps_list = "§eLista Warps";
    public static String commands = "§eObtener Commandos";

    /**
     * @param p Player who open's the inventory
     * @return Return inventory
     */
    public Inventory getGUI(Player p) {
        Inventory inv = Bukkit.createInventory(p, 9, title);
        inv.setItem(2, listItem());
        inv.setItem(6, helpItem());


        return inv;
    }

    /**
     * @return Return custom ItemStack
     */
    public static ItemStack listItem() {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(warps_list);
        //meta.setLore(list);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack helpItem() {
        ItemStack item = new ItemStack(Material.COMMAND);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(commands);
        //meta.setLore(list);
        item.setItemMeta(meta);
        return item;
    }
}
