package pwarps.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pwarps.main.Main;
import pwarps.menus.editGUI;
import pwarps.objects.Warp;

/**
 * @author blaze
 */
public class chatEvent implements Listener {
    private static final Main plugin = Main.getPlugin(Main.class);
    private final int desc_lenght = plugin.getFM().getConfig().getInt("max.desc");

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (plugin.editing_chat.containsKey(p.getName())) {
            e.setCancelled(true);
            String newDesc = (String) e.getMessage().replaceAll("&l", "").replaceAll("&k", "").replaceAll("&m", "").replaceAll("&n", "").replaceAll("&o", "");
            if (newDesc.length() > desc_lenght) {
                p.sendMessage("¡La descripción es demasiado larga!");
            } else {
                update(p, newDesc);
            }

        }
    }

    private void update(Player p, String desc) {
        Warp w = plugin.playerwarps.get(p.getName()).getWarp(plugin.editing_chat.get(p.getName()));
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                plugin.getPluginDatabase().updateWarpDesc(p.getName(), w, desc);
            }
        });
        plugin.playerwarps.get(p.getName()).getWarp(w.getName()).setDesc(desc);
        editGUI gui = new editGUI();
        p.openInventory(gui.getGUI(p, plugin.playerwarps.get(p.getName()).getWarp(plugin.editing_chat.get(p.getName())).getName()));
        plugin.editing_chat.remove(p.getName());
    }
}
