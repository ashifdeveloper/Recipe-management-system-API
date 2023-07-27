package com.geekster.Recipe.management.system.API.controller;
import com.geekster.Recipe.management.system.API.model.Comment;
import com.geekster.Recipe.management.system.API.model.Recipe;
import com.geekster.Recipe.management.system.API.model.User;
import com.geekster.Recipe.management.system.API.model.dto.SignInInput;
import com.geekster.Recipe.management.system.API.model.dto.SignUpOutput;
import com.geekster.Recipe.management.system.API.service.AuthenticationService;
import com.geekster.Recipe.management.system.API.service.RecipeService;
import com.geekster.Recipe.management.system.API.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
@Validated
@RestController
public class UserController {
    @Autowired
    UserService userService;


    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    RecipeService recipeService;


//    sign up, sign in , sign out a particular user
    @PostMapping("user/signup")
    public SignUpOutput signUpUser(@RequestBody @Valid User user)
    {

        return userService.signUpUser(user);
    }

    @PostMapping("user/signIn")
    public String sigInUser(@RequestBody @Valid SignInInput signInInput)
    {
        return userService.signInUser(signInInput);
    }

    @DeleteMapping("user/signOut")
    public String signOutUser(String email, String token) {
        if (authenticationService.authenticate(email, token)) {
            return userService.sigOutUser(email);
        } else {
            return "Sign out not allowed for non authenticated user.";
        }

    }


    @PostMapping("recipe")
    public String addRecipe(@RequestBody @Valid Recipe recipe, @RequestParam String email, @RequestParam String token)
    {
        if(authenticationService.authenticate(email,token)) {
            return userService.addRecipe(recipe,email);
        }
        else {
            return "Not an Authenticated user activity!!!";
        }
    }


    @PutMapping("recipe")

    public String updateRecipeById(@RequestParam Integer recipeId, @RequestParam String email, @RequestParam String token, @RequestParam String recipeName,String recipeIngredients,String recipeInstructions) throws AccessDeniedException {
        if (authenticationService.authenticate(email,token)) {
            return userService.updateRecipeById(recipeId,email,recipeName,recipeIngredients,recipeInstructions);
        } else {
            return "Not an Authenticated user activity!!!";
        }
    }


    @DeleteMapping("recipe")
    public String removeRecipe(@RequestParam Integer recipeId, @RequestParam String email, @RequestParam String token)
    {
        if(authenticationService.authenticate(email,token)) {
            return userService.removeRecipe(recipeId,email);
        }
        else {
            return "Not an Authenticated user activity!!!";
        }
    }


    @GetMapping("search")
    public List<Recipe> searchRecipes(@RequestParam String keyword) {
        return userService.searchRecipes(keyword);
    }

    @GetMapping("recipes")
    public List<Recipe> viewRecipes() {
        return userService.viewRecipes();
    }


    @PostMapping("comment")
    public String addComment(@RequestBody Comment comment, @RequestParam String commenterEmail, @RequestParam String commenterToken)
    {
        if(authenticationService.authenticate(commenterEmail,commenterToken)) {
            return userService.addComment(comment,commenterEmail);
        }
        else {
            return "Not an Authenticated user activity!!!";
        }


    }


    @PutMapping("comment")

    public String updateCommentById(@RequestParam Integer commentId, @RequestParam String email, @RequestParam String token, @RequestParam String commentText ) throws AccessDeniedException {
        if (authenticationService.authenticate(email,token)) {
            return userService.updateCommentById(commentId,email,commentText);
        } else {
            return "Not an Authenticated user activity!!!";
        }
    }


    @DeleteMapping("comment")
    public String removeComment(@RequestParam Integer commentId, @RequestParam String email, @RequestParam String token)
    {
        if(authenticationService.authenticate(email,token)) {
            return userService.removeComment(commentId,email);
        }
        else {
            return "Not an Authenticated user activity!!!";
        }
    }


}