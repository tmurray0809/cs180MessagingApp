import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;

/**
 * @author Anvi Kurade
 * 
 * @version 03 November 2024
 * 
 *          Tests for the Message class
 */

public class MessageTest {

    @Before
    public void setup() {
        // var message = new Message("Hello x", "x", "y");
        // var messageImage = new Message("Hello x", "imagex", "x", "y");

    }

    // test messageID
    @Test(timeout = 1000)
    public void testGetMessageID() {
        var message = new Message("Hello x", "x", "y");
        var messageImage = new Message("Hello x", "imagex", "x", "y");

        assertNotEquals(messageImage.getMessageID(), message.getMessageID());

        // assertEquals(8, message.getMessageID());
        // assertEquals(9, messageImage.getMessageID());
    }

    // Test message
    @Test(timeout = 1000)
    public void testGetImageID() {
        // var message = new Message("Hello x", "x", "y");
        var messageImage = new Message("Hello x", "imagex", "x", "y");
        var messageImageTwo = new Message("Hello Again x", "imagetwo", "z", "f");
        // assertEquals("Hello x", message.getMessage());

        assertNotEquals(messageImage.getImageID(), messageImageTwo.getImageID());
    }

    // Test message
    @Test(timeout = 1000)
    public void testGetMessage() {
        var message = new Message("Hello x", "x", "y");
        var messageImage = new Message("Hello x", "imagex", "x", "y");
        assertEquals("Hello x", message.getMessage());
        assertEquals("Hello x", messageImage.getMessage());
    }

    // test image
    @Test(timeout = 1000)
    public void testGetImage() {
        var message = new Message("Hello x", "x", "y");
        var messageImage = new Message("Hello x", "invalidpath", "x", "y");
        assertEquals(null, messageImage.getImage());
    }

    // Test getFromUser
    @Test(timeout = 1000)
    public void testGetFromUser() {
        var message = new Message("Hello x", "x", "y");
        var messageImage = new Message("Hello x", "imagex", "x", "y");
        assertEquals("y", message.getFromUser());
        assertEquals("y", messageImage.getFromUser());
    }

    // Test getToUser
    @Test(timeout = 1000)
    public void testGetToUser() {
        var message = new Message("Hello x", "x", "y");
        var messageImage = new Message("Hello x", "imagex", "x", "y");
        assertEquals("x", message.getToUser());
        assertEquals("x", messageImage.getToUser());
    }

    // Test set Message
    @Test(timeout = 1000)
    public void testSetMessage() {
        var message = new Message("Hello x", "x", "y");
        var messageImage = new Message("Hello x", "imagex", "x", "y");
        message.setMessage("Changed");
        messageImage.setMessage("Changed");
        assertEquals("Changed", message.getMessage());
        assertEquals("Changed", messageImage.getMessage());
    }

    // Test equals
    @Test(timeout = 1000)
    public void testEquals() {
        var message = new Message("Hello x", "x", "y");
        var message2 = new Message("Hello again", "x", "z");
        int random = 5;

        assertFalse(message.equals(random));
        assertFalse(message2.equals(message));
        assertTrue(message.equals(message));
    }

    // Test getFromUser
    @Test(timeout = 1000)
    public void testToString() {
        var message = new Message("Hello x", "x", "y");
        var messageImage = new Message("Hello x", "imagex", "x", "y");
        assertEquals("Hello x,;,x,;,y", message.toString());
        assertEquals("Hello x,;,x,;,y", messageImage.toString());
    }

    // Test getFromUser
    @Test(timeout = 1000)
    public void getFromUser() {
        var message = new Message("Hello x", "x", "y");
        var messageImage = new Message("Hello x", "imagex", "x", "y");
        assertEquals("y", message.getFromUser());
        assertEquals("y", messageImage.getFromUser());
    }

}
