package sk.tuke.gamestudio.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@NamedQuery( name = "Rating.getRating",
        query = "SELECT r FROM Rating r WHERE r.game = :game AND r.player = :player")
@NamedQuery( name = "Rating.resetRating",
        query = "DELETE FROM Rating")
@NamedQuery( name = "Rating.updateRating",
        query = "UPDATE Rating SET stars = :stars, ratedAt =  :ratedAt WHERE game = :game AND player = :player")
@NamedQuery( name = "Rating.getAverageRating",
        query = "SELECT AVG(stars) FROM Rating r WHERE r.game = :game")
        //query = "SELECT AVG(stars) FROM Rating WHERE game = :game")
public class Rating implements Serializable {
    @Id
    private String game;

    @Id
    private String player;
    private Integer stars;
    private Date ratedAt;

    public Rating(String game, String player, Integer stars, Date ratedAt) {
        this.game = game;
        this.player = player;
        if (stars >= 0 && stars <= 5) {
            this.stars = stars;
        } else {
            throw new IllegalArgumentException("Score value must be between 0 and 5.");
        }
        this.ratedAt = ratedAt;
    }

    public Rating(){}

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        if (stars >= 0 && stars <= 5) {
            this.stars = stars;
        } else {
            throw new IllegalArgumentException("Score value must be between 0 and 5.");
        }
    }

    public Date getRatedAt() {
        return ratedAt;
    }

    public void setRatedAt(Date ratedAt) {
        this.ratedAt = ratedAt;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "gameName='" + game + '\'' +
                ", playersName='" + player + '\'' +
                ", stars=" + stars +
                ", ratedAt=" + ratedAt +
                '}';
    }
}
