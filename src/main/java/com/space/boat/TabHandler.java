package com.space.boat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

public class TabHandler implements TabCompleter {
    @Override
    @ParametersAreNonnullByDefault
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(sender.isOp()) {
            List<String> s1 = new ArrayList<>();
            s1.add("reload");
            s1.add("saveData");
            s1.add("whitelist");
            s1.add("idcard");
            s1.add("spawn");
            s1.add("oxy");

            List<String> s2 = new ArrayList<>();
            s2.add("true");
            s2.add("false");

            if (args.length > 3) {
                // 前三个参数已经输入完成，不继续提示
                return null;
            }
            if (args.length == 0 || args.length == 1) {
                return s1;
            }
            if (args[0].equals("whitelist") && (args.length == 2 || args.length == 1)) {
                return Collections.singletonList("add");
            }
            if (args[0].equals("idcard") && (args.length == 2 || args.length == 1)) {
                return Collections.singletonList("<目标玩家ID>");
            }
            if (args[0].equals("spawn") && (args.length == 2 || args.length == 1)) {
                return Collections.singletonList("create");
            }
            if (args[0].equals("whitelist") && args[1].equals("add") && (args.length == 2 || args.length == 3)) {
                return Collections.singletonList("<目标玩家ID>");
            }
            if (args[0].equals("oxy")&& (args.length == 2 || args.length == 1)){
                return s2;
            }
        }
        else return null;
        return null;
    }
}
