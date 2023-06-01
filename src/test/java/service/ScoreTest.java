package service;

import sk.tuke.gamestudio.entity.Score;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;

import java.util.Date;

public class ScoreTest {
    @Test
    public void testToString(){
        Date date=new Date();
        Score score=new Score("nonogram","player",100,date);
        Assertions.assertEquals("Score{game='nonogram', player='player', points=100, playedOn=" + date + "}",score.toString());
    }
}
