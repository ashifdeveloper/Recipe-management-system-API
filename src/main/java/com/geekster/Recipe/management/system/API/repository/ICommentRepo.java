package com.geekster.Recipe.management.system.API.repository;

import com.geekster.Recipe.management.system.API.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICommentRepo extends JpaRepository<Comment,Integer> {
}
