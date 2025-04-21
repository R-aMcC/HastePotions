package xyz.wyan.hastepotions.potions;

import org.bukkit.Material;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import xyz.wyan.hastepotions.Utils;
import xyz.wyan.hastepotions.actions.BrewAction;

public class Haste1e extends BrewAction {

    @Override
    public void brew(BrewerInventory inventory, ItemStack item, ItemStack ingredient) {
    if(item.getType() == Material.POTION && ingredient.getType() == Material.REDSTONE) {
            Utils.setPotion(inventory, 0, true);
        }
    }
}
