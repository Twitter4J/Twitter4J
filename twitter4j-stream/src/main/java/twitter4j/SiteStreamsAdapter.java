package twitter4j;


/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.8
 */
public class SiteStreamsAdapter implements SiteStreamsListener {
    @Override
    public void onStatus(long forUser, Status status) {
    }

    @Override
    public void onDeletionNotice(long forUser, StatusDeletionNotice statusDeletionNotice) {
    }

    @Override
    public void onFriendList(long forUser, long[] friendIds) {
    }

    @Override
    public void onFavorite(long forUser, User source, User target, Status favoritedStatus) {
    }

    @Override
    public void onUnfavorite(long forUser, User source, User target, Status unfavoritedStatus) {
    }

    @Override
    public void onFollow(long forUser, User source, User followedUser) {
    }

    @Override
    public void onUnfollow(long forUser, User source, User followedUser) {
    }

    @Override
    public void onDirectMessage(long forUser, DirectMessage directMessage) {
    }

    @Override
    public void onDeletionNotice(long forUser, long directMessageId, long userId) {
    }

    @Override
    public void onUserListMemberAddition(long forUser, User addedUser, User listOwner, UserList list) {
    }

    @Override
    public void onUserListMemberDeletion(long forUser, User deletedUser, User listOwner, UserList list) {
    }

    @Override
    public void onUserListSubscription(long forUser, User subscriber, User listOwner, UserList list) {
    }

    @Override
    public void onUserListUnsubscription(long forUser, User subscriber, User listOwner, UserList list) {
    }

    @Override
    public void onUserListCreation(long forUser, User listOwner, UserList list) {
    }

    @Override
    public void onUserListUpdate(long forUser, User listOwner, UserList list) {
    }

    @Override
    public void onUserListDeletion(long forUser, User listOwner, UserList list) {
    }

    @Override
    public void onUserProfileUpdate(long forUser, User updatedUser) {
    }

    @Override
    public void onBlock(long forUser, User source, User blockedUser) {
    }

    @Override
    public void onUnblock(long forUser, User source, User unblockedUser) {
    }

    @Override
    public void onDisconnectionNotice(String screenName) {
    }

    @Override
    public void onException(Exception ex) {
    }
}
