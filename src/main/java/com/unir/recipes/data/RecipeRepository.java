package com.unir.recipes.data;

import com.unir.recipes.data.utils.SearchCriteria;
import com.unir.recipes.data.utils.SearchOperation;
import com.unir.recipes.data.utils.SearchStatement;
import com.unir.recipes.model.pojo.Recipe;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecipeRepository {

    private final RecipeJpaRepository repository;

    public List<Recipe> getRecipes(){
        return repository.findAll();
    }

    public Recipe getById(Long id){
        return repository.findById(id).orElse(null);
    }

    public Recipe save(Recipe recipe){
        return repository.save(recipe);
    }

    public void delete(Recipe recipe){
        repository.delete(recipe);
    }

    public List<Recipe> search(String nameRecipe, String calories, String ingredients, String image){
        SearchCriteria<Recipe> spec = new SearchCriteria<>();
        if(StringUtils.isNotBlank(nameRecipe)){
            spec.add(new SearchStatement("name",nameRecipe, SearchOperation.MATCH));
        }

        if (StringUtils.isNotBlank(calories)) {
            spec.add(new SearchStatement("calories", calories, SearchOperation.EQUAL));
        }

        if (StringUtils.isNotBlank(ingredients)) {
            spec.add(new SearchStatement("ingredients", ingredients, SearchOperation.MATCH));
        }

        if (StringUtils.isNotBlank(image)) {
            spec.add(new SearchStatement("image", image, SearchOperation.EQUAL));
        }
        return repository.findAll(spec);
    }
}
