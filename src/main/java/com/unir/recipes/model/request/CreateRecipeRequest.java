package com.unir.recipes.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateRecipeRequest {

    private String nameRecipe;
    private String calories;
    private String ingredients;
    private String image;
}
