package com.space.boat;

import com.space.boat.tools.ConfigManager;
import com.space.boat.tools.IBuilder;
import com.space.boat.tools.IString;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class cmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] strings) {
        if (strings.length > 0) {
            if (strings[0].equals("reload")) {
                Boat.plugin.reloadConfig();
                for(Player p : Bukkit.getOnlinePlayers()){ //重载玩家data数据
                    Boat.data.put(p.getName()+".Temp",ConfigManager.getConfig("data").getString(p.getName()+".Temp"));
                    Boat.data.put(p.getName()+".Oxy",ConfigManager.getConfig("data").getString(p.getName()+".Oxy"));
                    Boat.data.put(p.getName()+".Healthy",ConfigManager.getConfig("data").getString(p.getName()+".Healthy"));
                    Boat.data.put(p.getName()+".Psychology",ConfigManager.getConfig("data").getString(p.getName()+".Psychology"));
                }
//                Boat.data = ConfigManager.getConfig("data").getMapList("data");
                //重载白名单
                Boat.whitelist = ConfigManager.getConfig("whitelist").getStringList("List");
                sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 插件已重载"));
                return true;
            }
            if (strings[0].equals("saveData")) {
                Set<String> keys = Boat.data.keySet();
                for(String s : keys){
                    ConfigManager.writeConfig("data",s,Boat.data.get(keys));
                }
                sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 数据已保存"));
                return true;
            }

            if (strings[0].equals("whitelist")){
                if(strings[1].equals("add")) {
                    if (strings.length < 3 || strings.length > 3) {
                        sender.sendMessage("&e&l[寻隐|Boat] 请输入正确的玩家名字");
                        return true;
                    } else {
                        List<String> t = ConfigManager.getConfig("whitelist").getStringList("List");
                        t.add(strings[2]);
                        ConfigManager.writeConfig("whitelist","List",t);
                        sender.sendMessage("&e&l[寻隐|Boat] 添加成功！");
                        return true;
                    }
                }
            }
            if (strings[0].equals("idcard") ) {
                if(strings.length == 1) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        List<String> lore = new ArrayList<>();
                        lore.add(IString.addColor("&b 所属舰船: &r长辉"));
                        lore.add(IString.addColor("&b 拥有者: &r" + p.getName()));
                        lore.add(IString.addColor("&b 权限等级: &r") + ConfigManager.getConfig("permission").getString(p.getName()));
                        String name = IString.addColor("&c身份卡");

                        ItemStack card = new ItemStack(Material.PAPER);

                        ItemMeta im = card.getItemMeta();
                        im.setLore(lore);
                        im.setDisplayName(name);
                        im.setCustomModelData(10167);


                        card.setItemMeta(im);
                        p.getInventory().addItem(card);
                        sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 身份卡发送完成"));
                    }
                    return true;
                }
                else if(strings.length == 2 && sender instanceof Player){
                    Player p = (Player) sender;
                    List<String> lore = new ArrayList<>();
                    lore.add(IString.addColor("&b 所属舰船: &r长辉"));
                    lore.add(IString.addColor("&b 拥有者: &r" + strings[1]));
                    lore.add(IString.addColor("&b 权限等级: &r")+ConfigManager.getConfig("permission").getString(strings[1]));
                    String name = IString.addColor("&c身份卡");

                    ItemStack card = new ItemStack(Material.PAPER);

                    ItemMeta im = card.getItemMeta();
                    im.setLore(lore);
                    im.setDisplayName(name);
                    im.setCustomModelData(10167);


                    card.setItemMeta(im);
                    p.getInventory().addItem(card);
                    sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 身份卡发送完成"));
                    return true;
                }
                else sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 命令参数错误|此命令只能由玩家执行"));
            }
            if(strings[0].equals("oxy")){
                if(strings.length >= 1) {
                    if (strings[1].equals("true")) {
                        Boat.oxy = true;
                        sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 已设置氧气系统生效"));
                    } else if (strings[1].equals("false")) {
                        Boat.oxy = false;
                        sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 已设置氧气系统失效"));
                    }
                }
                else sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 缺失参数"));
            }
            if(strings[0].equals("get") && sender instanceof Player){
                if (strings[1].equals("helmet")){
                    ItemStack helmet = new ItemStack(Material.CHAINMAIL_HELMET);
                    List<String> lore = new ArrayList<>();
                    lore.add(IString.addColor("&b当前氧气值: 300"));
                    ItemMeta im = helmet.getItemMeta();
                    im.setDisplayName(IString.addColor("&c航天头盔"));
                    im.setLore(lore);
                    im.setCustomModelData(10000);
                    helmet.setItemMeta(im);
                    Player p = (Player) sender;
                    p.getInventory().addItem(helmet);
                    return true;
                }
            }
            return true;
        }
        if(sender instanceof Player){
            if(strings.length > 0){
                if(strings[0].equals("spawn") && strings[1].equals("create")){
                    Integer x = ((Player) sender).getPlayer().getLocation().getBlockX();
                    Integer y = ((Player) sender).getPlayer().getLocation().getBlockY();
                    Integer z = ((Player) sender).getPlayer().getLocation().getBlockZ();
                    World w = ((Player) sender).getPlayer().getLocation().getWorld();
                    String l = x+","+y+","+z+","+w.getName();
                    List<String> ll = ConfigManager.getConfig("spawn").getStringList("Location");
                    ll.add(l);
                    ConfigManager.writeConfig("spawn","Location",ll);
                    sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 出生点创建成功"));
                    return true;
                }
                else return false;
            }
        }
        return true;
    }

}
