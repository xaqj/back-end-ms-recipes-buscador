package com.unir.recipes.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.unir.recipes.model.pojo.Recipe;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RecipeJpaRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {

    List<Recipe> findByNameRecipe(String nameRecipe);

    List<Recipe> findByCalories(String calories);

    List<Recipe> findByIngredients(String ingredients);

    List<Recipe> findByNameRecipeAndCalories(String nameRecipe, String calories);
}
