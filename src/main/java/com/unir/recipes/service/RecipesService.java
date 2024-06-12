package com.unir.recipes.service;

import java.util.List;

import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.unir.recipes.model.pojo.Recipe;
import com.unir.recipes.model.pojo.RecipeDto;
import com.unir.recipes.model.request.CreateRecipeRequest;

public interface RecipesService {

    List<Recipe> getRecipes(String nameRecipe, String calories, String ingredients, String image);

    Recipe getRecipe(String recipeId);

    Boolean removeRecipe(String recipeId);

    Recipe createRecipe(CreateRecipeRequest request);

    Recipe updateRecipe(String recipeId, String updateRequest);

    Recipe updateRecipe(String recipeId, RecipeDto updateRequest);
}
