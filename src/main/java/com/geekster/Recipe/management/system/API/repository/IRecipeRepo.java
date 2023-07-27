package com.geekster.Recipe.management.system.API.repository;

import com.geekster.Recipe.management.system.API.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRecipeRepo extends JpaRepository<Recipe,Integer> {

    List<Recipe> findByRecipeNameContainingIgnoreCaseOrRecipeIngredientsContainingIgnoreCase(String keyword, String keyword1);
}
