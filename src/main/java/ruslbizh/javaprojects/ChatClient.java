package ruslbizh.javaprojects;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by ruslbizh on 15.11.2016.
 */
public class ChatClient implements Runnable{
    private Socket connection;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String Name;
    private MyFrame frame;
    private Object lock = new Object();

    public ChatClient(int port) {
        try {
            connection = new Socket(InetAddress.getByName("127.0.0.1"), port);
            output = new ObjectOutputStream(connection.getOutputStream());
            input = new ObjectInputStream(connection.getInputStream());

            boolean check = true;
            while(check) {
                Name = JOptionPane.showInputDialog(null, "Write your name", "Name", JOptionPane.QUESTION_MESSAGE);
                if ((Name == null) || (Name.equals(""))) {
                    System.out.println("Didn't write a name");
                    System.exit(1);
                }
                output.writeObject(new Message("name",Name));
                Message checkname = (Message) input.readObject();
                if(checkname.getValue().equals("good")){
                    check = false; //get out from cycle
                }
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        frame = new MyFrame(this);
        frame.setTitle("ChatRoom");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sayBye();
                super.windowClosing(e);
            }

        });
        frame.setVisible(true);
    }

    public void run() {
        try {
            while (true) {
                synchronized (lock) {
                    Message rdobj = (Message) input.readObject();
                    if (rdobj.getFunction().equals("message")) {
                        frame.getAllMessages().add(rdobj.getValue());
                        frame.rewriteAllMessages();
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void sayBye(){
        try {
            String tmp = new String(Name + " left the room\n\n");
            frame.getAllMessages().add(tmp);
            frame.rewriteAllMessages();
            output.writeObject(new Message("message",tmp));
        } catch (IOException e) {
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
        return Name;
    }

}

class MyFrame extends JFrame{

    private JTextArea textToShow;
    private ArrayList<String> allMessages;
    private ChatClient client;
    private Object lock1 = new Object();

    public MyFrame(ChatClient cl) {
        client = cl;
        allMessages = new ArrayList<String>();
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screensz = kit.getScreenSize();
        setSize((int)(screensz.width/1.5+100), (int)(screensz.height/1.3)+10);
        setLocation(screensz.width/5, screensz.height/8);
        setLayout(new FlowLayout(FlowLayout.CENTER,0,20));

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension((int)(screensz.width/1.5)-100, (int)(screensz.height/1.3)-100));
        panel.setVisible(true);

        textToShow = new JTextArea();
        textToShow.setEnabled(false);
        textToShow.setCaretPosition(0);
        JScrollPane scrollPane = new JScrollPane(textToShow);
        scrollPane.setBounds(70, 50, 700, 290);
        panel.add(scrollPane);
        final JTextArea textToWrite = new JTextArea();
        textToWrite.setCaretPosition(0);
        JScrollPane scrollPane2 = new JScrollPane(textToWrite);
        scrollPane2.setBounds(70, 350, 700, 100);
        panel.add(scrollPane2);

        JButton sendBut = new JButton("Send");
        sendBut.setBounds(700, 450, 70, 30);
        sendBut.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    allMessages.add(client.getName()+": \n"+textToWrite.getText()+"\n\n");
                    rewriteAllMessages();
                    client.getOutput().writeObject(new Message("message", client.getName()+": \n"+textToWrite.getText()+"\n\n"));
                    textToWrite.setText("");

                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });
        panel.add(sendBut);

        add(panel);
    }

    synchronized void rewriteAllMessages(){
        StringBuilder past = new StringBuilder();
        for (String el : allMessages) {
            past.append(el);
        }
        textToShow.setText(past.toString());
    }

    public JTextArea getTextToShow() {
        return textToShow;
    }

    public ArrayList<String> getAllMessages() {
        return allMessages;
    }

    public ChatClient getClient() {
        return client;
    }

    public void setTextToShow(JTextArea textToShow) {
        this.textToShow = textToShow;
    }

    public void setAllMessages(ArrayList<String> allMessages) {
        this.allMessages.clear();
        this.allMessages = new ArrayList<String>();
        for(String el : allMessages){
            this.allMessages.add(new String(el));
        }
    }
}
