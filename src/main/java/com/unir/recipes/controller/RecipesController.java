package com.unir.recipes.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.unir.recipes.model.pojo.RecipeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.unir.recipes.model.pojo.Recipe;
import com.unir.recipes.model.request.CreateRecipeRequest;
import com.unir.recipes.service.RecipesService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Recetas Controller", description = "Microservicio encargado de exponer operaciones CRUD sobre recertas alojadas en una base de datos en Postgres.")

public class RecipesController {
    private final RecipesService service;

    @GetMapping("/recipes")
    @Operation(
            operationId = "Obtener productos",
            description = "Operacion de lectura",
            summary = "Se devuelve una lista de todos los productos almacenados en la base de datos.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class)))
    public ResponseEntity<List<Recipe>> getRecipes(
            @RequestHeader Map<String, String> headers,
            @Parameter(name = "nameRecipe", description = "Nombre de la receta", example = "Salmon con patatas", required = false)
            @RequestParam(required = false) String nameRecipe,
            @Parameter(name = "calories", description = "Calorias de la receta", example = "300", required = false)
            @RequestParam(required = false) String calories,
            @Parameter(name = "ingredients", description = "ingredientesque compone la receta", example= "Salmon, patatas", required = false)
            @RequestParam(required = false) String ingredients,
            @Parameter(name = "image", description = "Imagen de la receta", example="Imagen de la receta del salmon con patatas")
            @RequestParam(required = false) String image){

        log.info("headers: {}", headers);
        List<Recipe> recipes = service.getRecipes(nameRecipe, calories, ingredients, image);

        if(recipes != null){
            return ResponseEntity.ok(recipes);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/recipes/{recipeId}")
    @Operation(
            operationId = "Obtener la receta",
            description = "Operacion de lectura",
            summary = "Se devuelve un producto a partir de su identificador.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado la receta con el identificador indicado.")
    public ResponseEntity<Recipe> getRecipe(@PathVariable String recipeId){

        log.info("Request received for product {}", recipeId);
        Recipe recipe = service.getRecipe(recipeId);

        if(recipe != null){
            return ResponseEntity.ok(recipe);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/recipes/{recipeId}")
    @Operation(
            operationId = "Eliminar una receta",
            description = "Operacion de escritura",
            summary = "Se elimina una receta a partir de su identificador.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado el producto con el identificador indicado.")
    public  ResponseEntity<Void> deleteRecipe(@PathVariable String recipeId){

        Boolean removed = service.removeRecipe(recipeId);

        if(Boolean.TRUE.equals(removed)){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/recipes")
    @Operation(
            operationId = "Inserta una receta",
            description = "Operacion de escritura",
            summary = "Se crea una receta a partir de los datos.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la receta a crear.",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateRecipeRequest.class))))
    @ApiResponse(
            responseCode = "201",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class)))
    @ApiResponse(
            responseCode = "400",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Datos incorrectos introducidos.")
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado el producto con el identificador indicado.")

    public ResponseEntity<Recipe> addRecipe(@RequestBody CreateRecipeRequest request){

        Recipe createRecipe = service.createRecipe(request);

        if(createRecipe != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(createRecipe);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/recipes/{recipeId}")
    @Operation(
            operationId = "Modificar parcialmente un producto",
            description = "RFC 7386. Operacion de escritura",
            summary = "RFC 7386. Se modifica parcialmente un producto.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del producto a crear.",
                    required = true,
                    content = @Content(mediaType = "application/merge-patch+json", schema = @Schema(implementation = String.class))))
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class)))
    @ApiResponse(
            responseCode = "400",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Producto inválido o datos incorrectos introducidos.")
    public ResponseEntity<Recipe> patchRecipe(@PathVariable String recipeId, @RequestBody String patchBody) {

        Recipe patched = service.updateRecipe(recipeId, patchBody);
        if (patched != null) {
            return ResponseEntity.ok(patched);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/recipes/{recipeId}")
    @Operation(
            operationId = "Modificar totalmente un producto",
            description = "Operacion de escritura",
            summary = "Se modifica totalmente un producto.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del producto a actualizar.",
                    required = true,
                    content = @Content(mediaType = "application/merge-patch+json", schema = @Schema(implementation = RecipeDto.class))))
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Producto no encontrado.")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable String recipeId, @RequestBody RecipeDto body) {

        Recipe updated = service.updateRecipe(recipeId, body);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
