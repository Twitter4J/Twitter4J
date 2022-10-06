package twitter4j;

import twitter4j.api.ListsResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

class ListsResourcesImpl extends APIResourceBase implements ListsResources {
    ListsResourcesImpl(HttpClient http, ObjectFactory factory, String restBaseURL, Authorization auth, boolean mbeanEnabled, HttpParameter[] IMPLICIT_PARAMS,
                       String IMPLICIT_PARAMS_STR,
                       List<Consumer<RateLimitStatusEvent>> rateLimitStatusListeners,
                       List<Consumer<RateLimitStatusEvent>> rateLimitReachedListeners) {
        super(http, factory, restBaseURL, auth, mbeanEnabled, IMPLICIT_PARAMS, IMPLICIT_PARAMS_STR, rateLimitStatusListeners, rateLimitReachedListeners);
    }

    @Override
    public ResponseList<UserList> getUserLists(String listOwnerScreenName) throws TwitterException {
        return getUserLists(listOwnerScreenName, false);
    }

    @Override
    public ResponseList<UserList> getUserLists(String listOwnerScreenName, boolean reverse) throws TwitterException {
        return factory.createUserListList(get(restBaseURL + "lists/list.json", new HttpParameter("screen_name", listOwnerScreenName), new HttpParameter("reverse", reverse)));
    }

    @Override
    public ResponseList<UserList> getUserLists(long listOwnerUserId) throws TwitterException {
        return getUserLists(listOwnerUserId, false);
    }

    @Override
    public ResponseList<UserList> getUserLists(long listOwnerUserId, boolean reverse) throws TwitterException {
        return factory.createUserListList(get(restBaseURL + "lists/list.json", new HttpParameter("user_id", listOwnerUserId), new HttpParameter("reverse", reverse)));
    }

    @Override
    public ResponseList<Status> getUserListStatuses(long listId, Paging paging) throws TwitterException {
        return factory.createStatusList(get(restBaseURL + "lists/statuses.json", mergeParameters(paging.asPostParameterArray(Paging.SMCP, Paging.COUNT), new HttpParameter("list_id", listId))));
    }

