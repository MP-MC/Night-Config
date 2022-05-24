package com.electronwill.nightconfig.core.utils.minecraft.converters.recipe;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class UnfinishedRecipe {
	protected final HashMap<Character, ItemStack> ingredients = new HashMap<>();
	protected final List<String> shape;

	protected UnfinishedRecipe(List<String> shape) {
		this.shape = shape;
	}

	void addIngredient(char key, ItemStack ingredient) {
		ingredients.put(key, ingredient);
	}
	Map<Character, ItemStack> getIngredients() {
		return Collections.unmodifiableMap(ingredients);
	}
	List<String> getShape() {
		return shape;
	}

	public abstract Recipe build(ShapedRecipe recipe);
	public abstract Recipe build(NamespacedKey key, ItemStack result);
	public abstract Recipe build(ItemStack result);

}
