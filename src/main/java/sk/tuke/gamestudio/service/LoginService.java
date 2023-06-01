package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Player;

import java.util.List;

public interface LoginService {
    boolean comparePasswords(String userName, String password);

    void addUser(Player player);
}
