package service;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Configuration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RatingServiceJPATest.class},webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan(basePackages = "sk.tuke.gamestudio.*")
@EntityScan(basePackages = "sk.tuke.gamestudio.*")
public class RatingServiceJPATest {
    @Autowired
    private RatingService ratingService;
    @Test
    public void testRating(){
        ratingService.reset();
        Rating rating0 =new Rating("nonogram","martin", 0, new Date());
        Rating rating1 =new Rating("nonogram", "martin", 1, new Date());
        Rating rating2 =new Rating("mines", "martin", 1, new Date());
        Rating rating3 =new Rating("tiles", "martin", 2, new Date());
        Rating rating4 =new Rating("nonogram", "katka", 3, new Date());
        Rating rating5 =new Rating("nonogram", "juraj", 4, new Date());
        ratingService.addRating(rating0);
        ratingService.addRating(rating1);
        ratingService.addRating(rating2);
        ratingService.addRating(rating3);
        ratingService.addRating(rating4);
        ratingService.addRating(rating5);


        List<Rating> allRatings =new ArrayList<>();
        allRatings.add(rating1);
        allRatings.add(rating2);
        allRatings.add(rating3);
        allRatings.add(rating4);
        allRatings.add(rating5);

        List<Rating> expectedRatings = allRatings.stream().filter(score -> score.getGame().equals("nonogram")).filter(score -> score.getPlayer().equals("martin")).toList();

        int rating=ratingService.getRatingValue("nonogram","martin");
        Assertions.assertEquals(expectedRatings.get(0).getStars(),rating);
    }
}
