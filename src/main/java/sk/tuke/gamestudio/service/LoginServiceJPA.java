package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Player;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class LoginServiceJPA implements LoginService {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean comparePasswords(String userName, String password){
        List<Player> players = entityManager.createNamedQuery("Player.getPassword")
                .setParameter("userName", userName)
                .getResultList();
        System.out.println(players.size());
        for(Player player:players){
            if(password.equals(player.getPassword())){
                return true;
            }
        }
        return false;
    }

    public void addUser(Player player){
        entityManager.persist(player);
    }
}
