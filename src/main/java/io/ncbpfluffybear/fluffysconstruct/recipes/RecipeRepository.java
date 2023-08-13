package io.ncbpfluffybear.fluffysconstruct.recipes;

import io.ncbpfluffybear.fluffysconstruct.api.items.FCItem;

import java.util.HashMap;
import java.util.Map;

public class RecipeRepository {

    private final Map<FCItem, FCFurnaceRecipe> furnaceItems;

    public RecipeRepository() {
        furnaceItems = new HashMap<>();
    }

    public void registerFurnaceRecipe(FCFurnaceRecipe recipe) {
        if (this.furnaceItems.containsKey(recipe.getFCInput())) {
            throw new RuntimeException("There already exists a recipe with the input " + recipe.getFCInput().getKey());
        }

        this.furnaceItems.put(recipe.getFCInput(), recipe);
    }

    public boolean canSmelt(FCItem fcItem) {
        return this.furnaceItems.containsKey(fcItem);
    }

}
