package binaris.convertor_block.custom_recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Probability_Recipe {
    public static HashMap<ItemStack, List<ItemStack>> recipe_list = new HashMap<>();

    public static void loadRecipes(){
        recipe_list.put(new ItemStack(Items.ROTTEN_FLESH), Arrays.asList(Items.LEATHER.getDefaultStack(), Items.LEATHER.getDefaultStack(), ItemStack.EMPTY));
        recipe_list.put(Items.IRON_BOOTS.getDefaultStack(), Arrays.asList(new ItemStack(Items.IRON_BARS, 2), new ItemStack(Items.IRON_BARS, 4)));
        recipe_list.put(Items.IRON_HELMET.getDefaultStack(), Arrays.asList(new ItemStack(Items.IRON_BARS, 2), new ItemStack(Items.IRON_BARS, 4)));
        recipe_list.put(Items.IRON_CHESTPLATE.getDefaultStack(), Arrays.asList(new ItemStack(Items.IRON_BARS, 2), new ItemStack(Items.IRON_BARS, 4)));

    }
}
