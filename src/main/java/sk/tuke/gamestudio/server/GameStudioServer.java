package sk.tuke.gamestudio.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.tuke.gamestudio.core.Field;
import sk.tuke.gamestudio.service.*;

import java.io.FileNotFoundException;


@SpringBootApplication
@Configuration
@EntityScan("sk.tuke.gamestudio.entity")
public class GameStudioServer {
    public static void main(String[] args) {
        SpringApplication.run(GameStudioServer.class, args);
    }

    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceJPA();
    }

    @Bean
    public CommentService commentService() {
        return new CommentServiceJPA();
    }

    @Bean
    public RatingService ratingService() {
        return new RatingServiceJPA();
    }


    @Bean
    public Field field(FieldGenerator fieldGenerator) throws FileNotFoundException {
        return fieldGenerator.generate();
    }
    @Bean
    public FieldGenerator fieldGenerator(){
        return new FieldGeneratorJPA();
    }

    @Bean
    public DataService dataService() {
        return new DataServiceJPA();
    }

    @Bean
    public LoginService loginService() {
        return new LoginServiceJPA();
    }
}