package com.geekster.Recipe.management.system.API.service;

import com.geekster.Recipe.management.system.API.model.Comment;
import com.geekster.Recipe.management.system.API.model.Recipe;
import com.geekster.Recipe.management.system.API.model.User;
import com.geekster.Recipe.management.system.API.repository.ICommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@Service
public class CommentService {
    @Autowired
    ICommentRepo commentRepo;


    public String addComment(Comment comment) {
        comment.setCommentCreationTimeStamp(LocalDateTime.now());

        commentRepo.save(comment);
        return "Comment has been added!!!";
    }
    public Comment findComment(Integer commentId) {
        return  commentRepo.findById(commentId).orElse(null);
    }
    public String updateCommentById(Integer commentId,String email, String commentText) throws AccessDeniedException {
        Comment comment = commentRepo.findById(commentId).orElse(null);

        if (comment != null && comment.getCommenter().getUserEmail().equals(email)) {
            comment.setCommentText(commentText);


            commentRepo.save(comment);
            return "Comment Updated successfully";
        } else if (comment == null) {
            return "Comment to be Updated does not exist";
        } else {
            return "Un-Authorized Update detected....Not allowed";
        }
    }

    public void removeComment(Comment comment) {
        commentRepo.delete(comment);
    }

}
