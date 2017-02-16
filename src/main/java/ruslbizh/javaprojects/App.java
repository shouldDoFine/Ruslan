package ruslbizh.javaprojects;

import java.awt.*;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable(){
            @Override
            public void run() {
                new Thread(new ChatServer()).start();
                new Thread(new ChatClient(1234)).start();
                new Thread(new ChatClient(1234)).start();
            }
        });
    }
}