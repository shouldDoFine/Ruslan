package ruslbizh.javaprojects;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Created by ruslbizh on 15.02.2017.
 */
public class Room {

    private ArrayList<ClientThread> threads;
    private ArrayList<String> allNames;
    private ArrayList<String> allMessages;

    public Room() {
        threads = new ArrayList<ClientThread>();
        allNames = new ArrayList<String>();
        allMessages = new ArrayList<String>();
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

    public ArrayList<String> getAllMessages() {
        return allMessages;
    }

    public ArrayList<ClientThread> getThreads() {

        return threads;
    }

    public ArrayList<String> getAllNames() {
        return allNames;
    }
}
