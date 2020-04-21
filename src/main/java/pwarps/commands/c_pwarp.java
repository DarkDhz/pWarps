package pwarps.commands;

/**
 * @author blazes
 */

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pwarps.main.Main;
import pwarps.menus.editGUI;
import pwarps.menus.listGUI;
import pwarps.menus.mainGUI;
import pwarps.objects.PlayerWarps;
import pwarps.objects.Warp;

import java.util.ArrayList;
import java.util.List;


public class c_pwarp implements CommandExecutor {
    private static final Main plugin = Main.getPlugin(Main.class);
    private final String noperms = plugin.noPermissionMessage;
    private final int name_lenght = plugin.getFM().getConfig().getInt("max.name");
    private final int desc_lenght = plugin.getFM().getConfig().getInt("max.desc");
    private final List<String> blocked = plugin.getFM().getConfig().getStringList("blocked");


    /**
     *
     * @param sender Object who send the message
     * @param command Basic Command
     * @param label
     * @param args Arguments of Command
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                mainGUI gui = new mainGUI();
                p.openInventory(gui.getGUI(p));
            } else {
                if (args[0].equalsIgnoreCase("create")) {
                    if (args.length == 2) {
                        if (!(args[1].length() > name_lenght)) {
                            if (!blocked.contains(p.getWorld().getName())) {
                                int sp = plugin.getFM().getConfig().getInt("max_wp.sp");
                                int galaxy = plugin.getFM().getConfig().getInt("max_wp.galaxy");
                                int hero = plugin.getFM().getConfig().getInt("max_wp.hero");
                                int normal = plugin.getFM().getConfig().getInt("max_wp.normal");

                                if (p.hasPermission(plugin.getFM().getConfig().getString("perms.unlimited"))) {
                                    this.setupWarp(p, args[1]);
                                } else if (p.hasPermission(plugin.getFM().getConfig().getString("perms.sp"))) {
                                    if (plugin.playerwarps.get(p.getName()).getList().size() < sp) {
                                        this.setupWarp(p, args[1]);
                                    } else {
                                        this.exceptionError(p, "¡Has alcanzado el maximo número de Warps!");
                                    }
                                } else if (p.hasPermission(plugin.getFM().getConfig().getString("perms.galaxy"))) {
                                    if (plugin.playerwarps.get(p.getName()).getList().size() < galaxy) {
                                        this.setupWarp(p, args[1]);
                                    } else {
                                        this.exceptionError(p, "¡Has alcanzado el maximo número de Warps!");
                                    }
                                } else if (p.hasPermission(plugin.getFM().getConfig().getString("perms.hero"))) {
                                    if (plugin.playerwarps.get(p.getName()).getList().size() < hero) {
                                        this.setupWarp(p, args[1]);
                                    } else {
                                        this.exceptionError(p, "¡Has alcanzado el maximo número de Warps!");
                                    }
                                } else {
                                    if (plugin.playerwarps.get(p.getName()).getList().size() < normal) {
                                        this.setupWarp(p, args[1]);
                                    } else {
                                        this.exceptionError(p, "¡Has alcanzado el maximo número de Warps!");
                                    }
                                }
                            } else {
                                p.sendMessage("¡No se pueden crear un PlayerWarp en este mundo!s");
                            }
                        } else {
                            this.exceptionError(p, "¡Has excedido el número de caracteres maximos!");
                        }
                    } else {
                        this.sintaxError(p, "create");
                    }

                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (args.length == 2) {
                        this.removeWarp(p, args[1]);
                    } else {
                        sintaxError(p, "remove");
                    }

                } else if (args[0].equalsIgnoreCase("help")) {
                    this.help(p);

                } else if (args[0].equalsIgnoreCase("edit")) {
                    if (args.length == 2) {
                        String name = args[1];
                        PlayerWarps pw = plugin.playerwarps.get(p.getName());
                        if (pw.existWarp(name)) {
                            editGUI gui = new editGUI();
                            p.openInventory(gui.getGUI(p, name));
                        } else {
                            this.exceptionError(p, "¡El Warp no existe!");
                        }
                    } else {
                        this.sintaxError(p, "edit");
                    }

                } else if (args[0].equalsIgnoreCase("list")) {
                    listGUI gui = new listGUI();
                    p.openInventory(gui.getGUI(p.getName()));

                } else if (args[0].equalsIgnoreCase("setlocation")) {
                    if (args.length == 2) {
                        String name = args[1];
                        PlayerWarps pw = plugin.playerwarps.get(p.getName());
                        if (pw.existWarp(name)) {
                            Location loc = p.getLocation();
                            pw.getWarp(name).setLocation(loc);

                            p.sendMessage("¡Se ha cambiado al localización del Warp!");
                            Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    plugin.getPluginDatabase().updateWarpLocation(p.getName(), pw.getWarp(name), loc);
                                }
                            });
                        } else {
                            this.exceptionError(p, "¡El Warp no existe!");
                        }
                    } else {
                        sintaxError(p, "location");
                    }

                } else {
                    String target;
                    switch (args.length) {
                        case 1:
                            target = args[0];
                            if (plugin.getPluginDatabase().playerWarpsExist(target)) {
                                listGUI gui = new listGUI();
                                p.openInventory(gui.getGUI(target));
                            } else {
                                this.exceptionError(p, "¡El jugador no existe!");
                            }

                            break;
                        case 2:
                            target = args[0];
                            String w_name = args[1];
                            if (!plugin.playerwarps.containsKey(target)) {
                                if (plugin.getPluginDatabase().playerWarpsExist(target)) {
                                    ArrayList<Warp> warps = plugin.getPluginDatabase().getWarpList(target);
                                    plugin.playerwarps.put(target, new PlayerWarps(warps));
                                    PlayerWarps pw = plugin.playerwarps.get(target);
                                    plugin.playerwarps.remove(target);

                                    if (pw.existWarp(w_name)) {
                                        if (!p.hasPermission(plugin.getFM().getConfig().getString("perms.tp_bypass"))) {
                                            Warp w = pw.getWarp(w_name);
                                            if (w.getState() == Warp.State.PUBLIC) {
                                                if (!plugin.timer.contains(p.getName())) {
                                                    Location actual = p.getLocation();
                                                    plugin.timer.add(p.getName());

                                                    p.sendMessage("¡Teletransportando no te muevas!");
                                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                                        public void run() {
                                                            if (actual.getX() == p.getLocation().getX() && actual.getY() == p.getLocation().getY()
                                                                    && actual.getZ() == p.getLocation().getZ()) {
                                                                p.teleport(pw.getWarp(w_name).getLocation());
                                                            } else {
                                                                p.sendMessage("¡Te has movido, tp cancelado!");
                                                            }
                                                        }
                                                    }, 20 * 3);
                                                    plugin.timer.remove(p.getName());
                                                }
                                            } else {
                                                p.sendMessage("¡Este Warp es privado!");
                                            }

                                        } else {
                                            p.teleport(pw.getWarp(w_name).getLocation());
                                        }
                                    } else {
                                        this.exceptionError(p, "¡El Warp no existe!");
                                    }
                                } else {
                                    this.exceptionError(p, "¡El jugador no existe!");
                                }

                            } else {
                                PlayerWarps pw = plugin.playerwarps.get(target);
                                if (pw.existWarp(w_name)) {
                                    if (!p.hasPermission(plugin.getFM().getConfig().getString("perms.tp_bypass"))) {
                                        Warp w = pw.getWarp(w_name);
                                        if (w.getState() == Warp.State.PUBLIC) {
                                            if (!plugin.timer.contains(p.getName())) {
                                                Location actual = p.getLocation();
                                                plugin.timer.add(p.getName());

                                                p.sendMessage("¡Teletransportando no te muevas!");
                                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                                    public void run() {
                                                        if (actual.getX() == p.getLocation().getX() && actual.getY() == p.getLocation().getY()
                                                                && actual.getZ() == p.getLocation().getZ()) {
                                                            p.teleport(pw.getWarp(w_name).getLocation());
                                                        } else {
                                                            p.sendMessage("¡Te has movido, tp cancelado!");
                                                        }
                                                    }
                                                }, 20 * 3);
                                                plugin.timer.remove(p.getName());
                                            }
                                        } else {
                                            p.sendMessage("¡Este Warp es privado!");
                                        }
                                    } else {
                                        p.teleport(pw.getWarp(w_name).getLocation());
                                    }
                                } else {
                                    this.exceptionError(p, "¡El Warp no existe!");
                                }
                            }

                            break;
                        default:
                            this.help(p);
                            break;
                    }
                }
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     *
     * @param p Player how create Warp
     * @param name Name of Warp
     */
    private void setupWarp(Player p, String name) {
        PlayerWarps pw = plugin.playerwarps.get(p.getName());
        try {
            pw.addWarp(p.getLocation(), name);
            this.createMessage(p, name);
            Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                @Override
                public void run() {
                    plugin.getPluginDatabase().savePlayerData(p.getName(), new Warp(p.getLocation(), name));
                }
            });
        } catch (Exception ex) {
            this.exceptionError(p, ex.getMessage());
        }
    }

    /**
     *
     * @param p Player how create Warp
     * @param name Name of Warp
     */
    private void removeWarp(Player p, String name) {
        PlayerWarps pw = plugin.playerwarps.get(p.getName());
        if (pw.existWarp(name)) {
            try {
                this.removeMessage(p, name);
                Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                    @Override
                    public void run() {
                        plugin.getPluginDatabase().deletePlayerData(p.getName(), pw.getWarp(name));
                    }
                });
                plugin.playerwarps.get(p.getName()).removeWarp(name);
            } catch (Exception ex) {
                this.exceptionError(p, ex.getMessage());
            }
        } else {
            this.exceptionError(p, "No existe el Warp! asd");
        }


    }

    /**
     *
     * @param sender Player to send Message
     */
    private void help(Player sender) {
        sender.sendMessage("");
        sender.sendMessage("§8» §f/pw create <WARP>");
        sender.sendMessage("§8» §f/pw remove <WARP>");
        sender.sendMessage("§8» §f/pw setlocation <WARP>");
        sender.sendMessage("§8» §f/pw help");
        sender.sendMessage("§8» §f/pw edit <WARP>");
        sender.sendMessage("§8» §f/pw list");
        sender.sendMessage("§8» §f/pw <PLAYER>");
        sender.sendMessage("§8» §f/pw <PLAYER> <WARP>");
        sender.sendMessage("");

    }

    /**
     *
     * @param sender Player to send Message
     * @param name Name of created Warp
     */
    private void createMessage(Player sender, String name) {
        sender.sendMessage("Warp created: " + name);
    }

    /**
     *
     * @param sender Player to send Message
     * @param name Name of deleted Warp
     */
    private void removeMessage(Player sender, String name) {
        sender.sendMessage("Warp deleted: " + name);
    }

    /**
     *
     * @param sender Player to send Message
     * @param message Exception message
     */
    private void exceptionError(Player sender, String message) {
        sender.sendMessage(message);
    }

    /**
     *
     * @param sender Player to send Message
     * @param op Option for the message
     */
    private void sintaxError(Player sender, String op) {
        switch (op) {
            case "create":
                sender.sendMessage("§cCorrect usage: /pwarp create <name>");
                break;
            case "remove":
                sender.sendMessage("§cCorrect usage: /pwarp remove <name>");
                break;
            case "edit":
                sender.sendMessage("§cCorrect usage: /pwarp edit <name>");
                break;
            case "location":
                sender.sendMessage("§cCorrect usage: /pwarp setlocation <name>");
                break;
            default:
                break;
        }
    }
}
