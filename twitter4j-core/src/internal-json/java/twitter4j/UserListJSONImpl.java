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

package twitter4j;

import twitter4j.conf.Configuration;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

/**
 * A data class representing Basic list information element
 *
 * @author Dan Checkoway - dcheckoway at gmail.com
 */
/*package*/ class UserListJSONImpl extends TwitterResponseImpl implements UserList, java.io.Serializable {

    private static final long serialVersionUID = 449418980060197008L;
    private long id;
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
    private Date createdAt;

    /*package*/ UserListJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        if (conf.isJSONStoreEnabled()) {
            TwitterObjectFactory.clearThreadLocalMap();
        }
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            TwitterObjectFactory.registerJSONObject(this, json);
        }
    }

    /*package*/ UserListJSONImpl(JSONObject json) throws TwitterException {
        super();
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        id = ParseUtil.getLong("id", json);
        name = ParseUtil.getRawString("name", json);
        fullName = ParseUtil.getRawString("full_name", json);
        slug = ParseUtil.getRawString("slug", json);
        description = ParseUtil.getRawString("description", json);
        subscriberCount = ParseUtil.getInt("subscriber_count", json);
        memberCount = ParseUtil.getInt("member_count", json);
        uri = ParseUtil.getRawString("uri", json);
        mode = "public".equals(ParseUtil.getRawString("mode", json));
        following = ParseUtil.getBoolean("following", json);
        createdAt = ParseUtil.getDate("created_at", json);

        try {
            if (!json.isNull("user")) {
                user = new UserJSONImpl(json.getJSONObject("user"));
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), jsone);
        }
    }

    @Override
    public int compareTo(UserList that) {
        long delta = this.id - that.getId();
        if (delta < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        } else if (delta > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) delta;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public String getSlug() {
        return slug;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getSubscriberCount() {
        return subscriberCount;
    }

    @Override
    public int getMemberCount() {
        return memberCount;
    }

    @Override
    public URI getURI() {
        try {
            return new URI(uri);
        } catch (URISyntaxException ex) {
            return null;
        }
    }

    @Override
    public boolean isPublic() {
        return mode;
    }

    @Override
    public boolean isFollowing() {
        return following;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public User getUser() {
        return user;
    }

    /*package*/
    static PagableResponseList<UserList> createPagableUserListList(HttpResponse res, Configuration conf) throws TwitterException {
        try {
            if (conf.isJSONStoreEnabled()) {
                TwitterObjectFactory.clearThreadLocalMap();
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
                    TwitterObjectFactory.registerJSONObject(userList, userListJson);
                }
            }
            if (conf.isJSONStoreEnabled()) {
                TwitterObjectFactory.registerJSONObject(users, json);
            }
            return users;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    /*package*/
    static ResponseList<UserList> createUserListList(HttpResponse res, Configuration conf) throws TwitterException {
        try {
            if (conf.isJSONStoreEnabled()) {
                TwitterObjectFactory.clearThreadLocalMap();
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
                    TwitterObjectFactory.registerJSONObject(userList, userListJson);
                }
            }
            if (conf.isJSONStoreEnabled()) {
                TwitterObjectFactory.registerJSONObject(users, list);
            }
            return users;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    @Override
    public int hashCode() {
        return (int) id;
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
