//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.space.boat;

import com.space.boat.tools.IString;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class recipe {
    ShapedRecipe shaped;

    public recipe() {
        this.shaped = new ShapedRecipe(new NamespacedKey(Boat.getPlugin(), "test"), new ItemStack(Material.CHAINMAIL_HELMET));
    }

    public void setShaped(ShapedRecipe shaped) {
        this.shaped = shaped;
        ItemStack helmet = shaped.getResult();
        List<String> lore = new ArrayList();
        lore.add(IString.addColor("&b当前氧气值: 300"));
        ItemMeta im = helmet.getItemMeta();
        im.setDisplayName(IString.addColor("&c航天头盔"));
        im.setLore(lore);
        im.setCustomModelData(10000);
        helmet.setItemMeta(im);
        shaped = shaped.shape(new String[]{"aaa", "aba", "ccc"});
        shaped = shaped.setIngredient('a', Material.IRON_INGOT).setIngredient('b', Material.GLASS).setIngredient('c', Material.REDSTONE);
        Bukkit.addRecipe(shaped);
    }
}
