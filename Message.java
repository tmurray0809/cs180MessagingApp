import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;

/**
 * Message.java
 *
 * This class creates the messages that the users will sent to each other. An
 * image can be
 * attatched.
 *
 * @author Timothy Murray, L11
 *
 * @version November 2 2024
 */
public class Message implements MessageInterface {

    private String message;
    private BufferedImage image;
    private String fromUser;
    private String toUser;
    private int messageId;
    private int imageId;
    private static AtomicInteger messageCount = new AtomicInteger(0);
    private static AtomicInteger imageCount = new AtomicInteger(0);

    public Message(String message, String imagePath, String toUser, String fromUser) {
        this.message = message;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.messageId = messageCount.getAndIncrement();
        this.imageId = imageCount.getAndIncrement();
        try {
            this.image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            this.image = null;
        }
    }

    public Message(String message, String toUser, String fromUser) {
        this.message = message;
        this.image = null;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.messageId = messageCount.getAndIncrement();
    }

    public String getMessage() {
        return message;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getMessageID() {
        return messageId;
    }

    public int getImageID() {
        return imageId;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Message)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        Message m = (Message) o;
        return message.equals(m.getMessage());
    }

    public String toString() {
        if (image == null) {
            return message + ",;," + toUser + ",;," + fromUser;
        } else {
            return message + ",;," + image + ",;," + toUser + ",;," + fromUser;
        }
    }

}
