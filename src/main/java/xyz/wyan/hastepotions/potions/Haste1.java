package xyz.wyan.hastepotions.potions;

import org.bukkit.Material;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.wyan.hastepotions.Utils;
import xyz.wyan.hastepotions.actions.BrewAction;

public class Haste1 extends BrewAction {

    @Override
    public void brew(BrewerInventory inventory, ItemStack item, ItemStack ingredient) {
        if(item.getType() == Material.POTION && Utils.isAwkwardPotion(item) && ingredient.getType() == Material.GOLD_NUGGET) {
            Utils.setPotion(inventory, 0, false);
        }
    }
}
