package io.ncbpfluffybear.fluffysconstruct.setup;

import io.ncbpfluffybear.fluffysconstruct.recipes.RecipeList;
import io.ncbpfluffybear.fluffysconstruct.recipes.RecipeRepository;

public class RecipeSetup {

    private final RecipeRepository repository;

    public RecipeSetup(RecipeRepository repository) {
        this.repository = repository;
    }

    public void register() {
        repository.registerFurnaceRecipe(RecipeList.SEARED_BRICKS);
    }

}
