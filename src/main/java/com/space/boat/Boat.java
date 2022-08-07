package com.space.boat;

import com.space.boat.tools.ConfigManager;
import com.space.boat.tools.IBuilder;
import com.space.boat.tools.IString;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.*;

public final class Boat extends JavaPlugin {
    public static Plugin plugin;


    public static Map<String,String> data = new HashMap<>(); //存储玩家数据的Data
    public static Map<String,Integer> time = new HashMap<>(); //存储玩家数据的Data
    public static List<String> whitelist = new ArrayList<>(); //白名单

    public static boolean oxy = true;//舰船氧气系统是否完好


    @Override
    public void onEnable() {
        runTimer();
        Debuff();

        plugin = this;
        this.getLogger().info(IString.addColor("&e&l[寻隐|Boat] &e陆地行舟功能性插件已经开启"));
        InitFile(); //初始化配置文件加存储文件


        Bukkit.getPluginManager().registerEvents(new Event(),this);
        Bukkit.getPluginManager().registerEvents(new Oxy(),this);
        Bukkit.getPluginCommand("yboat").setExecutor(new cmd());
        Objects.requireNonNull(Bukkit.getPluginCommand("yboat")).setTabCompleter(new TabHandler());

    }

    @Override
    public void onDisable() {
        Set<String> keys = Boat.data.keySet();
        for(String s : keys){
            ConfigManager.writeConfig("data",s,Boat.data.get(s));
        }
    }

    public void InitFile(){

        saveDefaultConfig();
        ConfigManager.createFile("first");
        ConfigManager.createFile("data");
        ConfigManager.createFile("spawn");
        ConfigManager.createFile("whitelist");
        ConfigManager.createFile("permission");

        whitelist = ConfigManager.getConfig("whitelist").getStringList("List");
    }
    private void runTimer() { //定时执行
        Bukkit.getScheduler().runTaskTimer(this , () -> {
            ForOxy();
        } , 0L , 20L);
    }

    public static void ReduceOxy(Player p){
        String i = data.get(p.getName()+".Oxy");
        i = i.substring(0,i.length()-1);
        Integer i1 = Integer.parseInt(i);
        if(i1 <= 0){
            return;
        }
        i1 -= 1;
        data.put(p.getName()+".Oxy",i1 + "%");
    }

    public static void InsOxy(Player p){
        String i = data.get(p.getName()+".Oxy");
        i = i.substring(0,i.length()-1);
        Integer i1 = Integer.parseInt(i);
        if(i1 >= 100){
            return;
        }
        i1 += 1;
        data.put(p.getName()+".Oxy",i1 + "%");
    }

    public static void room(){

    }

    public void Debuff(){
        Bukkit.getScheduler().runTaskTimer(this , () -> {
            for(Player p : Bukkit.getOnlinePlayers()) {
                String i = data.get(p.getName() + ".Oxy");
                i = i.substring(0, i.length() - 1);
                Integer i1 = Integer.parseInt(i);
                if(time.get(p.getName()) == null) {
                    if (i1 <= 0) {
                        time.put(p.getName(), 0);
                    }
                    
                }
                if(time.get(p.getName()) != null){
                    BaseComponent url = new TextComponent(IString.addColor("&c&l[Warning] 缺氧 !"));
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR,url);
                    Integer i2 = time.get(p.getName());
                    i2 += 1;
                    time.put(p.getName(),i2);
                    if(time.get(p.getName()) >= 60 && time.get(p.getName()) <= 180){
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40 , 1));
                    }
                    if(time.get(p.getName()) >= 180 &&time.get(p.getName())<=210){
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40 , 2));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 , 0));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 40 , 0));
                    }
                    if(time.get(p.getName()) >= 210){
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40 , 3));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40 , 1));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 40 , 1));
                        p.damage(1);
                    }
                    if(i1>=10){
                        time.put(p.getName(),null);
                    }
                }

            }
        } , 0L , 20L);
    }

//    public void hypoxia(Player p){
//        Bukkit.getScheduler().runTaskTimer(this , () -> {
//        } , 0L , 20L);
//    }
    public void ForOxy(){
        for (Player p : Bukkit.getOnlinePlayers()){
            if(p.getWorld().getName().equals("kaiwater1")){
                if(!oxy ){
                    ItemStack helmet = p.getInventory().getHelmet();
                    if(helmet != null) {
                        if(p.getInventory().getHelmet().getItemMeta().getCustomModelData() == 10000){
                            InsOxy(p);
                            ItemMeta im = p.getInventory().getHelmet().getItemMeta();
                            List<String> l1 = new ArrayList<>();
                            List<String> l2 = im.getLore();
                            String s = l2.get(0);
                            if(s.length() == 12) s = s.substring(s.length()-3,s.length());
                            else if(s.length() == 11) s = s.substring(s.length()-2,s.length());
                            else if(s.length() == 10) s = s.substring(s.length()-1,s.length());
                            Integer o = Integer.parseInt(s);
                            o -= 1;
                            if (o <= 0){
                                p.getInventory().setHelmet(new ItemStack(Material.AIR));
                                continue;
                            }
                            l1.add(IString.addColor("&b当前氧气值: "+o));
                            im.setLore(l1);

                            p.getInventory().getHelmet().setItemMeta(im);
                        }
                        else ReduceOxy(p);
                    }else {
                        ReduceOxy(p);
                    }
                }
                else{
                    InsOxy(p);
                }
            }
            else {
                ItemStack helmet = p.getInventory().getHelmet();
                if(helmet != null) {
                    if(p.getInventory().getHelmet().getItemMeta().getCustomModelData() == 10000){
                        InsOxy(p);
                        ItemMeta im = p.getInventory().getHelmet().getItemMeta();
                        List<String> l1 = new ArrayList<>();
                        List<String> l2 = im.getLore();
                        String s = l2.get(0);
                        s = s.substring(s.length()-3,s.length());
                        Integer o = Integer.parseInt(s);
                        o -= 1;
                        if (o <= 0){
                            p.getInventory().setHelmet(new ItemStack(Material.AIR));
                            continue;
                        }
                        l1.add(IString.addColor("&b当前氧气值: "+o));
                        im.setLore(l1);

                        p.getInventory().getHelmet().setItemMeta(im);
                    }
                    else ReduceOxy(p);
                }else {
                    ReduceOxy(p);
                }
            }
        }
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}
