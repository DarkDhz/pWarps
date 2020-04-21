package pwarps.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pwarps.main.Main;
import pwarps.objects.PlayerWarps;
import pwarps.objects.Warp;

import java.util.ArrayList;

/**
 * @author blaze
 */
public class joinEvent implements Listener {
    private static final Main plugin = Main.getPlugin(Main.class);

    /**
     * @param e Event when a Player join's server
     */
    @EventHandler
    public void joinEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!plugin.playerwarps.containsKey(p.getName())) {
            ArrayList<Warp> warps = plugin.getPluginDatabase().getWarpList(p.getName());
            plugin.playerwarps.put(p.getName(), new PlayerWarps(warps));
            System.out.println("LOADING MAP!");
        } else {
            System.out.println("NORMAL LOGIN!");
        }
        plugin.editing_chat.remove(p.getName());
        plugin.editing_object.remove(p.getName());
    }
}
