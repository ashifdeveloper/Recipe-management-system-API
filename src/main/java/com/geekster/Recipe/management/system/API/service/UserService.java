package com.geekster.Recipe.management.system.API.service;

import com.geekster.Recipe.management.system.API.model.*;
import com.geekster.Recipe.management.system.API.model.dto.SignInInput;
import com.geekster.Recipe.management.system.API.model.dto.SignUpOutput;
import com.geekster.Recipe.management.system.API.repository.IUserRepo;
import com.geekster.Recipe.management.system.API.service.hashingUtility.PasswordEncrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class UserService {
    @Autowired
    IUserRepo userRepo;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    RecipeService recipeService;



    @Autowired
    CommentService commentService;



    public SignUpOutput signUpUser(User user) {

        boolean signUpStatus = true;
        String signUpStatusMessage = null;

        String newEmail = user.getUserEmail();

        if (newEmail == null) {
            signUpStatusMessage = "Invalid email";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus, signUpStatusMessage);
        }

        //check if this user email already exists ??
        User existingUser = userRepo.findFirstByUserEmail(newEmail);

        if (existingUser != null) {
            signUpStatusMessage = "Email already registered!!!";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus, signUpStatusMessage);
        }

        //hash the password: encrypt the password
        try {
            String encryptedPassword = PasswordEncrypter.encryptPassword(user.getUserPassword());

            //saveAppointment the user with the new encrypted password

            user.setUserPassword(encryptedPassword);
            userRepo.save(user);

            return new SignUpOutput(signUpStatus, "User registered successfully!!!");
        } catch (Exception e) {
            signUpStatusMessage = "Internal error occurred during sign up";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus, signUpStatusMessage);
        }
    }


    public String signInUser(SignInInput signInInput) {


        String signInStatusMessage = null;

        String signInEmail = signInInput.getEmail();

        if (signInEmail == null) {
            signInStatusMessage = "Invalid email";
            return signInStatusMessage;


        }

        //check if this user email already exists ??
        User existingUser = userRepo.findFirstByUserEmail(signInEmail);

        if (existingUser == null) {
            signInStatusMessage = "Email not registered!!!";
            return signInStatusMessage;

        }

        //match passwords :

        //hash the password: encrypt the password
        try {
            String encryptedPassword = PasswordEncrypter.encryptPassword(signInInput.getPassword());
            if (existingUser.getUserPassword().equals(encryptedPassword)) {
                //session should be created since password matched and user id is valid
                AuthenticationToken authToken = new AuthenticationToken(existingUser);
                authenticationService.saveAuthToken(authToken);
                return "User SignIn Successfully";


            } else {
                signInStatusMessage = "Invalid credentials!!!";
                return signInStatusMessage;
            }
        } catch (Exception e) {
            signInStatusMessage = "Internal error occurred during sign in";
            return signInStatusMessage;
        }

    }


    public String sigOutUser(String email) {

        User user = userRepo.findFirstByUserEmail(email);
        AuthenticationToken token = authenticationService.findFirstByUser(user);
        authenticationService.removeToken(token);
        return "User Signed out successfully";
    }

    public String addRecipe(Recipe recipe, String email) {
        User user = userRepo.findFirstByUserEmail(email);
        recipe.setUser(user);


        return recipeService.addRecipe(recipe);
    }
    public String updateRecipeById(Integer recipeId, String email, String recipeName, String recipeIngredients, String recipeInstructions) throws AccessDeniedException {
        User user = userRepo.findFirstByUserEmail(email);
        return recipeService.updateRecipeById(recipeId,email,recipeName,recipeIngredients,recipeInstructions);
    }




    public String removeRecipe(Integer recipeId,String email) {

        User user = userRepo.findFirstByUserEmail(email);
        return recipeService.removeRecipe(recipeId,user);
    }

    public List<Recipe> viewRecipes() {
        return recipeService.viewRecipes();
    }


    public List<Recipe> searchRecipes(String keyword) {
        return recipeService.searchRecipes(keyword);
    }


    public String addComment(Comment comment, String commenterEmail) {

        boolean recipeValid = recipeService.validateRecipe(comment.getRecipe());
        if(recipeValid) {
            User commenter = userRepo.findFirstByUserEmail(commenterEmail);
            comment.setCommenter(commenter);
            return commentService.addComment(comment);
        }
        else {
            return "Cannot comment on Invalid Recipe!!";
        }
    }

    public String updateCommentById(Integer commentId, String email, String commentText) throws AccessDeniedException {
        User user = userRepo.findFirstByUserEmail(email);
        return commentService.updateCommentById(commentId,email,commentText);
    }
    boolean authorizeCommentRemover(String email,Comment comment)
    {
        String  commentOwnerEmail = comment.getCommenter().getUserEmail();
        String  recipeOwnerEmail  = comment.getRecipe().getUser().getUserEmail();

        return recipeOwnerEmail.equals(email) || commentOwnerEmail.equals(email);
    }
    public String removeComment(Integer commentId, String email) {
        Comment comment  = commentService.findComment(commentId);
        if(comment!=null)
        {
            if(authorizeCommentRemover(email,comment))
            {
                commentService.removeComment(comment);
                return "comment deleted successfully";
            }
            else
            {
                return "Unauthorized delete detected...Not allowed!!!!";
            }

        }
        else
        {
            return "Invalid Comment";
        }
    }



}
