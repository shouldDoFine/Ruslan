package ruslbizh.javaprojects;

import org.junit.Before;
import org.junit.Test;
import java.net.Socket;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by ruslbizh on 12.12.2016.
 */
public class ClientThreadTest {
    Room room;
    ClientThread ct;
    @Before
    public void before() {
        room = new Room();
        room.getAllNames().add("aaa");
        room.getAllNames().add("bbb");
        ct = new ClientThread();
        ct.setRoom(room);
    }

    @Test
    public void checkNameTest(){
        assertEquals(true, ct.checkName("ccc"));
        assertEquals(false, ct.checkName("aaa"));
    }
}
