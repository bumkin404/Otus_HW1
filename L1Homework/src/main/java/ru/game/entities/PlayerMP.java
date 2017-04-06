package ru.game.entities;

import java.net.InetAddress;
import ru.game.game.InputHandler;
import ru.game.level.Level;

/**
 * Created by Bumka on 05.04.2017.
 */
public class PlayerMP extends Player {

    public InetAddress ipAddress;
    public int port;

    public PlayerMP(Level level, InputHandler input, String username, InetAddress ipAddress, int port){
        super(level, input, username);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public PlayerMP(Level level, String username, InetAddress ipAddress, int port){
        super(level, null, username);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public void tick(){
        super.tick();
    }
}
