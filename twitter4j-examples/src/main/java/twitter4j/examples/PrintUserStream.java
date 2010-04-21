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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Timer;

import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.PropertyConfiguration;
import twitter4j.http.AccessToken;

/**
 * <p>
 * This is a code example of Twitter4J Streaming API - user stream.<br>
 * Usage: java twitter4j.examples.PrintUserStream. Needs a valid twitter4j.properties file with Basic Auth _and_ OAuth properties set<br>
 * </p>
 * 
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @author Remy Rakic - remy dot rakic at gmail.com
 */
public final class PrintUserStream implements StatusListener
{
    public static void main (String [] args) throws TwitterException
    {
        PrintUserStream printSampleStream = new PrintUserStream (args);
        printSampleStream.startConsuming ();
    }

    private TwitterStream twitterStream;
    
    // used for getting status and user info, for favoriting, following and unfollowing events
    private Twitter twitter;
    private User currentUser;
    
    public PrintUserStream (String [] args)
    {
        Configuration conf = new PropertyConfiguration (getClass ().getResourceAsStream ("twitter4j.properties"));
        
        twitterStream = new TwitterStreamFactory ().getInstance (conf.getUser (), conf.getPassword ());
        
        twitter = new TwitterFactory().getOAuthAuthorizedInstance (conf.getOAuthConsumerKey (), conf.getOAuthConsumerSecret (),
                  new AccessToken (conf.getOAuthAccessToken (), conf.getOAuthAccessTokenSecret ()));
        
        try
        {
            currentUser = twitter.verifyCredentials ();
        }
        catch (TwitterException e)
        {
            System.out.println ("Unexpected exception caught while trying to retrieve the current user: " + e);
            e.printStackTrace();
            System.exit (-1);
        }
        
        Timer t = new Timer (5 * 60 * 1000, new ActionListener ()
        {
            @Override
            public void actionPerformed (ActionEvent e)
            {
                System.out.println ("");
            }
        });
        
        t.start ();
    }

    private void startConsuming () throws TwitterException
    {
        // the user() method internally creates a thread which manipulates
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
        
        friends.put (currentUser.getId (), new SoftReference<User> (currentUser));
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
        
        if (friend == null)
            return new NullUser();
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

    @Override
    public void onDirectMessage (DirectMessage dm)
    {
        System.out.println ("DM from " + dm.getSenderScreenName () + " to " + dm.getRecipientScreenName () + ": " + dm.getText ());
    }

    public void onDeletionNotice (StatusDeletionNotice notice)
    {
        if (notice == null)
        {
            System.out.println ("Deletion notice is null!");
            return;
        }
        
        User user = friend (notice.getUserId ());
        System.out.println (user.getName () + " [" + user.getScreenName () + "] deleted the tweet " 
                + notice.getStatusId ());
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
    
    // Preventing the null users in most situations, only used in when there's problems in the stream
    private static class NullUser implements User
    {
        @Override
        public Date getCreatedAt ()
        {
            return null;
        }

        @Override
        public String getDescription ()
        {
            return null;
        }

        @Override
        public int getFavouritesCount ()
        {
            return 0;
        }

        @Override
        public int getFollowersCount ()
        {
            return 0;
        }

        @Override
        public int getFriendsCount ()
        {
            return 0;
        }

        @Override
        public int getId ()
        {
            return 0;
        }

        @Override
        public String getLang ()
        {
            return null;
        }

        @Override
        public String getLocation ()
        {
            return null;
        }

        @Override
        public String getName ()
        {
            return null;
        }

        @Override
        public String getProfileBackgroundColor ()
        {
            return null;
        }

        @Override
        public String getProfileBackgroundImageUrl ()
        {
            return null;
        }

        @Override
        public URL getProfileImageURL ()
        {
            return null;
        }

        @Override
        public String getProfileLinkColor ()
        {
            return null;
        }

        @Override
        public String getProfileSidebarBorderColor ()
        {
            return null;
        }

        @Override
        public String getProfileSidebarFillColor ()
        {
            return null;
        }

        @Override
        public String getProfileTextColor ()
        {
            return null;
        }

        @Override
        public String getScreenName ()
        {
            return null;
        }

        @Override
        public Status getStatus ()
        {
            return null;
        }

        @Override
        public Date getStatusCreatedAt ()
        {
            return null;
        }

        @Override
        public long getStatusId ()
        {
            return 0;
        }

        @Override
        public String getStatusInReplyToScreenName ()
        {
            return null;
        }

        @Override
        public long getStatusInReplyToStatusId ()
        {
            return 0;
        }

        @Override
        public int getStatusInReplyToUserId ()
        {
            return 0;
        }

        @Override
        public String getStatusSource ()
        {
            return null;
        }

        @Override
        public String getStatusText ()
        {
            return null;
        }

        @Override
        public int getStatusesCount ()
        {
            return 0;
        }

        @Override
        public String getTimeZone ()
        {
            return null;
        }

        @Override
        public URL getURL ()
        {
            return null;
        }

        @Override
        public int getUtcOffset ()
        {
            return 0;
        }

        @Override
        public boolean isContributorsEnabled ()
        {
            return false;
        }

        @Override
        public boolean isGeoEnabled ()
        {
            return false;
        }

        @Override
        public boolean isProfileBackgroundTiled ()
        {
            return false;
        }

        @Override
        public boolean isProtected ()
        {
            return false;
        }

        @Override
        public boolean isStatusFavorited ()
        {
            return false;
        }

        @Override
        public boolean isStatusTruncated ()
        {
            return false;
        }

        @Override
        public boolean isVerified ()
        {
            return false;
        }

        @Override
        public int compareTo (User o)
        {
            return 0;
        }

        @Override
        public RateLimitStatus getRateLimitStatus ()
        {
            return null;
        }
        
    }
}
