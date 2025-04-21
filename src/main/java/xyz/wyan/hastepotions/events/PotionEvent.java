package xyz.wyan.hastepotions.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.wyan.hastepotions.BrewingRecipe;
import xyz.wyan.hastepotions.HastePotions;
import xyz.wyan.hastepotions.Utils;

public class PotionEvent implements Listener {

    @EventHandler
    public void inventoryClick(InventoryClickEvent event){
        Inventory inv = event.getClickedInventory();
        if (inv == null || inv.getType() != InventoryType.BREWING) {
            return;
        }
        if(!(event.isRightClick() || event.isLeftClick()) || event.isShiftClick()){
            return;
        }

        ItemStack is = event.getCurrentItem();
        ItemStack is2 = event.getCursor();

        int clickedSlot = event.getSlot();

        if(clickedSlot != 3){
            return;
        }
        event.setCancelled(true);
        HastePotions.logger.info("Clicked Inventory: " + inv.getType());
        HastePotions.logger.info("Holder: " + inv.getHolder());
        HastePotions.logger.info("Clicked Slot: " + event.getSlot());

        Player p = (Player) event.getView().getPlayer();

        ClickType type = event.getClick();


        switch(type){
            case LEFT ->{
                if(is == null || is.getType() == Material.AIR){
                    inv.setItem(clickedSlot, is2);
                    p.setItemOnCursor(new ItemStack(Material.AIR));
                }else if(is2.getType() == Material.AIR) {
                    p.setItemOnCursor(is);
                    inv.setItem(clickedSlot, new ItemStack(Material.AIR));
                }else if(is2.isSimilar(is)){
                    int amount1 = is.getAmount();
                    int amount2 = is2.getAmount();
                    int maxSize = is.getMaxStackSize();
                    if(amount1 + amount2 > maxSize){
                        inv.setItem(clickedSlot, new ItemStack(is.getType(), is.getMaxStackSize()));
                        p.setItemOnCursor(new ItemStack(is.getType(), amount1 + amount2 - maxSize));
                    }else{
                        inv.setItem(clickedSlot, new ItemStack(is.getType(), amount1 + amount2));
                        p.setItemOnCursor(new ItemStack(Material.AIR));
                    }
                }else{
                    inv.setItem(clickedSlot, is2);
                    p.setItemOnCursor(is);
                }
            }
            case RIGHT ->{
                if(is == null || is.getType() == Material.AIR ){
                    inv.setItem(clickedSlot, new ItemStack(is2.getType(), 1));
                    p.setItemOnCursor(is2.getAmount() > 1 ? new ItemStack(is2.getType(), is2.getAmount()-1) : new ItemStack(Material.AIR));
                }else if(is2.getType() == Material.AIR) {
                    p.setItemOnCursor(new ItemStack(is.getType(), is.getAmount() % 2 == 0 ? is.getAmount() / 2 : is.getAmount() / 2 + 1));
                    inv.setItem(clickedSlot, new ItemStack(is.getType(), is.getAmount() / 2));
                }else if(is.isSimilar(is2)){
                    if(is.getAmount() == is.getMaxStackSize()) {
                        return;
                    }
                    p.setItemOnCursor(is2.getAmount() > 1 ? new ItemStack(is2.getType(), is2.getAmount() - 1) : new ItemStack(Material.AIR));
                    inv.setItem(clickedSlot, new ItemStack(is.getType(), is.getAmount() + 1));
                } else{
                    inv.setItem(clickedSlot, is2);
                    p.setItemOnCursor(is);
                }
            }
            case SHIFT_LEFT, SHIFT_RIGHT -> {
                //write this in later
                // No matter what you click with, it sends the item from the brewing stand into the inventory
            }
        }


        if(((BrewerInventory) inv).getIngredient() == null){
            HastePotions.logger.info("No ingredient found, not brewing.");
            return;
        }

        BrewingRecipe recipe = BrewingRecipe.getRecipe((BrewerInventory) inv);

        if(recipe == null){
            HastePotions.logger.info("No recipe found, not brewing.");
            return;
        }
        /*
        boolean shouldBrew = false;
        for(int i = 0; i<3; i++){
            if(inv.getItem(i) != null && Utils.isAwkwardPotion(inv.getItem(i))){
                HastePotions.logger.info("Found Awkward Potion in slot " + i);
                shouldBrew = true;
            }

        }
        if(!shouldBrew){
            HastePotions.logger.info("No Awkward Potions found, not brewing.");
            return;
        }

         */
        recipe.startBrewing((BrewerInventory) inv);

    }
}
