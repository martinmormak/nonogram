package service;

import sk.tuke.gamestudio.entity.Comment;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.CommentServiceJDBC;

import java.util.*;

public class CommentServiceJDBCTest {
    private CommentService commentService = new CommentServiceJDBC();
    @Test
    public void testComment(){
        commentService.reset();
        Comment comment1=new Comment("nonogram", "martin", "Very good game1", new Date(new Random().nextInt()));
        Comment comment2=new Comment("mines", "martin", "Very good game2", new Date(new Random().nextInt()));
        Comment comment3=new Comment("tiles", "martin", "Very good game3", new Date(new Random().nextInt()));
        Comment comment4=new Comment("nonogram", "katka", "Very good game4", new Date(new Random().nextInt()));
        Comment comment5=new Comment("nonogram", "juraj", "Very good game5", new Date(new Random().nextInt()));
        commentService.addComment(comment1);
        commentService.addComment(comment2);
        commentService.addComment(comment3);
        commentService.addComment(comment4);
        commentService.addComment(comment5);


        List<Comment> allComments=new ArrayList<>();
        allComments.add(comment1);
        allComments.add(comment2);
        allComments.add(comment3);
        allComments.add(comment4);
        allComments.add(comment5);

        List<Comment> expectedComments=allComments.stream().filter(comment -> comment.getGame().equals("nonogram")).sorted(Comparator.comparing(Comment::getCommentedOn).reversed()).toList();

        List<Comment> comments = commentService.getComments("nonogram");


        for(int i=0;i<expectedComments.size();i++) {
            Assertions.assertEquals(expectedComments.get(i).getPlayer(), comments.get(i).getPlayer());
            Assertions.assertEquals(expectedComments.get(i).getGame(), comments.get(i).getGame());
            Assertions.assertEquals(expectedComments.get(i).getComment(), comments.get(i).getComment());
            Assertions.assertEquals(expectedComments.get(i).getCommentedOn().getTime(), comments.get(i).getCommentedOn().getTime());
        }
    }
}
