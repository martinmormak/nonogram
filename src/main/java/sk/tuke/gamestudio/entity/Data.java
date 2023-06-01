package sk.tuke.gamestudio.entity;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@NamedQuery( name = "Data.saveGame",
        query = "UPDATE Data SET playedOn=:playedOn,data=:data,score=:score WHERE game=:game AND player=:player AND ident=:ident")
@NamedQuery( name = "Data.getSavedGameData",
        query = "SELECT d FROM Data d WHERE game=:game AND player=:player AND ident=:ident")

@NamedQuery( name = "Data.getRandomFile",
        query = "SELECT d.ident FROM Data d WHERE game=:game AND player='savedtemplate' ORDER BY RANDOM()")

@NamedQuery( name = "Data.getSavedGames",
        query = "SELECT d FROM Data d WHERE game=:game AND NOT player='savedtemplate' ORDER BY ident")

@NamedQuery( name = "Data.getPlayer",
        query = "SELECT d.player FROM Data d WHERE game=:game AND ident=:ident")
@NamedQuery( name = "Data.getSavedGameIdent",
        query = "SELECT d.ident FROM Data d WHERE game=:game AND player=:player")
@NamedQuery( name = "Data.getSavedGameDate",
        query = "SELECT d.playedOn FROM Data d WHERE game=:game AND player=:player")
@NamedQuery( name = "Data.getSavedGameScore",
        query = "SELECT d.score FROM Data d WHERE game=:game AND player=:player")


@NamedQuery( name = "Data.deleteSave",
        query = "DELETE FROM Data d WHERE game=:game AND player=:player AND ident=:ident")
@NamedQuery( name = "Data.resetData",
        query = "DELETE FROM Data")
public class Data implements Serializable {
    @Id
    private int ident;
    private byte[] data;
    @Nullable
    private String player;
    @Nullable
    private int score;
    @Nullable
    private Date playedOn;
    @Nullable
    private String game;

    public Data(int ident, byte[] data, String player, int score, Date playedOn, String game) {
        this.ident = ident;
        this.data = data;
        this.player = player;
        this.score = score;
        this.playedOn = playedOn;
        this.game = game;
    }

    public Data(){}

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Date getPlayedOn() {
        return playedOn;
    }

    public void setPlayedOn(Date playedOn) {
        this.playedOn = playedOn;
    }
}
