package ruslbizh.javaprojects;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by ruslbizh on 12.12.2016.
 */

class ClientThread implements Runnable {

    private Socket clientSocket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String name;
    private ChatServer chat;
    private Object lock = new Object();

    public ClientThread(Socket clientSt) {
        try {
            clientSocket = clientSt;
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            input = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while(true) {
                Message rdobj = (Message) input.readObject();

                boolean check = true;
                if (rdobj.getFunction().equals("name")) {
                    while (check) {
                        name = new String(rdobj.getValue());
                        check = !checkName(name);
                        if(!check){
                            System.out.println("name good");
                            chat.getAllNames().add(name);
                            output.writeObject(new Message("checkname","good"));
                        }
                        else{
                            System.out.println("name bad");
                            output.writeObject(new Message("checkname","bad"));
                            rdobj = (Message) input.readObject();
                        }
                    }
                    sayHello();
                }
                else if (rdobj.getFunction().equals("message")) {
                    chat.sendMessageToAllUsers(rdobj.getFunction(),rdobj.getValue(),name);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    synchronized void sayHello(){
        Message message = new Message("message",new String(name + " has been added to room\n\n"));
        chat.sendMessageToAllUsers(message.getFunction(),message.getValue(),name);
    }

    boolean checkName(String nm){
        if((nm != null) && (!nm.equals(""))) {
            for (String el : chat.getAllNames()) {
                if (el.equals(name)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    public ObjectInputStream getInput() {
        return input;
    }

    public String getName() {
        return name;
    }

    public void setChat(ChatServer chat) {
        this.chat = chat;
    }
}