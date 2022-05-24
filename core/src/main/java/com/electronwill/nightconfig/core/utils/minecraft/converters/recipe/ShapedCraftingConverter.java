package com.electronwill.nightconfig.core.utils.minecraft.converters.recipe;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.conversion.Converter;
import com.electronwill.nightconfig.core.utils.minecraft.converters.ItemStackConverter;

import java.util.Collections;
import java.util.Map;

public class ShapedCraftingConverter implements Converter<ShapedUnfinishedRecipe, Config> {

	public static final ShapedCraftingConverter DEFAULT = new ShapedCraftingConverter();

	@Override
	public ShapedUnfinishedRecipe convertToField(Config value) {

		ShapedUnfinishedRecipe recipe = new ShapedUnfinishedRecipe(
			value.getOrElse("recipe", Collections.EMPTY_LIST)
		);

		Config ingredients = value.get("ingredients");
		if(ingredients != null) {
			for(Map.Entry<String, Object> entry : ingredients.valueMap().entrySet()) {
				char key = entry.getKey().charAt(3);

				Object rawItem = entry.getValue();
				if(rawItem instanceof Config) {
					recipe.addIngredient(key, ItemStackConverter.DEFAULT.convertToField((Config) rawItem));
				}
			}
		}

		return recipe;
	}

	@Override
	public Config convertFromField(ShapedUnfinishedRecipe value) {
		Config config = Config.inMemory();

		config.set("recipe", value.getShape());
		config.set("ingredients", value.getIngredients());

		return config;
	}
}
