package ruslbizh.javaprojects;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
/**
 * Created by ruslbizh on 12.12.2016.
 */

public class MessageTest {

    Message message;
    @Before
    public void before() {
        message = new Message("checkname", "Ruslan");
    }

    @Test
    public void testInitializing(){
        assertEquals("checkname", message.getFunction());
        assertEquals("Ruslan", message.getValue());
    }
}
