package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Data;

import java.util.Date;
import java.util.List;

public interface DataService {
    void saveGameData(Data savedGameData, int slotNumber);
    Data getSavedGameData(String game,int slotNumber,String player);

    List<Data> getSavedGames(String game);
    String getPlayer(String game, int ident);
    int getSavedGameIdent(String game,String player);
    Date getSavedGameDate(String game, String player);
    int getSavedGameScore(String game,String player);
    void deleteSave(String game,int slotNumber,String player);
    void resetData();
}
