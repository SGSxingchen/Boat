package com.space.boat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.space.boat.tools.ConfigManager;
import com.space.boat.tools.IString;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Event implements Listener {
    private static List<String> laststr = new ArrayList<>();
    @EventHandler
    public void Join(PlayerJoinEvent e){
        List<String> PlayerList = ConfigManager.getConfig("first").getStringList("PlayerList");
        if(!PlayerList.contains(e.getPlayer().getName())){ //第一次进入服务器判定
            e.getPlayer().sendMessage(IString.addColor("&e&l[寻隐|Boat] &b欢迎来到陆地行舟 请等待游戏开始"));
            String PlayerData_Temp = "36.90";
            String PlayerData_Oxy = "100%";
            String PlayerData_Healthy = "100";
            String PlayerData_Psychology = "30";
            String per = "船员";

            ConfigManager.writeConfig("data",e.getPlayer().getName()+".Temp",PlayerData_Temp);
            ConfigManager.writeConfig("data",e.getPlayer().getName()+".Oxy",PlayerData_Oxy);
            ConfigManager.writeConfig("data",e.getPlayer().getName()+".Healthy",PlayerData_Healthy);
            ConfigManager.writeConfig("data",e.getPlayer().getName()+".Psychology",PlayerData_Psychology);
            ConfigManager.writeConfig("permission",e.getPlayer().getName(),per);

            Boat.data.put(e.getPlayer().getName()+".Temp",PlayerData_Temp);
            Boat.data.put(e.getPlayer().getName()+".Oxy",PlayerData_Oxy);
            Boat.data.put(e.getPlayer().getName()+".Healthy",PlayerData_Healthy);
            Boat.data.put(e.getPlayer().getName()+".Psychology",PlayerData_Psychology);


            PlayerList.add(e.getPlayer().getName());//添加玩家标记
            ConfigManager.writeConfig("first","PlayerList",PlayerList);
        }
        else {
            Boat.data.put(e.getPlayer().getName()+".Temp",ConfigManager.getConfig("data").getString(e.getPlayer().getName()+".Temp"));
            Boat.data.put(e.getPlayer().getName()+".Oxy",ConfigManager.getConfig("data").getString(e.getPlayer().getName()+".Oxy"));
            Boat.data.put(e.getPlayer().getName()+".Healthy",ConfigManager.getConfig("data").getString(e.getPlayer().getName()+".Healthy"));
            Boat.data.put(e.getPlayer().getName()+".Psychology",ConfigManager.getConfig("data").getString(e.getPlayer().getName()+".Psychology"));
        }

        //计分板显示 -玩家姓名-氧气值-温度-健康值
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Boat.getPlugin(), () -> {
            Scoreboard Data = manager.getNewScoreboard();
            Objective obj = Data.registerNewObjective("Test","dummy");
            obj.setDisplayName(IString.addColor("&6&l [ 数据面板 ]"));
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
//            for(String str : laststr){
//                Data.resetScores(str);
//            }
//            laststr.clear();
            String name = IString.addColor("&bID: &r")+e.getPlayer().getName();
            String Temp = IString.addColor("&b温度: &r")+Boat.data.get(e.getPlayer().getName()+".Temp");
            String Oxy = IString.addColor("&b氧气值: &r")+Boat.data.get(e.getPlayer().getName()+".Oxy");
            String Healthy = IString.addColor("&b健康值: &r")+Boat.data.get(e.getPlayer().getName()+".Healthy");
//            laststr.add(name);
//            laststr.add(Temp);
//            laststr.add(Oxy);
//            laststr.add(Healthy);
            obj.getScore(name).setScore(4);
            obj.getScore(Temp).setScore(3);
            obj.getScore(Oxy).setScore(2);
            obj.getScore(Healthy).setScore(1);
            e.getPlayer().setScoreboard(Data);

        },0L,20L);
    }
    //白名单
    @EventHandler
    public void onPlayerConnect(PlayerLoginEvent e) {
        String name = e.getPlayer().getName();
        if (!(Boat.whitelist.contains(name))) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "你不在白名单中\n请确认自己获得资格后联系管理理员");
        }
    }

}
