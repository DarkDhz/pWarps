package pwarps.menus;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pwarps.main.Main;
import pwarps.objects.PlayerWarps;
import pwarps.objects.Warp;

import java.util.ArrayList;

/**
 * @author blaze
 */
public class listGUI {
    private static final Main plugin = Main.getPlugin(Main.class);
    public static String title = "§8Warps de §b";

    /**
     * @param p Player who open's the inventory
     * @return Return inventory
     */
    public Inventory getGUI(String p) {
        Inventory inv = Bukkit.createInventory(null, 9, title + p);
        if (!plugin.playerwarps.containsKey(p)) {
            ArrayList<Warp> warps = plugin.getPluginDatabase().getWarpList(p);
            plugin.playerwarps.put(p, new PlayerWarps(warps));
            PlayerWarps pw = plugin.playerwarps.get(p);
            for (int i = 0; i < pw.getList().size(); i++) {
                inv.setItem(i, warpItem(pw.getList().get(i)));
            }

        } else {
            PlayerWarps pw = plugin.playerwarps.get(p);
            for (int i = 0; i < pw.getList().size(); i++) {
                inv.setItem(i, warpItem(pw.getList().get(i)));
            }
        }

        return inv;
    }

    /**
     * @param warp Warp to show
     * @return Return custom ItemStack
     */
    public static ItemStack warpItem(Warp warp) {
        ItemStack item = warp.getItem();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§4§l» §8WARP: §b" + warp.getName());
        ArrayList<String> list = new ArrayList<String>();
        list.add("§1");
        list.add("§7" + warp.getDesc());
        list.add("§2");
        list.add("§fMundo: §e" + warp.getLocation().getWorld().getName());
        switch (warp.getState()) {
            case PUBLIC:
                list.add("§fEstado: §6Publico");
                break;
            case PRIVATE:
                list.add("§fEstado: §cPrivado");
                break;
        }
        list.add("§3");
        list.add("§e§l¡Clic para ir!§r");
        meta.setLore(list);
        item.setItemMeta(meta);
        return item;
    }
}
