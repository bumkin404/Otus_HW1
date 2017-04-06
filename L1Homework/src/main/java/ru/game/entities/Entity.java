package ru.game.entities;

import ru.game.level.Level;

/**
 * Created by Bumka on 05.04.2017.
 */
public abstract class Entity {
    protected Level level;

    public Entity(Level level){
        init(level);
    }

    public final void init(Level level){
        this.level = level;
    }

    public abstract void tick();

    public abstract void render();
}
