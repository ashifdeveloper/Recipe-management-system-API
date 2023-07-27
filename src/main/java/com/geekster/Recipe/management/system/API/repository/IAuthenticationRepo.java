package com.geekster.Recipe.management.system.API.repository;


import com.geekster.Recipe.management.system.API.model.AuthenticationToken;
import com.geekster.Recipe.management.system.API.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuthenticationRepo extends JpaRepository<AuthenticationToken,Long> {


    AuthenticationToken findFirstByTokenValue(String authTokenValue);

    AuthenticationToken findFirstByUser(User user);
}
