package ruslbizh.javaprojects;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by ruslbizh on 14.11.2016.
 */

public class ChatServer implements Runnable{

    static private ServerSocket server;
    private Room room;

    public ChatServer() {
        try {
            server = new ServerSocket(1234, 10);
            room = new Room();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while(true){
                Socket socket = server.accept();
                System.out.println("accepted");
                ClientThread clientT = new ClientThread(socket);
                clientT.setRoom(room);
                room.getThreads().add(clientT);
                new Thread(clientT).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}