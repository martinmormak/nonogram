package service;

import sk.tuke.gamestudio.entity.Rating;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;

import java.util.Date;

public class RatingTest {
    @Test
    public void testToString(){
        Date date=new Date();
        Rating rating =new Rating("nonogram","player",1,date);
        Assertions.assertEquals("Rating{gameName='nonogram', playersName='player', stars=1, ratedAt=" + date + "}", rating.toString());
    }
}
