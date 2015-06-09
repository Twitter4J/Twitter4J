/*
 * Copyright 2007 Yusuke Yamamoto
 * Copyright (C) 2012 Twitter, Inc.
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

import twitter4j.auth.Authorization;
import twitter4j.auth.AuthorizationFactory;
import twitter4j.conf.Configuration;

import java.io.Serializable;
import java.util.Arrays;

import static twitter4j.ParseUtil.*;

/**
 * @author Yusuke Yamamoto - yusuke at twitter.com
 * @since Twitter4J 2.2.6
 */
public class StreamController {
    private String controlURI = null;
    private final HttpClient http;
    private final Authorization AUTH;
    private static final Logger logger = Logger.getLogger(StreamController.class);

    /*package*/ StreamController(HttpClient http, Authorization auth) {
        this.http = http;
        AUTH = auth;
    }

    /*package*/ StreamController(Configuration conf) {
        this.http = HttpClientFactory.getInstance(conf.getHttpClientConfiguration());
        AUTH = AuthorizationFactory.getInstance(conf);
    }

    void setControlURI(String controlURI) {
        this.controlURI = (controlURI!=null) ?controlURI.replace("/1.1//1.1/", "/1.1/") : null;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    private final Object lock = new Object();

    String getControlURI() {
        return controlURI;
    }

    void ensureControlURISet() throws TwitterException {
        synchronized (lock) {
            try {
		int waits = 0;
                while (controlURI == null) {
                    lock.wait(1000);
                    waits++;
                    
                    if (waits > 29) throw new TwitterException("timed out for control uri to be ready");
                }           
            } catch (InterruptedException e) {
            }
        }
    }

    public ControlStreamInfo getInfo() throws TwitterException {
        ensureControlURISet();
        HttpResponse res = http.get(controlURI + "/info.json", null, AUTH, null);
        return new ControlStreamInfo(this, res.asJSONObject());
    }

    public String addUsers(long... userIds) throws TwitterException {
        ensureControlURISet();
        HttpParameter param = new HttpParameter("user_id",
                StringUtil.join(userIds));
        HttpResponse res = http.post(controlURI + "/add_user.json",
                new HttpParameter[]{param}, AUTH, null);
        return res.asString();
    }

    public String removeUsers(long... userIds) throws TwitterException {
        ensureControlURISet();
        HttpParameter param = new HttpParameter("user_id",
                StringUtil.join(userIds));
        HttpResponse res = http.post(controlURI + "/remove_user.json",
                new HttpParameter[]{param}, AUTH, null);
        return res.asString();
    }


    public FriendsIDs getFriendsIDs(long userId, long cursor) throws TwitterException {
        ensureControlURISet();
        HttpResponse res = http.post(controlURI + "/friends/ids.json",
                new HttpParameter[]{new HttpParameter("user_id", userId),
                        new HttpParameter("cursor", cursor)}, AUTH, null
        );
        return new FriendsIDs(res);
    }

    public final class FriendsIDs implements CursorSupport, Serializable {
        private static final long serialVersionUID = -7393320878760329794L;
        private long[] ids;
        private long previousCursor = -1;
        private long nextCursor = -1;
        private User user;

        /*package*/ FriendsIDs(HttpResponse res) throws TwitterException {
            init(res.asJSONObject());
        }

        private void init(JSONObject json) throws TwitterException {
            try {
                JSONObject follow = json.getJSONObject("follow");
                JSONArray idList = follow.getJSONArray("friends");
                ids = new long[idList.length()];
                for (int i = 0; i < idList.length(); i++) {
                    try {
                        ids[i] = Long.parseLong(idList.getString(i));
                    } catch (NumberFormatException nfe) {
                        throw new TwitterException("Twitter API returned malformed response: " + json, nfe);
                    }
                }
                user = new User(follow.getJSONObject("user"));
                previousCursor = ParseUtil.getLong("previous_cursor", json);
                nextCursor = ParseUtil.getLong("next_cursor", json);
            } catch (JSONException jsone) {
                throw new TwitterException(jsone);
            }
        }

        @Override
        public boolean hasPrevious() {
            return 0 != previousCursor;
        }

        @Override
        public long getPreviousCursor() {
            return previousCursor;
        }

        @Override
        public boolean hasNext() {
            return 0 != nextCursor;
        }

        @Override
        public long getNextCursor() {
            return nextCursor;
        }

        public User getUser() {
            return user;
        }

        public long[] getIds() {
            return ids;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FriendsIDs that = (FriendsIDs) o;

            if (nextCursor != that.nextCursor) return false;
            if (previousCursor != that.previousCursor) return false;
            if (!Arrays.equals(ids, that.ids)) return false;
            if (user != null ? !user.equals(that.user) : that.user != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = ids != null ? Arrays.hashCode(ids) : 0;
            result = 31 * result + (int) (previousCursor ^ (previousCursor >>> 32));
            result = 31 * result + (int) (nextCursor ^ (nextCursor >>> 32));
            result = 31 * result + (user != null ? user.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "FriendsIDs{" +
                    "ids=" + Arrays.toString(ids) +
                    ", previousCursor=" + previousCursor +
                    ", nextCursor=" + nextCursor +
                    ", user=" + user +
                    '}';
        }
    }

    /*package*/ User createUser(JSONObject json) {
        return new User(json);
    }

    public final class User implements Serializable {
        private static final long serialVersionUID = -8741743249755418730L;
        private final long id;
        private final String name;
        private final boolean dm;

        /*package*/ User(JSONObject json) {
            id = getLong("id", json);
            name = getRawString("name", json);
            dm = getBoolean("dm", json);
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public boolean isDMAccessible() {
            return dm;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            User user = (User) o;

            if (dm != user.dm) return false;
            if (id != user.id) return false;
            if (name != null ? !name.equals(user.name) : user.name != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = (int) (id ^ (id >>> 32));
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + (dm ? 1 : 0);
            return result;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", dm=" + dm +
                    '}';
        }
    }
}
