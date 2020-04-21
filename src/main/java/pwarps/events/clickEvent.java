package pwarps.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import pwarps.main.Main;
import pwarps.menus.editGUI;
import pwarps.menus.listGUI;
import pwarps.menus.mainGUI;
import pwarps.objects.PlayerWarps;
import pwarps.objects.Warp;
import pwarps.objects.Warp.State;

import java.util.ArrayList;

public class clickEvent implements Listener {
    private static final Main plugin = Main.getPlugin(Main.class);
    private final String tp_msg = "§fTe has teletransportado a: §8%WARP%";

    /**
     * @param e Event when a player click something
     */
    @EventHandler
    public void invClick(InventoryClickEvent e) {

        Player p = (Player) e.getWhoClicked();

        if (e.getSlotType() == null) {
            return;
        }
        if (e.getClickedInventory() == null) {
            return;
        }

        if (e.getClickedInventory().getName().contains(mainGUI.title)) {
            if (e.getCurrentItem().getType() == Material.BOOK) {

                if (e.getCurrentItem().getItemMeta().getDisplayName().equals(mainGUI.warps_list)) {
                    listGUI gui = new listGUI();
                    p.openInventory(gui.getGUI(p.getName()));
                }
            }
            if (e.getCurrentItem().getType() == Material.COMMAND) {

                if (e.getCurrentItem().getItemMeta().getDisplayName().equals(mainGUI.commands)) {
                    p.closeInventory();
                    p.performCommand("pw help");
                }
            }

            e.setCancelled(true);
        }

        if (e.getClickedInventory().getName().contains(editGUI.title)) {
            e.setCancelled(true);
            String[] n = e.getClickedInventory().getName().split(editGUI.title);
            String name = n[1];
            Warp wp = plugin.playerwarps.get(p.getName()).getWarp(name);

            if (e.getCurrentItem().getType() == Material.WOOL) {
                plugin.playerwarps.get(p.getName()).getWarp(name).setState(Warp.State.PRIVATE);
                Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                    @Override
                    public void run() {
                        plugin.getPluginDatabase().updateWarpState(p.getName(), wp, Warp.State.PRIVATE);
                    }
                });
                p.closeInventory();

            }
            if (e.getCurrentItem().getType() == Material.BARRIER) {
                plugin.playerwarps.get(p.getName()).getWarp(name).setState(Warp.State.PUBLIC);
                Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                    @Override
                    public void run() {
                        plugin.getPluginDatabase().updateWarpState(p.getName(), wp, Warp.State.PUBLIC);
                    }
                });
                p.closeInventory();

            }
            if (e.getCurrentItem().getType() == Material.BOOK_AND_QUILL) {
                if (!plugin.editing_chat.containsKey(p.getName())) {
                    plugin.editing_chat.put(p.getName(), name);
                    p.closeInventory();
                    p.sendMessage("Escribe la nueva descripción por el chat (tienes 30s):");
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        public void run() {
                            if (plugin.editing_chat.containsKey(p.getName())) {
                                p.sendMessage("¡Se te ha acabado el tiempo rata!");
                                plugin.editing_chat.remove(p.getName());
                            }
                        }
                    }, 20 * 30);

                }
            }
            if (e.getCurrentItem().getType() == wp.getItem().getType()) {
                if (!plugin.editing_object.containsKey(p.getName())) {
                    plugin.editing_object.put(p.getName(), name);
                    p.closeInventory();
                    p.sendMessage("Haz clic derecho con el item que quieres usar (tienes 30s):");
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        public void run() {
                            if (plugin.editing_object.containsKey(p.getName())) {
                                p.sendMessage("¡Se te ha acabado el tiempo rata!");
                                plugin.editing_object.remove(p.getName());
                            }
                        }
                    }, 20 * 30);

                }

            }
            if (e.getCurrentItem().getType() == Material.REDSTONE_BLOCK) {
                p.closeInventory();
            }

        }

        if (e.getClickedInventory().getName().contains(listGUI.title)) {
            e.setCancelled(true);
            int slot = e.getSlot();
            String[] player = e.getClickedInventory().getName().split(listGUI.title);
            String target = player[1];

            if (!target.equals(p.getName())) {
                if (!plugin.playerwarps.containsKey(target)) {
                    ArrayList<Warp> warps = plugin.getPluginDatabase().getWarpList(target);
                    plugin.playerwarps.put(target, new PlayerWarps(warps));
                    try {
                        PlayerWarps wp = plugin.playerwarps.get(target);
                        plugin.playerwarps.remove(target);
                        Warp w = wp.getByID(slot);
                        plugin.playerwarps.remove(target);
                        if (w.getState() == State.PUBLIC) {
                            p.closeInventory();
                            if (!p.hasPermission(plugin.getFM().getConfig().getString("perms.tp_bypass"))) {
                                if (!plugin.timer.contains(p.getName())) {
                                    Location actual = p.getLocation();
                                    plugin.timer.add(p.getName());
                                    p.sendMessage("¡Teletransportando no te muevas!");
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                        public void run() {
                                            if (actual.getX() == p.getLocation().getX() && actual.getY() == p.getLocation().getY()
                                                    && actual.getZ() == p.getLocation().getZ()) {
                                                p.teleport(wp.getWarp(w.getName()).getLocation());
                                            } else {
                                                p.sendMessage("¡Te has movido, tp cancelado!");
                                            }
                                        }
                                    }, 20 * 3);
                                    plugin.timer.remove(p.getName());
                                }
                            } else {
                                p.teleport(wp.getWarp(w.getName()).getLocation());
                            }
                        }

                    } catch (Exception ex) {
                        System.err.println(ex.getMessage());
                    }

                } else {
                    PlayerWarps wp = plugin.playerwarps.get(target);
                    Warp w;
                    try {
                        w = wp.getByID(slot);
                        if (w.getState() == State.PUBLIC) {
                            p.closeInventory();
                            if (!p.hasPermission(plugin.getFM().getConfig().getString("perms.tp_bypass"))) {
                                if (!plugin.timer.contains(p.getName())) {
                                    Location actual = p.getLocation();
                                    plugin.timer.add(p.getName());
                                    p.sendMessage("¡Teletransportando no te muevas!");
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                        public void run() {
                                            if (actual.getX() == p.getLocation().getX() && actual.getY() == p.getLocation().getY()
                                                    && actual.getZ() == p.getLocation().getZ()) {
                                                p.teleport(wp.getWarp(w.getName()).getLocation());
                                            } else {
                                                p.sendMessage("¡Te has movido, tp cancelado!");
                                            }
                                        }
                                    }, 20 * 3);
                                    plugin.timer.remove(p.getName());
                                }
                            } else {
                                p.teleport(wp.getWarp(w.getName()).getLocation());
                            }
                        }
                    } catch (Exception ex) {

                    }


                }


            } else {
                try {
                    PlayerWarps wp = plugin.playerwarps.get(p.getName());
                    Warp w = wp.getByID(slot);
                    p.teleport(w.getLocation());
                    p.sendMessage(tp_msg.replaceAll("%WARP%", w.getName()));
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }


        }

    }
}
