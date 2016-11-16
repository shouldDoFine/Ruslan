package ruslbizh.javaprojects;

import java.io.Serializable;

/**
 * Created by ruslbizh on 16.11.2016.
 */
public class Message implements Serializable{
    private String function;
    private String value;

    public Message(String function, String value) {
        this.function = new String(function);
        this.value = new String(value);
    }

    public String getFunction() {
        return function;
    }

    public String getValue() {
        return value;
    }
}
