/*
Copyright (c) 2007-2010, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package twitter4j;

import junit.framework.AssertionFailedError;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.http.AuthorizationFactory;
import twitter4j.internal.http.HttpClient;
import twitter4j.internal.http.HttpClientFactory;
import twitter4j.internal.http.HttpClientImpl;
import twitter4j.internal.http.HttpClientWrapper;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class DAOTest extends TwitterTestBase {
    public DAOTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testEmptyJSON() throws Exception {
        HttpClientImpl http = new HttpClientImpl();

        // empty User list
        List<User> users = UserJSONImpl.createUserList(http.get("http://twitter4j.org/en/testcases/statuses/friends/T4J_hudson.json"));
        assertTrue(users.size() == 0);
        assertDeserializedFormIsEqual(users);

        // empty Status list
        List<Status> statuses = StatusJSONImpl.createStatusList(http.get("http://twitter4j.org/en/testcases/statuses/friends/T4J_hudson.json"));
        assertTrue(statuses.size() == 0);
        assertDeserializedFormIsEqual(statuses);

        // empty DirectMessages list
        List<DirectMessage> directMessages = DirectMessageJSONImpl.createDirectMessageList(http.get("http://twitter4j.org/en/testcases/statuses/friends/T4J_hudson.json"));
        assertTrue(directMessages.size() == 0);
        assertDeserializedFormIsEqual(directMessages);

        // empty Trends list
        List<Trends> trends = TrendsJSONImpl.createTrendsList(http.get("http://twitter4j.org/en/testcases/trends/daily-empty.json"));
        assertTrue(trends.size() == 0);
        assertDeserializedFormIsEqual(trends);
    }
    public void testTweet() throws Exception {
        JSONObject json = new JSONObject("{\"profile_image_url\":\"http://a3.twimg.com/profile_images/554278229/twitterProfilePhoto_normal.jpg\",\"created_at\":\"Thu, 24 Dec 2009 18:30:56 +0000\",\"from_user\":\"pskh\",\"to_user_id\":null,\"text\":\"test\",\"id\":7007483122,\"from_user_id\":215487,\"geo\":{\"type\":\"Point\",\"coordinates\":[37.78029, -122.39697]},\"source\":\"&lt;a href=&quot;http://twitter4j.org/&quot; rel=&quot;nofollow&quot;&gt;Twitter4J&lt;/a&gt;\"}");
        Tweet tweet = new TweetJSONImpl(json);
        GeoLocation geo = tweet.getGeoLocation();
        assertNotNull(geo);
        assertEquals(37.78029,geo.getLatitude());
        assertEquals(-122.39697,geo.getLongitude());
    }

    public void testLocation() throws Exception {
        JSONArray array = getJSONArrayFromClassPath("/trends-available.json");
        ResponseList<Location> locations = LocationJSONImpl.createLocationList(array, null);
        assertEquals(23,locations.size());
        Location location = locations.get(0);
        assertEquals("GB", location.getCountryCode());
        assertEquals("United Kingdom", location.getCountryName());
        assertEquals("United Kingdom", location.getName());
        assertEquals(12, location.getPlaceCode());
        assertEquals("Country", location.getPlaceName());
        assertEquals("http://where.yahooapis.com/v1/place/23424975", location.getURL());
        assertEquals(23424975, location.getWoeid());

    }

    public void testSchema() throws Exception {
        String[] schema;
        String url;

//        JSONObject json = new JSONObject("{\"a\":\"avalue\",\"b\":\"bvalue\",\"c\":{\"c-1\":12,\"c-2\":\"c-2value\"}}");
//        schema = new String[]{"a", "b", "c/c-1", "c/c-2"};
//        validateJSONObjectSchema(json, schema);
//        try {
//            schema = new String[]{"a", "b", "c/c-1"};
//            validateJSONObjectSchema(json, schema);
//            fail("c/c-2 is missing. expecting an AssertionFailedError.");
//        } catch (AssertionFailedError ignore) {
////            ignore.printStackTrace();
//        }
//        try {
//            schema = new String[]{"a", "b"};
//            validateJSONObjectSchema(json, schema);
//            fail("c is missing. expecting an AssertionFailedError.");
//        } catch (AssertionFailedError ignore) {
////            ignore.printStackTrace();
//        }
//        try {
//            schema = new String[]{"a", "b","c"};
//            validateJSONObjectSchema(json, schema);
//            fail("c/* is missing. expecting an AssertionFailedError.");
//        } catch (AssertionFailedError ignore) {
////            ignore.printStackTrace();
//        }
//        schema = new String[]{"a", "b", "c/*"};
//        validateJSONObjectSchema(json, schema);
//
//        JSONArray array = new JSONArray("[{\"slug\":\"art-design\",\"name\":\"Art & Design\"},{\"slug\":\"books\",\"name\":\"Books\"}]");
//        schema = new String[]{"slug", "name"};
//        validateJSONArraySchema(array, schema);

        // Location

//        schema = new String[]{"url","country","woeid","placeType/name","placeType/code","name","countryCode"};
//        url = "http://api.twitter.com/1/trends/available.json";
//        validateJSONArraySchema(url, schema);
        // Place
        if (!Boolean.valueOf(System.getProperties().getProperty("twitter4j.test.schema"))) {
            // skipping schema validation
            return;
        }

        schema = new String[]{
                "slug",
                "name",
        };
        url="http://api.twitter.com/1/users/suggestions.json";
        List categories = CategoryJSONImpl.createCategoriesList(validateJSONArraySchema(url, schema), null);
        assertEquals(20,categories.size());

        schema = new String[]{
                "slug",
                "name",
                "categories/*",
                "users/*"
        };
        url="http://api.twitter.com/1/users/suggestions/art-design.json";
        validateJSONObjectSchema(url, schema);


        schema = new String[]{
                "result/places/name",
                "result/places/street_address",
                "result/places/country_code",
                "result/places/id",
                "result/places/country",
                "result/places/place_type",
                "result/places/url",
                "result/places/full_name",
                "result/places/bounding_box/*",
                "result/places/contained_within/place_type",
                "result/places/contained_within/street_address",
                "result/places/contained_within/url",
                "result/places/contained_within/bounding_box/type",
                "result/places/contained_within/bounding_box/coordinates/*",
                "result/places/contained_within/full_name",
                "result/places/contained_within/country_code",
                "result/places/contained_within/name",
                "result/places/contained_within/id",
                "result/places/contained_within/country",
                "query",
                "query/type",
                "query/url",
                "query/params",
                "query/params/granularity",
                "query/params/coordinates",
                "query/params/coordinates/type",
                "query/params/coordinates/coordinates",
                "query/params/coordinates/coordinates/*",
                "query/params/accuracy",
        };
        url = "http://api.twitter.com/1/geo/reverse_geocode.json?lat=37.78215&long=-122.40060";
        validateJSONObjectSchema(url, schema);


        schema = new String[]{
                "next_cursor",
                "next_cursor_str",
                "previous_cursor",
                "previous_cursor_str",
                "lists/id",
                "lists/member_count",
                "lists/description",
                "lists/name",
                "lists/subscriber_count",
                "lists/slug",
                "lists/user/*",
                "lists/uri",
                "lists/full_name",
                "lists/mode",

        };
        url = "http://api.twitter.com/1/twit4j2/lists.json";
        validateJSONObjectSchema(url, schema);

        schema = new String[]{
                "id",
                "member_count",
                "description",
                "name",
                "subscriber_count",
                "slug",
                "user/*",
                "uri",
                "full_name",
                "mode",

        };
        url="http://api.twitter.com/1/twit4j2/lists/9499823.json";
        UserList userList = new UserListJSONImpl(validateJSONObjectSchema(url, schema));
        assertEquals("",userList.getDescription());
        assertEquals("@twit4j2/test",userList.getFullName());
        assertEquals(9499823,userList.getId());
        assertEquals(3,userList.getMemberCount());
        assertEquals("test",userList.getName());
        assertEquals("test",userList.getSlug());
        assertEquals(0,userList.getSubscriberCount());
        assertEquals("/twit4j2/test",userList.getURI().toString());
        assertNotNull(userList.getUser());
        assertTrue(userList.isPublic());


        schema = new String[]{
                "favorited",
                "in_reply_to_status_id",
                "created_at",
                "geo",
                "place",
                "source",
                "in_reply_to_screen_name",
                "in_reply_to_user_id",
                "coordinates",
                "truncated",
                "contributors",
                "id",
                "text",
                "user/*"

        };
        url="http://api.twitter.com/1/statuses/show/2245071380.json";
        Status status = new StatusJSONImpl(validateJSONObjectSchema(url, schema));

        schema = new String[]{
                "profile_background_image_url",
                "created_at",
                "friends_count",
                "profile_link_color",
                "description",
                "contributors_enabled",
                "status/*",
                "following",
                "profile_background_tile",
                "favourites_count",
                "profile_sidebar_fill_color",
                "url",
                "profile_image_url",
                "geo_enabled",
                "notifications",
                "profile_sidebar_border_color",
                "location",
                "screen_name",
                "verified",
                "time_zone",
                "profile_background_color",
                "protected",
                "name",
                "profile_text_color",
                "followers_count",
                "id",
                "lang",
                "statuses_count",
                "utc_offset"};

        url="http://api.twitter.com/1/users/show/yusukey.json";
        User user = new UserJSONImpl(validateJSONObjectSchema(url, schema));
    }

    private JSONObject validateJSONObjectSchema(String url, String[] knownNames) throws Exception {
        JSONObject json = getJSONObjectFromGetURL(url);
        validateJSONObjectSchema(json,knownNames);
        return json;
    }

    private static JSONObject validateJSONObjectSchema(JSONObject json, String[] knownNames) throws JSONException {
        boolean debug = false;
        Map<String, String[]> schemaMap = new HashMap<String, String[]>();
        List<String> names = new ArrayList<String>();
        if (debug) {
            System.out.println("validating:" + json);
        }
        for (int i = 0; i < knownNames.length; i++) {
            if (debug) {
                System.out.println("kownName[" + i + "]:" + knownNames[i]);
            }
            String knownName = knownNames[i];
            int index;
            if (-1 != (index = knownName.indexOf("/"))) {
                String parent = knownName.substring(0, index);
                String child = knownName.substring(index + 1);
                String[] array = schemaMap.get(parent);
                if (null == array) {
                    schemaMap.put(parent, new String[]{child});
                } else {
                    String[] newArray = new String[array.length + 1];
                    System.arraycopy(array, 0, newArray, 0, array.length);
                    newArray[newArray.length - 1] = child;
                    schemaMap.put(parent, newArray);
                }
                names.add(parent);
            } else {
                names.add(knownName);
            }
        }

        Iterator ite = json.keys();
        while (ite.hasNext()) {
            String name = (String) ite.next();
            boolean found = false;
            if (debug) {
                System.out.println("name:" + name);
            }
            for (String elementName : names) {
                if (debug) {
                    System.out.println("elementname:" + elementName);
                }
                Object obj = json.get(name);
                if (obj instanceof JSONObject || obj instanceof JSONArray) {
                    String[] children = schemaMap.get(name);
                    if (null == children) {
                        fail(elementName + ":" + name + " is not supposed to have any child but has:" + obj);
                    } else if (!children[0].equals("*")) {
                        validateJSONSchema(obj, children);
                    }
                }
                if (elementName.equals(name)) {
                    found = true;
                    break;
                }
            }
            if(!found){
                fail("unknown element:[" + name + "] in "+ json);
            }
        }
        return json;
    }
    private JSONArray validateJSONArraySchema(String url, String[] knownNames) throws Exception {
        return validateJSONArraySchema(getJSONArrayFromGetURL(url),knownNames);
    }

    private static void validateJSONSchema(Object json, String[] knownNames) throws JSONException {
        if (json instanceof JSONArray) {
            validateJSONArraySchema((JSONArray) json, knownNames);
        } else if (json instanceof JSONObject) {
            validateJSONObjectSchema((JSONObject) json, knownNames);
        } else {
            fail("expecting either JSONArray or JSONObject here. Passed:" + json.getClass().getName());
        }
    }
    private static JSONArray validateJSONArraySchema(JSONArray array, String[] knownNames) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            Object obj = array.get(i);
            if (obj instanceof JSONObject) {
                JSONObject json = array.getJSONObject(i);
                validateJSONObjectSchema(json, knownNames);
            }
        }
        return array;
    }

    private static JSONArray getJSONArrayFromClassPath(String path) throws Exception {
        return new JSONArray(getStringFromClassPath(path));
    }

    private static JSONObject getJSONObjectFromClassPath(String path) throws Exception {
        return new JSONObject(getStringFromClassPath(path));
    }

    private JSONObject getJSONObjectFromGetURL(String url) throws Exception {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setUser(id1.screenName);
        builder.setPassword(id1.password);
        return getJSONObjectFromGetURL(url, builder.build());
    }

    private static JSONObject getJSONObjectFromPostURL(String url, Configuration conf) throws Exception {
        HttpClientWrapper http = new HttpClientWrapper(conf);
        return http.post(url).asJSONObject();
    }

    private JSONObject getJSONObjectFromPostURL(String url) throws Exception {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setUser(id1.screenName);
        builder.setPassword(id1.password);
        return getJSONObjectFromPostURL(url, builder.build());
    }

    private static JSONObject getJSONObjectFromGetURL(String url, Configuration conf) throws Exception {
        HttpClientWrapper http = new HttpClientWrapper(conf);
        return http.get(url, AuthorizationFactory.getInstance(conf, false)).asJSONObject();
    }

    private JSONArray getJSONArrayFromGetURL(String url) throws Exception {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setUser(id1.screenName);
        builder.setPassword(id1.password);
        return getJSONArrayFromGetURL(url, builder.build());
    }


    private static JSONArray getJSONArrayFromGetURL(String url, Configuration conf) throws Exception {
        HttpClientWrapper http = new HttpClientWrapper(conf);
        return http.get(url).asJSONArray();
    }

    private static String getStringFromClassPath(String path) throws Exception {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            is = DAOTest.class.getResourceAsStream(path);
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            StringBuffer buf = new StringBuffer();
            String line;
            while (null != (line = br.readLine())) {
                buf.append(line);
            }
            return buf.toString();
        } finally {
            is.close();
            isr.close();
            br.close();
        }
    }


    public void testUserAsJSON() throws Exception {
        // single User
        HttpClientWrapper http = new HttpClientWrapper();
        User user = new UserJSONImpl(http.get("http://twitter4j.org/en/testcases/users/show/twit4j.json"));
        assertTrue(user.isGeoEnabled());
        assertFalse(user.isVerified());
        assertEquals(id1.screenName, user.getName());
        assertEquals(id1.screenName, user.getScreenName());
        assertNotNull(user.getLocation());
        assertNotNull(user.getDescription());
        assertNotNull(user.getProfileImageURL());
        assertNotNull(user.getURL());
        assertFalse(user.isProtected());

        assertTrue(0 <= user.getFavouritesCount());
        assertTrue(0 <= user.getFollowersCount());
        assertTrue(0 <= user.getFriendsCount());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getTimeZone());
        assertNotNull(user.getProfileBackgroundImageUrl());

        assertTrue(0 <= user.getStatusesCount());
        assertNotNull(user.getProfileBackgroundColor());
        assertNotNull(user.getProfileTextColor());
        assertNotNull(user.getProfileLinkColor());
        assertNotNull(user.getProfileSidebarBorderColor());
        assertNotNull(user.getProfileSidebarFillColor());
        assertNotNull(user.getProfileTextColor());

        assertTrue(1 < user.getFollowersCount());
        assertNotNull(user.getStatusCreatedAt());
        assertNotNull(user.getStatusText());
        assertNotNull(user.getStatusSource());
        assertFalse(user.isStatusFavorited());
        assertEquals(-1, user.getStatusInReplyToStatusId());
        assertEquals(-1, user.getStatusInReplyToUserId());
        assertFalse(user.isStatusFavorited());
        assertNull(user.getStatusInReplyToScreenName());
        assertDeserializedFormIsEqual(user);

        List<User> users;

        // User list
        users = UserJSONImpl.createUserList(http.get("http://twitter4j.org/en/testcases/statuses/followers/T4J_hudson.json"));
        assertTrue(users.size() > 0);
        assertDeserializedFormIsEqual(users);
    }
    public void testUserListAsJSON() throws Exception {
        
    }

    public void testStatusAsJSON() throws Exception {
        // single Status
        HttpClientImpl http = new HttpClientImpl();
        List<Status> statuses = StatusJSONImpl.createStatusList(http.get("http://twitter4j.org/en/testcases/statuses/public_timeline.json"));
        Status status = statuses.get(0);
        assertEquals(new Date(1259041785000l), status.getCreatedAt());
        assertEquals(6000554383l, status.getId());
        assertEquals("G_Shock22", status.getInReplyToScreenName());
        assertEquals(6000444309l, status.getInReplyToStatusId());
        assertEquals(20159829, status.getInReplyToUserId());
        assertNull(status.getGeoLocation());
        assertEquals("web", status.getSource());
        assertEquals("@G_Shock22 I smelled a roast session coming when yu said that shyt about @2koolNicia lol....", status.getText());
        assertEquals(23459577, status.getUser().getId());
        assertFalse(status.isRetweet());
        assertDeserializedFormIsEqual(statuses);
    }

    public void testRetweetStatusAsJSON() throws Exception {
        // single Status
        HttpClientImpl http = new HttpClientImpl();
        Status status = new StatusJSONImpl(http.get("http://twitter4j.org/en/testcases/statuses/retweet/6010814202.json"));
        assertEquals(new Date(1259078050000l), status.getCreatedAt());
        assertEquals(6011259778l, status.getId());
        assertEquals(null, status.getInReplyToScreenName());
        assertEquals(-1l, status.getInReplyToStatusId());
        assertEquals(-1, status.getInReplyToUserId());
        assertNull(status.getGeoLocation());
        assertEquals("<a href=\"http://apiwiki.twitter.com/\" rel=\"nofollow\">API</a>", status.getSource());
        assertEquals("RT @yusukey: この前取材受けた奴 -> 次世代のシステム環境を見据えたアプリケーションサーバー製品の選択 ITpro: http://special.nikkeibp.co.jp/ts/article/0iaa/104388/", status.getText());
        assertEquals(6358482, status.getUser().getId());
        assertTrue(status.isRetweet());
        assertDeserializedFormIsEqual(status);

    }

    public void testCategoryAsJSON() throws Exception {
        List<Category> categories = CategoryJSONImpl.createCategoriesList(
                getJSONArrayFromClassPath("/suggestions.json"),null);
        assertEquals(20, categories.size());
        assertEquals("art-design", categories.get(0).getSlug());
        assertEquals("Art & Design", categories.get(0).getName());

    }
    public void testPlaceAsJSON() throws Exception {
        List<Place> places = PlaceJSONImpl.createPlaceList(
                getJSONObjectFromClassPath("/reverse-geocode.json")
                        .getJSONObject("result").getJSONArray("places"),null);
        Place place = places.get(0);
        assertEquals("SoMa", place.getName());
        assertEquals("US", place.getCountryCode());
        assertEquals("2b6ff8c22edd9576", place.getId());
        assertEquals("", place.getCountry());
        assertEquals("neighborhood", place.getPlaceType());
        assertEquals("http://api.twitter.com/1/geo/id/2b6ff8c22edd9576.json", place.getURL());
        assertEquals("SoMa, San Francisco", place.getFullName());
        assertEquals("Polygon", place.getBoundingBoxType());
        GeoLocation[][] boundingBox = place.getBoundingBoxCoordinates();
        assertEquals(1, boundingBox.length);
        assertEquals(4, boundingBox[0].length);
        assertEquals(-122.42284884,boundingBox[0][0].getLatitude());
        assertEquals(37.76893497,boundingBox[0][0].getLongitude());
        assertEquals(-122.3964,boundingBox[0][1].getLatitude());
        assertEquals(37.76893497,boundingBox[0][1].getLongitude());
        assertEquals(-122.3964,boundingBox[0][2].getLatitude());
        assertEquals(37.78752897,boundingBox[0][2].getLongitude());
        assertEquals(-122.42284884,boundingBox[0][3].getLatitude());
        assertEquals(37.78752897,boundingBox[0][3].getLongitude());
        assertNull(place.getGeometryType());
        assertNull(place.getGeometryCoordinates());

        Place[] containedWithinArray = place.getContainedWithIn();
        assertEquals(1, containedWithinArray.length);
        Place containedWithin =containedWithinArray[0];
        assertNull(containedWithin.getContainedWithIn());
        assertEquals("San Francisco", containedWithin.getName());
        assertEquals("US", containedWithin.getCountryCode());
        assertEquals("5a110d312052166f", containedWithin.getId());
        assertEquals("", containedWithin.getCountry());
        assertEquals("city", containedWithin.getPlaceType());
        assertEquals("http://api.twitter.com/1/geo/id/5a110d312052166f.json", containedWithin.getURL());
        assertEquals("San Francisco", containedWithin.getFullName());
        boundingBox = containedWithin.getBoundingBoxCoordinates();
        assertEquals("Polygon", place.getBoundingBoxType());
        assertEquals(1, boundingBox.length);
        assertEquals(4, boundingBox[0].length);
        assertEquals(-122.51368188,boundingBox[0][0].getLatitude());
        assertEquals(37.70813196,boundingBox[0][0].getLongitude());
        assertEquals(-122.35845384,boundingBox[0][1].getLatitude());
        assertEquals(37.70813196,boundingBox[0][1].getLongitude());
        assertEquals(-122.35845384,boundingBox[0][2].getLatitude());
        assertEquals(37.83245301,boundingBox[0][2].getLongitude());
        assertEquals(-122.51368188,boundingBox[0][3].getLatitude());
        assertEquals(37.83245301,boundingBox[0][3].getLongitude());

        assertNull(place.getGeometryType());
        assertNull(place.getGeometryCoordinates());

        place = new PlaceJSONImpl(getJSONObjectFromClassPath("/5a110d312052166f.json"), null);
        assertNotNull(place.getGeometryType());
        assertNotNull(place.getGeometryCoordinates());


    }

    public void testDirectMessagesAsJSON() throws Exception {
        HttpClientImpl http = new HttpClientImpl();
        List<DirectMessage> directMessages = DirectMessageJSONImpl.createDirectMessageList(http.get("http://twitter4j.org/en/testcases/direct_messages.json"));
        DirectMessage dm = directMessages.get(0);
        assertEquals(new java.util.Date(1248177356000l),dm.getCreatedAt());
        assertEquals(6358482,dm.getRecipient().getId());
        assertEquals(246928323,dm.getId());
        assertEquals(6358482,dm.getRecipientId());
        assertEquals("twit4j",dm.getRecipientScreenName());
        assertEquals(6377362,dm.getSender().getId());
        assertEquals(6377362,dm.getSenderId());
        assertEquals("twit4j2",dm.getSenderScreenName());
        assertEquals("Tue Jul 21 20:55:39 KST 2009:directmessage test",dm.getText());
        assertDeserializedFormIsEqual(directMessages);
    }

    public void testTwitterMethod() throws Exception {
        assertDeserializedFormIsSingleton(TwitterMethod.ADD_LIST_MEMBER);
        assertDeserializedFormIsSingleton(TwitterMethod.BLOCKING_USERS);
    }

    public void testDevice() throws Exception {
        assertDeserializedFormIsSingleton(Device.SMS);
        assertDeserializedFormIsSingleton(Device.NONE);
    }

    /**
     *
     * @param obj the object to be asserted
     * @return the deserialized object
     * @throws Exception in the case the object is not (de)serializable
     */
    public static Object assertDeserializedFormIsEqual(Object obj) throws Exception {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(byteOutputStream);
        oos.writeObject(obj);
        byteOutputStream.close();
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteOutputStream.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(byteInputStream);
        Object that = ois.readObject();
        byteInputStream.close();
        ois.close();
        assertEquals(obj,that);
        return that;
    }

    public static Object assertDeserializedFormIsNotEqual(Object obj) throws Exception {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(byteOutputStream);
        oos.writeObject(obj);
        byteOutputStream.close();
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteOutputStream.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(byteInputStream);
        Object that = ois.readObject();
        byteInputStream.close();
        ois.close();
        assertFalse(obj.equals(that));
        return that;
    }

    /**
     *
     * @param obj the object to be asserted
     * @return the deserialized object
     * @throws Exception in the case the object is not (de)serializable
     */
    public static Object assertDeserializedFormIsSingleton(Object obj) throws Exception {
        Object that = assertDeserializedFormIsEqual(obj);
        assertTrue(obj == that);
        return that;
    }
}
