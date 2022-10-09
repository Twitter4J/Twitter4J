package twitter4j;

import twitter4j.v1.*;

/**
 * Interface represents Twitter API v1.1
 */
public interface TwitterV1 {
    /**
     * @return {@link TimelinesResources}
     * @since Twitter4J 3.0.4
     */
    TimelinesResources timelines();

    /**
     * @return {@link TweetsResources}
     * @since Twitter4J 3.0.4
     */
    TweetsResources tweets();

    /**
     * @return {@link SearchResource}
     * @since Twitter4J 3.0.4
     */
    SearchResource search();

    /**
     * @return {@link DirectMessagesResources}
     * @since Twitter4J 3.0.4
     */
    DirectMessagesResources directMessages();

    /**
     * @return {@link FriendsFollowersResources}
     * @since Twitter4J 3.0.4
     */
    FriendsFollowersResources friendsFollowers();

    /**
     * @return {@link UsersResources}
     * @since Twitter4J 3.0.4
     */
    UsersResources users();

    /**
     * @return {@link FavoritesResources}
     * @since Twitter4J 3.0.4
     */
    FavoritesResources favorites();

    /**
     * @return {@link ListsResources}
     * @since Twitter4J 3.0.4
     */
    ListsResources list();

    /**
     * @return {@link SavedSearchesResources}
     * @since Twitter4J 3.0.4
     */
    SavedSearchesResources savedSearches();

    /**
     * @return {@link PlacesGeoResources}
     * @since Twitter4J 3.0.4
     */
    PlacesGeoResources placesGeo();

    /**
     * @return {@link TrendsResources}
     * @since Twitter4J 3.0.4
     */
    TrendsResources trends();

    /**
     * @return {@link SpamReportingResource}
     * @since Twitter4J 3.0.4
     */
    SpamReportingResource spamReporting();

    /**
     * @return {@link HelpResources}
     * @since Twitter4J 3.0.4
     */
    HelpResources help();
}
