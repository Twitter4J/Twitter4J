Twitter Annotations

by Roy Reshef - royreshef @ gmail.com


UPDATE 2010-08-18, 15:30 EST

Unfortunately Twitter is really putting on hold annotations. I asked them 
(@twitterapi, @raffi, @noradio) yesterday and today multiple times to 
whitelist @twit4j for annotations (so that the unit test can  run smoothly).
I hoped they will realize that Twitter4J is used by  many Java developers and
it's not just "another application". I got no reaction, and it wasn't enabled. 
Eventually I logged into @twit4j account itself to send a request on behalf 
of that account to @twitterapi. And then I finally got the answer 
(http://twitter.com/twitterapi/status/21513861799): 

@twit4j Sorry, we're not enabling annotations for additional accounts 
at this time. We'll let folks know when we're ready. ^TS 

ORIGINAL README DOCUMENT, 2010-08-18 03:00 EST

Added support for Twitter Annotations:

1. Files created
twitter4j-core/src/main/java/twitter4j/Annotation.java
twitter4j-core/src/main/java/twitter4j/Annotations.java

2. Files changed
twitter4j-core/src/main/java/twitter4j/Status.java
twitter4j-core/src/main/java/twitter4j/StatusJSONImpl.java
twitter4j-core/src/main/java/twitter4j/StatusUpdate.java
twitter4j-core/src/main/java/twitter4j/Tweet.java
twitter4j-core/src/main/java/twitter4j/TweetJSONImpl.java
twitter4j-core/src/test/java/twitter4j/TwitterTestUnit.java

3. Twitter Annotations documentation:
http://dev.twitter.com/pages/annotations_overview

3. Rollout of Annotations feature (by Twitter)
The rollout of the Annotations feature is currently on-hold by Twitter, at least until after the oAuthcalypse (Aug 31). As a result, only developers who have been *whitelisted by Twitter* can call statuses/update for a status with annotations and see annotations of annotated statuses. I have asked @noradio and @raffi yesterday to whitelist the Twitter4J test account, @twit4j. So far I saw no reaction, so I conducted the tests with my test account (@_iskrica), which is whitelisted.

4. Twitter4J Implementation:
- Update status with annotations is only implemented using Twitter.updateStatus(StatusUpdate). See example how to do it in TwitterTestUnit.testAnnotations(). Class StatusUpdate has been modified accordingly.
- The Annotations feature is currently implemented (by Twitter) for the Streaming and REST API. I have modified Status and StatusJSONImpl to support annotations for any status returned by those APIs.
- The Annotations feature is not implemented yet (by Twitter) for the Search API. However, as these were small modifications, for completeness purposes (and for future use) I have modified Tweet and TweetJSONImpl to support annotations. I have tested it, and saw indeed that no annotations are returned by the Search API - even for annotated tweets.

5. Testing
I have tested my code, and verified that I can send a status update with annotations using Twitter4J and get an annotated status by the Streaming/REST API. Example for such a status: http://twitter.com/_iskrica/status/21467541388

Using Twitter web, you cannot see the annotations. A neat website which allows you to see annotations is http://www.tweetplugs.com (hopefully the Twitter guys will whitelist @twit4j soon).
In order to view it, do the following:
- make @twit4j follow @_iskrica
- go to http://www.tweetplugs.com, which will use oAuth to log into Twitter (as @twit4j)
- select "Deck"
- you are supposed to see there all statuses of the people @twit4j is following. Look for that status of @_iskrica, and you'll see the annotations.


