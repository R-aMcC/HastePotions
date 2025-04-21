package xyz.wyan.hastepotions;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import xyz.wyan.hastepotions.events.PotionEvent;
import xyz.wyan.hastepotions.potions.*;

import java.util.ArrayList;
import java.util.logging.Logger;

public final class HastePotions extends JavaPlugin {
    public static ArrayList<BrewingRecipe> recipes = new ArrayList<>();
    public static Logger logger;
    public static Plugin getInstance() {
        return HastePotions.getPlugin(HastePotions.class);
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PotionEvent(), this);
        logger = getLogger();
        ItemStack awkward = new ItemStack(Material.POTION);
        awkward.editMeta(meta -> {
            if (meta instanceof PotionMeta potionMeta) {
                potionMeta.setBasePotionType(PotionType.AWKWARD);
            }
        });

        recipes.add(new BrewingRecipe(Material.GOLD_NUGGET, new ItemStack(awkward), new Haste1()));
        //recipes.add(new BrewingRecipe(Material.REDSTONE, new Haste1e()));
        recipes.add(new BrewingRecipe(Material.GOLD_INGOT, new ItemStack(awkward), new Haste2()));
        //recipes.add(new BrewingRecipe(Material.REDSTONE, new Haste2e()));
        recipes.add(new BrewingRecipe(Material.GOLD_BLOCK, new ItemStack(awkward), new Haste3()));
        //recipes.add(new BrewingRecipe(Material.REDSTONE, new Haste3e()));
        recipes.add(new BrewingRecipe(Material.GLOWSTONE_DUST, getHasteBase(2, false), new Haste4()));
        recipes.add(new BrewingRecipe(Material.REDSTONE, getHasteBase(3, false), new Haste4e()));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ItemStack getHasteBase(int level, boolean extended){
        ItemStack hasteBase = new ItemStack(Material.POTION);
        hasteBase.editMeta(meta -> {
            if (meta instanceof PotionMeta potionMeta) {
                potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HASTE, (extended ? 12000 : 3600), level, true, true), true);
            }
        });
        return hasteBase;
    }
}
