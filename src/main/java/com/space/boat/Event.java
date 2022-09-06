//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.space.boat;

import com.space.boat.tools.ConfigManager;
import com.space.boat.tools.IString;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Event implements Listener {
    private static List<String> laststr = new ArrayList();

    public Event() {
    }

    @EventHandler
    public void Join(PlayerJoinEvent e) {
        List<String> PlayerList = ConfigManager.getConfig("first").getStringList("PlayerList");
        Boat.isDoor.put(e.getPlayer().getName(), false);
        if (!PlayerList.contains(e.getPlayer().getName())) {
            e.getPlayer().sendMessage(IString.addColor("&e&l[寻隐|Boat] &b欢迎来到陆地行舟 请等待游戏开始"));
            if (Boat.IS_start) {
                cmd.tpxmc(e.getPlayer());
            }

            String PlayerData_Temp = "36.90";
            String PlayerData_Oxy = "100";
            String PlayerData_Healthy = "100";
            String PlayerData_Psychology = "30";
            String per = "船员";
            ConfigManager.writeConfig("data", e.getPlayer().getName() + ".Temp", PlayerData_Temp);
            ConfigManager.writeConfig("data", e.getPlayer().getName() + ".Oxy", PlayerData_Oxy);
            ConfigManager.writeConfig("data", e.getPlayer().getName() + ".Healthy", PlayerData_Healthy);
            ConfigManager.writeConfig("data", e.getPlayer().getName() + ".Psychology", PlayerData_Psychology);
            ConfigManager.writeConfig("permission", e.getPlayer().getName(), per);
            Boat.data.put(e.getPlayer().getName() + ".Temp", PlayerData_Temp);
            Boat.data.put(e.getPlayer().getName() + ".Oxy", PlayerData_Oxy);
            Boat.data.put(e.getPlayer().getName() + ".Healthy", PlayerData_Healthy);
            Boat.data.put(e.getPlayer().getName() + ".Psychology", PlayerData_Psychology);
            PlayerList.add(e.getPlayer().getName());
            ConfigManager.writeConfig("first", "PlayerList", PlayerList);
            World world = Bukkit.getWorld("kaiwater1");
            e.getPlayer().teleport(new Location(world, 165.0, 117.0, 21.0));
        } else {
            Boat.data.put(e.getPlayer().getName() + ".Temp", ConfigManager.getConfig("data").getString(e.getPlayer().getName() + ".Temp"));
            Boat.data.put(e.getPlayer().getName() + ".Oxy", ConfigManager.getConfig("data").getString(e.getPlayer().getName() + ".Oxy"));
            Boat.data.put(e.getPlayer().getName() + ".Healthy", ConfigManager.getConfig("data").getString(e.getPlayer().getName() + ".Healthy"));
            Boat.data.put(e.getPlayer().getName() + ".Psychology", ConfigManager.getConfig("data").getString(e.getPlayer().getName() + ".Psychology"));
        }

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Boat.getPlugin(), () -> {
            Scoreboard Data = manager.getNewScoreboard();
            Objective obj = Data.registerNewObjective("Test", "dummy");
            obj.setDisplayName(IString.addColor("&6&l [ 数据面板 ]"));
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
            String name = IString.addColor("&bID: &r") + e.getPlayer().getName();
            String Oxy = IString.addColor("&b氧气值: &r") + (String)Boat.data.get(e.getPlayer().getName() + ".Oxy");
            String Healthy = IString.addColor("&b健康值: &r") + (String)Boat.data.get(e.getPlayer().getName() + ".Healthy");
            new DecimalFormat("#.0");
            obj.getScore(name).setScore(4);
            obj.getScore(Oxy).setScore(2);
            obj.getScore(Healthy).setScore(1);
            e.getPlayer().setScoreboard(Data);
        }, 0L, 20L);
    }

    @EventHandler
    public void onPlayerConnect(PlayerLoginEvent e) {
        String name = e.getPlayer().getName();
        if (!Boat.whitelist.contains(name) && Boat.IS_Whitelist) {
            e.disallow(Result.KICK_OTHER, "你不在白名单中\n请确认自己获得资格后联系管理理员");
        }

    }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        if (e != null) {
            if (e.getClickedBlock() != null) {
                if (e.getClickedBlock().getBlockData().getMaterial().name().equalsIgnoreCase("NOTE_BLOCK")) {
                    if (e.getItem() == null) {
                        return;
                    }

                    if (e.getItem().getItemMeta().getLore() == null) {
                        return;
                    }

                    e.setCancelled(true);
                    Block block = e.getClickedBlock();
                    if (e.getAction().name().equals("RIGHT_CLICK_BLOCK")) {
                        int x = block.getLocation().getBlockX();
                        int y = block.getLocation().getBlockY();
                        int z = block.getLocation().getBlockZ();
                        String loc = x + "," + y + "," + z + "," + block.getWorld().getName();
                        String T_per = (String)e.getItem().getItemMeta().getLore().get(2);
                        String per = T_per.substring(T_per.length() - 2, T_per.length());
                        if (Boat.read.contains(loc)) {
                            if (((List)Boat.permission.get(loc)).contains(per)) {
                                List<String> blocks = ConfigManager.getConfig("door").getStringList(loc);
                                Iterator var10 = blocks.iterator();

                                while(var10.hasNext()) {
                                    String s = (String)var10.next();
                                    String[] ss = s.split(",");
                                    int xx = Integer.parseInt(ss[0]);
                                    int yy = Integer.parseInt(ss[1]);
                                    int zz = Integer.parseInt(ss[2]);
                                    e.getPlayer().getWorld().getBlockAt(xx, yy, zz).setType(Material.AIR);
                                }

                                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0F, 1.0F);
                                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_DRIPSTONE_BLOCK_FALL, 1.0F, 1.0F);
                                Bukkit.getScheduler().runTaskLater(Boat.getPlugin(), () -> {
                                    List<String> blockss = ConfigManager.getConfig("door").getStringList(loc);
                                    Iterator var3 = blockss.iterator();

                                    while(var3.hasNext()) {
                                        String s = (String)var3.next();
                                        String[] ss = s.split(",");
                                        int xx = Integer.parseInt(ss[0]);
                                        int yy = Integer.parseInt(ss[1]);
                                        int zz = Integer.parseInt(ss[2]);
                                        e.getPlayer().getWorld().getBlockAt(xx, yy, zz).setType(Material.valueOf(ss[3]));
                                    }

                                }, 60L);
                                e.getPlayer().sendMessage(IString.addColor("&b[长辉] &r身份验证通过"));
                            } else {
                                e.getPlayer().sendMessage(IString.addColor("&b[长辉] &r权限不足"));
                            }
                        }
                    }
                }

            }
        }
    }

    @EventHandler
    public void PlayerInteract(PlayerInteractEvent e) {
        if ((Boolean)Boat.isDoor.get(e.getPlayer().getName())) {
            e.setCancelled(true);
            if (e.getItem() != null && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("1111")) {
                Boat.DoorLoc.put(e.getPlayer().getName() + "3", e.getClickedBlock().getLocation());
                e.getPlayer().sendMessage(IString.addColor("&e&l[寻隐|Boat] &b已经选择读卡器位置"));
                return;
            }

            if (e.getAction().name().equals("RIGHT_CLICK_BLOCK")) {
                Boat.DoorLoc.put(e.getPlayer().getName() + "2", e.getClickedBlock().getLocation());
                e.getPlayer().sendMessage(IString.addColor("&e&l[寻隐|Boat] &b第二个坐标点已经选择"));
                return;
            }

            if (e.getAction().name().equals("LEFT_CLICK_BLOCK")) {
                Boat.DoorLoc.put(e.getPlayer().getName() + "1", e.getClickedBlock().getLocation());
                e.getPlayer().sendMessage(IString.addColor("&e&l[寻隐|Boat] &b第一个坐标点已经选择"));
                return;
            }
        }

    }

    @EventHandler
    public void Break(BlockBreakEvent e) {
        if (!e.getPlayer().isOp()) {
            String name = e.getBlock().getType().name();
            if (!Boat.break_list.contains(name)) {
                e.setCancelled(true);
            }

        }
    }

    @EventHandler
    public void place(BlockPlaceEvent e) {
        if (!e.getPlayer().isOp()) {
            String name = e.getBlock().getType().name();
            if (!Boat.place_list.contains(name)) {
                e.setCancelled(true);
            }

        }
    }
}
