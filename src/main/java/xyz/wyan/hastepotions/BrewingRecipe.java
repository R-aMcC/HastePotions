package xyz.wyan.hastepotions;

import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.wyan.hastepotions.actions.BrewAction;

public class BrewingRecipe {

    private final ItemStack ingredient;
    private final ItemStack fuel;
    private final ItemStack base;

    private int fuelSet;
    private int fuelCharge;

    private BrewAction action;
    private BrewClock clock;

    private boolean perfect;

    public BrewingRecipe(ItemStack ingredient, ItemStack base, ItemStack fuel, BrewAction action, boolean perfect, int fuelSet, int fuelCharge) {
        this.ingredient = ingredient;
        this.fuel = (fuel == null ? new ItemStack(Material.AIR) : fuel);
        this.fuelSet = fuelSet;
        this.fuelCharge = fuelCharge;
        this.action = action;
        this.base = base;
    }
    public BrewingRecipe(Material ingredient, ItemStack base, BrewAction action) {
        this(new ItemStack(ingredient), base, null, action, true, 40, 1);
    }

    public ItemStack getIngredient() {
        return ingredient;
    }

    public ItemStack getFuel() {
        return fuel;
    }

    public BrewAction getAction() {
        return action;
    }

    public void setAction(BrewAction action) {
        this.action = action;
    }

    public BrewClock getClock() {
        return clock;
    }

    public void setClock(BrewClock clock) {
        this.clock = clock;
    }
    public boolean isPerfect() {
        return perfect;
    }

    public void setPerfect(boolean perfect) {
        this.perfect = perfect;
    }

    public void startBrewing(BrewerInventory inventory) {
        clock = new BrewClock(this, inventory, 400);
    }

    public int getFuelSet() {
        return fuelSet;
    }

    public void setFuelSet(int fuelSet) {
        this.fuelSet = fuelSet;
    }

    public int getFuelCharge() {
        return fuelCharge;
    }

    public void setFuelCharge(int fuelCharge) {
        this.fuelCharge = fuelCharge;
    }

    public ItemStack getBase() {
        return base;
    }

    public static BrewingRecipe getRecipe(BrewerInventory inventory){
        for(BrewingRecipe recipe: HastePotions.recipes) {
            PotionMeta baseMeta = (PotionMeta) recipe.getBase().getItemMeta();
            if((!recipe.isPerfect() && inventory.getIngredient().getType() == recipe.getIngredient().getType()) || (recipe.isPerfect() && inventory.getIngredient().isSimilar(recipe.getIngredient()))){
                for(int i = 0; i < 3; i++) {
                    ItemStack item = inventory.getItem(i);
                    if (item == null) {
                        continue;
                    }


                    ItemMeta meta = item.getItemMeta();
                    PotionMeta potionMeta = (PotionMeta) meta;

                    if(potionMeta.getBasePotionType() == baseMeta.getBasePotionType() && potionMeta.getCustomEffects().isEmpty() && baseMeta.getCustomEffects().isEmpty()){
                        HastePotions.logger.info("Found recipe for " + inventory.getIngredient().getType() + " with base potion type " + baseMeta.getBasePotionType());
                        return recipe;
                    }


                    for (PotionEffect effect : baseMeta.getCustomEffects()) {
                        if (potionMeta.getCustomEffects().stream().anyMatch(e -> e.getType().equals(effect.getType()) && e.getAmplifier() == effect.getAmplifier() && e.getDuration() == effect.getDuration())){
                            HastePotions.logger.info("Found recipe for " + inventory.getIngredient().getType() + " with custom effect potion");
                            return recipe;
                        }
                    }
                }
            }
        }
        HastePotions.logger.info("No recipe found for " + inventory.getIngredient().getType());
        return null;
    }


    public class BrewClock extends BukkitRunnable {

        private BrewerInventory inventory;
        private BrewingRecipe recipe;
        private ItemStack[] before;
        private BrewingStand stand;
        private int current;

        public BrewClock(BrewingRecipe recipe, BrewerInventory inventory, int time){
            this.recipe = recipe;
            this.inventory = inventory;
            this.stand = inventory.getHolder();
            this.before = inventory.getContents();
            this.current = time;

            this.runTaskTimer(HastePotions.getInstance(), 0L, 1L);
        }


        @Override
        public void run() {
            if (current == 0) {
                int oldFuel = stand.getFuelLevel();
                stand.setFuelLevel(oldFuel- recipe.fuelCharge);
                stand.update(true);
                HastePotions.logger.info("Brewing complete, fuel charge: "+ oldFuel + " -> " + stand.getFuelLevel() + "("+ recipe.fuelCharge + ")");

                if (inventory.getIngredient().getAmount() > 1) {
                    inventory.getIngredient().setAmount(inventory.getIngredient().getAmount() - 1);
                } else {
                    inventory.setIngredient(new ItemStack(Material.AIR));
                }

                for (int i = 0; i < 3; i++) {
                    if (inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR) {
                        continue;
                    }
                    recipe.getAction().brew(inventory, inventory.getItem(i), ingredient);
                }
                cancel();
                return;
            }
            if(searchChanged(before, inventory.getContents(), perfect)) {
                cancel();
                return;
            }
            current--;
            stand.setBrewingTime(current);
            stand.update(true);
        }

        public boolean searchChanged(ItemStack [] before, ItemStack [] after, boolean mode){
            for (int i = 0; i < before.length; i++){
                if (before[i] == null && after[i] == null) {
                    continue;
                }else if(before[i] == null || after[i] == null) {
                    return true;
                }else if(mode && !before[i].isSimilar(after[i])) {
                    return true;
                }else if(!mode && !(before[i].getType() == after[i].getType())) {
                    return true;
                }
            }
            return false;
        }
    }

}
