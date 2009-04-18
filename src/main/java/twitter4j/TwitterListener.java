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

    void gotFriendsTimeline(List<Status> statuses);

    void gotUserTimeline(List<Status> statuses);

    void gotShow(Status status);

    void updated(Status status);

    void gotReplies(List<Status> statuses);

    void destroyedStatus(Status destroyedStatus);

    void gotFriends(List<User> users);

    void gotFollowers(List<User> users);

    void gotFeatured(List<User> users);

    void gotUserDetail(ExtendedUser extendedUser);

    void gotDirectMessages(List<DirectMessage> messages);

    void gotSentDirectMessages(List<DirectMessage> messages);

    void sentDirectMessage(DirectMessage message);

    void deletedDirectMessage(DirectMessage message);

    void gotFriendsIDs(IDs ids);

    void gotFollowersIDs(IDs ids);

    void created(User user);

    void destroyed(User user);

    void gotExists(boolean exists);

    void updatedLocation(User user);

    void gotRateLimitStatus(RateLimitStatus rateLimitStatus);

    void updatedDeliverlyDevice(User user);

    void gotFavorites(List<Status> statuses);

    void createdFavorite(Status status);

    void destroyedFavorite(Status status);

    void followed(User user);

    void left(User user);

    void blocked(User user);

    void unblocked(User user);

    void tested(boolean test);

    void gotDowntimeSchedule(String schedule);

    void searched(QueryResult queryResult);

    /**
     * @param te     TwitterException
     * @param method int
     */
    void onException(TwitterException te, int method);

}
