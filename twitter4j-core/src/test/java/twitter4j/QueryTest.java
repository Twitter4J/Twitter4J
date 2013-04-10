package twitter4j;

import junit.framework.TestCase;
import twitter4j.internal.http.HttpParameter;
import twitter4j.json.DataObjectFactory;

/**
 * @author Keiichi Hirano - hirano.kei1 at gmail.com
 */
public class QueryTest extends TestCase {

    private static final String PARAM_NAME_INCLUDE_ENTITIES = "include_entities";
    private static final String PARAM_NAME_INCLUDE_ENTITIES_CAMELED = "includeEntities";

    public void testIncludeEntitiesDefault() throws Exception {
        Query q = new Query();
        assertEquals(false, q.getIncludeEntities());
    }

    public void testIncludeEntitiesTrue01() throws Exception {
        Query q = new Query();
        q.setIncludeEntities(true);
        assertEquals(true, q.getIncludeEntities());
    }

    public void testIncludeEntitiesTrue02() throws Exception {
        Query q = new Query();
        Query q2 = q.includeEntities(true);
        assertSame(q, q2);
        assertEquals(true, q.getIncludeEntities());
        assertEquals(true, q2.getIncludeEntities());
    }

    public void testIncludeEntitiesTrue03() throws Exception {
        Query q = new Query();
        q.setIncludeEntities(true);
        String queryString = q.toString();
        assertTrue(queryString.indexOf(PARAM_NAME_INCLUDE_ENTITIES_CAMELED + "='" + Boolean.toString(true) + "'") > -1);
        HttpParameter[] params = q.asHttpParameterArray();
        boolean has = false;
        for (HttpParameter p : params) {
            if (p.getName().equals(PARAM_NAME_INCLUDE_ENTITIES) && p.getValue().equals(Boolean.toString(true))) {
                has = true;
                break;
            }
        }
        assertTrue(has);
    }

    public void testIncludeEntitiesFalse01() throws Exception {
        Query q = new Query();
        q.setIncludeEntities(false);
        assertEquals(false, q.getIncludeEntities());
    }

    public void testIncludeEntitiesFalse02() throws Exception {
        Query q = new Query();
        Query q2 = q.includeEntities(false);
        assertSame(q, q2);
        assertEquals(false, q.getIncludeEntities());
        assertEquals(false, q2.getIncludeEntities());
    }

    public void testIncludeEntitiesFalse03() throws Exception {
        Query q = new Query();
        q.setIncludeEntities(false);
        String queryString = q.toString();
        assertTrue(queryString.indexOf(PARAM_NAME_INCLUDE_ENTITIES_CAMELED + "='" + Boolean.toString(false) + "'") > -1);
        HttpParameter[] params = q.asHttpParameterArray();
        boolean has = false;
        for (HttpParameter p : params) {
            if (p.getName().equals(PARAM_NAME_INCLUDE_ENTITIES) && p.getValue().equals(Boolean.toString(false))) {
                has = true;
                break;
            }
        }
        assertTrue(has);
    }

    public void testEquals() throws Exception {
        Query q1 = new Query();
        Query q2 = new Query();

        q1.setIncludeEntities(true);
        q2.setIncludeEntities(true);
        assertTrue(q1.equals(q2));

        q1.setIncludeEntities(true);
        q2.setIncludeEntities(false);
        assertFalse(q1.equals(q2));

        q1.setIncludeEntities(false);
        q2.setIncludeEntities(true);
        assertFalse(q1.equals(q2));

        q1.setIncludeEntities(false);
        q2.setIncludeEntities(false);
        assertTrue(q1.equals(q2));
    }

    public void testHashCode() throws Exception {
        Query q1 = new Query();
        Query q2 = new Query();

        q1.setIncludeEntities(true);
        q2.setIncludeEntities(true);
        assertEquals(q1.hashCode(), q2.hashCode());

        q1.setIncludeEntities(true);
        q2.setIncludeEntities(false);
        assertFalse(q1.hashCode() == q2.hashCode());

        q1.setIncludeEntities(false);
        q2.setIncludeEntities(true);
        assertFalse(q1.hashCode() == q2.hashCode());

        q1.setIncludeEntities(false);
        q2.setIncludeEntities(false);
        assertEquals(q1.hashCode(), q2.hashCode());
    }

}
