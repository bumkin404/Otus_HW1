package ru.game.net;

import ru.game.entities.PlayerMP;
import ru.game.game.Game;
import ru.game.net.packets.Packet;
import ru.game.net.packets.Packet.PacketTypes;
import ru.game.net.packets.Packet00Login;
import ru.game.net.packets.Packet01Disconnect;
import ru.game.net.packets.Packet02Move;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bumka on 06.04.2017.
 */
public class GameServer extends Thread {

    private DatagramSocket socket;
    private Game game;
    private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();

    public GameServer(Game game){
        this.game = game;
        try{
            this.socket = new DatagramSocket(1331);
        } catch (SocketException e){
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
                PlayerMP player = new PlayerMP(game.level, ((Packet00Login) packet).getUsername(), address, port);
                this.addConnection(player, (Packet00Login) packet);
                break;
            case DISCONNECT:
                packet = new Packet01Disconnect(data);
                this.removeConnection((Packet01Disconnect) packet);
                break;
            case MOVE:
                packet = new Packet02Move(data);
                this.handlemove(((Packet02Move)packet));
        }
    }

    public void addConnection(PlayerMP player, Packet00Login packet){
        boolean alreadyConnected = false;
        for (PlayerMP p: this.connectedPlayers){
            if (player.getUsername().equalsIgnoreCase(p.getUsername())){
                if (p.ipAddress == null){
                    p.ipAddress = player.ipAddress;
                }
                if (p.port == -1){
                    p.port = player.port;
                }
                alreadyConnected = true;
            } else {
                sendData(packet.getData(), p.ipAddress,p.port);
                packet = new Packet00Login(p.getUsername());
            }
        }
        if (!alreadyConnected){
            this.connectedPlayers.add(player);
        }
    }

    public void removeConnection(Packet01Disconnect packet){
        this.connectedPlayers.remove(getPlayerMPIndex(packet.getUsername()));
        packet.writeData(this);
    }

    public PlayerMP getPlayerMP(String username){
        for (PlayerMP player : this.connectedPlayers){
            if (player.getUsername().equals(username)){
                return player;
            }
        }
        return null;
    }

    public int getPlayerMPIndex(String username){
        int index = 0;
        for(PlayerMP player : this.connectedPlayers){
            if(player.getUsername().equals(username)){
                break;
            }
            index++;
        }
        return index;
    }

    public void sendData(byte[] data, InetAddress ipAddress, int port){
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try{
            this.socket.send(packet);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendDataToAllClients(byte[] data){
        for(PlayerMP p : connectedPlayers){
            sendData(data, p.ipAddress, p.port);
        }
    }

    private void handlemove(Packet02Move packet){
        if (getPlayerMP(packet.getUsername()) != null){
            int index = getPlayerMPIndex(packet.getUsername());
            PlayerMP player = this.connectedPlayers.get(index);

            packet.writeData(this);
        }
    }
}
