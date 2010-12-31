/*
Copyright (c) 2007-2011, Yusuke Yamamoto
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

import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import static twitter4j.internal.util.ParseUtil.getInt;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.9
 */
class FriendshipJSONImpl implements Friendship {
    private static final long serialVersionUID = -7592213959077711096L;
    private final int id;
    private final String name;
    private final String screenName;
    private boolean following = false;
    private boolean followedBy = false;

    /*package*/ FriendshipJSONImpl(HttpResponse res) throws TwitterException {
        this(res.asJSONObject());
        DataObjectFactoryUtil.clearThreadLocalMap();
        DataObjectFactoryUtil.registerJSONObject(this, res.asJSONObject());
    }

    /*package*/ FriendshipJSONImpl(JSONObject json) throws TwitterException {
        super();
        try {
            id = getInt("id", json);
            name = json.getString("name");
            screenName = json.getString("screen_name");
            JSONArray connections = json.getJSONArray("connections");
            for (int i = 0; i < connections.length(); i++) {
                String connection = connections.getString(i);
                if ("following".equals(connection)) {
                    following = true;
                } else if ("followed_by".equals(connection)) {
                    followedBy = true;
                }
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), jsone);
        }
    }

    /*package*/ static ResponseList<Friendship> createFriendshipList(HttpResponse res) throws TwitterException {
        try {
            DataObjectFactoryUtil.clearThreadLocalMap();
            JSONArray list = res.asJSONArray();
            int size = list.length();
            ResponseList<Friendship> friendshipList = new ResponseListImpl<Friendship>(size, res);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                Friendship friendship = new FriendshipJSONImpl(json);
                DataObjectFactoryUtil.registerJSONObject(friendship, json);
                friendshipList.add(friendship);
            }
            DataObjectFactoryUtil.registerJSONObject(friendshipList, list);
            return friendshipList;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public boolean isFollowing() {
        return following;
    }

    public boolean isFollowedBy() {
        return followedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FriendshipJSONImpl that = (FriendshipJSONImpl) o;

        if (followedBy != that.followedBy) return false;
        if (following != that.following) return false;
        if (id != that.id) return false;
        if (!name.equals(that.name)) return false;
        if (!screenName.equals(that.screenName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + screenName.hashCode();
        result = 31 * result + (following ? 1 : 0);
        result = 31 * result + (followedBy ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ConnectionsJSONImpl{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", screenName='" + screenName + '\'' +
                ", following=" + following +
                ", followedBy=" + followedBy +
                '}';
    }
}
