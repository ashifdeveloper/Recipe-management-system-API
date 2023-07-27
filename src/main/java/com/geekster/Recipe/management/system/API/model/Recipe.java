package com.geekster.Recipe.management.system.API.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.mapping.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recipeId;
    @NotBlank
    private String recipeName;
    @NotBlank
    private String recipeIngredients;
    @Size(min = 5,max = 200)


    private String recipeInstructions;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "fk_user_id")
    private User user;

}



