package ru.game.game;

import ru.game.entities.Player;
import ru.game.entities.PlayerMP;
import ru.game.level.Level;
import ru.game.net.GameClient;
import ru.game.net.GameServer;

/**
 * Created by Bumka on 05.04.2017.
 */
public class Game implements Runnable {

    public static final String NAME = "Game";
    public static Game game;

    private Thread thread;

    public boolean running = false;

    public int tickCount = 0;
    public InputHandler input;

    public Level level;
    public Player player;

    public GameClient socketClient;
    public GameServer socketServer;

    public void init(){
        game = this;
        input = new InputHandler(this);
        level = new Level();
        player = new PlayerMP(level, input, "DefaultUser", null, -1);
        level.addEntity(player);
        System.out.println("Server started");
    }

    public synchronized void start(){
        running = true;

        thread = new Thread(this, NAME + "_main");
        thread.start();
        socketServer = new GameServer(this);
        socketServer.start();
    }

    public synchronized void stop(){
        running = false;
        try {
            thread.join();

        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void run(){
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / 60D;

        int ticks = 0;

        long lastTimer = System.currentTimeMillis();
        double delta = 0;
        init();

        while (running){
            long now = System.nanoTime();
            delta+= (now - lastTime) / nsPerTick;
            lastTime = now;

            while(delta >= 1){
                ticks++;
                tick();
                delta -=1;
            }

            try{
                Thread.sleep(2);
            } catch (InterruptedException e){
                e.printStackTrace();
            }

            if (System.currentTimeMillis() - lastTimer >= 1000){
                lastTimer +=1000;
                ticks = 0;
            }
        }
    }

    public void tick(){
        tickCount++;
        level.tick();
    }

    public void render(){

    }

    public static long fact(int n){
        if (n <= 1){
            return 1;
        } else {
            return n * fact(n - 1);
        }
    }
}