    @Override
    public ResponseList<Status> getUserListStatuses(long ownerId, String slug, Paging paging) throws TwitterException {
        return factory.createStatusList(get(restBaseURL + "lists/statuses.json", mergeParameters(paging.asPostParameterArray(Paging.SMCP, Paging.COUNT), new HttpParameter[]{new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug)})));
    }

    @Override
    public ResponseList<Status> getUserListStatuses(String ownerScreenName,
                                                    String slug, Paging paging) throws TwitterException {
        return factory.createStatusList(get(restBaseURL + "lists/statuses.json", mergeParameters(paging.asPostParameterArray(Paging.SMCP, Paging.COUNT), new HttpParameter[]{new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug)})));
    }

    @Override
    public UserList destroyUserListMember(long listId, long userId) throws TwitterException {
        return factory.createAUserList(post(restBaseURL + "lists/members/destroy.json", new HttpParameter("list_id", listId), new HttpParameter("user_id", userId)));
    }

    @Override
    public UserList destroyUserListMember(long ownerId, String slug, long userId) throws TwitterException {
        return factory.createAUserList(post(restBaseURL + "lists/members/destroy.json", new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug), new HttpParameter("user_id", userId)));
    }

    @Override
    public UserList destroyUserListMember(long listId, String screenName) throws TwitterException {
        return factory.createAUserList(post(restBaseURL + "lists/members/destroy.json", new HttpParameter("list_id", listId), new HttpParameter("screen_name", screenName)));
    }

    @Override
    public UserList destroyUserListMember(String ownerScreenName, String slug,
                                          long userId) throws TwitterException {
        return factory.createAUserList(post(restBaseURL + "lists/members/destroy.json", new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug), new HttpParameter("user_id", userId)));
    }

    @Override
    public UserList destroyUserListMembers(long listId, String[] screenNames) throws TwitterException {
        return factory.createAUserList(post(restBaseURL + "lists/members/destroy_all.json", new HttpParameter("list_id", listId), new HttpParameter("screen_name", StringUtil.join(screenNames))));
    }

    @Override
    public UserList destroyUserListMembers(long listId, long[] userIds) throws TwitterException {
        return factory.createAUserList(post(restBaseURL + "lists/members/destroy_all.json", new HttpParameter("list_id", listId), new HttpParameter("user_id", StringUtil.join(userIds))));
    }

    @Override
    public UserList destroyUserListMembers(String ownerScreenName, String slug, String[] screenNames) throws TwitterException {
        return factory.createAUserList(post(restBaseURL + "lists/members/destroy_all.json", new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug), new HttpParameter("screen_name", StringUtil.join(screenNames))));
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(long cursor) throws TwitterException {
        return getUserListMemberships(20, cursor);
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(int count, long cursor) throws TwitterException {
        return factory.createPagableUserListList(get(restBaseURL + "lists/memberships.json", new HttpParameter("cursor", cursor), new HttpParameter("count", count)));
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(String listMemberScreenName, long cursor) throws TwitterException {
        return getUserListMemberships(listMemberScreenName, cursor, false);
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(String listMemberScreenName, int count, long cursor) throws TwitterException {
        return getUserListMemberships(listMemberScreenName, count, cursor, false);
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(String listMemberScreenName, long cursor, boolean filterToOwnedLists) throws TwitterException {
        return getUserListMemberships(listMemberScreenName, 20, cursor, filterToOwnedLists);
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(String listMemberScreenName, int count, long cursor, boolean filterToOwnedLists) throws TwitterException {
        return factory.createPagableUserListList(get(restBaseURL + "lists/memberships.json", new HttpParameter("screen_name", listMemberScreenName), new HttpParameter("count", count), new HttpParameter("cursor", cursor), new HttpParameter("filter_to_owned_lists", filterToOwnedLists)));
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(long listMemberId, long cursor) throws TwitterException {
        return getUserListMemberships(listMemberId, cursor, false);
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(long listMemberId, int count, long cursor) throws TwitterException {
        return getUserListMemberships(listMemberId, count, cursor, false);
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(long listMemberId, long cursor, boolean filterToOwnedLists) throws TwitterException {
        return getUserListMemberships(listMemberId, 20, cursor, filterToOwnedLists);
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(long listMemberId, int count, long cursor, boolean filterToOwnedLists) throws TwitterException {
        return factory.createPagableUserListList(get(restBaseURL + "lists/memberships.json", new HttpParameter("user_id", listMemberId), new HttpParameter("count", count), new HttpParameter("cursor", cursor), new HttpParameter("filter_to_owned_lists", filterToOwnedLists)));
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(long listId, long cursor) throws TwitterException {
        return getUserListSubscribers(listId, 20, cursor, false);
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(long listId, int count, long cursor) throws TwitterException {
        return getUserListSubscribers(listId, count, cursor, false);
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(long listId, int count, long cursor, boolean skipStatus) throws TwitterException {
        return factory.createPagableUserList(get(restBaseURL + "lists/subscribers.json", new HttpParameter("list_id", listId), new HttpParameter("count", count), new HttpParameter("cursor", cursor), new HttpParameter("skip_status", skipStatus)));
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(long ownerId, String slug, long cursor) throws TwitterException {
        return getUserListSubscribers(ownerId, slug, 20, cursor, false);
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(long ownerId, String slug, int count, long cursor) throws TwitterException {
        return getUserListSubscribers(ownerId, slug, count, cursor, false);
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(long ownerId, String slug, int count, long cursor, boolean skipStatus) throws TwitterException {
        return factory.createPagableUserList(get(restBaseURL + "lists/subscribers.json", new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug), new HttpParameter("count", count), new HttpParameter("cursor", cursor), new HttpParameter("skip_status", skipStatus)));
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(String ownerScreenName, String slug, long cursor) throws TwitterException {
        return getUserListSubscribers(ownerScreenName, slug, 20, cursor, false);
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(String ownerScreenName, String slug, int count, long cursor) throws TwitterException {
        return getUserListSubscribers(ownerScreenName, slug, count, cursor, false);
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(
            String ownerScreenName, String slug, int count, long cursor, boolean skipStatus)
            throws TwitterException {
        return factory.createPagableUserList(get(restBaseURL + "lists/subscribers.json", new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug), new HttpParameter("count", count), new HttpParameter("cursor", cursor), new HttpParameter("skip_status", skipStatus)));
    }

    @Override
    public UserList createUserListSubscription(long listId) throws TwitterException {
        return factory.createAUserList(post(restBaseURL + "lists/subscribers/create.json", new HttpParameter("list_id", listId)));
    }

    @Override
    public UserList createUserListSubscription(long ownerId, String slug) throws TwitterException {
        return factory.createAUserList(post(restBaseURL + "lists/subscribers/create.json", new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug)));
    }


    @Override
    public UserList createUserListSubscription(String ownerScreenName,
                                               String slug) throws TwitterException {
        return factory.createAUserList(post(restBaseURL + "lists/subscribers/create.json", new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug)));
    }

    @Override
    public User showUserListSubscription(long listId, long userId) throws TwitterException {
        return factory.createUser(get(restBaseURL + "lists/subscribers/show.json?list_id=" + listId + "&user_id=" + userId));
    }

    @Override
    public User showUserListSubscription(long ownerId, String slug, long userId) throws TwitterException {
        return factory.createUser(get(restBaseURL + "lists/subscribers/show.json?owner_id=" + ownerId + "&slug=" + slug + "&user_id=" + userId));
    }

    @Override
    public User showUserListSubscription(String ownerScreenName, String slug,
                                         long userId) throws TwitterException {
        return factory.createUser(get(restBaseURL + "lists/subscribers/show.json", new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug), new HttpParameter("user_id", userId)));
    }

    @Override
    public UserList destroyUserListSubscription(long listId) throws TwitterException {
        return factory.createAUserList(post(restBaseURL + "lists/subscribers/destroy.json", new HttpParameter("list_id", listId)));
    }

    @Override
    public UserList destroyUserListSubscription(long ownerId, String slug) throws TwitterException {
        return factory.createAUserList(post(restBaseURL + "lists/subscribers/destroy.json", new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug)));
    }

    @Override
    public UserList destroyUserListSubscription(String ownerScreenName,
                                                String slug) throws TwitterException {
        return factory.createAUserList(post(restBaseURL + "lists/subscribers/destroy.json", new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug)));
    }

    @Override
    public UserList createUserListMembers(long listId, long... userIds) throws TwitterException {
        return factory.createAUserList(post(restBaseURL + "lists/members/create_all.json", new HttpParameter("list_id", listId), new HttpParameter("user_id", StringUtil.join(userIds))));
    }

    @Override
    public UserList createUserListMembers(long ownerId, String slug, long... userIds) throws TwitterException {
        return factory.createAUserList(post(restBaseURL + "lists/members/create_all.json", new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug), new HttpParameter("user_id", StringUtil.join(userIds))));
    }

    @Override
    public UserList createUserListMembers(String ownerScreenName, String slug,
                                          long... userIds) throws TwitterException {
        return factory.createAUserList(post(restBaseURL + "lists/members/create_all.json", new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug), new HttpParameter("user_id", StringUtil.join(userIds))));
    }

    @Override
    public UserList createUserListMembers(long listId, String... screenNames) throws TwitterException {
        return factory.createAUserList(post(restBaseURL + "lists/members/create_all.json", new HttpParameter("list_id", listId), new HttpParameter("screen_name", StringUtil.join(screenNames))));
    }

    @Override
    public UserList createUserListMembers(long ownerId, String slug, String... screenNames) throws TwitterException {
        return factory.createAUserList(post(restBaseURL + "lists/members/create_all.json", new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug), new HttpParameter("screen_name", StringUtil.join(screenNames))));
    }

    @Override
    public UserList createUserListMembers(String ownerScreenName, String slug,
                                          String... screenNames) throws TwitterException {
        return factory.createAUserList(post(restBaseURL + "lists/members/create_all.json", new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug), new HttpParameter("screen_name", StringUtil.join(screenNames))));
    }

    @Override
    public User showUserListMembership(long listId, long userId) throws TwitterException {
        return factory.createUser(get(restBaseURL + "lists/members/show.json?list_id=" + listId + "&user_id=" + userId));
    }

    @Override
    public User showUserListMembership(long ownerId, String slug, long userId) throws TwitterException {
        return factory.createUser(get(restBaseURL + "lists/members/show.json?owner_id=" + ownerId + "&slug=" + slug + "&user_id=" + userId));
    }

    @Override
    public User showUserListMembership(String ownerScreenName, String slug,
                                       long userId) throws TwitterException {
        return factory.createUser(get(restBaseURL + "lists/members/show.json", new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug), new HttpParameter("user_id", userId)));
    }

    @Override
    public PagableResponseList<User> getUserListMembers(long listId
            , long cursor) throws TwitterException {
        return getUserListMembers(listId, 20, cursor, false);
    }

    @Override
    public PagableResponseList<User> getUserListMembers(long listId, int count, long cursor) throws TwitterException {
        return getUserListMembers(listId, count, cursor, false);
    }

    @Override
    public PagableResponseList<User> getUserListMembers(long listId, int count, long cursor, boolean skipStatus) throws TwitterException {
        return factory.createPagableUserList(get(restBaseURL + "lists/members.json", new HttpParameter("list_id", listId), new HttpParameter("count", count), new HttpParameter("cursor", cursor), new HttpParameter("skip_status", skipStatus)));
    }

    @Override
    public PagableResponseList<User> getUserListMembers(long ownerId, String slug, long cursor) throws TwitterException {
        return getUserListMembers(ownerId, slug, 20, cursor, false);
    }

    @Override
    public PagableResponseList<User> getUserListMembers(long ownerId, String slug, int count, long cursor) throws TwitterException {
        return getUserListMembers(ownerId, slug, count, cursor, false);
    }

    @Override
    public PagableResponseList<User> getUserListMembers(long ownerId, String slug, int count, long cursor, boolean skipStatus) throws TwitterException {
        return factory.createPagableUserList(get(restBaseURL + "lists/members.json", new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug), new HttpParameter("count", count), new HttpParameter("cursor", cursor), new HttpParameter("skip_status", skipStatus)));
    }

    @Override
    public PagableResponseList<User> getUserListMembers(String ownerScreenName, String slug, long cursor) throws TwitterException {
        return getUserListMembers(ownerScreenName, slug, 20, cursor, false);
    }

    @Override
    public PagableResponseList<User> getUserListMembers(String ownerScreenName, String slug, int count, long cursor) throws TwitterException {
        return getUserListMembers(ownerScreenName, slug, count, cursor, false);
    }

    @Override
    public PagableResponseList<User> getUserListMembers(String ownerScreenName,
                                                        String slug, int count, long cursor, boolean skipStatus) throws TwitterException {
        return factory.createPagableUserList(get(restBaseURL + "lists/members.json", new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug), new HttpParameter("count", count), new HttpParameter("cursor", cursor), new HttpParameter("skip_status", skipStatus)));
    }

    @Override
    public UserList createUserListMember(long listId, long userId) throws TwitterException {
        return factory.createAUserList(post(restBaseURL + "lists/members/create.json", new HttpParameter("user_id", userId), new HttpParameter("list_id", listId)));
    }

    @Override
    public UserList createUserListMember(long ownerId, String slug, long userId) throws TwitterException {
        return factory.createAUserList(post(restBaseURL + "lists/members/create.json", new HttpParameter("user_id", userId), new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug)));
    }

    @Override
    public UserList createUserListMember(String ownerScreenName, String slug,
                                         long userId) throws TwitterException {
        return factory.createAUserList(post(restBaseURL + "lists/members/create.json", new HttpParameter("user_id", userId), new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug)));
    }

    @Override
    public UserList destroyUserList(long listId) throws TwitterException {
        return factory.createAUserList(post(restBaseURL + "lists/destroy.json", new HttpParameter("list_id", listId)));
    }

    @Override
    public UserList destroyUserList(long ownerId, String slug) throws TwitterException {
        return factory.createAUserList(post(restBaseURL + "lists/destroy.json", new HttpParameter("owner_id", ownerId), new HttpParameter("slug", slug)));
    }

    @Override
    public UserList destroyUserList(String ownerScreenName, String slug)
            throws TwitterException {
        return factory.createAUserList(post(restBaseURL + "lists/destroy.json", new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug)));
    }

    @Override
    public UserList updateUserList(long listId, String newListName, boolean isPublicList, String newDescription) throws TwitterException {
        return updateUserList(newListName, isPublicList, newDescription, new HttpParameter("list_id", listId));
    }

    @Override
    public UserList updateUserList(long ownerId, String slug, String newListName, boolean isPublicList, String newDescription) throws TwitterException {
        return updateUserList(newListName, isPublicList, newDescription, new HttpParameter("owner_id", ownerId)
                , new HttpParameter("slug", slug));
    }

    @Override
    public UserList updateUserList(String ownerScreenName, String slug,
                                   String newListName, boolean isPublicList, String newDescription)
            throws TwitterException {
        return updateUserList(newListName, isPublicList, newDescription, new HttpParameter("owner_screen_name", ownerScreenName)
                , new HttpParameter("slug", slug));
    }

    private UserList updateUserList(String newListName, boolean isPublicList, String newDescription, HttpParameter... params) throws TwitterException {
        List<HttpParameter> httpParams = new ArrayList<>();
        Collections.addAll(httpParams, params);
        if (newListName != null) {
            httpParams.add(new HttpParameter("name", newListName));
        }
        httpParams.add(new HttpParameter("mode", isPublicList ? "public" : "private"));
        if (newDescription != null) {
            httpParams.add(new HttpParameter("description", newDescription));
        }
        return factory.createAUserList(post(restBaseURL + "lists/update.json", httpParams.toArray(new HttpParameter[0])));
    }

    @Override
    public UserList createUserList(String listName, boolean isPublicList, String description) throws TwitterException {
        List<HttpParameter> httpParams = new ArrayList<>();
        httpParams.add(new HttpParameter("name", listName));
        httpParams.add(new HttpParameter("mode", isPublicList ? "public" : "private"));
        if (description != null) {
            httpParams.add(new HttpParameter("description", description));
        }
        return factory.createAUserList(post(restBaseURL + "lists/create.json", httpParams.toArray(new HttpParameter[0])));
    }

    @Override
    public UserList showUserList(long listId) throws TwitterException {
        return factory.createAUserList(get(restBaseURL + "lists/show.json?list_id=" + listId));
    }

    @Override
    public UserList showUserList(long ownerId, String slug) throws TwitterException {
        return factory.createAUserList(get(restBaseURL + "lists/show.json?owner_id=" + ownerId + "&slug=" + slug));
    }

    @Override
    public UserList showUserList(String ownerScreenName, String slug)
            throws TwitterException {
        return factory.createAUserList(get(restBaseURL + "lists/show.json", new HttpParameter("owner_screen_name", ownerScreenName), new HttpParameter("slug", slug)));
    }

    @Override
    public PagableResponseList<UserList> getUserListSubscriptions(String listSubscriberScreenName, long cursor) throws TwitterException {
        return getUserListSubscriptions(listSubscriberScreenName, 20, cursor);
    }

    @Override
    public PagableResponseList<UserList> getUserListSubscriptions(String listSubscriberScreenName, int count, long cursor) throws TwitterException {
        return factory.createPagableUserListList(get(restBaseURL + "lists/subscriptions.json", new HttpParameter("screen_name", listSubscriberScreenName), new HttpParameter("count", count), new HttpParameter("cursor", cursor)));
    }

    @Override
    public PagableResponseList<UserList> getUserListSubscriptions(long listSubscriberId, long cursor) throws TwitterException {
        return getUserListSubscriptions(listSubscriberId, 20, cursor);
    }

    @Override
    public PagableResponseList<UserList> getUserListSubscriptions(long listSubscriberId, int count, long cursor) throws TwitterException {
        return factory.createPagableUserListList(get(restBaseURL + "lists/subscriptions.json", new HttpParameter("user_id", listSubscriberId), new HttpParameter("count", count), new HttpParameter("cursor", cursor)));
    }

    public PagableResponseList<UserList> getUserListsOwnerships(String listOwnerScreenName, long cursor) throws TwitterException {
        return getUserListsOwnerships(listOwnerScreenName, 20, cursor);
    }

    @Override
    public PagableResponseList<UserList> getUserListsOwnerships(String listOwnerScreenName, int count, long cursor) throws TwitterException {
        return factory.createPagableUserListList(get(restBaseURL + "lists/ownerships.json", new HttpParameter("screen_name", listOwnerScreenName), new HttpParameter("count", count), new HttpParameter("cursor", cursor)));
    }

    @Override
    public PagableResponseList<UserList> getUserListsOwnerships(long listOwnerId, long cursor) throws TwitterException {
        return getUserListsOwnerships(listOwnerId, 20, cursor);
    }

    @Override
    public PagableResponseList<UserList> getUserListsOwnerships(long listOwnerId, int count, long cursor) throws TwitterException {
        return factory.createPagableUserListList(get(restBaseURL + "lists/ownerships.json", new HttpParameter("user_id", listOwnerId), new HttpParameter("count", count), new HttpParameter("cursor", cursor)));
    }

}
