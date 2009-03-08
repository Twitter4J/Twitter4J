package twitter4j;

import java.util.List;

/**
 * A listner for receiving asynchronous responses from Twitter Async APIs.
 *
 * @see twitter4j.AsyncTwitter
 * @see twitter4j.TwitterAdapter
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

    void gotUserDetail(UserWithStatus userWithStatus);

    void gotDirectMessages(List<DirectMessage> messages);

    void gotSentDirectMessages(List<DirectMessage> messages);

    void sentDirectMessage(DirectMessage message);

    void deletedDirectMessage(DirectMessage message);

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
