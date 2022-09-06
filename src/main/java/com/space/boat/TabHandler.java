//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.space.boat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabHandler implements TabCompleter {
    public TabHandler() {
    }

    @ParametersAreNonnullByDefault
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            List<String> s1 = new ArrayList();
            s1.add("reload");
            s1.add("saveData");
            s1.add("whitelist");
            s1.add("idcard");
            s1.add("spawn");
            s1.add("oxy");
            s1.add("door");
            s1.add("open");
            s1.add("gravity");
            s1.add("tech");
            List<String> s2 = new ArrayList();
            s2.add("true");
            s2.add("false");
            List<String> s3 = new ArrayList();
            s3.add("create");
            s3.add("start");
            s3.add("break");
            if (args.length > 3) {
                return null;
            } else if (args.length != 0 && args.length != 1) {
                if (args[0].equals("whitelist") && (args.length == 2 || args.length == 1)) {
                    return Collections.singletonList("add");
                } else if (args[0].equals("spawn") && (args.length == 2 || args.length == 1)) {
                    return s3;
                } else if (args[0].equals("whitelist") && args[1].equals("add") && (args.length == 2 || args.length == 3)) {
                    return Collections.singletonList("<目标玩家ID>");
                } else if (!args[0].equals("oxy") || args.length != 2 && args.length != 1) {
                    return !args[0].equals("gravity") || args.length != 2 && args.length != 1 ? null : s2;
                } else {
                    return s2;
                }
            } else {
                return s1;
            }
        } else {
            return null;
        }
    }
}
