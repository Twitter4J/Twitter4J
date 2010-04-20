/*
Copyright (c) 2007-2010, Yusuke Yamamoto
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
package twitter4j.examples;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import twitter4j.*;

/**
 * <p>
 * This is a code example of Twitter4J Streaming API - user stream.<br>
 * Usage: java twitter4j.examples.PrintUserStream [<i>TwitterScreenName</i>
 * <i>TwitterPassword</i>]<br>
 * </p>
 * 
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class PrintUserStream implements StatusListener
{
    /**
     * Main entry of this application.
     * 
     * @param args
     *            String[] TwitterID TwitterPassword
     */
    public static void main (String [] args) throws TwitterException
    {
        PrintUserStream printSampleStream = new PrintUserStream (args);
        printSampleStream.startConsuming ();
    }

    TwitterStream twitterStream;
    
    // used for getting status and user info, for favoriting, following and unfollowing events
    Twitter twitter;
    
    PrintUserStream (String [] args)
    {
        try
        {
            twitterStream = new TwitterStreamFactory (this).getInstance ();
        }
        catch (IllegalStateException is)
        {
            // screen name / password combination is not in twitter4j.properties
            if (args.length < 2)
            {
                System.out.println ("Usage: java twitter4j.examples.PrintUserStream [ScreenName Password]");
                System.exit (-1);
            }
            
            twitterStream = new TwitterStreamFactory ().getInstance (args[0], args[1]);
            twitter = new TwitterFactory().getInstance (args[0], args[1]);
        }
    }

    private void startConsuming () throws TwitterException
    {
        // user() method internally creates a thread which manipulates
        // TwitterStream and calls these adequate listener methods continuously.
        twitterStream.setStatusListener (this);
        twitterStream.user ();
    }

    private Map<Integer, SoftReference<User>> friends;
    
    @Override
    public void onFriendList (int [] friendIds)
    {
        System.out.println ("Received friends list - Following " + friendIds.length + " people");

        friends = new HashMap<Integer, SoftReference<User>> ();
        for (int id : friendIds)
            friends.put (id, null);
    }
    
    private User friend (int id)
    {
        User friend = null;
        
        SoftReference<User> ref = friends.get (id); 
        
        if (ref != null)
            friend = ref.get ();
        
        if (friend == null)
        {
            try
            {
                friend = twitter.showUser (id);
                friends.put (id, new SoftReference<User> (friend));
            }
            catch (TwitterException e)
            {
                e.printStackTrace();
            }
        }
        
        return friend;
    }
    
    public void onStatus (Status status)
    {
        int replyTo = status.getInReplyToUserId ();
        if (replyTo > 0 && !friends.containsKey (replyTo))
            System.out.print ("[Out of band] ");

        User user = status.getUser ();
        System.out.println (user.getName () + " [" + user.getScreenName () + "] : " + status.getText ());
    }


    public void onDeletionNotice (StatusDeletionNotice statusDeletionNotice)
    {
        User user = friend (statusDeletionNotice.getUserId ());
        System.out.println (user.getName () + " [" + user.getScreenName () + "] deleted the tweet " 
                + statusDeletionNotice.getStatusId ());
    }

    public void onTrackLimitationNotice (int numberOfLimitedStatuses)
    {
        System.out.println ("track limitation: " + numberOfLimitedStatuses);
    }

    public void onException (Exception ex)
    {
        ex.printStackTrace ();
    }

    @Override
    public void onFavorite (int source, int target, long targetObject)
    {
        try
        {
            Status rt = twitter.showStatus (targetObject);
            User user = friend (source);
            
            System.out.println (user.getName () + " [" + user.getScreenName () + "] favorited "
                    + rt.getUser ().getName () + "'s [" + rt.getUser ().getScreenName () + "] tweet: " + rt.getText ());
        }
        catch (TwitterException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onUnfavorite (int source, int target, long targetObject)
    {
        try
        {
            Status rt = twitter.showStatus (targetObject);
            User user = friend (source);
            
            System.out.println (user.getName () + " [" + user.getScreenName () + "] unfavorited "
                    + rt.getUser ().getName () + "'s [" + rt.getUser ().getScreenName () + "] tweet: " + rt.getText ());
        }
        catch (TwitterException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onFollow (int source, int target)
    {
        User user = friend (source);
        User friend = friend (target);
        
        System.out.println (user.getName () + " [" + user.getScreenName () + "] started following "
                + friend.getName () + " [" + friend.getScreenName () +"]");
    }
    
    @Override
    public void onUnfollow (int source, int target)
    {
        User user = friend (source);
        User friend = friend (target);
        
        System.out.println (user.getName () + " [" + user.getScreenName () + "] unfollowed "
                + friend.getName () + " [" + friend.getScreenName () +"]");
    }
    
    @Override
    public void onRetweet (int source, int target, long targetObject)
    {
    }
    
    @Override
    public void onUnretweet (int source, int target, long targetObject)
    {
    }
}
