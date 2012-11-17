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

import twitter4j.IDs;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import java.util.Arrays;

/**
 * A data class representing array of numeric IDs.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
/*package*/ final class IDsJSONImpl extends TwitterResponseImpl implements IDs {

    private long[] ids;
    private long previousCursor = -1;
    private long nextCursor = -1;
    private static final long serialVersionUID = -6585026560164704953L;

    /*package*/ IDsJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        String json = res.asString();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(this, json);
        }
    }

    /*package*/ IDsJSONImpl(String json) throws TwitterException {
        init(json);
    }

    private void init(String jsonStr) throws TwitterException {
        JSONArray idList;
        try {
            if (jsonStr.startsWith("{")) {
                JSONObject json = new JSONObject(jsonStr);
                idList = json.getJSONArray("ids");
                ids = new long[idList.length()];
                for (int i = 0; i < idList.length(); i++) {
                    try {
                        ids[i] = Long.parseLong(idList.getString(i));
                    } catch (NumberFormatException nfe) {
                        throw new TwitterException("Twitter API returned malformed response: " + json, nfe);
                    }
                }
                previousCursor = z_T4JInternalParseUtil.getLong("previous_cursor", json);
                nextCursor = z_T4JInternalParseUtil.getLong("next_cursor", json);
            } else {
                idList = new JSONArray(jsonStr);
                ids = new long[idList.length()];
                for (int i = 0; i < idList.length(); i++) {
                    try {
                        ids[i] = Long.parseLong(idList.getString(i));
                    } catch (NumberFormatException nfe) {
                        throw new TwitterException("Twitter API returned malformed response: " + idList, nfe);
                    }
                }
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long[] getIDs() {
        return ids;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPrevious() {
        return 0 != previousCursor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getPreviousCursor() {
        return previousCursor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        return 0 != nextCursor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getNextCursor() {
        return nextCursor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IDs)) return false;

        IDs iDs = (IDs) o;

        if (!Arrays.equals(ids, iDs.getIDs())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return ids != null ? Arrays.hashCode(ids) : 0;
    }

    @Override
    public String toString() {
        return "IDsJSONImpl{" +
                "ids=" + Arrays.toString(ids) +
                ", previousCursor=" + previousCursor +
                ", nextCursor=" + nextCursor +
                '}';
    }
}