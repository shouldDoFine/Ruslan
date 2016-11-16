package ruslbizh.javaprojects;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by ruslbizh on 14.11.2016.
 */
public class ChatServer implements Runnable{
    static private ServerSocket server;
    private ArrayList<ServerThread> threads;
    private ArrayList<String> allNames;
    private Object lock = new Object();
    private ArrayList<String> allMessages;

    public ChatServer() {
        threads = new ArrayList<ServerThread>();
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

                ServerThread sv = new ServerThread(socket);
                threads.add(sv);
                new Thread(sv).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ServerThread implements Runnable{

        private Socket clientSocket;
        private ObjectOutputStream output;
        private ObjectInputStream input;
        private String name;
        private Object lock = new Object();

        public ServerThread(Socket clientSt) {
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
                                allNames.add(name);
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
                        allMessages.add(rdobj.getValue());
                        for(ServerThread sv : threads){
                            if(sv != this){
                                sv.getOutput().writeObject(rdobj);
                            }
                        }
                    }



                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

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

        synchronized void sayHello(){
            try {
                for(ServerThread sv : threads){
                    if(sv != this){
                        sv.getOutput().writeObject(new Message("message",new String(name + " has been added to room\n\n")));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        boolean checkName(String nm){
            if((nm != null) && (!nm.equals(""))) {
                for (String el : allNames) {
                    if (el.equals(name)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

    }
}