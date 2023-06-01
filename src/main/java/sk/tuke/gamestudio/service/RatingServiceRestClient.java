package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Rating;

public class RatingServiceRestClient implements RatingService {
    private final String url = "http://localhost:8080/api/rating";

    @Autowired
    private RestTemplate restTemplate;
    //private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void addRating(Rating rating) {
        restTemplate.postForEntity(url, rating, Rating.class);
    }

    @Override
    public int getAverageRating(String gameName) {
        /*Rating[] ratings = restTemplate.getForEntity(url + "/" + gameName, Rating[].class).getBody();
        int i;
        for (Rating rating : ratings) {
            return rating.getStars();
        }*/
        return restTemplate.getForEntity(url + "/" + gameName, Integer.class).getBody();
    }

    @Override
    public int getRatingValue(String gameName, String player) {
        /*int ratings = restTemplate.getForEntity(url + "/" + gameName+ "/" + player, Integer.class).getBody();
        for (Rating rating : ratings) {
            return rating.getStars();
        }*/
        return restTemplate.getForEntity(url + "/" + gameName+ "/" + player, Integer.class).getBody();
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
