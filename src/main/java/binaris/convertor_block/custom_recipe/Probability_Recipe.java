package binaris.convertor_block.custom_recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Probability_Recipe {
    public static HashMap<Item, List<ItemStack>> recipe_list = new HashMap<>();

    public static void loadRecipes(){
        recipe_list.put(Items.ROTTEN_FLESH, Collections.singletonList(Items.LEATHER.getDefaultStack()));

        recipe_list.put(Items.IRON_BOOTS, List.of(new ItemStack(Items.IRON_INGOT, 2), new ItemStack(Items.IRON_INGOT, 1)));
        recipe_list.put(Items.IRON_HELMET, List.of(new ItemStack(Items.IRON_INGOT, 2), new ItemStack(Items.IRON_INGOT, 3)));
        recipe_list.put(Items.IRON_CHESTPLATE, List.of(new ItemStack(Items.IRON_INGOT, 3), new ItemStack(Items.IRON_BARS, 4)));
        recipe_list.put(Items.IRON_LEGGINGS, List.of(new ItemStack(Items.IRON_INGOT, 3), new ItemStack(Items.IRON_BARS, 4)));
        recipe_list.put(Items.IRON_AXE, Collections.singletonList(new ItemStack(Items.IRON_INGOT)));
        recipe_list.put(Items.IRON_SWORD, Collections.singletonList(new ItemStack(Items.IRON_INGOT)));
        recipe_list.put(Items.IRON_SHOVEL, Collections.singletonList(new ItemStack(Items.IRON_NUGGET, 7)));
        recipe_list.put(Items.IRON_PICKAXE, Collections.singletonList(new ItemStack(Items.IRON_INGOT)));



        recipe_list.put(Items.FEATHER, List.of(new ItemStack(Items.ARROW, 1), new ItemStack(Items.ARROW, 2)));
        recipe_list.put(Items.INK_SAC, Collections.singletonList(new ItemStack(Items.GLOW_INK_SAC, 1)));

        recipe_list.put(Items.GOLDEN_BOOTS, List.of(new ItemStack(Items.GOLD_INGOT, 2), new ItemStack(Items.GOLD_INGOT, 1)));
        recipe_list.put(Items.GOLDEN_HELMET, List.of(new ItemStack(Items.GOLD_INGOT, 2), new ItemStack(Items.GOLD_INGOT, 3)));
        recipe_list.put(Items.GOLDEN_CHESTPLATE, List.of(new ItemStack(Items.GOLD_INGOT, 3), new ItemStack(Items.GOLD_INGOT, 4)));
        recipe_list.put(Items.GOLDEN_LEGGINGS, List.of(new ItemStack(Items.GOLD_INGOT, 3), new ItemStack(Items.GOLD_INGOT, 4)));

        recipe_list.put(Items.CROSSBOW, Collections.singletonList(new ItemStack(Items.STRING, 2)));
        recipe_list.put(Items.BLAZE_ROD, Collections.singletonList(new ItemStack(Items.BLAZE_POWDER, 3)));
        recipe_list.put(Items.WET_SPONGE, Collections.singletonList(new ItemStack(Items.SPONGE)));
        recipe_list.put(Items.PRISMARINE_SHARD, Collections.singletonList(new ItemStack(Items.PRISMARINE)));

        recipe_list.put(Items.SLIME_BALL, Collections.singletonList(new ItemStack(Items.MAGMA_CREAM)));
        recipe_list.put(Items.MAGMA_CREAM, Collections.singletonList(new ItemStack(Items.SLIME_BALL)));

        recipe_list.put(Items.BONE, List.of(new ItemStack(Items.BONE_MEAL, 4), new ItemStack(Items.BONE_MEAL, 3)));
        recipe_list.put(Items.GLOWSTONE, List.of(new ItemStack(Items.GLOWSTONE_DUST, 3), new ItemStack(Items.GLOWSTONE_DUST, 4)));

        recipe_list.put(Items.CHAINMAIL_HELMET, List.of(new ItemStack(Items.IRON_INGOT)));
        recipe_list.put(Items.CHAINMAIL_LEGGINGS, List.of(new ItemStack(Items.IRON_INGOT)));
        recipe_list.put(Items.CHAINMAIL_BOOTS, List.of(new ItemStack(Items.IRON_INGOT)));
        recipe_list.put(Items.CHAINMAIL_CHESTPLATE, List.of(new ItemStack(Items.IRON_INGOT, 2)));
        recipe_list.put(Items.SHIELD, List.of(new ItemStack(Items.IRON_INGOT)));

        recipe_list.put(Items.LEATHER, List.of(new ItemStack(Items.RABBIT_HIDE)));
        recipe_list.put(Items.RABBIT_HIDE, List.of(new ItemStack(Items.LEATHER)));
        recipe_list.put(Items.BRICK, List.of(new ItemStack(Items.NETHER_BRICK)));




    }
}
