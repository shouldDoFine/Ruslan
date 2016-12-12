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
    private ArrayList<ClientThread> threads;
    private ArrayList<String> allNames;
    private Object lock = new Object();
    private ArrayList<String> allMessages;

    public ChatServer() {
        threads = new ArrayList<ClientThread>();
        allNames = new ArrayList<String>();
        allMessages = new ArrayList<String>();
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(1234, 10);
            while(true){
                Socket socket = server.accept();
                System.out.println("accepted");

                ClientThread sv = new ClientThread(socket);
                sv.setChat(this);
                threads.add(sv);
                new Thread(sv).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendMessageToAllUsers(String func, String value, String name){
        allMessages.add(value);
        for(ClientThread sv : threads){
            if(!sv.getName().equals(name)){
                Message message = new Message(func,value);
                try {
                    sv.getOutput().writeObject(message);
                } catch (IOException e) {
                    System.out.println("IOException");
                }
            }
        }
    }

    public ArrayList<String> getAllNames() {
        return allNames;
    }
}