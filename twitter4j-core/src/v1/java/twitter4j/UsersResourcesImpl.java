package twitter4j;

import twitter4j.v1.*;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class UsersResourcesImpl extends APIResourceBase implements UsersResources {
    UsersResourcesImpl(HttpClient http, ObjectFactory factory, String restBaseURL, Authorization auth, boolean mbeanEnabled, HttpParameter[] IMPLICIT_PARAMS,
                       String IMPLICIT_PARAMS_STR,
                       List<Consumer<RateLimitStatusEvent>> rateLimitStatusListeners,
                       List<Consumer<RateLimitStatusEvent>> rateLimitReachedListeners) {
        super(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
    }

    @Override
    public AccountSettings getAccountSettings() throws TwitterException {
        return factory.createAccountSettings(get(restBaseURL + "account/settings.json"));
    }

    @Override
    public User verifyCredentials() throws TwitterException {
        return factory.createUser(get(restBaseURL + "account/verify_credentials.json", new HttpParameter("include_email", "true")));
    }

    @Override
    public AccountSettings updateAccountSettings(Integer trend_locationWoeid,
                                                 Boolean sleep_timeEnabled, String start_sleepTime,
                                                 String end_sleepTime, String time_zone, String lang)
            throws TwitterException {
        List<HttpParameter> profile = new ArrayList<>(6);
        if (trend_locationWoeid != null) {
            profile.add(new HttpParameter("trend_location_woeid", trend_locationWoeid));
        }
        if (sleep_timeEnabled != null) {
            profile.add(new HttpParameter("sleep_time_enabled", sleep_timeEnabled.toString()));
        }
        if (start_sleepTime != null) {
            profile.add(new HttpParameter("start_sleep_time", start_sleepTime));
        }
        if (end_sleepTime != null) {
            profile.add(new HttpParameter("end_sleep_time", end_sleepTime));
        }
        if (time_zone != null) {
            profile.add(new HttpParameter("time_zone", time_zone));
        }
        if (lang != null) {
            profile.add(new HttpParameter("lang", lang));
        }
        return factory.createAccountSettings(post(restBaseURL + "account/settings.json", profile.toArray(new HttpParameter[0])));

    }

    @Override
    public AccountSettings updateAllowDmsFrom(String allowDmsFrom) throws TwitterException {
        return factory.createAccountSettings(post(restBaseURL + "account/settings.json?allow_dms_from=" + allowDmsFrom));
    }

    @Override
    public User updateProfile(String name, String url
            , String location, String description) throws TwitterException {
        List<HttpParameter> profile = new ArrayList<>(4);
        addParameterToList(profile, "name", name);
        addParameterToList(profile, "url", url);
        addParameterToList(profile, "location", location);
        addParameterToList(profile, "description", description);
        return factory.createUser(post(restBaseURL + "account/update_profile.json", profile.toArray(new HttpParameter[0])));
    }


    private void addParameterToList(List<HttpParameter> colors,
                                    String paramName, String color) {
        if (color != null) {
            colors.add(new HttpParameter(paramName, color));
        }
    }

    @Override
    public User updateProfileImage(File image) throws TwitterException {
        checkFileValidity(image);
        return factory.createUser(post(restBaseURL + "account/update_profile_image.json", new HttpParameter("image", image)));
    }

    @Override
    public User updateProfileImage(InputStream image) throws TwitterException {
        return factory.createUser(post(restBaseURL + "account/update_profile_image.json", new HttpParameter("image", "image", image)));
    }


    @Override
    public PagableResponseList<User> getBlocksList() throws
            TwitterException {
        return getBlocksList(-1L);
    }

    @Override
    public PagableResponseList<User> getBlocksList(long cursor) throws
            TwitterException {
        return factory.createPagableUserList(get(restBaseURL + "blocks/list.json?cursor=" + cursor));
    }

    @Override
    public IDs getBlocksIDs() throws TwitterException {
        return factory.createIDs(get(restBaseURL + "blocks/ids.json"));
    }

    @Override
    public IDs getBlocksIDs(long cursor) throws TwitterException {
        return factory.createIDs(get(restBaseURL + "blocks/ids.json?cursor=" + cursor));
    }

    @Override
    public User createBlock(long userId) throws TwitterException {
        return factory.createUser(post(restBaseURL + "blocks/create.json?user_id=" + userId));
    }

    @Override
    public User createBlock(String screenName) throws TwitterException {
        return factory.createUser(post(restBaseURL + "blocks/create.json", new HttpParameter("screen_name", screenName)));
    }

    @Override
    public User destroyBlock(long userId) throws TwitterException {
        return factory.createUser(post(restBaseURL + "blocks/destroy.json?user_id=" + userId));
    }

    @Override
    public User destroyBlock(String screenName) throws TwitterException {
        return factory.createUser(post(restBaseURL + "blocks/destroy.json", new HttpParameter("screen_name", screenName)));
    }


    @Override
    public PagableResponseList<User> getMutesList(long cursor) throws
            TwitterException {
        return factory.createPagableUserList(get(restBaseURL + "mutes/users/list.json?cursor=" + cursor));
    }

    @Override
    public IDs getMutesIDs(long cursor) throws TwitterException {
        return factory.createIDs(get(restBaseURL + "mutes/users/ids.json?cursor=" + cursor));
    }

    @Override
    public User createMute(long userId) throws TwitterException {
        return factory.createUser(post(restBaseURL + "mutes/users/create.json?user_id=" + userId));
    }

    @Override
    public User createMute(String screenName) throws TwitterException {
        return factory.createUser(post(restBaseURL + "mutes/users/create.json", new HttpParameter("screen_name", screenName)));
    }

    @Override
    public User destroyMute(long userId) throws TwitterException {
        return factory.createUser(post(restBaseURL + "mutes/users/destroy.json?user_id=" + userId));
    }

    @Override
    public User destroyMute(String screenName) throws TwitterException {
        return factory.createUser(post(restBaseURL + "mutes/users/destroy.json", new HttpParameter("screen_name", screenName)));
    }

    @Override
    public ResponseList<User> lookupUsers(long... ids) throws TwitterException {
        return factory.createUserList(get(restBaseURL + "users/lookup.json", new HttpParameter("user_id", StringUtil.join(ids))));
    }

    @Override
    public ResponseList<User> lookupUsers(String... screenNames) throws TwitterException {
        return factory.createUserList(get(restBaseURL + "users/lookup.json", new HttpParameter("screen_name", StringUtil.join(screenNames))));
    }

    @Override
    public User showUser(long userId) throws TwitterException {
        return factory.createUser(get(restBaseURL + "users/show.json?user_id=" + userId));
    }

    @Override
    public User showUser(String screenName) throws TwitterException {
        return factory.createUser(get(restBaseURL + "users/show.json", new HttpParameter("screen_name", screenName)));
    }

    @Override
    public ResponseList<User> searchUsers(String query, int page) throws TwitterException {
        return factory.createUserList(get(restBaseURL + "users/search.json", new HttpParameter("q", query), new HttpParameter("per_page", 20), new HttpParameter("page", page)));
    }

    @Override
    public ResponseList<User> getContributees(long userId) throws TwitterException {
        return factory.createUserList(get(restBaseURL + "users/contributees.json?user_id=" + userId));
    }

    @Override
    public ResponseList<User> getContributees(String screenName) throws TwitterException {
        return factory.createUserList(get(restBaseURL + "users/contributees.json", new HttpParameter("screen_name", screenName)));
    }

    @Override
    public ResponseList<User> getContributors(long userId) throws TwitterException {
        return factory.createUserList(get(restBaseURL + "users/contributors.json?user_id=" + userId));
    }

    @Override
    public ResponseList<User> getContributors(String screenName) throws TwitterException {
        return factory.createUserList(get(restBaseURL + "users/contributors.json", new HttpParameter("screen_name", screenName)));
    }

    @Override
    public void removeProfileBanner() throws TwitterException {
        post(restBaseURL + "account/remove_profile_banner.json");
    }

    @Override
    public void updateProfileBanner(File image) throws TwitterException {
        checkFileValidity(image);
        post(restBaseURL + "account/update_profile_banner.json", new HttpParameter("banner", image));
    }

    @Override
    public void updateProfileBanner(InputStream image) throws TwitterException {
        post(restBaseURL + "account/update_profile_banner.json", new HttpParameter("banner", "banner", image));
    }

}
