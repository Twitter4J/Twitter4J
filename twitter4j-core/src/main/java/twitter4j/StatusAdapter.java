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
    public void onFavorite (int source, int target, long targetObject)
    {
    }

    @Override
    public void onFollow (int source, int target)
    {
    }

    @Override
    public void onUnfavorite (int source, int target, long targetObject)
    {
    }

    @Override
    public void onRetweet (int source, int target, long targetObject)
    {
    }

    @Override
    public void onUnfollow (int source, int target)
    {
    }

    @Override
    public void onUnretweet (int source, int target, long targetObject)
    {
    }
    
    @Override
    public void onDirectMessage (DirectMessage directMessage)
    {
    }
}
