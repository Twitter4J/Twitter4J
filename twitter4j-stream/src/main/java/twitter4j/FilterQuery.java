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

import twitter4j.internal.http.HttpParameter;
import twitter4j.internal.util.z_T4JInternalStringUtil;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.2
 */
public final class FilterQuery implements java.io.Serializable {
    private static final long serialVersionUID = 430966623248982833L;
    private int count;
    private long[] follow;
    private String[] track;
    private double[][] locations;

    /**
     * Creates a new FilterQuery
     */
    public FilterQuery() {
        this.count = 0;
        this.follow = null;
        this.track = null;
        this.locations = null;
    }

    /**
     * Creates a new FilterQuery
     *
     * @param follow Specifies the users, by ID, to receive public tweets from.
     */
    public FilterQuery(long[] follow) {
        this();
        this.count = 0;
        this.follow = follow;
    }

    /**
     * Creates a new FilterQuery
     *
     * @param count  Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @param follow Specifies the users, by ID, to receive public tweets from.
     */
    public FilterQuery(int count, long[] follow) {
        this();
        this.count = count;
        this.follow = follow;
    }

    /**
     * Creates a new FilterQuery
     *
     * @param count  Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @param follow Specifies the users, by ID, to receive public tweets from.
     * @param track  Specifies keywords to track.
     */
    public FilterQuery(int count, long[] follow, String[] track) {
        this();
        this.count = count;
        this.follow = follow;
        this.track = track;
    }

    /**
     * Creates a new FilterQuery
     *
     * @param count     Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @param follow    Specifies the users, by ID, to receive public tweets from.
     * @param track     Specifies keywords to track.
     * @param locations Specifies the locations to track. 2D array
     */
    public FilterQuery(int count, long[] follow, String[] track, double[][] locations) {
        this.count = count;
        this.follow = follow;
        this.track = track;
        this.locations = locations;
    }

    /**
     * Sets count
     *
     * @param count Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @return this instance
     */
    public FilterQuery count(int count) {
        this.count = count;
        return this;
    }

    /**
     * Sets follow
     *
     * @param follow Specifies the users, by ID, to receive public tweets from.
     * @return this instance
     */
    public FilterQuery follow(long[] follow) {
        this.follow = follow;
        return this;
    }

    /**
     * Sets track
     *
     * @param track Specifies keywords to track.
     * @return this instance
     */
    public FilterQuery track(String[] track) {
        this.track = track;
        return this;
    }

    /**
     * Sets locations
     *
     * @param locations Specifies the locations to track. 2D array
     * @return this instance
     */
    public FilterQuery locations(double[][] locations) {
        this.locations = locations;
        return this;
    }

    /*package*/ HttpParameter[] asHttpParameterArray(HttpParameter stallWarningsParam) {
        ArrayList<HttpParameter> params = new ArrayList<HttpParameter>();

        params.add(new HttpParameter("count", count));
        if (follow != null && follow.length > 0) {
            params.add(new HttpParameter("follow"
                    , z_T4JInternalStringUtil.join(follow)));
        }
        if (track != null && track.length > 0) {
            params.add(new HttpParameter("track"
                    , z_T4JInternalStringUtil.join(track)));
        }
        if (locations != null && locations.length > 0) {
            params.add(new HttpParameter("locations"
                    , toLocationsString(locations)));
        }
        params.add(stallWarningsParam);
        HttpParameter[] paramArray = new HttpParameter[params.size()];
        return params.toArray(paramArray);
    }

    private String toLocationsString(final double[][] keywords) {
        final StringBuilder buf = new StringBuilder(20 * keywords.length * 2);
        for (double[] keyword : keywords) {
            if (0 != buf.length()) {
                buf.append(",");
            }
            buf.append(keyword[0]);
            buf.append(",");
            buf.append(keyword[1]);
        }
        return buf.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilterQuery that = (FilterQuery) o;

        if (count != that.count) return false;
        if (!Arrays.equals(follow, that.follow)) return false;
        if (!Arrays.equals(track, that.track)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = count;
        result = 31 * result + (follow != null ? Arrays.hashCode(follow) : 0);
        result = 31 * result + (track != null ? Arrays.hashCode(track) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FilterQuery{" +
                "count=" + count +
                ", follow=" + Arrays.toString(follow) +
                ", track=" + (track == null ? null : Arrays.asList(track)) +
                ", locations=" + (locations == null ? null : Arrays.asList(locations)) +
                '}';
    }
}
