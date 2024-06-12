package com.unir.recipes.service;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.unir.recipes.data.RecipeRepository;
import com.unir.recipes.model.pojo.RecipeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.unir.recipes.model.pojo.Recipe;
import com.unir.recipes.model.request.CreateRecipeRequest;

@Service
@Slf4j
public class RecipesServiceImpl implements RecipesService{

    @Autowired
    private RecipeRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<Recipe> getRecipes(String nameRecipe, String calories, String ingredients, String image){

        if(StringUtils.hasLength(nameRecipe) || StringUtils.hasLength(calories) || StringUtils.hasLength(ingredients)
                || StringUtils.hasLength(image)){
            return repository.search(nameRecipe, calories, ingredients, image);
        }
        List<Recipe> recipes = repository.getRecipes();
        return recipes.isEmpty() ? null : recipes;
    }

    @Override
    public Recipe getRecipe(String recipeId) { return repository.getById(Long.valueOf(recipeId)); }

    @Override
    public Boolean removeRecipe(String recipeId){
        Recipe recipe = repository.getById(Long.valueOf(recipeId));

        if(recipe != null){
            repository.delete(recipe);
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }

    @Override
    public Recipe createRecipe(CreateRecipeRequest request){

        if(request != null && StringUtils.hasLength(request.getNameRecipe().trim())
            && StringUtils.hasLength(request.getCalories().trim())
            && StringUtils.hasLength(request.getIngredients().trim())
            && StringUtils.hasLength(request.getImage().trim())){

            Recipe recipe = Recipe.builder().nameRecipe(request.getNameRecipe()).calories(request.getCalories())
                    .ingredients(request.getIngredients()).image(request.getImage()).build();

            return repository.save(recipe);
        }else{
            return null;
        }
    }

    @Override
    public Recipe updateRecipe(String recipeId, String request){

        Recipe recipe = repository.getById(Long.valueOf(recipeId));

        if(recipe != null){
            try {
                JsonMergePatch jsonMergePatch = JsonMergePatch.fromJson(objectMapper.readTree(request));
                JsonNode target = jsonMergePatch.apply(objectMapper.readTree(objectMapper.writeValueAsString(recipe)));
                Recipe patched = objectMapper.treeToValue(target, Recipe.class);
                repository.save(patched);
                return patched;
            } catch (JsonProcessingException | JsonPatchException e){
                log.error("Error updating recipe {}", recipeId, e);
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public Recipe updateRecipe(String recipeId, RecipeDto updateRequest){
        Recipe recipe = repository.getById(Long.valueOf(recipeId));
        if(recipe != null){
            recipe.update(updateRequest);
            repository.save(recipe);
            return recipe;
        } else{
            return null;
        }
    }
}
