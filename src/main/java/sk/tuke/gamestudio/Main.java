package sk.tuke.gamestudio;

import sk.tuke.gamestudio.game.Game;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Game game = new Game();
        game.play();
    }
}