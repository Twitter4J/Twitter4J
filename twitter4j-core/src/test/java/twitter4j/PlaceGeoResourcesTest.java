/*
 * Copyright 2007 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package twitter4j;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
public class PlaceGeoResourcesTest extends TwitterTestBase {
    public PlaceGeoResourcesTest(String name) {
        super(name);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGeoMethods() throws Exception {
        GeoQuery query;
        ResponseList<Place> places;
        query = new GeoQuery(new GeoLocation(0, 0));

        places = twitter1.reverseGeoCode(query);
        assertEquals(0, places.size());

        query = new GeoQuery(new GeoLocation(37.78215, -122.40060));
        places = twitter1.reverseGeoCode(query);
        assertNotNull(TwitterObjectFactory.getRawJSON(places));
        assertEquals(places.get(0), TwitterObjectFactory.createPlace(TwitterObjectFactory.getRawJSON(places.get(0))));

        assertTrue(places.size() > 0);

        places = twitter1.searchPlaces(query);
        assertNotNull(TwitterObjectFactory.getRawJSON(places));
        assertEquals(places.get(0), TwitterObjectFactory.createPlace(TwitterObjectFactory.getRawJSON(places.get(0))));
        assertTrue(places.size() > 0);
        places = twitter1.getSimilarPlaces(new GeoLocation(37.78215, -122.40060), "SoMa", null, null);
        assertNotNull(TwitterObjectFactory.getRawJSON(places));
        assertEquals(places.get(0), TwitterObjectFactory.createPlace(TwitterObjectFactory.getRawJSON(places.get(0))));
        assertTrue(places.size() > 0);

        try {
            Place place = twitter1.getGeoDetails("5a110d312052166f");
            assertNotNull(TwitterObjectFactory.getRawJSON(place));
            assertEquals(place, TwitterObjectFactory.createPlace(TwitterObjectFactory.getRawJSON(place)));
            assertEquals("San Francisco, CA", place.getFullName());
            assertEquals("California, USA", place.getContainedWithIn()[0].getFullName());
        } catch (TwitterException te) {
            // is being rate limited
            assertEquals(400, te.getStatusCode());
        }
        String sanFrancisco = "5a110d312052166f";
        Status status = twitter1.updateStatus(new StatusUpdate(new java.util.Date() + " status with place").
                placeId(sanFrancisco));
        assertNotNull(TwitterObjectFactory.getRawJSON(status));
        assertEquals(status, TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(status)));
        assertEquals(sanFrancisco, status.getPlace().getId());
    }

    public void testGeoLocation() throws Exception {
        final double LATITUDE = 12.3456;
        final double LONGITUDE = -34.5678;

        Status withgeo = twitter1.updateStatus(new StatusUpdate(new java.util.Date().toString() + ": updating geo location").location(new GeoLocation(LATITUDE, LONGITUDE)));
        assertNotNull(TwitterObjectFactory.getRawJSON(withgeo));
        assertEquals(withgeo, TwitterObjectFactory.createStatus(TwitterObjectFactory.getRawJSON(withgeo)));
        assertTrue(withgeo.getUser().isGeoEnabled());
        assertEquals(LATITUDE, withgeo.getGeoLocation().getLatitude());
        assertEquals(LONGITUDE, withgeo.getGeoLocation().getLongitude());
        assertFalse(twitter2.verifyCredentials().isGeoEnabled());
    }

}
