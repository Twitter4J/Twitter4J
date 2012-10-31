package twitter4j;

import twitter4j.json.DataObjectFactory;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 3.0.0
 */
public class SuggestedUsersResourcesTest extends TwitterTestBase {
    public SuggestedUsersResourcesTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSuggestion() throws Exception {
        ResponseList<Category> categories = twitter1.getSuggestedUserCategories();
        assertTrue(categories.size() > 0);
        assertNotNull(DataObjectFactory.getRawJSON(categories));
        assertNotNull(DataObjectFactory.getRawJSON(categories.get(0)));
        assertEquals(categories.get(0), DataObjectFactory.createCategory(DataObjectFactory.getRawJSON(categories.get(0))));
        ResponseList<User> users = twitter1.getUserSuggestions(categories.get(0).getSlug());
        assertTrue(users.size() > 0);
        assertNull(users.get(0).getStatus());
        assertNotNull(DataObjectFactory.getRawJSON(users));
        assertNotNull(DataObjectFactory.getRawJSON(users.get(0)));
        assertEquals(users.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(users.get(0))));

        users = twitter1.getMemberSuggestions(categories.get(0).getSlug());
        assertTrue(users.size() > 0);
        assertNotNull(users.get(0).getStatus());
        assertNotNull(DataObjectFactory.getRawJSON(users));
        assertNotNull(DataObjectFactory.getRawJSON(users.get(0)));
        assertEquals(users.get(0), DataObjectFactory.createUser(DataObjectFactory.getRawJSON(users.get(0))));
    }
}
