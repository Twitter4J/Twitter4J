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

import junit.framework.Assert;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static twitter4j.TwitterMethod.*;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.9
 */
public class UserStreamTest extends TwitterTestBase implements UserStreamListener {
    public UserStreamTest(String name) {
        super(name);
    }

    private User source;
    private User target;

    private Exception ex;

    public void testUserStreamEventTypes() throws Exception {
        InputStream is = TwitterTestBase.class.getResourceAsStream("/streamingapi-event-testcase.json");
        UserStream stream = new UserStreamImpl(new DispatcherFactory().getInstance(), is, conf1);

        source = null;
        target = null;
        ex = null;

        stream.next(this);
        waitForStatus("follow", CREATE_FRIENDSHIP);
        Assert.assertEquals(23456789, source.getId());
        Assert.assertEquals(12345678, target.getId());
        Assert.assertNull(ex);

        source = null;
        target = null;
        ex = null;
        stream.next(this);
        waitForStatus("delete direct message", DESTROY_DIRECT_MESSAGE);

        // This one is an unknown event type.  We should safely ignore it.
        stream.next(this);
//        waitForStatus("unknown", "unknown");
        Assert.assertNull(source);
        Assert.assertNull(target);
        Assert.assertNull(ex);
    }

    public void testUserStream() throws Exception {
        TwitterStream twitterStream = new TwitterStreamFactory(conf1).getInstance();
        twitterStream.addListener(this);
        try {
            twitter1.destroyBlock(id2.id);
        } catch (TwitterException ignore) {
        }
        try {
            twitter2.destroyBlock(id1.id);
        } catch (TwitterException ignore) {
        }
        try {
            twitter1.createFriendship(id2.id);
        } catch (TwitterException ignore) {
        }
        try {
            twitter2.createFriendship(id1.id);
        } catch (TwitterException ignore) {
        }

        //twit4j: id1.id
        //twit4j2: 6377362
        twitterStream.user("BAh7CToPY3JlYXR");
        //expecting onFriendList for twit4j and twit4j2
        waitForStatus("friend list", "onfriendlist");


        DirectMessage dm = twitter2.sendDirectMessage(id1.id, "test " + new Date());
        waitForStatus("sentDirectMessage", SEND_DIRECT_MESSAGE);

        twitter1.destroyDirectMessage(dm.getId());
//        waitForStatus("destroyedDirectMessage");


        Status status = twitter2.updateStatus(String.format("@%s %s", id1.screenName, new Date()));
        //expecting onStatus for twit4j from twit4j
        waitForStatus("onStatus", UPDATE_STATUS);

        twitter1.retweetStatus(status.getId());
        waitForStatus("onStatus", UPDATE_STATUS);

        twitter1.createFavorite(status.getId());
        waitForStatus("createdFavorite", CREATE_FAVORITE);

        twitter1.destroyFavorite(status.getId());
        waitForStatus("destroyedFavorite", DESTROY_FAVORITE);

        // unfollow twit4j
        twitter1.destroyFriendship(id2.id);
        waitForStatus("destroyedFriendship", DESTROY_FRIENDSHIP);

        // follow twit4j
        twitter1.createFriendship(id2.id);
        waitForStatus("createdFriendship", CREATE_FRIENDSHIP);

        status = twitter1.updateStatus("somerandometext " + new Date());
        waitForStatus("updatedStatus", UPDATE_STATUS);

        twitter1.destroyStatus(status.getId());
        waitForStatus("destroyedStatus", DESTROY_STATUS);

        // block twit4j2
        twitter1.createBlock(id2.id);
        waitForStatus("createdBlock", CREATE_BLOCK);

        // unblock twit4j2
        twitter1.destroyBlock(id2.id);
        waitForStatus("destroyedBlock", DESTROY_BLOCK);

        twitter1.updateProfile(null, null, new Date().toString(), null);
        waitForStatus("updateProfile", UPDATE_PROFILE);

        UserList list = twitter1.createUserList("test", true, "desctription");
        waitForStatus("createdUserList", CREATE_USER_LIST);

        list = twitter1.updateUserList(list.getId(), "test2", true, "description2");
        waitForStatus("updatedUserList", UPDATE_USER_LIST);

        twitter1.createUserListMember(list.getId(), id2.id);
        waitForStatus("addedListMember", CREATE_LIST_MEMBER);

        twitter2.createUserListSubscription(list.getId());
        waitForStatus("createdUserListSubscription", SUBSCRIBE_LIST);

        twitter1.destroyUserListMember(list.getId(), id2.id);
        waitForStatus("deletedUserListMember", DESTROY_LIST_MEMBER);

        twitter2.destroyUserListSubscription(list.getId());
        waitForStatus("destroyedUserListSubscription", UNSUBSCRIBE_LIST);

        twitter1.destroyUserList(list.getId());
        waitForStatus("destroyedUserList", DESTROY_USER_LIST);

        // doesn't seem to get direct message deletion message now
//        assertReceived("onDeletionNotice-directmessage", TwitterMethod.DESTROY_DIRECT_MESSAGE);
        // confirm if tracking term is effective
        boolean found = false;
        for (Object[] event : this.received) {
            if (UPDATE_STATUS.equals(event[0])) {
                Status status1 = (Status) event[1];
                if (status1.getText().contains("somerandometext")) {
                    found = true;
                    break;
                }
            }
        }
        Assert.assertTrue(found);
    }

