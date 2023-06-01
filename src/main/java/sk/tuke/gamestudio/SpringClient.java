package sk.tuke.gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.core.Field;
import sk.tuke.gamestudio.service.FieldGeneratorJDBC;
import sk.tuke.gamestudio.service.*;
import sk.tuke.gamestudio.userInterface.ConsoleUI;

import java.io.FileNotFoundException;
import java.util.Scanner;


@SpringBootApplication
@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
        pattern = "sk.tuke.gamestudio.server.*"))
public class SpringClient {
    public static void main(String[] args) {
        //SpringApplication.run(SpringClient.class);
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
    }
    @Bean
    public CommandLineRunner runner(ConsoleUI consoleUI) {
        return s -> consoleUI.play();
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
    public ConsoleUI consoleUI(Field field,String playerName) {
        return new ConsoleUI(field,playerName);

    }
    @Bean
    public String playerName() {
        System.out.println("Enter your name");
        Scanner scanner=new Scanner(System.in);
        return scanner.nextLine();

    }
    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceRestClient();
    }
    @Bean
    public RatingService ratingService() {
        return new RatingServiceRestClient();
    }
    @Bean
    public CommentService commentService() {
        return new CommentServiceRestClient();
    }
    @Bean
    public DataService dataService() {
        return new DataServiceJPA();
    }
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
