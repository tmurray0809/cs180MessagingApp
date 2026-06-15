import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * User Interface
 * 
 * The Interface implemented by the User class. Outlines the methods to be used in the User class.
 * 
 * @author Alston Lin, lab sec L11
 * 
 * @version November 2 2024
*/

public interface UserInterface {
    public String getUsername();
    public void setUsername(String username);
    public String getPassword();
    public void setPassword(String password);
    //public BufferedImage getProfileImage();
    //public void setProfileImage(String profileImage);
    public ArrayList<User> getFriends();
    public boolean checkFriend(User u);
    public void addFriend(User u);
    public void removeFriend(User u);
    public ArrayList<User> getBlocked();
    public boolean checkBlocked(User u);
    public void addBlocked(User u);
    public void removeBlocked(User u);
    public int getUserId();
    public boolean equals(Object o);
    String toString();
}