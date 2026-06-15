import java.awt.image.BufferedImage;

/**
 * MessageInterface.java
 *
 * The interface that is implemented by the message class
 *
 * @author Timothy Murray, L11
 *
 * @version November 2 2024
 */
public interface MessageInterface {
    String getMessage();

    BufferedImage getImage();

    int getMessageID();

    int getImageID();

    boolean equals(Object o);

    void setMessage(String message);

    String toString();

    String getToUser();

    String getFromUser();
}
