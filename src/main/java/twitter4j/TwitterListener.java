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

import java.util.List;

/**
 * A listner for receiving asynchronous responses from Twitter Async APIs.
 *
 * @see twitter4j.AsyncTwitter
 * @see twitter4j.TwitterAdapter
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public interface TwitterListener {
    void gotPublicTimeline(List<Status> statuses);

    /**
     * @since Twitter4J 2.0.10
     */
    void gotHomeTimeline(List<Status> statuses);

    void gotFriendsTimeline(List<Status> statuses);

    void gotUserTimeline(List<Status> statuses);

    /**
     * @deprecated use gotShowStatus instead
     */
    void gotShow(Status status);

    /**
     * @since Twitter4J 2.0.1
     */
    void gotShowStatus(Status status);

    /**
     * @deprecated use updatedStatus instead
     */
    void updated(Status status);

    void updatedStatus(Status status);

    /**
     * @deprecated use gotMentions instead
     */
    void gotReplies(List<Status> statuses);

    /**
     * @since Twitter4J 2.0.1
     */
    void gotMentions(List<Status> statuses);
    /**
     * @since Twitter4J 2.0.10
     */
    void gotRetweetedByMe(List<Status> statuses);
    /**
     * @since Twitter4J 2.0.10
     */
    void gotRetweetedToMe(List<Status> statuses);
    /**
     * @since Twitter4J 2.0.10
     */
    void gotRetweetsOfMe(List<Status> statuses);

    void destroyedStatus(Status destroyedStatus);

    /**
     * @since Twitter4J 2.0.10
     */
    void retweetedStatus(Status retweetedStatus);

    void gotFriends(List<User> users);

    void gotFollowers(List<User> users);

    void gotFeatured(List<User> users);

    void gotUserDetail(User user);

    void gotDirectMessages(List<DirectMessage> messages);

    void gotSentDirectMessages(List<DirectMessage> messages);

    void sentDirectMessage(DirectMessage message);

    /**
     * @deprecated use destroyedDirectMessage instead
     */
    void deletedDirectMessage(DirectMessage message);

    /**
     * @since Twitter4J 2.0.1
     */
    void destroyedDirectMessage(DirectMessage message);

    void gotFriendsIDs(IDs ids);

    void gotFollowersIDs(IDs ids);

    /**
     * @deprecated use createdFriendship instead
     */
    void created(User user);

    /**
     * @since Twitter4J 2.0.1
     */
    void createdFriendship(User user);

    /**
     * @deprecated use destroyedFriendship instead
     */
    void destroyed(User user);

    /**
     * @since Twitter4J 2.0.1
     */
    void destroyedFriendship(User user);

    /**
     * @deprecated use gotExistsFriendship instead
     */
    void gotExists(boolean exists);

    /**
     * @since Twitter4J 2.0.1
     */
    void gotExistsFriendship(boolean exists);

    /**
     * @deprecated Use updatedProfile instead
     */
    void updatedLocation(User user);

    /**
     * @since Twitter4J 2.0.2
     */
    void updatedProfile(User user);

    void updatedProfileColors(User user);

    void gotRateLimitStatus(RateLimitStatus rateLimitStatus);

    void updatedDeliverlyDevice(User user);

    void gotFavorites(List<Status> statuses);

    void createdFavorite(Status status);

    void destroyedFavorite(Status status);

    /**
     * @deprecated use enabledNotification instead
     */
    void followed(User user);
    /**
     * @since Twitter4J 2.0.1
     */
    void enabledNotification(User user);

    /**
     * @deprecated use disabledNotification instead
     */
    void left(User user);
    /**
     * @since Twitter4J 2.0.1
     */
    void disabledNotification(User user);

    /**
     * @deprecated use createdBlock instead
     */
    void blocked(User user);

    /**
     * @since Twitter4J 2.0.1
     */
    void createdBlock(User user);

    /**
     * @deprecated use destroyedBlock instead
     */
    void unblocked(User user);

    /**
     * @since Twitter4J 2.0.1
     */
    void destroyedBlock(User user);

    /**
     * @since Twitter4J 2.0.4
     */
    void gotExistsBlock(boolean blockExists);
    
    /**
     * @since Twitter4J 2.0.4
     */
    void gotBlockingUsers(List<User> blockingUsers);

    /**
     * @since Twitter4J 2.0.4
     */
    void gotBlockingUsersIDs(IDs blockingUsersIDs);

    void tested(boolean test);

    /**
     * @deprecated not supported by Twitter API anymore
     */
    void gotDowntimeSchedule(String schedule);

    void searched(QueryResult queryResult);

    /**
     * @since Twitter4J 2.0.2
     */
    void gotTrends(Trends trends);

    /**
     * @since Twitter4J 2.0.2
     */
    void gotCurrentTrends(Trends trends);

    /**
     * @since Twitter4J 2.0.2
     */
    void gotDailyTrends(List<Trends> trendsList);

    /**
     * @since Twitter4J 2.0.2
     */
    void gotWeeklyTrends(List<Trends> trendsList);

    /**
     * @param te     TwitterException
     * @param method int
     */
    void onException(TwitterException te, int method);

}
