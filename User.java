import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * User
 *
 * This class is a representative of all the users that have registered into the
 * program.
 * Each User is given a set unique ID and is able to manage their username,
 * password, and profile picture.
 * Each user also has a block list and friend list that they can manage at will.
 *
 * @author Alston Lin, lab sec L11
 *
 * @version November 2 2024
 */

public class User implements UserInterface, Serializable {
    private String username;
    private String password;
    private String profileImagePath;
    private transient BufferedImage profileImage;
    private ArrayList<User> friends;
    private ArrayList<User> blocked;
    private int userId;
    private static AtomicInteger userCount = new AtomicInteger(0);
    private static final String DEFAULT_PROFILE_IMAGE = "default.png";

    public User(String username, String password, String profileImagePath) {
        this.username = username;
        this.password = password;
        this.profileImagePath = profileImagePath;
        this.friends = new ArrayList<User>();
        this.blocked = new ArrayList<User>();
        this.userId = userCount.getAndIncrement();
        try {
            this.profileImage = ImageIO.read(new File(DEFAULT_PROFILE_IMAGE));
        } catch (Exception e) {
            try {
                this.profileImage = ImageIO.read(new File(DEFAULT_PROFILE_IMAGE));
            } catch (IOException f) {
                f.printStackTrace();
            }
        }
    }

    public User(String username, String password) {
        this(username, password, DEFAULT_PROFILE_IMAGE);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImagePath() {
        return this.profileImagePath;
    }

    public BufferedImage getProfileImage() {
        return this.profileImage;
    }

    public void setProfileImage(String imagePath) {
        try {
            this.profileImage = ImageIO.read(new File(DEFAULT_PROFILE_IMAGE));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public ArrayList<User> getFriends() {
        return this.friends;
    }

    public boolean checkFriend(User u) {
        for (int i = 0; i < this.friends.size(); i++) {
            if (u.equals(this.friends.get(i))) {
                return true;
            }
        }
        return false;
    }

    public void addFriend(User u) {
        this.friends.add(u);
    }

    public void removeFriend(User u) {
        for (int i = 0; i < this.friends.size(); i++) {
            if (u.equals(this.friends.get(i))) {
                this.friends.remove(i);
                return;
            }
        }
    }

    public ArrayList<User> getBlocked() {
        return this.blocked;
    }

    public boolean checkBlocked(User u) {
        for (int i = 0; i < this.blocked.size(); i++) {
            if (u.equals(this.blocked.get(i))) {
                return true;
            }
        }
        return false;
    }

    public void addBlocked(User u) {
        this.blocked.add(u);
    }

    public void removeBlocked(User u) {
        for (int i = 0; i < this.blocked.size(); i++) {
            if (u.equals(this.blocked.get(i))) {
                this.blocked.remove(i);
                return;
            }
        }
    }

    public int getUserId() {
        return this.userId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User)) {
            return false;
        }

        User user = (User) o;
        return this.username.equals(user.getUsername());
    }

    @Override
    public String toString() {
        BufferedImage def;
        try {
            def = ImageIO.read(new File(DEFAULT_PROFILE_IMAGE));
        } catch (IOException e) {
            return username + ";" + password + (profileImagePath != null ? ";" + profileImagePath : "");
        }
        if (this.profileImage == null || this.profileImage.equals(def)) {
            return username + ";" + password;
        } else {
            return username + ";" + password + ";" + profileImagePath;
        }
    }

}