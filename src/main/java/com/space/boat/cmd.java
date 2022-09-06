//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.space.boat;

import com.space.boat.tools.ConfigManager;
import com.space.boat.tools.IString;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class cmd implements CommandExecutor {
    public cmd() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] strings) {
        if (strings.length <= 0) {
            return true;
        } else {
            Iterator var5;
            Player p;
            if (strings[0].equals("reload")) {
                Boat.plugin.reloadConfig();
                var5 = Bukkit.getOnlinePlayers().iterator();

                while(var5.hasNext()) {
                    p = (Player)var5.next();
                    Boat.data.put(p.getName() + ".Temp", ConfigManager.getConfig("data").getString(p.getName() + ".Temp"));
                    Boat.data.put(p.getName() + ".Oxy", ConfigManager.getConfig("data").getString(p.getName() + ".Oxy"));
                    Boat.data.put(p.getName() + ".Healthy", ConfigManager.getConfig("data").getString(p.getName() + ".Healthy"));
                    Boat.data.put(p.getName() + ".Psychology", ConfigManager.getConfig("data").getString(p.getName() + ".Psychology"));
                }

                Boat.itemlist = ConfigManager.getConfig("tech_item").getStringList("List");
                Boat.whitelist = ConfigManager.getConfig("whitelist").getStringList("List");
                Boat.read = ConfigManager.getConfig("room").getStringList("read");
                Boat.SpawnLoction = ConfigManager.getConfig("spawn").getStringList("Location");
                Boat.break_list = ConfigManager.getConfig("block").getStringList("break");
                Boat.place_list = ConfigManager.getConfig("block").getStringList("place");
                var5 = Boat.read.iterator();

                String itemnode;
                while(var5.hasNext()) {
                    itemnode = (String)var5.next();
                    Boat.permission.put(itemnode, ConfigManager.getConfig("room").getStringList(itemnode));
                }

                var5 = Boat.itemlist.iterator();

                while(var5.hasNext()) {
                    itemnode = (String)var5.next();
                    Boat.techitem_recipe.put(itemnode, ConfigManager.getConfig("tech_item").getStringList(itemnode + ".recipe"));
                }

                sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 插件已重载"));
                return true;
            } else {
                if (strings[0].equals("saveData")) {
                    Set<String> keys = Boat.data.keySet();
                    Iterator var29 = keys.iterator();

                    while(var29.hasNext()) {
                        String name = (String)var29.next();
                        ConfigManager.writeConfig("data", name, Boat.data.get(name));
                    }

                    sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 数据已保存"));
                    return true;
                } else {
                    String ss;
                    if (strings[0].equals("whitelist")) {
                        if (strings[1].equals("add")) {
                            if (strings.length >= 3 && strings.length <= 3) {
                                List<String> t = ConfigManager.getConfig("whitelist").getStringList("List");
                                t.add(strings[2]);
                                ConfigManager.writeConfig("whitelist", "List", t);
                                sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 添加成功！"));
                                return true;
                            }

                            sender.sendMessage("&e&l[寻隐|Boat] 请输入正确的玩家名字");
                            return true;
                        }

                        if (strings[1].equals("close")) {
                            Boat.IS_Whitelist = false;
                        }

                        if (strings[1].equals("open")) {
                            Boat.IS_Whitelist = true;
                            var5 = Bukkit.getOnlinePlayers().iterator();

                            while(var5.hasNext()) {
                                p = (Player)var5.next();
                                String name = p.getName();
                                if (!Boat.whitelist.contains(name)) {
                                    ss = "kick %player%";
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ss.replaceAll("%player%", name));
                                }
                            }
                        }
                    }
                    if (strings[0].equals("idcard")) {
                        if (strings.length == 1) {
                            var5 = Bukkit.getOnlinePlayers().iterator();

                            while(var5.hasNext()) {
                                p = (Player)var5.next();
                                List<String> lore = new ArrayList();
                                lore.add(IString.addColor("&b 所属舰船: &f长辉"));
                                lore.add(IString.addColor("&b 拥有者: &f" + p.getName()));
                                lore.add(IString.addColor("&b 权限等级: &f") + ConfigManager.getConfig("permission").getString(p.getName()));
                                ss = IString.addColor("&c身份卡");
                                ItemStack card = new ItemStack(Material.PAPER);
                                ItemMeta im = card.getItemMeta();
                                im.setLore(lore);
                                im.setDisplayName(ss);
                                im.setCustomModelData(10167);
                                card.setItemMeta(im);
                                p.getInventory().addItem(new ItemStack[]{card});
                                sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 身份卡发送完成"));
                            }

                            return true;
                        }

                        if (strings.length == 2 && sender instanceof Player) {
                            p = (Player)sender;
                            List<String> lore = new ArrayList();
                            lore.add(IString.addColor("&b 所属舰船: &f长辉"));
                            lore.add(IString.addColor("&b 拥有者: &f" + strings[1]));
                            lore.add(IString.addColor("&b 权限等级: &f") + ConfigManager.getConfig("permission").getString(strings[1]));
                            String name = IString.addColor("&c身份卡");
                            ItemStack card = new ItemStack(Material.PAPER);
                            ItemMeta im = card.getItemMeta();
                            im.setLore(lore);
                            im.setDisplayName(name);
                            im.setCustomModelData(10167);
                            card.setItemMeta(im);
                            p.getInventory().addItem(new ItemStack[]{card});
                            sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 身份卡发送完成"));
                            return true;
                        }

                        sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 命令参数错误|此命令只能由玩家执行"));
                    }

                    if (strings[0].equals("oxy")) {
                        if (strings.length >= 1) {
                            if (strings[1].equals("true")) {
                                Boat.oxy = true;
                                sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 已设置氧气系统生效"));
                            } else if (strings[1].equals("false")) {
                                Boat.oxy = false;
                                sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 已设置氧气系统失效"));
                            }
                        } else {
                            sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 缺失参数"));
                        }
                    }

                    if (strings[0].equals("gravity")) {
                        if (strings.length >= 1) {
                            if (strings[1].equals("true")) {
                                Boat.gravity = true;
                                sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 已设置重力系统生效"));
                            } else if (strings[1].equals("false")) {
                                Boat.gravity = false;
                                sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 已设置重力系统失效"));
                            }
                        } else {
                            sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 缺失参数"));
                        }
                    }

                    int zz;
                    int y1;
                    int z1;
                    if (strings[0].equals("door") && sender instanceof Player) {
                        p = (Player)sender;
                        if (strings.length == 1) {
                            if ((Boolean)Boat.isDoor.get(p.getName())) {
                                Boat.isDoor.put(p.getName(), false);
                                sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 已关闭设定模式"));
                            } else {
                                Boat.isDoor.put(p.getName(), true);
                                sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 已开启设定模式"));
                            }

                            return true;
                        } else {
                            if (strings.length == 2 && strings[1].equals("create")) {
                                if (Boat.DoorLoc.get(p.getName() + "1") != null && Boat.DoorLoc.get(p.getName() + "2") != null && Boat.DoorLoc.get(p.getName() + "3") != null) {
                                    int x1 = ((Location)Boat.DoorLoc.get(p.getName() + "1")).getBlockX();
                                    y1 = ((Location)Boat.DoorLoc.get(p.getName() + "1")).getBlockY();
                                    z1 = ((Location)Boat.DoorLoc.get(p.getName() + "1")).getBlockZ();
                                    zz = ((Location)Boat.DoorLoc.get(p.getName() + "2")).getBlockX();
                                    int y2 = ((Location)Boat.DoorLoc.get(p.getName() + "2")).getBlockY();
                                    int z2 = ((Location)Boat.DoorLoc.get(p.getName() + "2")).getBlockZ();
                                    int t;
                                    if (x1 > zz) {
                                        t = x1;
                                        x1 = zz;
                                        zz = t;
                                    }
                                    if (y1 > y2) {
                                        t = y1;
                                        y1 = y2;
                                        y2 = t;
                                    }
                                    if (z1 > z2) {
                                        t = z1;
                                        z1 = z2;
                                        z2 = t;
                                    }
                                    World world = ((Location)Boat.DoorLoc.get(p.getName() + "2")).getWorld();
                                    List<String> blocks = new ArrayList();
                                    List<String> pppp = new ArrayList();
                                    List<String> nnnn = new ArrayList();
                                    int i;
                                    int m;
                                    int n;
                                    for(i = x1; i <= zz; ++i) {
                                        for(m = y1; m <= y2; ++m) {
                                            for(n = z1; n <= z2; ++n) {
                                                String name = world.getBlockAt(i, m, n).getType().name();
                                                String ddd = i + "," + m + "," + n + "," + name;
                                                blocks.add(ddd);
                                            }
                                        }
                                    }

                                    i = ((Location)Boat.DoorLoc.get(p.getName() + "3")).getBlockX();
                                    m = ((Location)Boat.DoorLoc.get(p.getName() + "3")).getBlockY();
                                    n = ((Location)Boat.DoorLoc.get(p.getName() + "3")).getBlockZ();
                                    String name = i + "," + m + "," + n + "," + ((Location)Boat.DoorLoc.get(p.getName() + "3")).getWorld().getName();
                                    Boat.read.add(name);
                                    pppp.add("船员");
                                    nnnn.add(name);
                                    ConfigManager.writeConfig("room", "read", Boat.read);
                                    ConfigManager.writeConfig("room", name, pppp);
                                    ConfigManager.writeConfig("door", name, blocks);
                                    Boat.read = ConfigManager.getConfig("room").getStringList("read");
                                    Iterator var37 = Boat.read.iterator();

                                    while(var37.hasNext()) {
                                        String key = (String)var37.next();
                                        Boat.permission.put(key, ConfigManager.getConfig("room").getStringList(key));
                                    }

                                    sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 创建成功"));
                                } else {
                                    sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 请选择完整的点"));
                                }
                            }

                            return true;
                        }
                    } else if (strings[0].equals("open") && sender instanceof Player) {
                        if (strings.length == 1) {
                            sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 缺少参数，请输入玩家名字"));
                            return true;
                        } else {
                            if (strings.length == 2) {
                                p = (Player)sender;
                                p = Bukkit.getPlayer(strings[1]);
                                if (p == null) {
                                    return true;
                                }

                                p.openInventory(p.getInventory());
                                sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 已打开玩家背包"));
                            }

                            return true;
                        }
                    } else if (sender instanceof Player && strings.length > 1) {
                        if (strings[0].equals("spawn") && strings[1].equals("create")) {
                            this.write_spawn((Player)sender);
                            return true;
                        } else {
                            if (strings[0].equals("spawn") && strings[1].equals("start")) {
                                Boat.IS_start = true;
                                var5 = Bukkit.getOnlinePlayers().iterator();

                                while(var5.hasNext()) {
                                    p = (Player)var5.next();
                                    tpxmc(p);
                                }
                            } else if (strings[0].equals("spawn") && strings[1].equals("break")) {
                                var5 = Bukkit.getOnlinePlayers().iterator();

                                while(true) {
                                    while(var5.hasNext()) {
                                        p = (Player)var5.next();
                                        y1 = p.getLocation().getBlockX();
                                        z1 = p.getLocation().getBlockY();
                                        zz = p.getLocation().getBlockZ();
                                        if (z1 != 107 && z1 != 99) {
                                            this.breakGlass(p, y1, z1, zz);
                                        } else {
                                            this.breakGlass(p, y1, z1 - 1, zz);
                                        }
                                    }

                                    return true;
                                }
                            }

                            return true;
                        }
                    } else {
                        sender.sendMessage("参数错误！");
                        return true;
                    }
                }
            }
        }
    }

    public void write_spawn(Player p) {
        Double x = p.getPlayer().getLocation().getX();
        Double y = p.getPlayer().getLocation().getY();
        Double z = p.getPlayer().getLocation().getZ();
        World w = p.getPlayer().getLocation().getWorld();

        assert w != null;

        String l = x + "," + y + "," + z + "," + w.getName();
        Boat.SpawnLoction.add(l);
        ConfigManager.writeConfig("spawn", "Location", Boat.SpawnLoction);
        p.sendMessage(IString.addColor("&e&l[寻隐|Boat] 出生点创建成功"));
    }

    public static void tpxmc(Player p) {
        Iterator var1 = Boat.SpawnLoction.iterator();

        while(var1.hasNext()) {
            String sss = (String)var1.next();
            String[] ss = sss.split(",");
            double x2 = Double.parseDouble(ss[0]);
            double y2 = Double.parseDouble(ss[1]);
            double z2 = Double.parseDouble(ss[2]);
            String named = (String)Boat.SpawnLoction_Used.get(sss);
            if (named == null) {
                World world = Bukkit.getWorld(ss[3]);
                p.teleport(new Location(world, x2, y2, z2));
                Boat.SpawnLoction_Used.put(sss, p.getName());
                break;
            }
        }

    }

    private void breakGlass(Player p, int xx, int yy, int zz) {
        Bukkit.getScheduler().runTaskLater(Boat.getPlugin(), () -> {
            p.getWorld().getBlockAt(xx, yy + 2, zz).setType(Material.AIR);
        }, 20L);
        Bukkit.getScheduler().runTaskLater(Boat.getPlugin(), () -> {
            p.getWorld().getBlockAt(xx, yy + 1, zz).setType(Material.AIR);
        }, 40L);
        Bukkit.getScheduler().runTaskLater(Boat.getPlugin(), () -> {
            p.getWorld().getBlockAt(xx, yy, zz).setType(Material.AIR);
        }, 60L);
        Bukkit.getScheduler().runTaskLater(Boat.getPlugin(), () -> {
            for(int i = 0; i < 4; ++i) {
                if (i == 0 && p.getWorld().getBlockAt(xx + 1, yy, zz).getType().name().equals("GLASS")) {
                    p.getWorld().getBlockAt(xx + 1, yy, zz).setType(Material.AIR);
                    p.getWorld().getBlockAt(xx + 1, yy + 1, zz).setType(Material.AIR);
                    p.getWorld().getBlockAt(xx + 1, yy + 2, zz).setType(Material.AIR);
                    p.playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0F, 1.0F);
                }

                if (i == 1 && p.getWorld().getBlockAt(xx - 1, yy, zz).getType().name().equals("GLASS")) {
                    p.getWorld().getBlockAt(xx - 1, yy, zz).setType(Material.AIR);
                    p.getWorld().getBlockAt(xx - 1, yy + 1, zz).setType(Material.AIR);
                    p.getWorld().getBlockAt(xx - 1, yy + 2, zz).setType(Material.AIR);
                    p.playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0F, 1.0F);
                }

                if (i == 2 && p.getWorld().getBlockAt(xx, yy, zz + 1).getType().name().equals("GLASS")) {
                    p.getWorld().getBlockAt(xx, yy, zz + 1).setType(Material.AIR);
                    p.getWorld().getBlockAt(xx, yy + 1, zz + 1).setType(Material.AIR);
                    p.getWorld().getBlockAt(xx, yy + 2, zz + 1).setType(Material.AIR);
                    p.playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0F, 1.0F);
                }

                if (i == 3 && p.getWorld().getBlockAt(xx, yy, zz - 1).getType().name().equals("GLASS")) {
                    p.getWorld().getBlockAt(xx, yy, zz - 1).setType(Material.AIR);
                    p.getWorld().getBlockAt(xx, yy + 1, zz - 1).setType(Material.AIR);
                    p.getWorld().getBlockAt(xx, yy + 2, zz - 1).setType(Material.AIR);
                    p.playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0F, 1.0F);
                }
            }

        }, 100L);
    }
}
