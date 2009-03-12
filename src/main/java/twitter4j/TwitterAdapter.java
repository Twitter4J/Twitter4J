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
    public void gotPublicTimeline(List<Status> statuses){
    }
    public void gotFriendsTimeline(List<Status> statuses){
    }
    public void gotUserTimeline(List<Status> statuses){
    }
    public void gotShow(Status statuses){
    }
    public void updated(Status statuses){
    }
    public void gotReplies(List<Status> statuses){
    }
    public void destroyedStatus(Status destroyedStatus){
    }
    public void gotFriends(List<User> users){
    }
    public void gotFollowers(List<User> users){
    }
    public void gotFeatured(List<User> users){
    }
    public void gotUserDetail(UserWithStatus userWithStatus){
    }
    public void gotDirectMessages(List<DirectMessage> messages){
    }
    public void gotSentDirectMessages(List<DirectMessage> messages){
    }
    public void sentDirectMessage(DirectMessage message){
    }
    public void deletedDirectMessage(DirectMessage message){
    }
    public void created(User user){
    }
    public void destroyed(User user){
    }
    public void gotExists(boolean exists) {
    }
    public void updatedLocation(User user){
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
    public void followed(User user){
    }
    public void left(User user){
    }
    public void blocked(User user){
    }
    public void unblocked(User user){
    }
    public void tested(boolean test){
    }
    public void gotDowntimeSchedule(String schedule){
    }
    public void searched(QueryResult result){
    }
    /**
     * @param ex TwitterException
     * @param method int
     */
    public void onException(TwitterException ex,int method) {
    }
}
