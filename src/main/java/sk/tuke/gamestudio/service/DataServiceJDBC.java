package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Data;

import javax.transaction.Transactional;
import java.sql.*;
import java.util.Date;
import java.util.List;

@Transactional
public class DataServiceJDBC implements DataService{
    private static final String JDBC_URL = "jdbc:postgresql://localhost/gamestudio";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "postgres";
    private static final String INSERT_STATEMENT = "INSERT INTO Data (ident, data, game, played_On, player,score) VALUES (?,?,?,?,?,?)";
    public static final String SELECT_STATEMENT = "SELECT ident, data, game, played_On, player,score FROM Data WHERE game=?";
    private static final String DELETE_STATEMENT = "DELETE FROM Data";
    @Override
    public void saveGameData(Data savedGameData, int slotNumber) {
        try (Connection connections = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connections.prepareStatement(INSERT_STATEMENT)
        ) {
            preparedStatement.setInt(1, slotNumber);
            preparedStatement.setBytes(2, savedGameData.getData());
            preparedStatement.setString(3, savedGameData.getGame());
            preparedStatement.setDate(4, (java.sql.Date) savedGameData.getPlayedOn());
            preparedStatement.setString(5, savedGameData.getPlayer());
            preparedStatement.setInt(6, savedGameData.getScore());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.toString());
            throw new CommentException("Problem inserting comment",e);
        }
    }

    @Override
    public Data getSavedGameData(String game, int slotNumber, String player) {
        return null;
    }

    @Override
    public List<Data> getSavedGames(String game) {
        return null;
    }

    @Override
    public String getPlayer(String game, int ident) {
        return null;
    }

    @Override
    public int getSavedGameIdent(String game, String player) {
        return 0;
    }

    @Override
    public Date getSavedGameDate(String game, String player) {
        return null;
    }

    @Override
    public int getSavedGameScore(String game, String player) {
        return 0;
    }

    @Override
    public void deleteSave(String game, int slotNumber, String player) {

    }

    @Override
    public void resetData() {

    }
}
