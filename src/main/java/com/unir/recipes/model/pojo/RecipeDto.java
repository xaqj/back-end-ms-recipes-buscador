package com.unir.recipes.model.pojo;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RecipeDto {

    private String nameRecipe;
    private String calories;
    private String ingredients;
    private String image;

}