    private synchronized void waitForStatus(String waitFor, Object waitfor) {
        System.out.println("waiting for:" + waitFor);
        boolean got = false;
        for (int i = 0; i < 10; i++) {
            try {
                this.wait(1000);
                if (received.size() > 0) {
                    Object lastReceived = received.get(received.size() - 1)[0];
                    System.out.println("lastReceived:" + lastReceived);
                    if (waitfor.equals(lastReceived)) {
                        got = true;
                        break;
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!got) {
            fail("didn't receive: " + waitfor);
        }
    }

    public void testDisplayURLNullCase() throws Exception {
        // http://jira.twitter4j.org/browse/TFJ-704
        // https://gist.github.com/4071950#file_2.parse_error_json_example.txt
        // sometimes display_url is null and display_url doesn't exist
        String rawJSON = "{  \"geo\": null,  \"in_reply_to_screen_name\": null,  \"favorited\": false,  \"text\": \"RT @fxThailandfans: RT @princezephyr: [PRESS PICS] 121114 f(x) - KOR-AUS Football Match Half-time Show http:\\/\\/t.co\\/cbm1aPCU http:\\/\\/t.co\\/ ...\",  \"possibly_sensitive\": false,  \"in_reply_to_status_id_str\": null,  \"created_at\": \"Wed Nov 14 12:03:02 +0000 2012\",  \"in_reply_to_user_id_str\": null,  \"retweet_count\": 2,  \"coordinates\": null,  \"source\": \"<a href=\\\"http:\\/\\/blackberry.com\\/twitter\\\" rel=\\\"nofollow\\\">Twitter for BlackBerry\\u00ae<\\/a>\",  \"entities\": {    \"hashtags\": [          ],    \"user_mentions\": [      {        \"indices\": [          3,          18        ],        \"screen_name\": \"fxThailandfans\",        \"id_str\": \"563934022\",        \"name\": \"fx_thailand\",        \"id\": 563934022      },      {        \"indices\": [          23,          36        ],        \"screen_name\": \"princezephyr\",        \"id_str\": \"251443396\",        \"name\": \"princezephyr\",        \"id\": 251443396      }    ],    \"urls\": [      {        \"indices\": [          103,          123        ],        \"display_url\": \"twitpic.com\\/bd46lk\",        \"url\": \"http:\\/\\/t.co\\/cbm1aPCU\",        \"expanded_url\": \"http:\\/\\/twitpic.com\\/bd46lk\"      },      {        \"indices\": [          124,          136        ],        \"url\": \"http:\\/\\/t.co\\/\",        \"expanded_url\": null      }    ]  },  \"place\": null,  \"retweeted\": false,  \"truncated\": false,  \"id_str\": \"268685470089764864\",  \"retweeted_status\": {    \"geo\": null,    \"in_reply_to_screen_name\": null,    \"favorited\": false,    \"text\": \"RT @princezephyr: [PRESS PICS] 121114 f(x) - KOR-AUS Football Match Half-time Show http:\\/\\/t.co\\/cbm1aPCU http:\\/\\/t.co\\/UW5HBXHn (7)\",    \"possibly_sensitive\": false,    \"in_reply_to_status_id_str\": null,    \"created_at\": \"Wed Nov 14 11:42:13 +0000 2012\",    \"in_reply_to_user_id_str\": null,    \"retweet_count\": 2,    \"coordinates\": null,    \"source\": \"<a href=\\\"http:\\/\\/www.tweetdeck.com\\\" rel=\\\"nofollow\\\">TweetDeck<\\/a>\",    \"entities\": {      \"hashtags\": [              ],      \"user_mentions\": [        {          \"indices\": [            3,            16          ],          \"screen_name\": \"princezephyr\",          \"id_str\": \"251443396\",          \"name\": \"princezephyr\",          \"id\": 251443396        }      ],      \"urls\": [        {          \"indices\": [            83,            103          ],          \"display_url\": \"twitpic.com\\/bd46lk\",          \"url\": \"http:\\/\\/t.co\\/cbm1aPCU\",          \"expanded_url\": \"http:\\/\\/twitpic.com\\/bd46lk\"        },        {          \"indices\": [            104,            124          ],          \"display_url\": \"twitpic.com\\/bd46tf\",          \"url\": \"http:\\/\\/t.co\\/UW5HBXHn\",          \"expanded_url\": \"http:\\/\\/twitpic.com\\/bd46tf\"        }      ]    },    \"place\": null,    \"retweeted\": false,    \"truncated\": false,    \"id_str\": \"268680229562744832\",    \"contributors\": null,    \"in_reply_to_user_id\": null,    \"in_reply_to_status_id\": null,    \"user\": {      \"friends_count\": 44,      \"profile_link_color\": \"939AED\",      \"followers_count\": 834,      \"is_translator\": false,      \"default_profile\": false,      \"follow_request_sent\": null,      \"contributors_enabled\": false,      \"time_zone\": \"Bangkok\",      \"created_at\": \"Thu Apr 26 18:22:34 +0000 2012\",      \"profile_background_color\": \"C0DEED\",      \"profile_background_tile\": true,      \"profile_background_image_url_https\": \"https:\\/\\/si0.twimg.com\\/profile_background_images\\/576266919\\/0bvixbmvg2lj69ajtg6i.jpeg\",      \"url\": \"http:\\/\\/www.fxthailand.com\\/\",      \"description\": \"f(x) Thailand Fans Board.\\r\\n\\r\\nhttps:\\/\\/www.facebook.com\\/Fxthailandfans\",      \"profile_sidebar_fill_color\": \"DDEEF6\",      \"default_profile_image\": false,      \"lang\": \"en\",      \"favourites_count\": 1,      \"profile_sidebar_border_color\": \"C0DEED\",      \"profile_image_url_https\": \"https:\\/\\/si0.twimg.com\\/profile_images\\/2592067376\\/3q4797y4mhfcvh7b7k14_normal.png\",      \"location\": \"\",      \"id_str\": \"563934022\",      \"verified\": false,      \"notifications\": null,      \"protected\": false,      \"screen_name\": \"fxThailandfans\",      \"following\": null,      \"geo_enabled\": false,      \"profile_use_background_image\": true,      \"profile_image_url\": \"http:\\/\\/a0.twimg.com\\/profile_images\\/2592067376\\/3q4797y4mhfcvh7b7k14_normal.png\",      \"name\": \"fx_thailand\",      \"profile_text_color\": \"333333\",      \"id\": 563934022,      \"listed_count\": 6,      \"statuses_count\": 1840,      \"profile_background_image_url\": \"http:\\/\\/a0.twimg.com\\/profile_background_images\\/576266919\\/0bvixbmvg2lj69ajtg6i.jpeg\",      \"utc_offset\": 25200    },    \"id\": 268680229562744832,    \"possibly_sensitive_editable\": true  },  \"contributors\": null,  \"in_reply_to_user_id\": null,  \"in_reply_to_status_id\": null,  \"user\": {    \"friends_count\": 96,    \"profile_link_color\": \"0084B4\",    \"followers_count\": 62,    \"is_translator\": false,    \"default_profile\": true,    \"follow_request_sent\": null,    \"contributors_enabled\": false,    \"time_zone\": null,    \"created_at\": \"Tue Jul 17 12:29:08 +0000 2012\",    \"profile_background_color\": \"C0DEED\",    \"profile_background_tile\": false,    \"profile_background_image_url_https\": \"https:\\/\\/si0.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",    \"url\": null,    \"description\": \"\\u0e2d\\u0e31\\u0e19\\u0e22\\u0e2d\\u0e07\\u0e04\\u0e48\\u0e30 !! \\u0e09\\u0e31\\u0e19\\u0e0b\\u0e39\\u0e08\\u0e35\\u0e27\\u0e07\\u0e21\\u0e34\\u0e2a\\u0e40\\u0e2d \\u0e09\\u0e31\\u0e19\\u0e23\\u0e31\\u0e48\\u0e27 \\u0e19\\u0e48\\u0e32\\u0e23\\u0e31\\u0e01 \\u0e25\\u0e2d\\u0e07\\u0e1f\\u0e2d\\u0e25\\u0e21\\u0e32\\u0e2a\\u0e34\\u0e04\\u0e48\\u0e30\\u0e41\\u0e25\\u0e49\\u0e27\\u0e04\\u0e38\\u0e13\\u0e08\\u0e30\\u0e23\\u0e39\\u0e49 *\\u0e22\\u0e34\\u0e49\\u0e21\\u0e2a\\u0e27\\u0e22*\",    \"profile_sidebar_fill_color\": \"DDEEF6\",    \"default_profile_image\": false,    \"lang\": \"th\",    \"favourites_count\": 2,    \"profile_sidebar_border_color\": \"C0DEED\",    \"profile_image_url_https\": \"https:\\/\\/si0.twimg.com\\/profile_images\\/2748527294\\/6cd82d00840820ddc2bedffdfb5e15bd_normal.jpeg\",    \"location\": \"\\u0e43\\u0e19\\u0e04\\u0e48\\u0e32\\u0e22 JYP \\u0e04\\u0e48\\u0e30\",    \"id_str\": \"700968576\",    \"verified\": false,    \"notifications\": null,    \"protected\": false,    \"screen_name\": \"suzy12missA\",    \"following\": null,    \"geo_enabled\": false,    \"profile_use_background_image\": true,    \"profile_image_url\": \"http:\\/\\/a0.twimg.com\\/profile_images\\/2748527294\\/6cd82d00840820ddc2bedffdfb5e15bd_normal.jpeg\",    \"name\": \"\\u0e19\\u0e32\\u0e07\\u0e40\\u0e1a!!\\u0e0b\\u0e39\\u0e08\\u0e35^^\",    \"profile_text_color\": \"333333\",    \"id\": 700968576,    \"listed_count\": 1,    \"statuses_count\": 428,    \"profile_background_image_url\": \"http:\\/\\/a0.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",    \"utc_offset\": null  },  \"id\": 268680229562744832,  \"possibly_sensitive_editable\": true}";
        Status status = TwitterObjectFactory.createStatus(rawJSON);
        URLEntity urlEntity = status.getURLEntities()[1];
        assertEquals(urlEntity.getURL(), urlEntity.getDisplayURL());
        assertEquals(urlEntity.getURL(), urlEntity.getExpandedURL());
    }

    private final List<Object[]> received = new ArrayList<Object[]>(3);

    private synchronized void notifyResponse() {
        this.notify();
    }

    @Override
    public void onStatus(Status status) {
        System.out.println("onStatus");
        received.add(new Object[]{UPDATE_STATUS, status});
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(status));
        notifyResponse();
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        System.out.println("onDeletionNotice");
        received.add(new Object[]{DESTROY_STATUS, statusDeletionNotice});
        notifyResponse();
    }

    @Override
    public void onDeletionNotice(long directMessageId, long userId) {
        System.out.println("onDeletionNotice(DirectMessage)");
        received.add(new Object[]{DESTROY_DIRECT_MESSAGE, directMessageId, userId});
        notifyResponse();
    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        System.out.println("onTrackLimitationNotice");
        received.add(new Object[]{"tracklimitation", numberOfLimitedStatuses});
        notifyResponse();
    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {
        System.out.println("onScrubGeo");
        received.add(new Object[]{"scrubgeo", userId, upToStatusId});
        notifyResponse();
    }

    @Override
    public void onStallWarning(StallWarning warning) {
        System.out.println("onStallWarning");
        received.add(new Object[]{"stallwarning", warning});
        notifyResponse();
    }

    @Override
    public void onFriendList(long[] friendIds) {
        System.out.println("onFriendList:" + Arrays.toString(friendIds));
        received.add(new Object[]{"onfriendlist", friendIds});
        notifyResponse();
    }

    @Override
    public void onFavorite(User source, User target, Status favoritedStatus) {
        System.out.println("onFavorite");
        received.add(new Object[]{CREATE_FAVORITE, source, target, favoritedStatus});
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(source));
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(target));
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(favoritedStatus));
        notifyResponse();
    }

