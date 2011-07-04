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

import twitter4j.HashtagEntity;
import twitter4j.TwitterException;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

/**
 * A data class representing one single Hashtag entity.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.9
 */
/*package*/ class HashtagEntityJSONImpl implements HashtagEntity {
    private static final long serialVersionUID = 4068992372784813200L;
    private int start = -1;
    private int end = -1;
    private String text;


    /* package */ HashtagEntityJSONImpl(JSONObject json) throws TwitterException {
        super();
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        try {
            JSONArray indicesArray = json.getJSONArray("indices");
            this.start = indicesArray.getInt(0);
            this.end = indicesArray.getInt(1);

            if (!json.isNull("text")) {
                this.text = json.getString("text");
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getText() {
        return text;
    }

    /**
     * {@inheritDoc}
     */
    public int getStart() {
        return start;
    }

    /**
     * {@inheritDoc}
     */
    public int getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HashtagEntityJSONImpl that = (HashtagEntityJSONImpl) o;

        if (end != that.end) return false;
        if (start != that.start) return false;
        if (text != null ? !text.equals(that.text) : that.text != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = start;
        result = 31 * result + end;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HashtagEntityJSONImpl{" +
                "start=" + start +
                ", end=" + end +
                ", text='" + text + '\'' +
                '}';
    }
}