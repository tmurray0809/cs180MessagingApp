import java.util.ArrayList;

/**
 * Page Interface
 * 
 * This interface is to be implemented by all page-type objects.
 *
 * @author Purdue CS: Shreyas Aryah
 *
 * @version November 2, 2024
 */

public interface PageInterface {
    Object getPageContent(String pageId);

    // content display
    void displayContent(Object content);

    // updating content
    boolean updateContent(String contentId, Object newContent);

    // deletes content depending on the id (could be a message or post)
    boolean deleteContent(String contentId);

    // opposite of deleteContent
    boolean addContent(Object content);

    // user interaction with page (different interactions can include liking,
    // comment -> displayed in an enum like hw10?)
    boolean interactWithContent(String contentId, InteractionType interaction);

    // traverse through arraylist to find content
    ArrayList<Object> searchContent(String keyword);

}
