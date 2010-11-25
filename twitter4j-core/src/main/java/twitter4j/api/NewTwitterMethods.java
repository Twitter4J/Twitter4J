package twitter4j.api;

import twitter4j.RelatedResults;
import twitter4j.TwitterException;

public interface NewTwitterMethods {

    /**
     * If available, returns an array of replies and mentions related to the specified Tweet. There is no guarantee there will be any replies or mentions in the response. This method is only available to users who have access to #newtwitter.
     * <br>This method calls http://api.twitter.com/1/related_results/show/:id
     * @param statusId the numerical ID of the status you're trying to retrieve
     * @return the related results of a given tweet
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://groups.google.com/group/twitter-api-announce/msg/34909da7c399169e">#newtwitter and the API - Twitter API Announcements | Google Group</a>
     */
    RelatedResults getRelatedResults(long statusId) throws TwitterException;
}
