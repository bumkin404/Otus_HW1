package ru.game.entities;

import ru.game.level.Level;

/**
 * Created by Bumka on 05.04.2017.
 */
public abstract class Mob extends Entity {
    protected String name;

    public Mob(Level level, String name){
        super(level);
        this.name = name;
    }

    public void move(){

    }

    public String getName() {
        return name;
    }
}
