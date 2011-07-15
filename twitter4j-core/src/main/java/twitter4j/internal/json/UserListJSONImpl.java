/*
 * Copyright 2007 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package twitter4j.internal.json;

import twitter4j.PagableResponseList;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

import static twitter4j.internal.util.z_T4JInternalParseUtil.getBoolean;
import static twitter4j.internal.util.z_T4JInternalParseUtil.getInt;
import static twitter4j.internal.util.z_T4JInternalParseUtil.getRawString;

/**
 * A data class representing Basic list information element
 *
 * @author Dan Checkoway - dcheckoway at gmail.com
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
    private boolean following;
    private static final long serialVersionUID = -6345893237975349030L;

    /*package*/ UserListJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
        }
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.registerJSONObject(this, json);
        }
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
        mode = "public".equals(getRawString("mode", json));
        following = getBoolean("following", json);

        try {
            if (!json.isNull("user")) {
                user = new UserJSONImpl(json.getJSONObject("user"));
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), jsone);
        }
    }

    public int compareTo(UserList that) {
        return this.id - that.getId();
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
    public boolean isFollowing() {
        return following;
    }

    /**
     * {@inheritDoc}
     */
    public User getUser() {
        return user;
    }

    /*package*/
    static PagableResponseList<UserList> createPagableUserListList(HttpResponse res, Configuration conf) throws TwitterException {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            JSONObject json = res.asJSONObject();
            JSONArray list = json.getJSONArray("lists");
            int size = list.length();
            PagableResponseList<UserList> users =
                    new PagableResponseListImpl<UserList>(size, json, res);
            for (int i = 0; i < size; i++) {
                JSONObject userListJson = list.getJSONObject(i);
                UserList userList = new UserListJSONImpl(userListJson);
                users.add(userList);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(userList, userListJson);
                }
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(users, json);
            }
            return users;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }

    /*package*/
    static ResponseList<UserList> createUserListList(HttpResponse res, Configuration conf) throws TwitterException {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            JSONArray list = res.asJSONArray();
            int size = list.length();
            ResponseList<UserList> users =
                    new ResponseListImpl<UserList>(size, res);
            for (int i = 0; i < size; i++) {
                JSONObject userListJson = list.getJSONObject(i);
                UserList userList = new UserListJSONImpl(userListJson);
                users.add(userList);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(userList, userListJson);
                }
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(users, list);
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
                ", mode=" + mode +
                ", user=" + user +
                ", following=" + following +
                '}';
    }
}
