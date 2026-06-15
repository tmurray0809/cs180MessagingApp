import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.junit.runners.Parameterized.BeforeParam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.BeforeClass; 

/**
 * @author Anvi Kurade
 * 
 * @version 03 November 2024
 * 
 * Tests for the User class
 */

    
public class UserTest {

    @Before
    public void setup() {

    }

    @Test(timeout = 1000)
    public void testUserID() {
        var testUser = new User("TestUserOne", "TestPasswordOne");
        var testFriend = new User("TestFriendOne", "TestFriendPasswordOne");
        // testUser.addFriend(testFriend);

        assertNotEquals(testUser.getUserId(), testFriend.getUserId());



        // assertEquals("Error with GetUserID",
        //     1, 
        //     testUser.getUserId()); 
            
        // assertEquals("Error with GetUserID",
        //     2, 
        //     testFriend.getUserId()); 

    }
        
    @Test(timeout = 1000)
    public void testGetUsername() {
        var testUser = new User("TestUserOne", "TestPasswordOne");
        assertEquals("Error with username", 
            "TestUserOne", 
            testUser.getUsername());
       
    }

    @Test(timeout = 1000)
    public void testSetUsername() {
        var testUser = new User("TestUserOne", "TestPasswordOne");
        testUser.setUsername("Changed");

        assertEquals("Error with setUsername()", 
            "Changed", 
            testUser.getUsername());       
    }


    @Test(timeout = 1000)
    public void testGetPassword() {
        var testUser = new User("TestUserOne", "TestPasswordOne");
        assertEquals("Error with getPassword()", 
            "TestPasswordOne", 
            testUser.getPassword());       
    }


    @Test(timeout = 1000)
    public void testSetPassword() {
        var testUser = new User("TestUserOne", "TestPasswordOne");
        testUser.setPassword("ChangedPassword");

        assertEquals("Error with setPassword()", 
            "ChangedPassword", 
            testUser.getPassword());       
    }


//    @Test(timeout = 1000)
//    public void testSetProfileImage() {
//        // TODO: put an image into the dir and use that
//
//        var testUser = new User("TestUserOne", "TestPasswordOne");
//        testUser.setProfileImage("\\TestFiles\\testImage.jpg");
//        BufferedImage test;
//
//        try {
//            test = ImageIO.read(new File("\\TestFiles\\testImage.jpg"));
//            assertTrue(test.equals(testUser.getProfileImage()));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


   @Test(timeout = 1000)
   public void testGetProfileImage() {
       // TODO: put an image into the dir and use that

       var testUser = new User("TestUserOne", "TestPasswordOne");
       BufferedImage test;
       BufferedImage output = testUser.getProfileImage();

       try {
           test = ImageIO.read(new File("\\default.png"));
           assertTrue(test.equals(output));

       } catch (IOException e) {
           e.printStackTrace();
       }
   }


    @Test(timeout = 1000)
    public void testAddFriend() {
        var testUser = new User("TestUserOne", "TestPasswordOne");
        var testFriend = new User("TestFriendOne", "TestFriendPasswordOne");
        testUser.addFriend(testFriend);

        assertTrue(testUser.checkFriend(testFriend));
        assertFalse(testFriend.checkFriend(testUser));  
        
        testFriend.addFriend(testUser);
        assertTrue(testFriend.checkFriend(testUser));

    }


    @Test(timeout = 1000)
    public void testRemoveFriend() {
        var testUser = new User("TestUserOne", "TestPasswordOne");
        var testFriend = new User("TestFriendOne", "TestFriendPasswordOne");
        
        testUser.addFriend(testFriend);
        testFriend.addFriend(testUser);

        testUser.removeFriend(testFriend);
        testFriend.removeFriend(testUser);


        assertFalse(testUser.checkFriend(testFriend)); 
        assertFalse(testFriend.checkFriend(testUser));       
    }

    @Test(timeout = 1000)
    public void testAddBlocked() {
        var testUser = new User("TestUserOne", "TestPasswordOne");
        var testFriend = new User("TestFriendOne", "TestFriendPasswordOne");

        assertFalse(testUser.checkBlocked(testFriend));
        assertFalse(testFriend.checkBlocked(testUser));
        
        testUser.addBlocked(testFriend);
        testFriend.addBlocked(testUser);

        assertTrue(testUser.checkBlocked(testFriend));
        assertTrue(testFriend.checkBlocked(testUser));

    }

    @Test(timeout = 1000)
    public void testRemovedBlocked() {
        var testUser = new User("TestUserOne", "TestPasswordOne");
        var testFriend = new User("TestFriendOne", "TestFriendPasswordOne");
        
        testUser.addBlocked(testFriend);
        testUser.removeBlocked(testFriend);

        assertFalse(testUser.checkBlocked(testFriend));

    }


    // Test equals
    @Test(timeout = 1000)
    public void testEquals() {
        var testUser = new User("TestUserOne", "TestPasswordOne");
        var testFriend = new User("TestFriendOne", "TestFriendPasswordOne");
        int random = 5;

        assertFalse(testUser.equals(testFriend));
        assertFalse(testFriend.equals(random));
        assertTrue(testUser.equals(testUser));
    }

    // Test toString
    @Test(timeout = 1000)
    public void testToString() {
        var testUser = new User("TestUserOne", "TestPasswordOne");
        var strOutput = "TestUserOne;" + "TestPasswordOne";
        String strOutReal = testUser.toString();
        strOutReal = strOutReal.split(";")[0] + ";" + strOutReal.split(";")[1];

        assertEquals(strOutput, strOutReal);

        // testUser.setProfileImage("image");
        // strOutput = "TestUserOne," + "TestPasswordOne," + "image";
        // assertEquals(strOutput, testUser.toString());
    }





}
    