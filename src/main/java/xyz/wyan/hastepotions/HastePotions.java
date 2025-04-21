package xyz.wyan.hastepotions;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
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

        recipes.add(new BrewingRecipe(Material.GOLD_NUGGET, new Haste1()));
        //recipes.add(new BrewingRecipe(Material.REDSTONE, new Haste1e()));
        recipes.add(new BrewingRecipe(Material.GOLD_INGOT, new Haste2()));
        //recipes.add(new BrewingRecipe(Material.REDSTONE, new Haste2e()));
        recipes.add(new BrewingRecipe(Material.GOLD_BLOCK, new Haste3()));
        //recipes.add(new BrewingRecipe(Material.REDSTONE, new Haste3e()));
        recipes.add(new BrewingRecipe(Material.GLOWSTONE_DUST, new Haste4()));
        recipes.add(new BrewingRecipe(Material.REDSTONE, new Haste4e()));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
