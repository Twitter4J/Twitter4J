package twitter4j;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 3.0.0
 */
class SuggestedUsersResourcesTest extends TwitterTestBase {

    @Test
    void testSuggestion() throws Exception {
        ResponseList<Category> categories = twitter1.getSuggestedUserCategories();
        assertTrue(categories.size() > 0);
        assertNotNull(TwitterObjectFactory.getRawJSON(categories));
        assertNotNull(TwitterObjectFactory.getRawJSON(categories.get(0)));
        assertEquals(categories.get(0), TwitterObjectFactory.createCategory(TwitterObjectFactory.getRawJSON(categories.get(0))));
        ResponseList<User> users = twitter1.getUserSuggestions(categories.get(0).getSlug());
        assertTrue(users.size() > 0);
        assertNull(users.get(0).getStatus());
        assertNotNull(TwitterObjectFactory.getRawJSON(users));
        assertNotNull(TwitterObjectFactory.getRawJSON(users.get(0)));
        assertEquals(users.get(0), TwitterObjectFactory.createUser(TwitterObjectFactory.getRawJSON(users.get(0))));

        users = twitter1.getMemberSuggestions(categories.get(0).getSlug());
        assertTrue(users.size() > 0);
        assertNotNull(users.get(0).getStatus());
        assertNotNull(TwitterObjectFactory.getRawJSON(users));
        assertNotNull(TwitterObjectFactory.getRawJSON(users.get(0)));
        assertEquals(users.get(0), TwitterObjectFactory.createUser(TwitterObjectFactory.getRawJSON(users.get(0))));
    }
}
