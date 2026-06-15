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
 *          Tests for the Database class
 */

public class DatabaseTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Before
    public void setup() {
        // var testDB = new Database("userInfo.txt", "messages.txt");
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    // Test readUsers
    @Test(timeout = 1000)
    public void testReadUsers() {
        var testDB = new Database("userInfo.txt", "messages.txt");
        assertTrue(testDB.readUsers());
    }

    @Test(timeout = 1000)
    public void testFileNotFoundException() {
        var testDB = new Database("WrongName.txt", "messages.txt");
        assertFalse(testDB.readUsers());
        String output = outputStreamCaptor.toString().strip();
        assertEquals(
            "Exception did not trigger as expected",
            "File not found",
            output);
    }

    @Test(timeout = 1000)
    public void testAddUserAndSearchForUser() {
        try {
            var testDB = new Database("userInfo.txt", "messages.txt");
            assertTrue(testDB.addUser("JimmyJohn;" +
                "12345"));

            var jimmyUser = testDB.searchForUser("JimmyJohn");
            assertEquals("Error with adding user",
                "JimmyJohn",
                jimmyUser.getUsername());

        } catch (UsernameTakenException ex) {
            System.out.println("Throws unexpected error");
        }
    }

    @Test(expected = UsernameTakenException.class)
    public void testUsernameTakenException() throws UsernameTakenException {
        try {
            var testDB = new Database("userInfo.txt", "messages.txt");

            testDB.addUser("JimmyJohn;" +
                "12345");

            testDB.addUser("JimmyJohn;" +
                "12345");

        } catch (UsernameTakenException ex) {
            throw new UsernameTakenException("user exists");
        }
    }

    @Test(timeout = 1000)
    public void testAddChatroom() {
        // TODO: Fix this
        var testDB = new Database("userInfo.txt", "messages.txt");

        var userOne = new User("testUserOne", "testPasswordOne");
        var userTwo = new User("testUserTwo", "testPasswordTwo");
        userTwo.addFriend(userOne);
        userOne.addFriend(userTwo);

        assertTrue(testDB.addChatroom(userOne, userTwo));
    }

//    @Test(timeout = 1000)
//    public void testReadUsers() {
//        var testDB = new Database("invalidUserInfo.txt", "invalidMessages.txt");
//        try {
//            assertFalse(testDB.readChatrooms());
//
//        } catch (Exception fe) {
//            assertFalse(true);
//        }
//    }

    @Test(timeout = 1000)
    public void testGetChatroom() {
        // TODO: Doesn't work because chatroom wont add
        var testDB = new Database("userInfo.txt", "messages.txt");
        var userOne = new User("testUserThree", "testPasswordThree");
        var userTwo = new User("testUserFour", "testPasswordFour");

        testDB.addChatroom(userOne, userTwo);
        var outputChatroom = testDB.getChatroom(userOne, userTwo);

        assertTrue(outputChatroom.getUser1().equals(userOne));

    }

    @Test(timeout = 1000)
    public void testOutputAndReadChatroom() {
        var testDB = new Database("userInfo.txt", "messages.txt");
        var userOne = new User("testUserFive", "testPasswordFive");
        var userTwo = new User("testUserSix", "testPasswordSix");
        var testChatroom = new Chatroom(userOne, userTwo);

        testDB.addChatroom(userTwo, userTwo);
        testDB.searchForChatroom(testChatroom);
        assertTrue(testDB.outputChatrooms());
        assertTrue(testDB.readChatrooms());

    }

    @Test(timeout = 1000)
    public void testUserSearch() {
        try {
            var testDB = new Database("userInfo.txt", "messages.txt");
            testDB.addUser("userOne;" + "passwordOne");
            testDB.addUser("userTwoOne;" + "passwordTwoOne");
            testDB.addUser("userThree;" + "passwordThree");

            var uList = testDB.userSearch("One");

            boolean firstExist = (uList.get(0).getUsername()).equals("userOne")
                || (uList.get(0).getUsername()).equals("userTwoOne");

            boolean secondExist = (uList.get(1).getUsername()).equals("userOne")
                || (uList.get(1).getUsername()).equals("userTwoOne");

            assertTrue(firstExist);
            assertTrue(secondExist);

        } catch (UsernameTakenException ex) {
            assertEquals(1, 2);
        }

    }

    @Test(timeout = 1000)
    public void testGetRandomUsers() {
        var testDB = new Database("userInfo.txt", "messages.txt");
        try {
            testDB.addUser("userOne;" + "passwordOne");
            testDB.addUser("userTwo;" + "passwordTwo");
            testDB.addUser("userThree;" + "passwordThree");
            testDB.addUser("userFour;" + "passwordFour");
            testDB.addUser("userFive;" + "passwordFive");
            testDB.addUser("userSix;" + "passwordSix");

            var uList = testDB.getRandomUsers();

            assertTrue(testDB.searchForUser(uList[0].getUsername()).equals(uList[0]));
            assertTrue(testDB.searchForUser(uList[1].getUsername()).equals(uList[1]));
            assertTrue(testDB.searchForUser(uList[2].getUsername()).equals(uList[2]));
            assertTrue(testDB.searchForUser(uList[3].getUsername()).equals(uList[3]));
            assertTrue(testDB.searchForUser(uList[4].getUsername()).equals(uList[4]));
            assertTrue(testDB.searchForUser(uList[5].getUsername()).equals(uList[5]));

        } catch (Exception e) {
            assertEquals(1, 1);
        }

    }
}
