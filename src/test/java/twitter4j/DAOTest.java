/*
Copyright (c) 2007-2009, Yusuke Yamamoto
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

import twitter4j.http.HttpClient;
import twitter4j.org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.List;

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
        HttpClient http = new HttpClient();

        // empty User list
        List<User> users = UserJSONImpl.createUserList(http.get("http://yusuke.homeip.net/twitter4j/en/testcases/statuses/friends/T4J_hudson.json"));
        assertTrue(users.size() == 0);
        assertDeserializedFormIsEqual(users);

        // empty Status list
        List<Status> statuses = StatusJSONImpl.createStatusList(http.get("http://yusuke.homeip.net/twitter4j/en/testcases/statuses/friends/T4J_hudson.json"));
        assertTrue(statuses.size() == 0);
        assertDeserializedFormIsEqual(statuses);

        // empty DirectMessages list
        List<DirectMessage> directMessages = DirectMessageJSONImpl.createDirectMessageList(http.get("http://yusuke.homeip.net/twitter4j/en/testcases/statuses/friends/T4J_hudson.json"));
        assertTrue(directMessages.size() == 0);
        assertDeserializedFormIsEqual(directMessages);

        // empty Trends list
        List<Trends> trends = TrendsJSONImpl.createTrendsList(http.get("http://yusuke.homeip.net/twitter4j/en/testcases/trends/daily-empty.json"));
        assertTrue(trends.size() == 0);
        assertDeserializedFormIsEqual(trends);

    }
    public void testTweet() throws Exception {
        JSONObject json = new JSONObject("{\"profile_image_url\":\"http://a3.twimg.com/profile_images/554278229/twitterProfilePhoto_normal.jpg\",\"created_at\":\"Thu, 24 Dec 2009 18:30:56 +0000\",\"from_user\":\"pskh\",\"to_user_id\":null,\"text\":\"test\",\"id\":7007483122,\"from_user_id\":215487,\"geo\":{\"type\":\"Point\",\"coordinates\":[37.78029, -122.39697]},\"source\":\"&lt;a href=&quot;http://yusuke.homeip.net/twitter4j/&quot; rel=&quot;nofollow&quot;&gt;Twitter4J&lt;/a&gt;\"}");
        Tweet tweet = new TweetJSONImpl(json);
        GeoLocation geo = tweet.getGeoLocation();
        assertNotNull(geo);
        assertEquals(37.78029,geo.getLatitude());
        assertEquals(-122.39697,geo.getLongitude());
    }

    public void testUserAsJSON() throws Exception {
        // single User
        HttpClient http = new HttpClient();
        User user = new UserJSONImpl(http.get("http://yusuke.homeip.net/twitter4j/en/testcases/users/show/twit4j.json"));
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
        users = UserJSONImpl.createUserList(http.get("http://yusuke.homeip.net/twitter4j/en/testcases/statuses/followers/T4J_hudson.json"));
        assertTrue(users.size() > 0);
        assertDeserializedFormIsEqual(users);
    }

    public void testStatusAsJSON() throws Exception {
        // single Status
        HttpClient http = new HttpClient();
        List<Status> statuses = StatusJSONImpl.createStatusList(http.get("http://yusuke.homeip.net/twitter4j/en/testcases/statuses/public_timeline.json"));
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
        HttpClient http = new HttpClient();
        Status status = new StatusJSONImpl(http.get("http://yusuke.homeip.net/twitter4j/en/testcases/statuses/retweet/6010814202.json"));
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

    public void testDirectMessagesAsJSON() throws Exception {
        HttpClient http = new HttpClient();
        List<DirectMessage> directMessages = DirectMessageJSONImpl.createDirectMessageList(http.get("http://yusuke.homeip.net/twitter4j/en/testcases/direct_messages.json"));
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
