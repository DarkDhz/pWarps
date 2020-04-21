package pwarps.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import pwarps.main.Main;
import pwarps.menus.editGUI;
import pwarps.objects.Warp;

/**
 * @author blaze
 */
public class playerInteract implements Listener {
    private static final Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void interact(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (plugin.editing_object.containsKey(p.getName())) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR && p.getItemInHand().getType() != Material.AIR) {
                ItemStack item = p.getItemInHand();
                this.update(p, item);
            }
        }
    }

    private void update(Player p, ItemStack item) {
        Warp w = plugin.playerwarps.get(p.getName()).getWarp(plugin.editing_object.get(p.getName()));
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                plugin.getPluginDatabase().updateWarpItem(p.getName(), w, item);
            }
        });
        plugin.playerwarps.get(p.getName()).getWarp(w.getName()).setItem(item);
        editGUI gui = new editGUI();
        p.openInventory(gui.getGUI(p, plugin.playerwarps.get(p.getName()).getWarp(plugin.editing_object.get(p.getName())).getName()));
        plugin.editing_object.remove(p.getName());
    }
}
