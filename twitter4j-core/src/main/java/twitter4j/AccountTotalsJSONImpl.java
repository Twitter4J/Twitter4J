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
import twitter4j.internal.org.json.JSONObject;

import static twitter4j.internal.util.ParseUtil.getInt;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.9
 */
class AccountTotalsJSONImpl extends TwitterResponseImpl implements AccountTotals, java.io.Serializable {

    private static final long serialVersionUID = -2291419345865627123L;
    private final int updates;
    private final int followers;
    private final int favorites;
    private final int friends;
    private AccountTotalsJSONImpl(HttpResponse res, JSONObject json) throws TwitterException {
        super(res);
        updates = getInt("updates", json);
        followers = getInt("followers", json);
        favorites = getInt("favorites", json);
        friends = getInt("friends", json);
    }

    /*package*/ AccountTotalsJSONImpl(HttpResponse res) throws TwitterException {
        this(res, res.asJSONObject());
        DataObjectFactoryUtil.clearThreadLocalMap();
        DataObjectFactoryUtil.registerJSONObject(this, res.asJSONObject());
    }

    /*package*/ AccountTotalsJSONImpl(JSONObject json) throws TwitterException {
        this(null, json);
    }

    public int getUpdates() {
        return updates;
    }

    public int getFollowers() {
        return followers;
    }

    public int getFavorites() {
        return favorites;
    }

    public int getFriends() {
        return friends;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountTotalsJSONImpl that = (AccountTotalsJSONImpl) o;

        if (favorites != that.favorites) return false;
        if (followers != that.followers) return false;
        if (friends != that.friends) return false;
        if (updates != that.updates) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = updates;
        result = 31 * result + followers;
        result = 31 * result + favorites;
        result = 31 * result + friends;
        return result;
    }

    @Override
    public String toString() {
        return "AccountTotalsJSONImpl{" +
                "updates=" + updates +
                ", followers=" + followers +
                ", favorites=" + favorites +
                ", friends=" + friends +
                '}';
    }
}
