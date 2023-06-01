package service;

import sk.tuke.gamestudio.entity.Comment;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;

import java.util.Date;

public class CommentTest {
    @Test
    public void testToString(){
        Date date=new Date();
        Comment comment=new Comment("nonogram","player","Very good game",date);
        Assertions.assertEquals("Comment{gameName='nonogram', playersName='player', comment='Very good game', commentedAt=" + date + "}",comment.toString());
    }
}