    @Override
    public void onUnfavorite(User source, User target, Status unfavoritedStatus) {
        System.out.println("onUnfavorite");
        received.add(new Object[]{DESTROY_FAVORITE, source, target, unfavoritedStatus});
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(source));
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(target));
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(unfavoritedStatus));
        notifyResponse();
    }

    @Override
    public void onFollow(User source, User followedUser) {
        System.out.println("onfollow");
        this.source = source;
        this.target = followedUser;
        received.add(new Object[]{CREATE_FRIENDSHIP, source, followedUser});
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(source));
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(followedUser));
        notifyResponse();
    }

    @Override
    public void onUnfollow(User source, User unfollowedUser) {
        System.out.println("onunfollow");
        this.source = source;
        this.target = unfollowedUser;
        received.add(new Object[]{DESTROY_FRIENDSHIP, source, unfollowedUser});
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(source));
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(unfollowedUser));
        notifyResponse();
    }

    @Override
    public void onDirectMessage(DirectMessage directMessage) {
        System.out.println("onDirectMessage");
        received.add(new Object[]{SEND_DIRECT_MESSAGE, directMessage});
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(directMessage));
        notifyResponse();
    }

    @Override
    public void onUserListMemberAddition(User addedMember, User listOwner, UserList list) {
        System.out.println("onUserListMemberAddition");
        received.add(new Object[]{CREATE_LIST_MEMBER, addedMember, listOwner, list});
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(addedMember));
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(listOwner));
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(list));
        notifyResponse();
    }

    @Override
    public void onUserListMemberDeletion(User deletedMember, User listOwner, UserList list) {
        System.out.println("onUserListMemberDeletion");
        received.add(new Object[]{DESTROY_LIST_MEMBER, deletedMember, listOwner, list});
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(deletedMember));
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(listOwner));
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(list));
        notifyResponse();
    }

    @Override
    public void onUserListSubscription(User subscriber, User listOwner, UserList list) {
        System.out.println("onUserListSubscription");
        received.add(new Object[]{SUBSCRIBE_LIST, subscriber, listOwner, list});
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(subscriber));
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(listOwner));
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(list));
        notifyResponse();
    }

    @Override
    public void onUserListUnsubscription(User subscriber, User listOwner, UserList list) {
        System.out.println("onUserListUnsubscription");
        received.add(new Object[]{UNSUBSCRIBE_LIST, subscriber, listOwner, list});
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(subscriber));
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(listOwner));
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(list));
        notifyResponse();
    }

    @Override
    public void onUserListCreation(User listOwner, UserList list) {
        System.out.println("onUserListCreation");
        received.add(new Object[]{CREATE_USER_LIST, listOwner, list});
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(listOwner));
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(list));
        notifyResponse();
    }

    @Override
    public void onUserListUpdate(User listOwner, UserList list) {
        System.out.println("onUserListUpdate");
        received.add(new Object[]{UPDATE_USER_LIST, listOwner, list});
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(listOwner));
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(list));
        notifyResponse();
    }

    @Override
    public void onUserListDeletion(User listOwner, UserList list) {
        System.out.println("onUserListDeletion");
        received.add(new Object[]{DESTROY_USER_LIST, listOwner, list});
        notifyResponse();
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(listOwner));
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(list));
    }

    @Override
    public void onUserProfileUpdate(User updatedUser) {
        System.out.println("onUserProfileUpdate");
        received.add(new Object[]{UPDATE_PROFILE, updatedUser});
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(updatedUser));
        notifyResponse();
    }

    @Override
    public void onUserDeletion(long deletedUser) {
        System.out.println("onUserDeletion");
        received.add(new Object[]{"user_delete", deletedUser});
        notifyResponse();
    }

    @Override
    public void onUserSuspension(long suspendedUser) {
        System.out.println("onUserSuspension");
        received.add(new Object[]{"user_suspend", suspendedUser});
        notifyResponse();
    }

    @Override
    public void onBlock(User source, User blockedUser) {
        System.out.println("onBlock");
        received.add(new Object[]{CREATE_BLOCK, source, blockedUser});
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(source));
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(blockedUser));
        notifyResponse();
    }

    @Override
    public void onUnblock(User source, User unblockedUser) {
        System.out.println("onUnblock");
        received.add(new Object[]{DESTROY_BLOCK, source, unblockedUser});
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(source));
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(unblockedUser));
        notifyResponse();
    }

    @Override
    public void onRetweetedRetweet(User source, User target, Status retweetedStatus) {
        System.out.println("onRetweetedRetweet");
        received.add(new Object[]{"retweeted_retweet", source, target, retweetedStatus});
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(source));
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(target));
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(retweetedStatus));
        notifyResponse();
    }

    @Override
    public void onFavoritedRetweet(User source, User target, Status favoritedStatus) {
        received.add(new Object[]{"favorited_retweet", source, target, favoritedStatus});
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(source));
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(target));
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(favoritedStatus));
        notifyResponse();
    }

    @Override
    public void onQuotedTweet(User source, User target, Status quotedStatus) {
        received.add(new Object[]{"quoted_tweet", source, target, quotedStatus});
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(source));
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(target));
        Assert.assertNotNull(TwitterObjectFactory.getRawJSON(quotedStatus));
        notifyResponse();
    }

    @Override
    public void onException(Exception ex) {
        System.out.println("onException");
        received.add(new Object[]{ex});
        ex.printStackTrace();
        notifyResponse();
    }

    public void testUserStreamWithFollowing() {
        TwitterStream twitterStream = new TwitterStreamFactory(conf1).getInstance();
        twitterStream.addListener(this);
        twitterStream.user();
    }

}
