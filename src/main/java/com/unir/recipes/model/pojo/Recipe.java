package com.unir.recipes.model.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "recipes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;

    @Column(name = "nameRecipe", unique = true)
    private String nameRecipe;

    @Column(name = "calories")
    private String calories;

    @Column(name = "ingredients")
    private String ingredients;

    @Column(name = "image")
    private String image;

    public void update(RecipeDto recipeDto){
        this.nameRecipe = recipeDto.getNameRecipe();
        this.calories = recipeDto.getCalories();
        this.ingredients = recipeDto.getIngredients();
        this.image = recipeDto.getImage();
    }
}
