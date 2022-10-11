package twitter4j;

import twitter4j.v1.*;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

class TwitterV1Impl implements TwitterV1 {
    private final PlacesGeoResourcesImpl placeGeoResources;
    private final TimelinesResources timelinesResources;
    private final TweetsResources tweetsResources;
    private final SearchResource searchResource;
    private final DirectMessagesResources directMessagesResources;
    private final FriendsFollowersResources friendsFollowersResources;
    private final SavedSearchesResources savedSearchesResources;
    private final FavoritesResources favoritesResources;
    private final ListsResources listResources;
    private final HelpResources helpResources;
    private final SpamReportingResource spamReportingResource;
    private final TrendsResources trendResources;
    private final UsersResources usersResources;

    private final TwitterStream twitterStream;

    TwitterV1Impl(HttpClient http, ObjectFactory factory, String restBaseURL, String streamBaseURL,String uploadBaseURL,
                  Authorization auth, boolean mbeanEnabled, HttpParameter[] IMPLICIT_PARAMS, String IMPLICIT_PARAMS_STR,
                  List<Consumer<RateLimitStatusEvent>> rateLimitStatusListeners,
                  List<Consumer<RateLimitStatusEvent>> rateLimitReachedListeners,
                  String streamThreadName,     List<ConnectionLifeCycleListener> connectionLifeCycleListeners,

                 List<StreamListener> streamListeners,
   List<RawStreamListener> rawStreamListeners,boolean jsonStoreEnabled,boolean prettyDebug,boolean stallWarningsEnabled){
        helpResources = new HelpResourcesImpl(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
        spamReportingResource = new SpamReportingResourceImpl(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
        trendResources = new TrendsResourcesImpl(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
        placeGeoResources = new PlacesGeoResourcesImpl(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
        savedSearchesResources = new SavedSearchesResourcesImpl(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
        listResources = new ListsResourcesImpl(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
        timelinesResources = new TimelinesResourcesImpl(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
        tweetsResources = new TweetsResourcesImpl(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners, uploadBaseURL);
        searchResource = new SearchResourceImpl(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
        directMessagesResources = new DirectMessagesResourcesImpl(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
        friendsFollowersResources = new FriendsFollowersResourcesImpl(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
        favoritesResources = new FavoritesResourcesImpl(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
        usersResources = new UsersResourcesImpl(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
        twitterStream = new TwitterStreamImpl(streamBaseURL, streamThreadName, connectionLifeCycleListeners, streamListeners, rawStreamListeners, jsonStoreEnabled, prettyDebug, stallWarningsEnabled, http, auth);

    }
    @Override
    public TimelinesResources timelines() {
        return timelinesResources;
    }
    @Override
    public TweetsResources tweets() {
        return tweetsResources;
    }

    @Override
    public SearchResource search() {
        return searchResource;
    }

    @Override
    public DirectMessagesResources directMessages() {
        return directMessagesResources;
    }

    @Override
    public FriendsFollowersResources friendsFollowers() {
        return friendsFollowersResources;
    }

    @Override
    public UsersResources users() {
        return usersResources;
    }

    @Override
    public FavoritesResources favorites() {
        return favoritesResources;
    }

    @Override
    public ListsResources list() {
        return listResources;
    }

    @Override
    public SavedSearchesResources savedSearches() {
        return savedSearchesResources;
    }

    @Override
    public PlacesGeoResources placesGeo() {
        return placeGeoResources;
    }

    @Override
    public TrendsResources trends() {
        return trendResources;
    }

    @Override
    public SpamReportingResource spamReporting() {
        return spamReportingResource;
    }

    @Override
    public HelpResources help() {
        return helpResources;
    }
    @Override
    public TwitterStream stream() {
        return twitterStream;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TwitterV1Impl twitterV1 = (TwitterV1Impl) o;
        return Objects.equals(placeGeoResources, twitterV1.placeGeoResources) && Objects.equals(timelinesResources, twitterV1.timelinesResources) && Objects.equals(tweetsResources, twitterV1.tweetsResources) && Objects.equals(searchResource, twitterV1.searchResource) && Objects.equals(directMessagesResources, twitterV1.directMessagesResources) && Objects.equals(friendsFollowersResources, twitterV1.friendsFollowersResources) && Objects.equals(savedSearchesResources, twitterV1.savedSearchesResources) && Objects.equals(favoritesResources, twitterV1.favoritesResources) && Objects.equals(listResources, twitterV1.listResources) && Objects.equals(helpResources, twitterV1.helpResources) && Objects.equals(spamReportingResource, twitterV1.spamReportingResource) && Objects.equals(trendResources, twitterV1.trendResources) && Objects.equals(usersResources, twitterV1.usersResources) && Objects.equals(twitterStream, twitterV1.twitterStream);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeGeoResources, timelinesResources, tweetsResources, searchResource, directMessagesResources, friendsFollowersResources, savedSearchesResources, favoritesResources, listResources, helpResources, spamReportingResource, trendResources, usersResources, twitterStream);
    }

    @Override
    public String toString() {
        return "TwitterV1Impl{" +
                "placeGeoResources=" + placeGeoResources +
                ", timelinesResources=" + timelinesResources +
                ", tweetsResources=" + tweetsResources +
                ", searchResource=" + searchResource +
                ", directMessagesResources=" + directMessagesResources +
                ", friendsFollowersResources=" + friendsFollowersResources +
                ", savedSearchesResources=" + savedSearchesResources +
                ", favoritesResources=" + favoritesResources +
                ", listResources=" + listResources +
                ", helpResources=" + helpResources +
                ", spamReportingResource=" + spamReportingResource +
                ", trendResources=" + trendResources +
                ", usersResources=" + usersResources +
                ", twitterStream=" + twitterStream +
                '}';
    }
}
