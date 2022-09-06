//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.space.boat;

import com.space.boat.tools.ConfigManager;
import com.space.boat.tools.IString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class Boat extends JavaPlugin {
    public static Plugin plugin;
    public static Map<String, String> data = new HashMap();
    public static Map<String, Integer> time = new HashMap();
    public static List<String> whitelist = new ArrayList();
    public static List<String> read = new ArrayList();
    public static List<String> isOpen = new ArrayList();
    public static List<String> SpawnLoction = new ArrayList();
    public static Map<String, String> SpawnLoction_Used = new HashMap();
    public static Map<String, List<String>> permission = new HashMap();
    public static Map<String, Boolean> isDoor = new HashMap();
    public static Map<String, Location> DoorLoc = new HashMap();
    public static List<String> itemlist = new ArrayList();
    public static List<String> break_list = new ArrayList();
    public static List<String> place_list = new ArrayList();
    public static Map<String, List> techitem_recipe = new HashMap();
    public static boolean oxy = true;
    public static boolean gravity = true;
    public static boolean IS_start = false;
    public static boolean IS_Whitelist = true;

    public Boat() {
    }

    public void onEnable() {
        System.out.println("断点调试");
        this.runTimer();
        plugin = this;
        this.getLogger().info(IString.addColor("&e&l[寻隐|Boat] &e陆地行舟功能性插件已经开启"));
        this.InitFile();
        Bukkit.getPluginManager().registerEvents(new Event(), this);
        Bukkit.getPluginManager().registerEvents(new ia(), this);
        Bukkit.getPluginManager().registerEvents(new Technology(), this);
        Bukkit.getPluginCommand("yboat").setExecutor(new cmd());
        Bukkit.getPluginCommand("research").setExecutor(new Technology());
        ((PluginCommand)Objects.requireNonNull(Bukkit.getPluginCommand("yboat"))).setTabCompleter(new TabHandler());
    }

    public void onDisable() {
        Bukkit.resetRecipes();
        Set<String> keys = data.keySet();
        Iterator var2 = keys.iterator();

        while(var2.hasNext()) {
            String s = (String)var2.next();
            ConfigManager.writeConfig("data", s, data.get(s));
        }

    }

    public void InitFile() {
        this.saveDefaultConfig();
        ConfigManager.createFile("first");
        ConfigManager.createFile("data");
        ConfigManager.createFile("spawn");
        ConfigManager.createFile("whitelist");
        ConfigManager.createFile("permission");
        ConfigManager.createFile("room");
        ConfigManager.createFile("door");
        ConfigManager.createFile("tech_item");
        ConfigManager.createFile("block");
        whitelist = ConfigManager.getConfig("whitelist").getStringList("List");
        read = ConfigManager.getConfig("room").getStringList("read");
        SpawnLoction = ConfigManager.getConfig("spawn").getStringList("Location");
        itemlist = ConfigManager.getConfig("tech_item").getStringList("List");
        break_list = ConfigManager.getConfig("block").getStringList("break");
        place_list = ConfigManager.getConfig("block").getStringList("place");
        Iterator var1 = itemlist.iterator();

        String key;
        while(var1.hasNext()) {
            key = (String)var1.next();
            techitem_recipe.put(key, ConfigManager.getConfig("tech_item").getStringList(key + ".recipe"));
        }

        var1 = read.iterator();

        while(var1.hasNext()) {
            key = (String)var1.next();
            permission.put(key, ConfigManager.getConfig("room").getStringList(key));
        }

    }

    private void runTimer() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            this.ForOxy();
            this.Debuff_oxy();
            this.For_G();
            this.Debuff_G();
        }, 0L, 20L);
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            Reduce_Healthy();
        }, 0L, 600L);
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            Reduce_Healthy2();
        }, 0L, 100L);
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            Reduce_Healthy3();
        }, 0L, 1200L);
    }

    public static void Reduce_Healthy() {
        Iterator var0 = Bukkit.getOnlinePlayers().iterator();

        while(var0.hasNext()) {
            Player p = (Player)var0.next();
            if (!p.getWorld().getName().equals("kaiwater1")) {
                String i = (String)data.get(p.getName() + ".Healthy");
                Integer i1 = Integer.parseInt(i);
                i1 = i1 - 1;
                if (i1 > 0) {
                    data.put(p.getName() + ".Healthy", String.valueOf(i1));
                }
            }
        }

    }

    public static void Reduce_Healthy2() {
        if (!gravity) {
            Iterator var0 = Bukkit.getOnlinePlayers().iterator();

            while(var0.hasNext()) {
                Player p = (Player)var0.next();
                if (p.getWorld().getName().equals("kaiwater1") && !gravity) {
                    String i = (String)data.get(p.getName() + ".Healthy");
                    Integer i1 = Integer.parseInt(i);
                    i1 = i1 - 1;
                    if (i1 > 0) {
                        data.put(p.getName() + ".Healthy", String.valueOf(i1));
                    }
                }
            }

        }
    }

    public static void Reduce_Healthy3() {
        Iterator var0 = Bukkit.getOnlinePlayers().iterator();

        while(var0.hasNext()) {
            Player p = (Player)var0.next();
            if (p.getWorld().getName().equals("kaiwater1") && !gravity) {
                String i = (String)data.get(p.getName() + ".Healthy");
                Integer i1 = Integer.parseInt(i);
                i1 = i1 - 1;
                if (i1 > 0) {
                    data.put(p.getName() + ".Healthy", String.valueOf(i1));
                }
            }
        }

    }

    public static void Increase_Healthy(Player p) {
        if (p.getWorld().getName().equals("kaiwater1") && gravity) {
            String i = (String)data.get(p.getName() + ".Healthy");
            Integer i1 = Integer.parseInt(i);
            i1 = i1 + 1;
            if (p.getSaturation() > 10.0F && i1 <= 100) {
                p.setSaturation(p.getSaturation() - 1.0F);
                data.put(p.getName() + ".Healthy", String.valueOf(i1));
            }
        }

    }

    public static void ReduceOxy(Player p) {
        String i = (String)data.get(p.getName() + ".Oxy");
        Integer i1 = Integer.parseInt(i);
        if (i1 > 0) {
            i1 = i1 - 1;
            data.put(p.getName() + ".Oxy", String.valueOf(i1));
        }
    }

    public static void InsOxy(Player p) {
        String i = (String)data.get(p.getName() + ".Oxy");
        Integer i1 = Integer.parseInt(i);
        if (i1 < 100) {
            i1 = i1 + 1;
            data.put(p.getName() + ".Oxy", String.valueOf(i1));
        }
    }

    public static void room() {
    }

    public void Debuff_oxy() {
        Iterator var1 = Bukkit.getOnlinePlayers().iterator();

        while(var1.hasNext()) {
            Player p = (Player)var1.next();
            String i = (String)data.get(p.getName() + ".Oxy");
            Integer i1 = Integer.parseInt(i);
            if (time.get(p.getName()) == null) {
                if (i1 <= 0) {
                    time.put(p.getName(), 0);
                }

                if (i1 <= 20 && i1 != 0) {
                    p.sendTitle(" ", IString.addColor("&c&l[Warning] 氧气含量低!"), 3, 14, 3);
                }
            }

            if (time.get(p.getName()) != null) {
                p.sendTitle(" ", IString.addColor("&c&l[Warning] 缺氧 !"), 3, 14, 3);
                Integer i2 = (Integer)time.get(p.getName());
                i2 = i2 + 1;
                time.put(p.getName(), i2);
                if ((Integer)time.get(p.getName()) >= 60 && (Integer)time.get(p.getName()) <= 180) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
                }

                if ((Integer)time.get(p.getName()) >= 180 && (Integer)time.get(p.getName()) <= 210) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 2));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 0));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0));
                }

                if ((Integer)time.get(p.getName()) >= 210) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 3));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 1));
                    p.damage(1.0);
                }

                if (i1 >= 10) {
                    time.put(p.getName(), null);
                }
            }
        }

    }

    public void For_G() {
        Iterator var1 = Bukkit.getOnlinePlayers().iterator();

        while(var1.hasNext()) {
            Player p = (Player)var1.next();
            Increase_Healthy(p);
            if (p.getWorld().getName().equals("kaiwater1")) {
                if (!gravity) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 60, 255));
                }
            } else {
                p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 60, 252));
                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 60, 6));
            }
        }

    }

    public void Debuff_G() {
        Iterator var1 = Bukkit.getOnlinePlayers().iterator();

        while(var1.hasNext()) {
            Player p = (Player)var1.next();
            String i = (String)data.get(p.getName() + ".Healthy");
            Integer i1 = Integer.parseInt(i);
            if (i1 < 60 && i1 >= 40) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 0));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 40, 0));
                p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 40, 0));
            }

            if (i1 < 40 && i1 >= 10) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 1));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 40, 1));
                p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 40, 1));
                p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 0));
            }

            if (i1 < 10) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 3));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 40, 3));
                p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 40, 3));
                p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 3));
            }
        }

    }

    public void ForOxy() {
        Iterator var1 = Bukkit.getOnlinePlayers().iterator();

        while(var1.hasNext()) {
            Player p = (Player)var1.next();
            ItemStack helmet;
            ItemMeta im;
            ArrayList l1;
            List l2;
            String s;
            Integer o;
            if (p.getWorld().getName().equals("kaiwater1")) {
                if (!oxy) {
                    helmet = p.getInventory().getHelmet();
                    if (helmet != null) {
                        if (p.getInventory().getHelmet().getItemMeta().getCustomModelData() == 10000) {
                            InsOxy(p);
                            im = p.getInventory().getHelmet().getItemMeta();
                            l1 = new ArrayList();
                            l2 = im.getLore();
                            s = (String)l2.get(0);
                            if (s.length() == 12) {
                                s = s.substring(s.length() - 3, s.length());
                            } else if (s.length() == 11) {
                                s = s.substring(s.length() - 2, s.length());
                            } else if (s.length() == 10) {
                                s = s.substring(s.length() - 1, s.length());
                            }

                            o = Integer.parseInt(s);
                            o = o - 1;
                            if (o <= 0) {
                                p.getInventory().setHelmet(new ItemStack(Material.AIR));
                            } else {
                                l1.add(IString.addColor("&b当前氧气值: " + o));
                                im.setLore(l1);
                                p.getInventory().getHelmet().setItemMeta(im);
                            }
                        } else {
                            ReduceOxy(p);
                        }
                    } else {
                        ReduceOxy(p);
                    }
                } else {
                    InsOxy(p);
                }
            } else {
                helmet = p.getInventory().getHelmet();
                if (helmet != null) {
                    if (p.getInventory().getHelmet().getItemMeta().getCustomModelData() == 10000) {
                        InsOxy(p);
                        im = p.getInventory().getHelmet().getItemMeta();
                        l1 = new ArrayList();
                        l2 = im.getLore();
                        s = (String)l2.get(0);
                        if (s.length() == 12) {
                            s = s.substring(s.length() - 3, s.length());
                        } else if (s.length() == 11) {
                            s = s.substring(s.length() - 2, s.length());
                        } else if (s.length() == 10) {
                            s = s.substring(s.length() - 1, s.length());
                        }

                        o = Integer.parseInt(s);
                        o = o - 1;
                        if (o <= 0) {
                            p.getInventory().setHelmet(new ItemStack(Material.AIR));
                        } else {
                            l1.add(IString.addColor("&b当前氧气值: " + o));
                            im.setLore(l1);
                            p.getInventory().getHelmet().setItemMeta(im);
                        }
                    } else {
                        ReduceOxy(p);
                    }
                } else {
                    ReduceOxy(p);
                }
            }
        }

    }

    public static Plugin getPlugin() {
        return plugin;
    }
}
