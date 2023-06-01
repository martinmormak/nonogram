package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class RatingServiceJPA implements RatingService{

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public void addRating(Rating rating) throws RatingException {
        Rating ratingObject= getRating(rating.getGame(), rating.getPlayer());
        if (ratingObject == null) {
            entityManager.persist(rating);
        } else {
            ratingObject.setStars(rating.getStars());
            ratingObject.setRatedAt(rating.getRatedAt());
        }
    }

    private Rating getRating(String game, String player) throws RatingException {
        try {
            Rating rating = (Rating) entityManager.createNamedQuery("Rating.getRating")
                    .setParameter("game", game)
                    .setParameter("player", player)
                    .getSingleResult();
            return rating;
        }
        catch (NoResultException noResultException) {
            return null;
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        /*try {
            Double averageRating = (Double) entityManager.createNamedQuery("Rating.getAverageRating").setParameter("game", game).getSingleResult();
            return averageRating.intValue();
        }
        catch (NoResultException noResultException) {
            return -1;
        }*/
        try {
            Double averageRating = (Double) entityManager.createNamedQuery("Rating.getAverageRating").setParameter("game", game).getSingleResult();
            return averageRating.intValue();
        }
        catch (NoResultException noResultException) {
            return -1;
        }
        catch (Exception exception){
            return -1;
        }
    }

    @Override
    public int getRatingValue(String game, String player) throws RatingException {
        try {
            Rating rating = (Rating) entityManager.createNamedQuery("Rating.getRating")
                    .setParameter("game", game)
                    .setParameter("player", player)
                    .getSingleResult();
            return rating.getStars();
        }
        catch (NoResultException noResultException) {
            return -1;
        }
    }

    @Override
    public void reset() throws RatingException {
        entityManager.createNamedQuery("Rating.resetRating").executeUpdate();
    }
}
