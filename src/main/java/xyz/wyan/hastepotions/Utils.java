package xyz.wyan.hastepotions;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class Utils {


    public static boolean isWaterBottle(ItemStack item){
        if (item == null || item.getType() != Material.POTION) return false;

        if (!(item.getItemMeta() instanceof PotionMeta)) return false;

        PotionMeta meta = (PotionMeta) item.getItemMeta();

        // Check if there are no custom effects and the base effect is null (i.e., it's a plain water bottle)
        return meta.getBasePotionType() == org.bukkit.potion.PotionType.WATER && meta.getCustomEffects().isEmpty();
    }
    public static boolean isAwkwardPotion(ItemStack item){
        if (item == null || item.getType() != Material.POTION) return false;

        if (!(item.getItemMeta() instanceof PotionMeta)) return false;

        PotionMeta meta = (PotionMeta) item.getItemMeta();

        return meta.getBasePotionType() == PotionType.AWKWARD;
    }

    public static void setPotion(Inventory inventory, int tier, boolean extended){
        ItemStack hastePotion = new ItemStack(Material.POTION, 1);
        ItemMeta meta = hastePotion.getItemMeta();
        PotionMeta potionMeta = (PotionMeta) meta;
        HastePotions.logger.info("Setting potion to tier "+tier+1+" extended: "+extended);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HASTE, (extended ? 12000: 3600), tier, true, true), true);
        potionMeta.setDisplayName("§6Haste "+ (tier == 0 ? "I" : tier == 1 ? "II" : tier ==2 ? "III" : "IV")+" Potion");
        hastePotion.setItemMeta(potionMeta);
        if(tier < 3){
            for (int i = 0; i<3; i++){
                if(inventory.getItem(i) != null && Utils.isAwkwardPotion(inventory.getItem(i))){
                    inventory.setItem(i, hastePotion);
                }
            }
        }else{
            for (int i = 0; i<3; i++) {
                ItemStack item = inventory.getItem(i);
                if (item != null && item.getType() == Material.POTION) {
                    ItemMeta md = item.getItemMeta();
                    PotionMeta pmd = (PotionMeta) md;
                    if (pmd != null) {
                        for (PotionEffect effect : pmd.getCustomEffects()) {
                            if (effect.getType() == PotionEffectType.HASTE && effect.getAmplifier() == (extended? tier : tier - 1)) {
                                inventory.setItem(i, hastePotion);
                                break;
                            }
                        }
                    }
                }
            }

        }

    }

}
