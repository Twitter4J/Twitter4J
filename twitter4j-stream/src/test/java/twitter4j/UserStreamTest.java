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
import twitter4j.internal.async.DispatcherFactory;
import twitter4j.json.DataObjectFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.9
 */
public class UserStreamTest extends TwitterTestBase implements UserStreamListener {
    public UserStreamTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private User source;
    private User target;

    Exception ex;

    public void testUserStreamEventTypes() throws Exception {
        InputStream is = TwitterTestBase.class.getResourceAsStream("/streamingapi-event-testcase.json");
        UserStream stream = new UserStreamImpl(new DispatcherFactory().getInstance(), is, conf1);

        source = null;
        target = null;
        ex = null;

        stream.next(this);
        waitForStatus();
        Assert.assertEquals(23456789, source.getId());
        Assert.assertEquals(12345678, target.getId());
        Assert.assertNull(ex);

        source = null;
        target = null;
        ex = null;
        stream.next(this);
        waitForStatus();
        assertReceived("onDeletionNotice-directmessage", TwitterMethod.DESTROY_DIRECT_MESSAGE);

        // This one is an unknown event type.  We should safely ignore it.
        stream.next(this);
        waitForStatus();
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
        twitterStream.user(new String[]{"BAh7CToPY3JlYXR"});
        //expecting onFriendList for twit4j and twit4j2
        waitForStatus();
        waitForStatus();

        Status status = twitter2.updateStatus("@twit4j " + new Date());
        //expecting onStatus for twit4j from twit4j
        waitForStatus();

        twitter1.createFavorite(status.getId());
        waitForStatus();

        twitter1.destroyFavorite(status.getId());
        waitForStatus();

        // unfollow twit4j
        twitter2.destroyFriendship(id1.id);
        waitForStatus();

        // follow twit4j
        twitter2.createFriendship(id1.id);
        waitForStatus();

        // unfollow twit4j2
        twitter1.destroyFriendship(id2.id);
        waitForStatus();

        status = twitter2.updateStatus("somerandometext " + new Date());
        waitForStatus();
        // follow twit4j2
        twitter1.createFriendship(id2.id);
        waitForStatus();

        twitter1.retweetStatus(status.getId());
        waitForStatus();
        DirectMessage dm = twitter1.sendDirectMessage(id2.id, "test " + new Date());
        waitForStatus();

        twitter2.destroyStatus(status.getId());
        waitForStatus();
        // twitter1 is not permitted to delete direct message
        // twitter1.destroyDirectMessage(dm.getId());
        // waitForStatus();

        // block twit4j
        twitter1.createBlock(id2.id);
        waitForStatus();

        // unblock twit4j
        twitter1.destroyBlock(id2.id);
        waitForStatus();

        try {
            twitter1.createFriendship(id2.id);
            waitForStatus();
        } catch (TwitterException ignore) {
        }
        try {
            twitter2.createFriendship(id1.id);
            waitForStatus();
        } catch (TwitterException ignore) {
        }
        twitter1.updateProfile(null, null, new Date().toString(), null);
        waitForStatus();

        UserList list = twitter1.createUserList("test", true, "desctription");
        waitForStatus();
        list = twitter1.updateUserList(list.getId(), "test2", true, "description2");
        waitForStatus();
        twitter1.addUserListMember(list.getId(), id2.id);
        waitForStatus();
        twitter2.createUserListSubscription(list.getId());
        waitForStatus();
        twitter1.deleteUserListMember(list.getId(), id2.id);
        waitForStatus();
        twitter2.destroyUserListSubscription(list.getId());
        waitForStatus();
        twitter1.destroyUserList(list.getId());
        waitForStatus();

        // confirm if tracking term is effective
        boolean found = false;
        for (Object[] event : this.received) {
            if ("onstatus".equals(event[0])) {
                Status status1 = (Status) event[1];
                if (-1 != status1.getText().indexOf("somerandometext")) {
                    found = true;
                    break;
                }
            }
        }
        Assert.assertTrue(found);


        assertReceived("onstatus", "onstatus");
        assertReceived("onfriendlist", "onfriendlist");
        assertReceived("onFavorite", TwitterMethod.CREATE_FAVORITE);
        assertReceived("onUnfavorite", TwitterMethod.DESTROY_FAVORITE);
        assertReceived("onFollow", TwitterMethod.CREATE_FRIENDSHIP);
//            assertReceived(TwitterMethod.RETWEET_STATUS);
        assertReceived("onDirectMessage", TwitterMethod.SEND_DIRECT_MESSAGE);

        assertReceived("onDeletionNotice-status", TwitterMethod.DESTROY_STATUS);
//        assertReceived("onDeletionNotice-directmessage", TwitterMethod.DESTROY_DIRECT_MESSAGE);

        assertReceived("onUserListMemberAddition", TwitterMethod.ADD_LIST_MEMBER);
        assertReceived("onUserListMemberDeletion", TwitterMethod.DELETE_LIST_MEMBER);
        assertReceived("onUserListSubscription", TwitterMethod.SUBSCRIBE_LIST);
        assertReceived("onUserListUnsubscription", TwitterMethod.UNSUBSCRIBE_LIST);
        assertReceived("onUserListCreated", TwitterMethod.CREATE_USER_LIST);
        assertReceived("onUserListUpdated", TwitterMethod.UPDATE_USER_LIST);
        assertReceived("onUserListDestoyed", TwitterMethod.DESTROY_USER_LIST);


        assertReceived("onUserProfileUpdated", TwitterMethod.UPDATE_PROFILE);

        assertReceived("onBlock", TwitterMethod.CREATE_BLOCK);
        assertReceived("onUnblock", TwitterMethod.DESTROY_BLOCK);
    }

