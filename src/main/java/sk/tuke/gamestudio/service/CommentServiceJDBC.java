package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentServiceJDBC implements CommentService {
    private static final String JDBC_URL = "jdbc:postgresql://localhost/gamestudio";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "postgres";
    private static final String INSERT_STATEMENT = "INSERT INTO Comment (game, player, comment, commented_On) VALUES (?,?,?,?)";
    public static final String SELECT_STATEMENT = "SELECT game, player, comment, commented_On FROM Comment WHERE game=? ORDER BY commented_On DESC LIMIT 5";
    private static final String DELETE_STATEMENT = "DELETE FROM Comment";
    @Override
    public void addComment(Comment comment) {
        try (Connection connections = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connections.prepareStatement(INSERT_STATEMENT)
        ) {
            preparedStatement.setString(1, comment.getGame());
            preparedStatement.setString(2, comment.getPlayer());
            preparedStatement.setString(3, comment.getComment());
            preparedStatement.setTimestamp(4, new Timestamp(comment.getCommentedOn().getTime()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new CommentException("Problem inserting comment",e);
        }
    }

    @Override
    public List<Comment> getComments(String gameName) {
        try (Connection connections = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connections.prepareStatement(SELECT_STATEMENT)
        ) {
            preparedStatement.setString(1, gameName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Comment> comments = new ArrayList<>();
                while (resultSet.next()) {
                    comments.add(new Comment(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getTimestamp(4)));
                }
                return comments;
            }
        } catch (SQLException e) {
            throw new CommentException("Problem selecting comment",e);
        }
    }

    @Override
    public void reset() {
        try (Connection connections = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement statement = connections.createStatement()
        ) {
            statement.executeUpdate(DELETE_STATEMENT);
        } catch (SQLException e) {
            throw new CommentException("Problem deleting comment",e);
        }
    }
}
