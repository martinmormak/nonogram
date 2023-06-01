package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

public interface RatingService {
    void addRating(Rating rating) throws RatingException;
    int getAverageRating(String game) throws RatingException;
    int getRatingValue(String game, String player) throws RatingException;
    void reset() throws RatingException;
}
