package sk.tuke.gamestudio.entity;

import javax.persistence.*;

@Entity
@NamedQuery( name = "Player.getPassword",
        query = "SELECT p FROM Player p WHERE p.userName=:userName")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ident;
    private String userName;
    private String password;

    public Player(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public Player() {}

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
