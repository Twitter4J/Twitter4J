/*
Copyright (c) 2007-2010, Yusuke Yamamoto
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

import twitter4j.internal.http.HttpParameter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.2
 */
public final class FilterQuery implements java.io.Serializable {
    private int count;
    private int[] follow;
    private String[] track;
    private double[][] locations;
    private boolean includeEntities;

    /**
     * Creates a new FilterQuery
     */
    public FilterQuery(){
        this.count = 0;
        this.follow = null;
        this.track = null;
        this.locations = null;
    }
    /**
     * Creates a new FilterQuery
     * @param follow Specifies the users, by ID, to receive public tweets from.
     */
    public FilterQuery(int[] follow){
        this();
        this.count = 0;
        this.follow = follow;
    }
    /**
     * Creates a new FilterQuery
     * @param count  Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @param follow Specifies the users, by ID, to receive public tweets from.
     */
    public FilterQuery(int count, int[] follow){
        this();
        this.count = count;
        this.follow = follow;
    }
    /**
     * Creates a new FilterQuery
     * @param count  Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @param follow Specifies the users, by ID, to receive public tweets from.
     * @param track  Specifies keywords to track.
     */
    public FilterQuery(int count, int[] follow, String[] track){
        this();
        this.count = count;
        this.follow = follow;
        this.track = track;
    }

    /**
     * Creates a new FilterQuery
     * @param count  Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @param follow Specifies the users, by ID, to receive public tweets from.
     * @param track  Specifies keywords to track.
     * @param locations Specifies the locations to track. 2D array
     */
    public FilterQuery(int count, int[] follow, String[] track, double[][] locations){
        this.count = count;
        this.follow = follow;
        this.track = track;
        this.locations = locations;
    }

    /**
     * Sets count
     * @param count  Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @return this instance
     */
    public FilterQuery count(int count){
        this.count = count;
        return this;
    }
    /**
     * Sets follow
     * @param follow Specifies the users, by ID, to receive public tweets from.
     * @return this instance
     */
    public FilterQuery follow(int[] follow){
        this.follow = follow;
        return this;
    }
    /**
     * Sets track
     * @param track  Specifies keywords to track.
     * @return this instance
     */
    public FilterQuery track(String[] track){
        this.track = track;
        return this;
    }
    /**
     * Sets locations
     * @param locations Specifies the locations to track. 2D array
     * @return this instance
     */
    public FilterQuery locations(double[][] locations){
        this.locations = locations;
        return this;
    }

    /**
     * Set whether to include extracted entities in the stream.
     * @param include True if entities should be included, else false.
     * @return this instance
     * @since Twitter4J 2.1.4
     */
    public FilterQuery setIncludeEntities(boolean include) {
        includeEntities = include;
        return this;
    }

    /*package*/ HttpParameter[] asHttpParameterArray(){
        ArrayList<HttpParameter> params = new ArrayList<HttpParameter>();

        params.add(new HttpParameter("count", count));
        if (null != follow && follow.length > 0) {
            params.add(new HttpParameter("follow"
                    , toFollowString(follow)));
        }
        if (null != track && track.length > 0) {
            params.add(new HttpParameter("track"
                    , toTrackString(track)));
        }
        if (null != locations && locations.length > 0) {
            params.add(new HttpParameter("locations"
                    , toLocationsString(locations)));
        }
        if (includeEntities) {
            params.add(new HttpParameter("include_entities", true));
        }

        HttpParameter[] paramArray = new HttpParameter[params.size()];
        return params.toArray(paramArray);
    }

    private String toLocationsString(final double[][] keywords) {
        final StringBuffer buf = new StringBuffer(20 * keywords.length * 2);
        for (int c = 0; c < keywords.length; c++) {
            if (0 != buf.length()) {
                buf.append(",");
            }
            buf.append(keywords[c][0]);
            buf.append(",");
            buf.append(keywords[c][1]);
        }
        return buf.toString();
    }
    private String toFollowString(int[] follows) {
        StringBuffer buf = new StringBuffer(11 * follows.length);
        for (int follow : follows) {
            if (0 != buf.length()) {
                buf.append(",");
            }
            buf.append(follow);
        }
        return buf.toString();
    }

    private String toTrackString(final String[] keywords) {
        final StringBuffer buf = new StringBuffer(20 * keywords.length * 4);
        for (String keyword : keywords) {
            if (0 != buf.length()) {
                buf.append(",");
            }
            buf.append(keyword);
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
                ", follow=" + follow +
                ", track=" + (track == null ? null : Arrays.asList(track)) +
                ", locations=" + (locations == null ? null : Arrays.asList(locations)) +
                '}';
    }
}
