import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.junit.Test;
import org.junit.runners.Parameterized.BeforeParam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.BeforeClass;

/**
 * @author Anvi Kurade
 * 
 * @version 03 November 2024
 * 
 *          Tests for the Chatroom class
 */

public class ChatroomTest {

    @Before
    public void setup() {

    }

    @Test(timeout = 1000)
    public void testAddChatroom() {
        var userOne = new User("testUserOne", "testPasswordOne");
        var userTwo = new User("testUserTwo", "testPasswordTwo");
        var testChatroom = new Chatroom(userOne, userTwo);

        assertEquals("Error adding chatroom",
                testChatroom.getUser1().getUserId(),
                userOne.getUserId());

        assertEquals("Error adding chatroom",
                testChatroom.getUser2().getUserId(),
                userTwo.getUserId());
    }

    @Test(timeout = 1000)
    public void testAddMessage() {
        var userOne = new User("testUserThree", "testPasswordThree");
        var userTwo = new User("testUserFour", "testPasswordFour");
        var testChatroom = new Chatroom(userOne, userTwo);

        testChatroom.addMessage("Hello x", "testUserThree", "testUserFour");
        var messages = testChatroom.getMessages();
        String expected = messages.get(0).toString();
        expected = expected.strip();

        assertEquals(
                "Error adding messages",
                ("Hello x," +
                        ";," +
                        "testUserThree," +
                        ";," +
                        "testUserFour"),
                expected);

    }

    @Test(timeout = 1000)
    public void testAddMessageWithImage() {
        // TODO: Fix this based on the code
        var userOne = new User("testUserThree", "testPasswordThree");
        var userTwo = new User("testUserFour", "testPasswordFour");
        var testChatroom = new Chatroom(userOne, userTwo);

        testChatroom.addMessage("Hello x", "imagex", "testUserThree", "testUserFour");
        var messages = testChatroom.getMessages();
        String expected = messages.get(0).toString();
        expected = expected.strip();

        assertEquals(
                "Error adding messages",
                ("Hello x," +
                        ";," +
                        "testUserThree," +
                        ";," +
                        "testUserFour"),
                expected);

    }

    @Test(timeout = 1000)
    public void testDeleteMessage() {
        var userOne = new User("testUserThree", "testPasswordThree");
        var userTwo = new User("testUserFour", "testPasswordFour");
        var testChatroom = new Chatroom(userOne, userTwo);

        testChatroom.addMessage("Hello x", "testUserThree", "testUserFour");
        testChatroom.addMessage("Hello Again", "testUserThree", "testUserFour");

        var toDelete = new Message("Hello Again", "testUserThree", "testUserFour");

        testChatroom.deleteMessage(toDelete);

        var messages = testChatroom.getMessages();
        String expected = messages.get(0).toString();
        expected = expected.strip();

        assertEquals(
                "Error adding messages",
                ("Hello x," +
                        ";," +
                        "testUserThree," +
                        ";," +
                        "testUserFour"),
                expected);

    }

    @Test(timeout = 1000)
    public void testEditMessage() {
        var userOne = new User("testUserThree", "testPasswordThree");
        var userTwo = new User("testUserFour", "testPasswordFour");
        var testChatroom = new Chatroom(userOne, userTwo);

        testChatroom.addMessage("Hello x", "testUserThree", "testUserFour");

        var toEdit = new Message("Hello x", "testUserThree", "testUserFour");
        testChatroom.editMessage(toEdit, "Hello Again");

        var messages = testChatroom.getMessages();
        String expected = messages.get(0).toString();
        expected = expected.strip();

        assertEquals(
                "Error adding messages",
                ("Hello Again," +
                        ";," +
                        "testUserThree," +
                        ";," +
                        "testUserFour"),
                expected);

    }

    // Test equals
    @Test(timeout = 1000)
    public void testEquals() {
        var userOne = new User("testUserThree", "testPasswordThree");
        var userTwo = new User("testUserFour", "testPasswordFour");
        var userThree = new User("testUserFive", "testPasswordFive");

        var chatroomOne = new Chatroom(userTwo, userTwo);
        var chatroomTwo = new Chatroom(userTwo, userThree);
        int random = 5;

        assertFalse(chatroomOne.equals(chatroomTwo));
        assertFalse(chatroomOne.equals(random));
        assertTrue(chatroomOne.equals(chatroomOne));
    }
}
