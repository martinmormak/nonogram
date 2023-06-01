package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Data;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Transactional
public class DataServiceJPA implements DataService{

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public void saveGameData(Data savedGameData, int slotNumber) {
        if(getSavedGameData(savedGameData.getGame(),slotNumber,savedGameData.getPlayer())==null){
            entityManager.persist(savedGameData);
        }else {
            entityManager.createNamedQuery("Data.saveGame")
                    .setParameter("ident",slotNumber)
                    .setParameter("game",savedGameData.getGame())
                    .setParameter("player",savedGameData.getPlayer())
                    .setParameter("score",savedGameData.getScore())
                    .setParameter("playedOn",savedGameData.getPlayedOn())
                    .setParameter("data",savedGameData.getData())
                    .executeUpdate();
        }
    }

    @Override
    public Data getSavedGameData(String game, int slotNumber, String player) {
        try {
            Data data = (Data) entityManager.createNamedQuery("Data.getSavedGameData")
                    .setParameter("game", game)
                    .setParameter("player", player)
                    .setParameter("ident",slotNumber)
                    .getSingleResult();
            return data;
        }
        catch (NoResultException noResultException) {
            return null;
        }
    }
    @Override
    public List<Data> getSavedGames(String game) {
        try {
            List<Data> list = entityManager.createNamedQuery("Data.getSavedGames")
                    .setParameter("game", game)
                    .getResultList();
            return list;
        }
        catch (NoResultException noResultException) {
            return null;
        }
    }

    @Override
    public String getPlayer(String game, int ident) {
        try {
            String name = (String) entityManager.createNamedQuery("Data.getPlayer")
                    .setParameter("game", game)
                    .setParameter("ident", ident)
                    .getSingleResult();
            return name;
        }
        catch (NoResultException noResultException) {
            return null;
        }
    }


    @Override
    public int getSavedGameIdent(String game, String player) {
        try {
            int ident = (int) entityManager.createNamedQuery("Data.getSavedGameIdent")
                    .setParameter("game", game)
                    .setParameter("player", player)
                    .getSingleResult();
            return ident;
        }
        catch (NoResultException noResultException) {
            return 0;
        }
    }

    @Override
    public Date getSavedGameDate(String game, String player) {
        try {
            Date date = (Date) entityManager.createNamedQuery("Data.getSavedGameDate")
                    .setParameter("game", game)
                    .setParameter("player", player)
                    .getSingleResult();
            return date;
        }
        catch (NoResultException noResultException) {
            return null;
        }
    }

    @Override
    public int getSavedGameScore(String game, String player) {
        try {
            int score = (int) entityManager.createNamedQuery("Data.getSavedGameScore")
                    .setParameter("game", game)
                    .setParameter("player", player)
                    .getSingleResult();
            return score;
        }
        catch (NoResultException noResultException) {
            return 0;
        }
    }

    @Override
    public void deleteSave(String game, int slotNumber, String player) {
        try {
            entityManager.createNamedQuery("Data.deleteSave")
                    .setParameter("game", game)
                    .setParameter("player", player)
                    .setParameter("ident",slotNumber)
                    .executeUpdate();
            return;
        }
        catch (NoResultException noResultException) {
            return;
        }
    }

    @Override
    public void resetData() {
        entityManager.createNamedQuery("Data.resetData").executeUpdate();
    }
}
