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

import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.conf.ConfigurationContext;

import java.io.*;
import java.util.*;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class DAOTest extends TwitterTestBase {
    private final Configuration conf = ConfigurationContext.getInstance();

    public DAOTest(String name) {
        super(name);
    }

    public void testEmptyJSON() throws Exception {
        HttpClientImpl http = new HttpClientImpl();

        // empty User list
        List<User> users = UserJSONImpl.createUserList(http.get("http://twitter4j.org/en/testcases/statuses/friends/T4J_hudson.json"), conf);
        assertTrue(users.size() == 0);
        assertDeserializedFormIsEqual(users);

        // empty Status list
        List<Status> statuses = StatusJSONImpl.createStatusList(http.get("http://twitter4j.org/en/testcases/statuses/friends/T4J_hudson.json"), conf);
        assertTrue(statuses.size() == 0);
        assertDeserializedFormIsEqual(statuses);

        // empty DirectMessages list
        List<DirectMessage> directMessages = DirectMessageJSONImpl.createDirectMessageList(http.get("http://twitter4j.org/en/testcases/statuses/friends/T4J_hudson.json"), conf);
        assertTrue(directMessages.size() == 0);
        assertDeserializedFormIsEqual(directMessages);

        // empty Trends list
        List<Trends> trends = TrendsJSONImpl.createTrendsList(http.get("http://twitter4j.org/en/testcases/trends/daily-empty.json"), conf.isJSONStoreEnabled());
        assertTrue(trends.size() == 0);
        assertDeserializedFormIsEqual(trends);
    }

    public void testLocation() throws Exception {
        JSONArray array = getJSONArrayFromClassPath("/dao/trends-available.json");
        ResponseList<Location> locations = LocationJSONImpl.createLocationList(array, conf.isJSONStoreEnabled());
        assertEquals(23, locations.size());
        Location location = locations.get(0);
        assertEquals("GB", location.getCountryCode());
        assertEquals("United Kingdom", location.getCountryName());
        assertEquals("United Kingdom", location.getName());
        assertEquals(12, location.getPlaceCode());
        assertEquals("Country", location.getPlaceName());
        assertEquals("http://where.yahooapis.com/v1/place/23424975", location.getURL());
        assertEquals(23424975, location.getWoeid());

    }

    public void testUnparsable() throws Exception {
        String str;
        str = "";
        try {
            TwitterObjectFactory.createStatus(str);
            fail("should fail");
        } catch (TwitterException ignored) {
        } catch (Error notExpected) {
            fail("failed" + notExpected.getMessage());
        }
        try {
            TwitterObjectFactory.createStatus(str);
            fail("should fail");
        } catch (TwitterException ignored) {
        } catch (Error notExpected) {
            fail("failed" + notExpected.getMessage());
        }
        str = "{\"in_reply_to_status_id_str\":null,\"place\":null,\"in_reply_to_user_id\":null,\"text\":\"working\",\"contributors\":null,\"retweet_count\":0,\"in_reply_to_user_id_str\":null,\"retweeted\":false,\"id_str\":\"794626207\",\"source\":\"\\u003Ca href=\\\"http:\\/\\/twitterhelp.blogspot.com\\/2008\\/05\\/twitter-via-mobile-web-mtwittercom.html\\\" rel=\\\"nofollow\\\"\\u003Emobile web\\u003C\\/a\\u003E\",\"truncated\":false,\"geo\":null,\"in_reply_to_status_id\":null,\"favorited\":false,\"user\":{\"show_all_inline_media\":false,\"geo_enabled\":false,\"profile_background_tile\":false,\"time_zone\":null,\"favourites_count\":0,\"description\":null,\"friends_count\":0,\"profile_link_color\":\"0084B4\",\"location\":null,\"profile_sidebar_border_color\":\"C0DEED\",\"id_str\":\"14481043\",\"url\":null,\"follow_request_sent\":false,\"statuses_count\":1,\"profile_use_background_image\":true,\"lang\":\"en\",\"profile_background_color\":\"C0DEED\",\"profile_image_url\":\"http:\\/\\/a3.twimg.com\\/a\\/1292975674\\/images\\/default_profile_3_normal.png\",\"profile_background_image_url\":\"http:\\/\\/a3.twimg.com\\/a\\/1292975674\\/images\\/themes\\/theme1\\/bg.png\",\"followers_count\":44,\"protected\":false,\"contributors_enabled\":false,\"notifications\":false,\"screen_name\":\"Yusuke\",\"name\":\"Yusuke\",\"is_translator\":false,\"listed_count\":1,\"following\":false,\"verified\":false,\"profile_text_color\":\"333333\",\"id\":14481043,\"utc_offset\":null,\"created_at\":\"Tue Apr 22 21:49:13 +0000 2008\",\"profile_sidebar_fill_color\":\"DDEEF6\"},\"id\":794626207,\"coordinates\":null,\"in_reply_to_screen_name\":null,\"created_at\":\"Tue Apr 2200 21:49:34 +0000 2008\"";

        try {
            TwitterObjectFactory.createCategory(str);
            fail("should fail");
        } catch (TwitterException ignored) {
        } catch (Error notExpected) {
            fail("failed" + notExpected.getMessage());
        }
        try {
            TwitterObjectFactory.createCategory(str);
            fail("should fail");
        } catch (TwitterException ignored) {
        } catch (Error notExpected) {
            fail("failed" + notExpected.getMessage());
        }

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
//        url = "https://api.twitter.com/1.1/trends/available.json";
//        validateJSONArraySchema(url, schema);
        // Place
        if (!Boolean.valueOf(System.getProperties().getProperty("twitter4j.test.schema"))) {
            // skipping schema validation
            return;
        }

        schema = new String[]{
                "slug",
                "name",
                "size",
        };
        url = "https://api.twitter.com/1.1/users/suggestions.json";
        List categories = CategoryJSONImpl.createCategoriesList(validateJSONArraySchema(url, schema), null, conf);
        assertEquals(20, categories.size());

        schema = new String[]{
                "slug",
                "name",
                "size",
                "categories/*",
                "users/*"
        };
        url = "https://api.twitter.com/1.1/users/suggestions/art-design.json";
        validateJSONObjectSchema(url, schema);


        schema = new String[]{
                "result/places/name",
                "result/places/street_address",
                "result/places/attributes/*",
                "result/places/country_code",
                "result/places/id",
                "result/places/country",
                "result/places/place_type",
                "result/places/url",
                "result/places/full_name",
                "result/places/bounding_box/*",
                "result/places/contained_within/place_type",
                "result/places/contained_within/attributes/*",
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
        url = "https://api.twitter.com/1.1/geo/reverse_geocode.json?lat=37.78215&long=-122.40060";
        validateJSONObjectSchema(url, schema);


        schema = new String[]{
                "next_cursor",
                "next_cursor_str",
                "previous_cursor",
                "previous_cursor_str",
                "lists/id",
                "lists/id_str",
                "lists/member_count",
                "lists/description",
                "lists/name",
                "lists/subscriber_count",
                "lists/slug",
                "lists/user/*",
                "lists/uri",
                "lists/full_name",
                "lists/mode",
                "lists/following",

        };
        url = "https://api.twitter.com/1.1/twit4j2/lists.json";
        validateJSONObjectSchema(url, schema);

        schema = new String[]{
                "id",
                "id_str",
                "member_count",
                "description",
                "name",
                "subscriber_count",
                "slug",
                "user/*",
                "uri",
                "full_name",
                "mode",
                "following",

        };
        url = "https://api.twitter.com/1.1/twit4j2/lists/9499823.json";
        UserList userList = new UserListJSONImpl(validateJSONObjectSchema(url, schema));
        assertEquals("", userList.getDescription());
        assertEquals("@twit4j2/test", userList.getFullName());
        assertEquals(9499823, userList.getId());
        assertTrue(1 < userList.getMemberCount());
        assertEquals("test", userList.getName());
        assertEquals("test", userList.getSlug());
        assertEquals(0, userList.getSubscriberCount());
        assertEquals("/twit4j2/test", userList.getURI().toString());
        assertNotNull(userList.getUser());
        assertTrue(userList.isPublic());
        assertFalse(userList.isFollowing());


        schema = new String[]{
                "favorited",
                "in_reply_to_status_id",
                "in_reply_to_status_id_str",
                "created_at",
                "geo",
                "place",
                "source",
                "in_reply_to_screen_name",
                "in_reply_to_user_id",
                "in_reply_to_user_id_str",
                "coordinates",
                "truncated",
                "contributors",
                "id",
                "id_str",
                "text",
                "user/*",
                "retweeted",
                "retweet_count"

        };
        url = "https://api.twitter.com/1.1/statuses/show/2245071380.json";
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
                "profile_use_background_image",
                "protected",
                "name",
                "profile_text_color",
                "followers_count",
                "id",
                "id_str",
                "lang",
                "statuses_count",
                "follow_request_sent",
                "utc_offset",
                "listed_count",
                "is_translator",
                "show_all_inline_media"};

        url = "https://api.twitter.com/1.1/users/show/yusukey.json";
        User user = new UserJSONImpl(validateJSONObjectSchema(url, schema));
    }

    private JSONObject validateJSONObjectSchema(String url, String[] knownNames) throws Exception {
        JSONObject json = getJSONObjectFromGetURL(url);
        validateJSONObjectSchema(json, knownNames);
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
                System.out.println("knownName[" + i + "]:" + knownNames[i]);
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
            if (!found) {
                fail("unknown element:[" + name + "] in " + json);
            }
        }
        return json;
    }

    private JSONArray validateJSONArraySchema(String url, String[] knownNames) throws Exception {
        return validateJSONArraySchema(getJSONArrayFromGetURL(url), knownNames);
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
        HttpClient http = HttpClientFactory.getInstance(conf.getHttpClientConfiguration());
        return http.post(url).asJSONObject();
    }

    private JSONObject getJSONObjectFromPostURL(String url) throws Exception {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setUser(id1.screenName);
        builder.setPassword(id1.password);
        return getJSONObjectFromPostURL(url, builder.build());
    }

    private JSONObject getJSONObjectFromGetURL(String url, Configuration conf) throws Exception {
        HttpClient http = HttpClientFactory.getInstance(conf.getHttpClientConfiguration());
        return http.get(url, null, getOAuthOuthorization(conf), null).asJSONObject();
    }

    private JSONArray getJSONArrayFromGetURL(String url) throws Exception {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setUser(id1.screenName);
        builder.setPassword(id1.password);
        return getJSONArrayFromGetURL(url, builder.build());
    }


    private JSONArray getJSONArrayFromGetURL(String url, Configuration conf) throws Exception {
        HttpClient http = HttpClientFactory.getInstance(conf.getHttpClientConfiguration());
        return http.get(url, null, getOAuthOuthorization(conf), null).asJSONArray();
    }

    private OAuthAuthorization getOAuthOuthorization(Configuration conf) {
        OAuthAuthorization oauth = new OAuthAuthorization(conf);
        oauth.setOAuthConsumer(desktopConsumerKey, desktopConsumerSecret);
        oauth.setOAuthAccessToken(new AccessToken(id1.accessToken, id1.accessTokenSecret));
        return oauth;
    }

    private static String getStringFromClassPath(String path) throws Exception {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            is = DAOTest.class.getResourceAsStream(path);
            if (is == null) {
                throw new IllegalStateException(path + " not found.");
            }
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            StringBuilder buf = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                buf.append(line);
            }
            return buf.toString();
        } finally {
            if (is != null) {
                is.close();
                isr.close();
                br.close();
            }
        }
    }


    public void testUserAsJSON() throws Exception {
        // single User
        HttpClient http = HttpClientFactory.getInstance(conf.getHttpClientConfiguration());
        JSONObject json = getJSONObjectFromClassPath("/dao/user.json");
        User user = new UserJSONImpl(json);
        assertTrue(user.isGeoEnabled());
        assertFalse(user.isVerified());
        assertNotNull(id1.screenName, user.getName());
        assertNotNull(id1.screenName, user.getScreenName());
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
        assertNotNull(user.getProfileBackgroundImageURL());

        assertTrue(0 <= user.getStatusesCount());
        assertNotNull(user.getProfileBackgroundColor());
        assertNotNull(user.getProfileTextColor());
        assertNotNull(user.getProfileLinkColor());
        assertNotNull(user.getProfileSidebarBorderColor());
        assertNotNull(user.getProfileSidebarFillColor());
        assertNotNull(user.getProfileTextColor());

        assertTrue(1 < user.getFollowersCount());
        assertNotNull(user.getStatus().getCreatedAt());
        assertNotNull(user.getStatus().getText());
        assertNotNull(user.getStatus().getSource());
        assertFalse(user.getStatus().isFavorited());
        assertEquals(-1, user.getStatus().getInReplyToStatusId());
        assertEquals(-1, user.getStatus().getInReplyToUserId());
        assertFalse(user.getStatus().isFavorited());
        assertNull(user.getStatus().getInReplyToScreenName());
        assertDeserializedFormIsEqual(user);
        assertTrue(0 <= user.getListedCount());
        List<User> users;

        // User list
        users = UserJSONImpl.createUserList(http.get("http://twitter4j.org/en/testcases/statuses/followers/T4J_hudson.json"), conf);
        assertTrue(users.size() > 0);
        assertDeserializedFormIsEqual(users);
    }

    public void testStatusAsJSON() throws Exception {
        // single Status
        HttpClientImpl http = new HttpClientImpl();
        List<Status> statuses = StatusJSONImpl.createStatusList(http.get("http://twitter4j.org/en/testcases/statuses/public_timeline.json"), conf);
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
        Status status = new StatusJSONImpl(http.get("http://twitter4j.org/en/testcases/statuses/retweet/6010814202.json"), conf);
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
                getJSONArrayFromClassPath("/dao/suggestions.json"), null, conf);
        assertEquals(20, categories.size());
        assertEquals("art-design", categories.get(0).getSlug());
        assertEquals("Art & Design", categories.get(0).getName());
        assertTrue(0 < categories.get(0).getSize());

    }

    public void testPlaceAsJSON() throws Exception {
        List<Place> places = PlaceJSONImpl.createPlaceList(
                getJSONObjectFromClassPath("/dao/reverse-geocode.json")
                        .getJSONObject("result").getJSONArray("places"), null, conf
        );
        Place place = places.get(0);
        assertEquals("SoMa", place.getName());
        assertEquals("US", place.getCountryCode());
        assertEquals("2b6ff8c22edd9576", place.getId());
        assertEquals("", place.getCountry());
        assertEquals("neighborhood", place.getPlaceType());
        assertEquals("https://api.twitter.com/1/geo/id/2b6ff8c22edd9576.json", place.getURL());
        assertEquals("SoMa, San Francisco", place.getFullName());
        assertEquals("Polygon", place.getBoundingBoxType());
        GeoLocation[][] boundingBox = place.getBoundingBoxCoordinates();
        assertEquals(1, boundingBox.length);
        assertEquals(4, boundingBox[0].length);
        assertEquals(37.76893497, boundingBox[0][0].getLatitude());
        assertEquals(-122.42284884, boundingBox[0][0].getLongitude());
        assertEquals(37.76893497, boundingBox[0][1].getLatitude());
        assertEquals(-122.3964, boundingBox[0][1].getLongitude());
        assertEquals(37.78752897, boundingBox[0][2].getLatitude());
        assertEquals(-122.3964, boundingBox[0][2].getLongitude());
        assertEquals(37.78752897, boundingBox[0][3].getLatitude());
        assertEquals(-122.42284884, boundingBox[0][3].getLongitude());
        assertNull(place.getGeometryType());
        assertNull(place.getGeometryCoordinates());

        Place[] containedWithinArray = place.getContainedWithIn();
        assertEquals(1, containedWithinArray.length);
        Place containedWithin = containedWithinArray[0];
        assertNull(containedWithin.getContainedWithIn());
        assertEquals("San Francisco", containedWithin.getName());
        assertEquals("US", containedWithin.getCountryCode());
        assertEquals("5a110d312052166f", containedWithin.getId());
        assertEquals("", containedWithin.getCountry());
        assertEquals("city", containedWithin.getPlaceType());
        assertEquals("https://api.twitter.com/1/geo/id/5a110d312052166f.json", containedWithin.getURL());
        assertEquals("San Francisco", containedWithin.getFullName());
        boundingBox = containedWithin.getBoundingBoxCoordinates();
        assertEquals("Polygon", place.getBoundingBoxType());
        assertEquals(1, boundingBox.length);
        assertEquals(4, boundingBox[0].length);
        assertEquals(37.70813196, boundingBox[0][0].getLatitude());
        assertEquals(-122.51368188, boundingBox[0][0].getLongitude());
        assertEquals(37.70813196, boundingBox[0][1].getLatitude());
        assertEquals(-122.35845384, boundingBox[0][1].getLongitude());
        assertEquals(37.83245301, boundingBox[0][2].getLatitude());
        assertEquals(-122.35845384, boundingBox[0][2].getLongitude());
        assertEquals(37.83245301, boundingBox[0][3].getLatitude());
        assertEquals(-122.51368188, boundingBox[0][3].getLongitude());

        assertNull(place.getGeometryType());
        assertNull(place.getGeometryCoordinates());

        place = new PlaceJSONImpl(getJSONObjectFromClassPath("/dao/5a110d312052166f.json"));
        assertNotNull(place.getGeometryType());
        assertNotNull(place.getGeometryCoordinates());

        // Test that a geo object with geometry type "Point" works.
        place = new PlaceJSONImpl(getJSONObjectFromClassPath("/dao/3c6797665e2d42eb.json"));
        assertEquals(place.getGeometryType(), "Point");
        assertNotNull(place.getGeometryCoordinates());

        place = new PlaceJSONImpl(getJSONObjectFromClassPath("/dao/c3f37afa9efcf94b.json"));
        // MultiPolygon is not supported by twitter4j yet, so we set geometryType to null
        assertNull(place.getGeometryType());
        assertNull(place.getGeometryCoordinates());
    }

    public void testDirectMessagesAsJSON() throws Exception {
        HttpClientImpl http = new HttpClientImpl();
        List<DirectMessage> directMessages = DirectMessageJSONImpl.createDirectMessageList(http.get("http://twitter4j.org/en/testcases/direct_messages.json"), conf);
        DirectMessage dm = directMessages.get(0);
        assertEquals(new java.util.Date(1248177356000l), dm.getCreatedAt());
        assertEquals(6358482, dm.getRecipient().getId());
        assertEquals(246928323, dm.getId());
        assertEquals(6358482, dm.getRecipientId());
        assertEquals("twit4j", dm.getRecipientScreenName());
        assertEquals(6377362, dm.getSender().getId());
        assertEquals(6377362, dm.getSenderId());
        assertEquals("twit4j2", dm.getSenderScreenName());
        assertEquals("Tue Jul 21 20:55:39 KST 2009:directmessage test", dm.getText());
        assertDeserializedFormIsEqual(directMessages);
    }

    public void testTwitterMethod() throws Exception {
        assertDeserializedFormIsSingleton(TwitterMethod.CREATE_LIST_MEMBER);
        assertDeserializedFormIsSingleton(TwitterMethod.BLOCK_LIST);
    }

    /**
     * @param obj the object to be asserted
     * @return the deserialized object
     * @throws Exception in the case the object is not (de)serializable
     */
    private static Object assertDeserializedFormIsEqual(Object obj) throws Exception {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(byteOutputStream);
        oos.writeObject(obj);
        byteOutputStream.close();
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteOutputStream.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(byteInputStream);
        Object that = ois.readObject();
        byteInputStream.close();
        ois.close();
        assertEquals(obj, that);
        return that;
    }


    /**
     * @param obj the object to be asserted
     * @return the deserialized object
     * @throws Exception in the case the object is not (de)serializable
     */
    private static Object assertDeserializedFormIsSingleton(Object obj) throws Exception {
        Object that = assertDeserializedFormIsEqual(obj);
        assertTrue(obj == that);
        return that;
    }

    public void testStatusJSONImplSupportsMoreThan100RetweetedStatus() throws Exception {
        UserJSONImpl user = new UserJSONImpl(new JSONObject(getStringFromClassPath("/dao/24696018620.json")));
        assertNotNull(user.getStatus());
        assertNotNull(user.getStatus().getRetweetCount());
    }
}
