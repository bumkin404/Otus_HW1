package ru.game.net;

import ru.game.entities.PlayerMP;

import ru.game.net.packets.Packet;
import ru.game.net.packets.Packet.PacketTypes;
import ru.game.net.packets.Packet00Login;
import ru.game.net.packets.Packet01Disconnect;
import ru.game.net.packets.Packet02Move;
import ru.game.game.Game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by Bumka on 06.04.2017.
 */
public class GameClient extends Thread {
    private InetAddress ipAddress;
    private DatagramSocket socket;
    private Game game;

    public GameClient(Game game, String ipAddress){
        this.game = game;
        try{
            this.socket = new DatagramSocket();
            this.ipAddress = InetAddress.getByName(ipAddress);
        } catch (SocketException e){
            e.printStackTrace();
        } catch (UnknownHostException e){
            e.printStackTrace();
        }
    }

    public void run(){
        while(true){
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e){
                e.printStackTrace();
            }
            this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port){
        String message = new String(data).trim();
        PacketTypes type = Packet.lookupPacket(message.substring(0,2));
        Packet packet = null;
        switch (type){
            default:
            case INVALID:
                break;
            case LOGIN:
                packet = new Packet00Login(data);
                handleLogin((Packet00Login) packet, address, port);
            case DISCONNECT:
                packet = new Packet01Disconnect(data);
                game.level.removePlayerMP(((Packet01Disconnect) packet).getUsername());
                break;
            case MOVE:
                packet = new Packet02Move(data);
                handleMove((Packet02Move)packet);
        }
    }

    public void sendData(byte[] data){
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
        try{
            socket.send(packet);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void handleLogin(Packet00Login packet, InetAddress address, int port){
        PlayerMP player = new PlayerMP(game.level, packet.getUsername(), address, port);
        game.level.addEntity(player);
    }

    private void handleMove(Packet02Move packet){

    }
}
