package ru.game.entities;

import ru.game.game.InputHandler;
import ru.game.level.Level;

/**
 * Created by Bumka on 05.04.2017.
 */
public class Player extends Mob {

    private InputHandler input;
    private int tickCount = 0;
    private String username;

    public Player(Level level, InputHandler input, String username){
        super(level, username);
        this.input = input;
        this.username = username;
    }

    public void tick(){
        tickCount++;
    }

    public void render(){

    }

    public String getUsername(){
        return this.username;
    }
}
