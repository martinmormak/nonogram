package service;

import sk.tuke.gamestudio.entity.Score;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import sk.tuke.gamestudio.service.ScoreService;
import sk.tuke.gamestudio.service.ScoreServiceJDBC;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ScoreServiceJDBCTest {
    private ScoreService scoreService = new ScoreServiceJDBC();
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
