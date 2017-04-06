package ru.game;

import ru.game.game.Game;

/**
 * Created by Bumka on 06.04.2017.
 */
public class GameLauncher {

    private static Game game = new Game();

    public void init(){}

    public void start(){
        game.start();
    }

    public void stop(){
        game.stop();
    }

    public static void main(String[] args) {
        game.start();
    }
}
