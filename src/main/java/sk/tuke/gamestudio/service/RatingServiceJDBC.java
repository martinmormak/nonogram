package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import java.sql.*;

public class RatingServiceJDBC implements RatingService {
    private static final String JDBC_URL = "jdbc:postgresql://localhost/gamestudio";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "postgres";
    private static final String INSERT_STATEMENT = "INSERT INTO Rating (player, game, stars, rated_At) VALUES (?, ?, ?, ?) ON CONFLICT (player, game) DO UPDATE SET stars = ?, rated_At = ?;";
    public static final String SELECT_STATEMENT = "SELECT player, game, stars, rated_At FROM Rating WHERE game=? AND player=?";
    public static final String AVERAGE_STATEMENT = "SELECT avg(stars) FROM Rating WHERE game=?";
    private static final String DELETE_STATEMENT = "DELETE FROM Rating";

    @Override
    public void addRating(Rating rating) {
        try (Connection connections = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connections.prepareStatement(INSERT_STATEMENT)
        ) {
            Timestamp timestamp =new Timestamp(rating.getRatedAt().getTime());
            preparedStatement.setString(1, rating.getPlayer());
            preparedStatement.setString(2, rating.getGame());
            preparedStatement.setInt(3, rating.getStars());
            preparedStatement.setTimestamp(4, timestamp);
            preparedStatement.setInt(5, rating.getStars());
            preparedStatement.setTimestamp(6, timestamp);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RatingException("Problem inserting rating",e);
        }

    }

    @Override
    public int getRatingValue(String game, String player) {
        try (Connection connections = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement selectPreparedStatement = connections.prepareStatement(SELECT_STATEMENT)
        ) {
            selectPreparedStatement.setString(1, game);
            selectPreparedStatement.setString(2, player);
            try (ResultSet resultSet = selectPreparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return resultSet.getInt(3);
                }else {
                    return -1;
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Problem selecting rating",e);
        }
    }

    @Override
    public int getAverageRating(String game) {
        try (Connection connections = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement averagePreparedStatement = connections.prepareStatement(AVERAGE_STATEMENT)
        ) {
            averagePreparedStatement.setString(1, game);
            try (ResultSet resultSet = averagePreparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return resultSet.getInt(1);
                }else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Problem selecting average rating",e);
        }
    }

    @Override
    public void reset() {
        try (Connection connections = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement statement = connections.createStatement()
        ) {
            statement.executeUpdate(DELETE_STATEMENT);
        } catch (SQLException e) {
            throw new RatingException("Problem deleting rating",e);
        }
    }
}
