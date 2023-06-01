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
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


@Configuration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ScoreServiceJPATest.class},webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan(basePackages = "sk.tuke.gamestudio.*")
@EntityScan(basePackages = "sk.tuke.gamestudio.*")
public class ScoreServiceJPATest {
    @Autowired
    private ScoreService scoreService;
    @Test
    public void testScore(){
        scoreService.reset();
        Score score1=new Score("nonogram", "martin", 110, new Date());
        Score score2=new Score("mines", "martin", 100, new Date());
        Score score3=new Score("tiles", "martin", 110, new Date());
        Score score4=new Score("nonogram", "katka", 128, new Date());
        Score score5=new Score("nonogram", "juraj", 105, new Date());
        scoreService.addScore(score1);
        scoreService.addScore(score2);
        scoreService.addScore(score3);
        scoreService.addScore(score4);
        scoreService.addScore(score5);


        List<Score> allScores=new ArrayList<>();
        allScores.add(score1);
        allScores.add(score2);
        allScores.add(score3);
        allScores.add(score4);
        allScores.add(score5);

        List<Score> expectedScores=allScores.stream().filter(score -> score.getGame().equals("nonogram")).sorted(Comparator.comparing(Score::getPoints).reversed()).toList();

        List<Score> scores = scoreService.getTopScores("nonogram");

        for(int i=0;i<expectedScores.size();i++) {
            Assertions.assertEquals(expectedScores.get(i).getPlayer(), scores.get(i).getPlayer());
            Assertions.assertEquals(expectedScores.get(i).getGame(), scores.get(i).getGame());
            Assertions.assertEquals(expectedScores.get(i).getPoints(), scores.get(i).getPoints());
            Assertions.assertEquals(expectedScores.get(i).getPlayedOn().getTime(), scores.get(i).getPlayedOn().getTime());
        }
    }
}
