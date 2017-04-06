package ru.game.net.packets;

import ru.game.net.GameServer;
import ru.game.net.GameClient;

/**
 * Created by Bumka on 06.04.2017.
 */
public class Packet00Login extends Packet {

    private String username;

    public Packet00Login(byte[] data){
        super(00);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
    }

    public Packet00Login(String username){
        super(00);
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
        return ("00" + this.username ).getBytes();
    }

    public String getUsername(){
        return username;
    }
}
