package twitter4j;

public class StatusAdapter implements StatusListener
{
    @Override
    public void onDeletionNotice (StatusDeletionNotice statusDeletionNotice)
    {
    }

    @Override
    public void onException (Exception ex)
    {
    }

    @Override
    public void onFriendList (int [] friendIds)
    {
    }

    @Override
    public void onStatus (Status status)
    {
    }

    @Override
    public void onTrackLimitationNotice (int numberOfLimitedStatuses)
    {
    }

    @Override
    public void onFavorite (User source, User target, Status targetObject)
    {
    }

    @Override
    public void onFollow (User source, User target)
    {
    }

    @Override
    public void onUnfavorite (User source, User target, Status targetObject)
    {
    }

    @Override
    public void onRetweet (User source, User target, Status targetObject)
    {
    }

    @Override
    public void onUnfollow (User source, User target)
    {
    }

    @Override
    public void onDirectMessage (DirectMessage directMessage)
    {
    }
}
