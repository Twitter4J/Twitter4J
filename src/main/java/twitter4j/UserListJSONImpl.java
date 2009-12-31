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

import twitter4j.http.HttpResponse;
import twitter4j.org.json.JSONArray;
import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;
import static twitter4j.ParseUtil.*;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * A data class representing Basic list information element
 * @author Dan Checkoway - dcheckoway at gmail.com
 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-GET-list-id">REST API Documentation - Basic list information element</a>
 */
/*package*/ class UserListJSONImpl extends TwitterResponseImpl implements UserList, java.io.Serializable {

    private int id;
    private String name;
    private String fullName;
    private String slug;
    private String description;
    private int subscriberCount;
    private int memberCount;
    private String uri;
    private boolean mode;
    private User user;
    private static final long serialVersionUID = -6345893237975349030L;

    /*package*/ UserListJSONImpl(HttpResponse res) throws TwitterException {
        super(res);
        init(res.asJSONObject());
    }

    /*package*/ UserListJSONImpl(JSONObject json) throws TwitterException {
        super();
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
            id = getInt("id", json);
            name = getRawString("name", json);
            fullName = getRawString("full_name", json);
            slug = getRawString("slug", json);
            description = getRawString("description", json);
            subscriberCount = getInt("subscriber_count", json);
            memberCount = getInt("member_count", json);
            uri = getRawString("uri", json);
            mode = "pubic".equals(getRawString("mode", json));
        try {
            if (!json.isNull("user")) {
                user = new UserJSONImpl(json.getJSONObject("user"));
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), jsone);
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * {@inheritDoc}
     */
    public String getSlug() {
        return slug;
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    public int getSubscriberCount() {
        return subscriberCount;
    }

    /**
     * {@inheritDoc}
     */
    public int getMemberCount() {
        return memberCount;
    }

    /**
     * {@inheritDoc}
     */
    public URI getURI() {
        try {
            return new URI(uri);
        } catch (URISyntaxException ex) {
            return null;
        }
    }
        
    /**
     * {@inheritDoc}
     */
    public boolean isPublic() {
        return mode;
    }
        
    /**
     * {@inheritDoc}
     */
    public User getUser() {
        return user;
    }

    /*package*/ static PagableResponseList<UserList> createUserListList(HttpResponse res) throws TwitterException {
        try {
            JSONObject json = res.asJSONObject();
            JSONArray list = json.getJSONArray("lists");
            int size = list.length();
            PagableResponseList<UserList> users =
                    new PagableResponseList<UserList>(size, json, res);
            for (int i = 0; i < size; i++) {
                users.add(new UserListJSONImpl(list.getJSONObject(i)));
            }
            return users;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return obj instanceof UserList && ((UserList) obj).getId() == this.id;
    }

    @Override
    public String toString() {
        return "UserListJSONImpl{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", slug='" + slug + '\'' +
                ", description='" + description + '\'' +
                ", subscriberCount=" + subscriberCount +
                ", memberCount=" + memberCount +
                ", uri='" + uri + '\'' +
                ", mode='" + mode + '\'' +
                ", user=" + user +
                '}';
    }
}
