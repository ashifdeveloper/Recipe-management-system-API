package com.geekster.Recipe.management.system.API.service;

import com.geekster.Recipe.management.system.API.model.Recipe;
import com.geekster.Recipe.management.system.API.model.User;
import com.geekster.Recipe.management.system.API.repository.IRecipeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Service
public class RecipeService {

    @Autowired
    IRecipeRepo recipeRepo;

    public String addRecipe(Recipe recipe) {
            recipeRepo.save(recipe);
            return "Recipe Added Successfully!!!!";

    }


    public String updateRecipeById(Integer recipeId, String email, String recipeName, String recipeIngredients, String recipeInstructions){
        Recipe recipe = recipeRepo.findById(recipeId).orElse(null);

        if (recipe != null && recipe.getUser().getUserEmail().equals(email)) {
            recipe.setRecipeName(recipeName);
            recipe.setRecipeIngredients(recipeIngredients);
            recipe.setRecipeInstructions(recipeInstructions);

            recipeRepo.save(recipe);
            return "Recipe Updated successfully";
        } else if (recipe == null) {
            return "Recipe Post to be Updated does not exist";
        } else {
            return "Un-Authorized Update detected....Not allowed";
        }
    }

    public String removeRecipe(Integer recipeId, User user) {

        Recipe recipe  = recipeRepo.findById(recipeId).orElse(null);
        if(recipe != null && recipe.getUser().equals(user))
        {
            recipeRepo.deleteById(recipeId);
            return "Recipe Removed successfully";
        }
        else if (recipe == null)
        {
            return "Recipe Post to be deleted does not exist";
        }
        else{
            return "Un-Authorized delete detected....Not allowed";
        }
    }

    public List<Recipe> viewRecipes() {

        return recipeRepo.findAll();
    }

    public List<Recipe> searchRecipes(String keyword) {
        List<Recipe> foundRecipes = recipeRepo.findByRecipeNameContainingIgnoreCaseOrRecipeIngredientsContainingIgnoreCase(keyword, keyword);

        // Check if no recipes are found
        if (foundRecipes.isEmpty()) {
            Recipe noSearchResult = new Recipe();
            noSearchResult.setRecipeName("No search found"); // Set a message for no search results
            noSearchResult.setRecipeIngredients("No search found"); // Set a message for no search results
            noSearchResult.setRecipeInstructions("No search found"); // Set a message for no search results

            return Collections.singletonList(noSearchResult); // Wrap the message in a singleton list
        } else {
            return foundRecipes;
        }
    }

    public boolean validateRecipe(Recipe recipe) {
        return (recipe!=null && recipeRepo.existsById(recipe.getRecipeId()));
    }



}
