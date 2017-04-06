package ru.game.net.packets;

import ru.game.net.GameServer;
import ru.game.net.GameClient;

/**
 * Created by Bumka on 06.04.2017.
 */
public class Packet02Move extends Packet {

    private String username;

    public Packet02Move(byte[] data){
        super(02);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
    }

    public Packet02Move(String username){
        super(02);
        this.username = username;
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());

    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    @Override
    public byte[] getData() {
        return ("02" + this.username).getBytes();
    }

    public String getUsername(){
        return username;
    }
}