    private void assertReceived(String assertion, Object obj) {
        boolean received = false;
        for (Object[] event : this.received) {
            if (obj.equals(event[0])) {
                received = true;
                break;
            }
        }
        Assert.assertTrue(assertion, received);
    }

    private synchronized void waitForStatus() {
        try {
            this.wait(5000);
            System.out.println("notified.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    List<Object[]> received = new ArrayList<Object[]>(3);

    private synchronized void notifyResponse() {
        this.notify();
    }

    public void onStatus(Status status) {
        System.out.println("onStatus");
        received.add(new Object[]{"onstatus", status});
        Assert.assertNotNull(DataObjectFactory.getRawJSON(status));
        notifyResponse();
    }

    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        System.out.println("onDeletionNotice");
        received.add(new Object[]{TwitterMethod.DESTROY_STATUS, statusDeletionNotice});
        notifyResponse();
    }

    public void onDeletionNotice(long directMessageId, long userId) {
        System.out.println("onDeletionNotice");
        received.add(new Object[]{TwitterMethod.DESTROY_DIRECT_MESSAGE, directMessageId, userId});
        notifyResponse();
    }

    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        System.out.println("onTrackLimitationNotice");
        received.add(new Object[]{"tracklimitation", numberOfLimitedStatuses});
        notifyResponse();
    }

    public void onScrubGeo(long userId, long upToStatusId) {
        System.out.println("onScrubGeo");
        received.add(new Object[]{"scrubgeo", userId, upToStatusId});
        notifyResponse();
    }

    public void onFriendList(long[] friendIds) {
        System.out.println("onFriendList");
        received.add(new Object[]{"onfriendlist", friendIds});
        notifyResponse();
    }

    public void onFavorite(User source, User target, Status favoritedStatus) {
        System.out.println("onFavorite");
        received.add(new Object[]{TwitterMethod.CREATE_FAVORITE, source, target, favoritedStatus});
        Assert.assertNotNull(DataObjectFactory.getRawJSON(source));
        Assert.assertNotNull(DataObjectFactory.getRawJSON(target));
        Assert.assertNotNull(DataObjectFactory.getRawJSON(favoritedStatus));
        notifyResponse();
    }

    public void onUnfavorite(User source, User target, Status unfavoritedStatus) {
        System.out.println("onUnfavorite");
        received.add(new Object[]{TwitterMethod.DESTROY_FAVORITE, source, target, unfavoritedStatus});
        Assert.assertNotNull(DataObjectFactory.getRawJSON(source));
        Assert.assertNotNull(DataObjectFactory.getRawJSON(target));
        Assert.assertNotNull(DataObjectFactory.getRawJSON(unfavoritedStatus));
        notifyResponse();
    }

    public void onFollow(User source, User followedUser) {
        System.out.println("onfollow");
        this.source = source;
        this.target = followedUser;
        received.add(new Object[]{TwitterMethod.CREATE_FRIENDSHIP, source, followedUser});
        Assert.assertNotNull(DataObjectFactory.getRawJSON(source));
        Assert.assertNotNull(DataObjectFactory.getRawJSON(followedUser));
        notifyResponse();
    }

    public void onRetweet(User source, User target, Status retweetedStatus) {
        System.out.println("onRetweet");
        received.add(new Object[]{TwitterMethod.RETWEET_STATUS, retweetedStatus});
        Assert.assertNotNull(DataObjectFactory.getRawJSON(retweetedStatus));
        notifyResponse();
    }

    public void onDirectMessage(DirectMessage directMessage) {
        System.out.println("onDirectMessage");
        received.add(new Object[]{TwitterMethod.SEND_DIRECT_MESSAGE, directMessage});
        Assert.assertNotNull(DataObjectFactory.getRawJSON(directMessage));
        notifyResponse();
    }

    public void onUserListMemberAddition(User addedMember, User listOwner, UserList list) {
        System.out.println("onUserListMemberAddition");
        received.add(new Object[]{TwitterMethod.ADD_LIST_MEMBER, addedMember, listOwner, list});
        Assert.assertNotNull(DataObjectFactory.getRawJSON(addedMember));
        Assert.assertNotNull(DataObjectFactory.getRawJSON(listOwner));
        Assert.assertNotNull(DataObjectFactory.getRawJSON(list));
        notifyResponse();
    }

    public void onUserListMemberDeletion(User deletedMember, User listOwner, UserList list) {
        System.out.println("onUserListMemberDeletion");
        received.add(new Object[]{TwitterMethod.DELETE_LIST_MEMBER, deletedMember, listOwner, list});
        Assert.assertNotNull(DataObjectFactory.getRawJSON(deletedMember));
        Assert.assertNotNull(DataObjectFactory.getRawJSON(listOwner));
        Assert.assertNotNull(DataObjectFactory.getRawJSON(list));
        notifyResponse();
    }

    public void onUserListSubscription(User subscriber, User listOwner, UserList list) {
        System.out.println("onUserListSubscription");
        received.add(new Object[]{TwitterMethod.SUBSCRIBE_LIST, subscriber, listOwner, list});
        Assert.assertNotNull(DataObjectFactory.getRawJSON(subscriber));
        Assert.assertNotNull(DataObjectFactory.getRawJSON(listOwner));
        Assert.assertNotNull(DataObjectFactory.getRawJSON(list));
        notifyResponse();
    }

    public void onUserListUnsubscription(User subscriber, User listOwner, UserList list) {
        System.out.println("onUserListUnsubscription");
        received.add(new Object[]{TwitterMethod.UNSUBSCRIBE_LIST, subscriber, listOwner, list});
        Assert.assertNotNull(DataObjectFactory.getRawJSON(subscriber));
        Assert.assertNotNull(DataObjectFactory.getRawJSON(listOwner));
        Assert.assertNotNull(DataObjectFactory.getRawJSON(list));
        notifyResponse();
    }

    public void onUserListCreation(User listOwner, UserList list) {
        System.out.println("onUserListCreation");
        received.add(new Object[]{TwitterMethod.CREATE_USER_LIST, listOwner, list});
        Assert.assertNotNull(DataObjectFactory.getRawJSON(listOwner));
        Assert.assertNotNull(DataObjectFactory.getRawJSON(list));
        notifyResponse();
    }

    public void onUserListUpdate(User listOwner, UserList list) {
        System.out.println("onUserListUpdate");
        received.add(new Object[]{TwitterMethod.UPDATE_USER_LIST, listOwner, list});
        Assert.assertNotNull(DataObjectFactory.getRawJSON(listOwner));
        Assert.assertNotNull(DataObjectFactory.getRawJSON(list));
        notifyResponse();
    }

    public void onUserListDeletion(User listOwner, UserList list) {
        System.out.println("onUserListDeletion");
        received.add(new Object[]{TwitterMethod.DESTROY_USER_LIST, listOwner, list});
        notifyResponse();
        Assert.assertNotNull(DataObjectFactory.getRawJSON(listOwner));
        Assert.assertNotNull(DataObjectFactory.getRawJSON(list));
    }

    public void onUserProfileUpdate(User updatedUser) {
        System.out.println("onUserProfileUpdate");
        received.add(new Object[]{TwitterMethod.UPDATE_PROFILE, updatedUser});
        Assert.assertNotNull(DataObjectFactory.getRawJSON(updatedUser));
        notifyResponse();
    }

    public void onBlock(User source, User blockedUser) {
        System.out.println("onBlock");
        received.add(new Object[]{TwitterMethod.CREATE_BLOCK, source, blockedUser});
        Assert.assertNotNull(DataObjectFactory.getRawJSON(source));
        Assert.assertNotNull(DataObjectFactory.getRawJSON(blockedUser));
        notifyResponse();
    }

    public void onUnblock(User source, User unblockedUser) {
        System.out.println("onUnblock");
        received.add(new Object[]{TwitterMethod.DESTROY_BLOCK, source, unblockedUser});
        Assert.assertNotNull(DataObjectFactory.getRawJSON(source));
        Assert.assertNotNull(DataObjectFactory.getRawJSON(unblockedUser));
        notifyResponse();
    }

    public void onException(Exception ex) {
        System.out.println("onException");
        received.add(new Object[]{ex});
        ex.printStackTrace();
        notifyResponse();
    }


}
