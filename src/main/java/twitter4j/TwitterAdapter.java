/*
Copyright (c) 2007-2009, Yusuke Yamamoto
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
package twitter4j;

import java.util.List;

/**
 * A handy adapter of TwitterListener.
 * @see twitter4j.AsyncTwitter
 * @see twitter4j.TwitterListener
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class TwitterAdapter implements TwitterListener {
    public TwitterAdapter() {
    }
    /**
     * @since Twitter4J 2.0.10
     */
    public void gotHomeTimeline(List<Status> statuses){
    }
    public void gotPublicTimeline(List<Status> statuses){
    }
    public void gotFriendsTimeline(List<Status> statuses){
    }
    public void gotUserTimeline(List<Status> statuses){
    }
    /**
     * @deprecated use gotShowStatus instead
     */
    public void gotShow(Status statuses){
    }
    /**
     * @since Twitter4J 2.0.1
     */
    public void gotShowStatus(Status statuses){
    }
    /**
     * @deprecated use updatedStatus instead
     */
    public void updated(Status statuses){
    }
    public void updatedStatus(Status statuses){
    }
    /**
     * @deprecated use gotMentions instead
     */
    public void gotReplies(List<Status> statuses){
    }
    /**
     * @since Twitter4J 2.0.1
     */
    public void gotMentions(List<Status> statuses){
    }
    /**
     * @since Twitter4J 2.0.10
     */
    public void gotRetweetedByMe(List<Status> statuses) {
    }
    /**
     * @since Twitter4J 2.0.10
     */
    public void gotRetweetedToMe(List<Status> statuses) {
    }
    /**
     * @since Twitter4J 2.0.10
     */
    public void gotRetweetsOfMe(List<Status> statuses) {
    }
    public void destroyedStatus(Status destroyedStatus){
    }
    /**
     * @since Twitter4J 2.0.10
     */
    public void retweetedStatus(Status retweetedStatus){
    }

    public void gotFriends(List<User> users){
    }
    public void gotFollowers(List<User> users){
    }
    public void gotFeatured(List<User> users){
    }
    public void gotUserDetail(User user){
    }
    public void gotDirectMessages(List<DirectMessage> messages){
    }
    public void gotSentDirectMessages(List<DirectMessage> messages){
    }
    public void sentDirectMessage(DirectMessage message){
    }
    /**
     * @deprecated use destroyedDirectMessage instead
     */
    public void deletedDirectMessage(DirectMessage message){
    }
    /**
     * @since Twitter4J 2.0.1
     */
    public void destroyedDirectMessage(DirectMessage message){
    }
    public void gotFriendsIDs(IDs ids){
    }
    public void gotFollowersIDs(IDs ids){
    }
    /**
     * @deprecated use createdFriendship instead
     */
    public void created(User user){
    }
    /**
     * @since Twitter4J 2.0.1
     */
    public void createdFriendship(User user){
    }
    /**
     * @deprecated use destroyedFriendship instead
     */
    public void destroyed(User user){
    }
    /**
     * @since Twitter4J 2.0.1
     */
    public void destroyedFriendship(User user){
    }
    /**
     * @deprecated use gotExistsFriendship instead
     */
    public void gotExists(boolean exists) {
    }
    /**
     * @since Twitter4J 2.0.1
     */
    public void gotExistsFriendship(boolean exists) {
    }
    /**
     * @deprecated Use updatedProfile instead
     */
    public void updatedLocation(User user){
    }
    /**
     * @since Twitter4J 2.0.2
     */
    public void updatedProfile(User user){
    }
    public void updatedProfileColors(User user){
    }
    public void gotRateLimitStatus(RateLimitStatus status){
    }
    public void updatedDeliverlyDevice(User user){
    }
    public void gotFavorites(List<Status> statuses){
    }
    public void createdFavorite(Status status){
    }
    public void destroyedFavorite(Status status){
    }
    /**
     * @deprecated use enabledNotification instead
     */
    public void followed(User user){
    }
    /**
     * @since Twitter4J 2.0.1
     */
    public void enabledNotification(User user){
    }
    /**
     * @deprecated use disabledNotification instead
     */
    public void left(User user){
    }
    /**
     * @since Twitter4J 2.0.1
     */
    public void disabledNotification(User user){
    }
    /**
     * @deprecated use createdBlock instead
     */
    public void blocked(User user){
    }
    /**
     * @since Twitter4J 2.0.1
     */
    public void createdBlock(User user){
    }
    /**
     * @deprecated use destroyedBlock instead
     */
    public void unblocked(User user){
    }
    /**
     * @since Twitter4J 2.0.1
     */
    public void destroyedBlock(User user){
    }
    /**
     * @since Twitter4J 2.0.4
     */
    public void gotExistsBlock(boolean blockExists){
    }

    /**
     * @since Twitter4J 2.0.4
     */
    public void gotBlockingUsers(List<User> blockingUsers){
    }

    /**
     * @since Twitter4J 2.0.4
     */
    public void gotBlockingUsersIDs(IDs blockingUsersIDs){
    }

    public void tested(boolean test){
    }
    /**
     * @deprecated not supported by Twitter API anymore
     */
    public void gotDowntimeSchedule(String schedule){
    }
    public void searched(QueryResult result){
    }
    /**
     * @since Twitter4J 2.0.2
     */
    public void gotTrends(Trends trends) {
    }
    /**
     * @since Twitter4J 2.0.2
     */
    public void gotCurrentTrends(Trends trends) {
    }
    /**
     * @since Twitter4J 2.0.2
     */
    public void gotDailyTrends(List<Trends> trendsList) {
    }
    /**
     * @since Twitter4J 2.0.2
     */
    public void gotWeeklyTrends(List<Trends> trendsList) {
    }

    /**
     * @param ex TwitterException
     * @param method int
     */
    public void onException(TwitterException ex,int method) {
    }
}
