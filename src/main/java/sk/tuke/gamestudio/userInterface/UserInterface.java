package sk.tuke.gamestudio.userInterface;

import sk.tuke.gamestudio.core.Field;

import java.io.IOException;

public interface UserInterface {
    Field getField();
    String getPlayersName();
    void play() throws IOException, ClassNotFoundException;
}
