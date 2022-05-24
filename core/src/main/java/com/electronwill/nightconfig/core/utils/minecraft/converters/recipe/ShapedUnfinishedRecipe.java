package com.electronwill.nightconfig.core.utils.minecraft.converters.recipe;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;
import java.util.Map;

public class ShapedUnfinishedRecipe extends UnfinishedRecipe {

	public ShapedUnfinishedRecipe(List<String> shape) {
		super(shape);
	}

	@Override
	public Recipe build(ShapedRecipe recipe) {
		recipe.shape(
			shape.toArray(new String[]{})
		);

		for(Map.Entry<Character, ItemStack> entry : ingredients.entrySet()) {
			recipe.setIngredient(entry.getKey(), entry.getValue().getType());
		}

		return recipe;
	}

	public Recipe build(NamespacedKey key, ItemStack result) {
		return build(new ShapedRecipe(key, result));
	}

	public Recipe build(ItemStack result) {
		return build(new ShapedRecipe(result));
	}


}

