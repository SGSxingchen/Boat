//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.space.boat;

import com.space.boat.tools.ConfigManager;
import com.space.boat.tools.Create;
import com.space.boat.tools.IBuilder;
import com.space.boat.tools.IString;
import com.space.boat.tools.Table;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Technology implements Listener, CommandExecutor {
    public Technology() {
    }

    public boolean onCommand( CommandSender sender,Command command, String label, String[] args) {

        if (args.length != 0) {
            if (args[0].equals("get") && sender instanceof Player) {
                Player p = (Player)sender;
                List<String> lore = new ArrayList();
                lore.add(IString.addColor("&e当前研究项目: &f空白"));
                String name = IString.addColor("&b蓝图");
                ItemStack card = new ItemStack(Material.PAPER);
                ItemMeta im = card.getItemMeta();
                im.setLore(lore);
                im.setDisplayName(name);
                im.setCustomModelData(10172);
                card.setItemMeta(im);
                p.getInventory().addItem(new ItemStack[]{card});
                sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 蓝图发送完成"));
            }

            return true;
        } else {
            sender.sendMessage(IString.addColor("&e&l[寻隐|Boat] 缺少参数 "));
            return true;
        }
    }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) {
            if (e.getItem() != null) {
                if (e.getItem().getItemMeta().hasCustomModelData()) {
                    Player p = e.getPlayer();
                    if (e.getAction().name().equals("RIGHT_CLICK_AIR") && e.getItem().getItemMeta().getCustomModelData() == 10172) {
                        openGroupChoose(p);
                    }

                }
            }
        } else {
            if (e.getClickedBlock().getBlockData().getMaterial().name().equalsIgnoreCase("ENDER_CHEST")) {
                e.setCancelled(true);
                List<String> lss = ConfigManager.getConfig("config").getStringList("Tech_Table");
                Iterator var3 = lss.iterator();

                while(var3.hasNext()) {
                    String ss = (String)var3.next();
                    String[] s = ss.split(",");
                    int xx1 = e.getClickedBlock().getX();
                    int yy1 = e.getClickedBlock().getY();
                    int zz1 = e.getClickedBlock().getZ();
                    int xx2 = Integer.parseInt(s[0]);
                    int yy2 = Integer.parseInt(s[1]);
                    int zz2 = Integer.parseInt(s[2]);
                    e.getPlayer().sendMessage(xx1 + String.valueOf(yy1) + zz1);
                    if (xx1 == xx2 && yy1 == yy2 && zz1 == zz2 && e.getClickedBlock().getWorld().getName().equalsIgnoreCase(s[3])) {
                        e.setCancelled(true);
                        openGroupChoose2(e.getPlayer());
                    }
                }
            }

        }
    }

    @EventHandler
    public void close(InventoryCloseEvent e) {
        int j;
        ItemStack item;
        int i;
        if (e.getInventory().getHolder() instanceof Create) {
            if (e.getInventory().getItem(9) != null) {
                item = e.getInventory().getItem(9);
                e.getPlayer().getInventory().addItem(new ItemStack[]{item});
            }

            for(i = 10; i <= 37; i += 9) {
                for(j = i; j < i + 4; ++j) {
                    if (e.getInventory().getItem(j) != null) {
                        item = e.getInventory().getItem(j);
                        e.getPlayer().getInventory().addItem(new ItemStack[]{item});
                    }
                }
            }
        }

        if (e.getInventory().getHolder() instanceof Table) {
            for(i = 10; i <= 37; i += 9) {
                for(j = i; j < i + 4; ++j) {
                    if (e.getInventory().getItem(j) != null) {
                        item = e.getInventory().getItem(j);
                        e.getPlayer().getInventory().addItem(new ItemStack[]{item});
                    }
                }
            }
        }

    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        String clickitem;
        boolean flag;
        int j;
        if (e.getInventory().getHolder() instanceof Table) {
            if (e.getCurrentItem() == null) {
                return;
            }

            clickitem = e.getCurrentItem().getType().name();
            if (clickitem.equals("GREEN_STAINED_GLASS_PANE") || clickitem.equals("BLACK_STAINED_GLASS_PANE") || clickitem.equals("RED_STAINED_GLASS_PANE") || clickitem.equals("PURPLE_STAINED_GLASS_PANE") || clickitem.equals("GRAY_STAINED_GLASS_PANE")) {
                e.setCancelled(true);
            }

            switch (e.getRawSlot()) {
                case 24:
                    String node = null;
                    Inventory inv = e.getInventory();
                    flag = false;
                    Iterator var7 = Boat.itemlist.iterator();

                    while(var7.hasNext()) {
                        String itemnode = (String)var7.next();
                        j = 0;

                        for(int i = 10; i <= 37 && !flag; ++j) {
                            for(int k = i; k < i + 4; ++k) {
                                ItemStack inv_item = inv.getItem(k);
                                List<String> temp = (List)Boat.techitem_recipe.get(itemnode);
                                String ss = (String)temp.get(k);
                                String s = itemnode + ".ingredient." + ss.charAt(k - i);
                                if (inv_item != null || !ConfigManager.getConfig("tech_item").getString(s).equals("AIR")) {
                                    if (inv_item == null) {
                                        flag = true;
                                        break;
                                    }

                                    if (!inv_item.getType().name().equals(ConfigManager.getConfig("tech_item").getString(s))) {
                                        flag = true;
                                        break;
                                    }
                                }
                            }

                            i += 9;
                        }

                        if (!flag) {
                            node = itemnode;
                            break;
                        }
                    }

                    ItemStack hand;
                    if (!flag) {
                        p.sendMessage(IString.addColor("&e&l[寻隐|Boat] 研究成功"));
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                        hand = p.getInventory().getItemInMainHand();
                        ItemMeta hand_meta = hand.getItemMeta();
                        List<String> lore = new ArrayList();
                        lore.add(IString.addColor("&6研究序号:&f" + node));
                        lore.add(IString.addColor("&e当前研究项目: &f" + ConfigManager.getConfig("tech_item").getString(node + ".name")));
                        lore.add(IString.addColor(ConfigManager.getConfig("tech_item").getString(node + ".recipe_message")));
                        hand_meta.setLore(lore);
                        hand.setItemMeta(hand_meta);
                        p.getInventory().setItemInMainHand(hand);
                        p.closeInventory();
                    } else {
                        p.sendMessage(IString.addColor("&e&l[寻隐|Boat] 研究失败"));
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1.0F, 1.0F);
                        hand = p.getInventory().getItemInMainHand();
                        hand.setAmount(hand.getAmount() - 1);
                        p.getInventory().setItemInMainHand(hand);
                        p.closeInventory();
                    }
            }
        }

        if (e.getInventory().getHolder() instanceof Create) {
            if (e.getCurrentItem() == null) {
                return;
            }

            clickitem = e.getCurrentItem().getType().name();
            if (clickitem.equals("GREEN_STAINED_GLASS_PANE") || clickitem.equals("BLACK_STAINED_GLASS_PANE") || clickitem.equals("RED_STAINED_GLASS_PANE") || clickitem.equals("PURPLE_STAINED_GLASS_PANE") || clickitem.equals("GRAY_STAINED_GLASS_PANE") || clickitem.equals("CRAFTING_TABLE")) {
                e.setCancelled(true);
            }

            switch (e.getRawSlot()) {
                case 24:
                    if (e.getInventory().getItem(24).getType().name().equalsIgnoreCase("RED_STAINED_GLASS_PANE")) {
                        return;
                    }

                    if (e.getInventory().getItem(9) == null) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0F, 1.0F);
                        e.getWhoClicked().sendMessage(IString.addColor("&e&l[寻隐|Boat] &b请先放入蓝图！"));
                        return;
                    }

                    Inventory inv = e.getInventory();
                    String itemnode = ((String)inv.getItem(9).getItemMeta().getLore().get(0)).substring(9);
                    flag = false;
                    int hang = 0;

                    int i;
                    for(i = 10; i <= 37 && !flag; ++hang) {
                        for(j = i; j < i + 4; ++j) {
                            ItemStack inv_item = inv.getItem(j);
                            List<String> temp = ConfigManager.getConfig("tech_item").getStringList(itemnode + ".recipe2");
                            String ss = (String)temp.get(hang);
                            String s = itemnode + ".ingredient2." + ss.charAt(j - i);
                            if (inv_item != null || !ConfigManager.getConfig("tech_item").getString(s).equals("AIR")) {
                                if (inv_item == null) {
                                    flag = true;
                                    break;
                                }

                                if (!inv_item.getType().name().equals(ConfigManager.getConfig("tech_item").getString(s))) {
                                    flag = true;
                                    break;
                                }
                            }
                        }

                        i += 9;
                    }

                    if (!flag) {
                        p.sendMessage(IString.addColor("&e&l[寻隐|Boat] 制作成功"));
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);

                        for(i = 10; i <= 37; i += 9) {
                            for(j = i; j < i + 4; ++j) {
                                inv.setItem(j, (ItemStack)null);
                            }
                        }

                        List<String> command = ConfigManager.getConfig("tech_item").getStringList(itemnode + ".result.command");
                        e.getWhoClicked().sendMessage(itemnode + "result.command");
                        Iterator var27 = command.iterator();

                        while(var27.hasNext()) {
                            String command2 = (String)var27.next();
                            e.getWhoClicked().sendMessage(command2);
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command2.replaceAll("%player%", e.getWhoClicked().getName()));
                        }
                    } else {
                        p.sendMessage(IString.addColor("&e&l[寻隐|Boat] 制作失败"));
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0F, 1.0F);
                        ItemStack Error = IBuilder.buildItem(Material.RED_STAINED_GLASS_PANE, IString.addColor("&a&l材料错误！"));
                        inv.setItem(24, Error);
                        Bukkit.getScheduler().runTaskLater(Boat.getPlugin(), () -> {
                            ItemStack Confirm = IBuilder.buildItem(Material.GREEN_STAINED_GLASS_PANE, IString.addColor("&a&l确认"));
                            inv.setItem(24, Confirm);
                        }, 60L);
                    }
            }
        }

    }

    public static void getInvItem(Player p) {
    }

    public static void openGroupChoose2(Player p) {
        Inventory menu = Bukkit.createInventory(new Create(), 54, IString.addColor("&6&l工作台"));
        ItemStack board = IBuilder.getBorder(Material.BLACK_STAINED_GLASS_PANE);
        ItemStack craft = IBuilder.getBorder(Material.CRAFTING_TABLE);
        ItemMeta im = craft.getItemMeta();
        List<String> lore = new ArrayList();
        lore.add(IString.addColor("&e请在下方放入研究蓝图"));
        im.setLore(lore);
        craft.setItemMeta(im);
        ItemStack purple = IBuilder.getBorder(Material.PURPLE_STAINED_GLASS_PANE);
        ItemStack bg = IBuilder.getBorder(Material.GRAY_STAINED_GLASS_PANE);
        ItemStack Confirm = IBuilder.buildItem(Material.GREEN_STAINED_GLASS_PANE, IString.addColor("&a&l确认"));
        menu.setItem(53, purple);
        menu.setItem(45, purple);
        menu.setItem(0, craft);
        menu.setItem(8, purple);
        menu.setItem(24, Confirm);

        int i;
        for(i = 14; i <= 41; i += 9) {
            for(int j = i; j <= i + 3; ++j) {
                if (j != 24) {
                    menu.setItem(j, bg);
                }
            }
        }

        for(i = 1; i <= 7; ++i) {
            menu.setItem(i, board);
        }

        for(i = 46; i <= 52; ++i) {
            menu.setItem(i, board);
        }

        for(i = 9; i < 45; i += 9) {
            if (i != 9) {
                menu.setItem(i, board);
            }
        }

        for(i = 17; i < 53; i += 9) {
            menu.setItem(i, board);
        }

        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0F, 1.0F);
        p.openInventory(menu);
    }

    public static void openGroupChoose(Player p) {
        Inventory menu = Bukkit.createInventory(new Table(), 54, IString.addColor("&6&l初始研究面板"));
        ItemStack board = IBuilder.getBorder(Material.BLACK_STAINED_GLASS_PANE);
        ItemStack red = IBuilder.getBorder(Material.RED_STAINED_GLASS_PANE);
        ItemStack purple = IBuilder.getBorder(Material.PURPLE_STAINED_GLASS_PANE);
        ItemStack bg = IBuilder.getBorder(Material.GRAY_STAINED_GLASS_PANE);
        ItemStack Confirm = IBuilder.buildItem(Material.GREEN_STAINED_GLASS_PANE, IString.addColor("&a&l确认"));
        menu.setItem(53, purple);
        menu.setItem(45, purple);
        menu.setItem(0, purple);
        menu.setItem(8, purple);
        menu.setItem(24, Confirm);

        int i;
        for(i = 14; i <= 41; i += 9) {
            for(int j = i; j <= i + 3; ++j) {
                if (j != 24) {
                    menu.setItem(j, bg);
                }
            }
        }

        for(i = 1; i <= 7; ++i) {
            menu.setItem(i, board);
        }

        for(i = 46; i <= 52; ++i) {
            menu.setItem(i, board);
        }

        for(i = 9; i < 45; i += 9) {
            menu.setItem(i, board);
        }

        for(i = 17; i < 53; i += 9) {
            menu.setItem(i, board);
        }

        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0F, 1.0F);
        p.openInventory(menu);
    }
}
