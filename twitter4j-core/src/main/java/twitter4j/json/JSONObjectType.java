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

package twitter4j.json;

import twitter4j.internal.logging.Logger;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

/**
 * @author Dan Checkoway - dcheckoway at gmail.com
 * @since Twitter4J 2.1.9
 */
public final class JSONObjectType implements java.io.Serializable {
    private static final Logger logger = Logger.getLogger(JSONObjectType.class);

    public static final JSONObjectType SENDER = new JSONObjectType("SENDER");
    public static final JSONObjectType STATUS = new JSONObjectType("STATUS");
    public static final JSONObjectType DIRECT_MESSAGE = new JSONObjectType("DIRECT_MESSAGE");
    public static final JSONObjectType DELETE = new JSONObjectType("DELETE");
    public static final JSONObjectType LIMIT = new JSONObjectType("LIMIT");
    public static final JSONObjectType SCRUB_GEO = new JSONObjectType("SCRUB_GEO");
    public static final JSONObjectType FRIENDS = new JSONObjectType("FRIENDS");
    public static final JSONObjectType FAVORITE = new JSONObjectType("FAVORITE");
    public static final JSONObjectType UNFAVORITE = new JSONObjectType("UNFAVORITE");
    public static final JSONObjectType RETWEET = new JSONObjectType("RETWEET");
    public static final JSONObjectType FOLLOW = new JSONObjectType("FOLLOW");
    public static final JSONObjectType UNFOLLOW = new JSONObjectType("UNFOLLOW");
    public static final JSONObjectType USER_LIST_MEMBER_ADDED = new JSONObjectType("USER_LIST_MEMBER_ADDED");
    public static final JSONObjectType USER_LIST_MEMBER_DELETED = new JSONObjectType("USER_LIST_MEMBER_DELETED");
    public static final JSONObjectType USER_LIST_SUBSCRIBED = new JSONObjectType("USER_LIST_SUBSCRIBED");
    public static final JSONObjectType USER_LIST_UNSUBSCRIBED = new JSONObjectType("USER_LIST_UNSUBSCRIBED");
    public static final JSONObjectType USER_LIST_CREATED = new JSONObjectType("USER_LIST_CREATED");
    public static final JSONObjectType USER_LIST_UPDATED = new JSONObjectType("USER_LIST_UPDATED");
    public static final JSONObjectType USER_LIST_DESTROYED = new JSONObjectType("USER_LIST_DESTROYED");
    public static final JSONObjectType USER_UPDATE = new JSONObjectType("USER_UPDATE");
    public static final JSONObjectType BLOCK = new JSONObjectType("BLOCK");
    public static final JSONObjectType UNBLOCK = new JSONObjectType("UNBLOCK");
    private static final long serialVersionUID = -4487565183481849892L;

    private final String name;

    private JSONObjectType() {
        throw new AssertionError();
    }

    private JSONObjectType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Determine the respective object type for a given JSONObject.  This
     * method inspects the object to figure out what type of object it
     * represents.  This is useful when processing JSON events of mixed type
     * from a stream, in which case you may need to know what type of object
     * to construct, or how to handle the event properly.
     *
     * @param json the JSONObject whose type should be determined
     * @return the determined JSONObjectType, or null if not recognized
     */
    public static JSONObjectType determine(JSONObject json) {
        // This code originally lived in AbstractStreamImplementation.
        // I've moved it in here to expose it as a public encapsulation of
        // the object type determination logic.
        if (!json.isNull("sender")) {
            return SENDER;
        } else if (!json.isNull("text")) {
            return STATUS;
        } else if (!json.isNull("direct_message")) {
            return DIRECT_MESSAGE;
        } else if (!json.isNull("delete")) {
            return DELETE;
        } else if (!json.isNull("limit")) {
            return LIMIT;
        } else if (!json.isNull("scrub_geo")) {
            return SCRUB_GEO;
        } else if (!json.isNull("friends")) {
            return FRIENDS;
        } else if (!json.isNull("event")) {
            String event;
            try {
                event = json.getString("event");
                if ("favorite".equals(event)) {
                    return FAVORITE;
                } else if ("unfavorite".equals(event)) {
                    return UNFAVORITE;
                } else if ("retweet".equals(event)) {
                    // note: retweet events also show up as statuses
                    return RETWEET;
                } else if ("follow".equals(event)) {
                    return FOLLOW;
                } else if ("unfollow".equals(event)) {
                    return UNFOLLOW;
                } else if (event.startsWith("list")) {
                    if ("list_member_added".equals(event)) {
                        return USER_LIST_MEMBER_ADDED;
                    } else if ("list_member_removed".equals(event)) {
                        return USER_LIST_MEMBER_DELETED;
                    } else if ("list_user_subscribed".equals(event)) {
                        return USER_LIST_SUBSCRIBED;
                    } else if ("list_user_unsubscribed".equals(event)) {
                        return USER_LIST_UNSUBSCRIBED;
                    } else if ("list_created".equals(event)) {
                        return USER_LIST_CREATED;
                    } else if ("list_updated".equals(event)) {
                        return USER_LIST_UPDATED;
                    } else if ("list_destroyed".equals(event)) {
                        return USER_LIST_DESTROYED;
                    }
                } else if ("user_update".equals(event)) {
                    return USER_UPDATE;
                } else if ("block".equals(event)) {
                    return BLOCK;
                } else if ("unblock".equals(event)) {
                    return UNBLOCK;
                }
            } catch (JSONException jsone) {
                try {
                    logger.warn("Failed to get event element: ", json.toString(2));
                } catch (JSONException ignore) {
                }
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JSONObjectType that = (JSONObjectType) o;

        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return name;
    }
}