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

package twitter4j.v1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.2
 */
public final class FilterQuery implements java.io.Serializable {
    private static final long serialVersionUID = -142808200594318258L;
    /**
     * count
     */
    public final int count;
    /**
     * follow
     */
    public final long[] follow;
    /**
     * track
     */
    public final String[] track;
    /**
     * locations
     */
    public final double[][] locations;
    /**
     * language
     */
    public final String[] language;
    /**
     * filter level
     */
    public final FilterLevel filterLevel;

    private FilterQuery(int count, long @Nullable [] follow, @Nullable String[] track,
                        double[][] locations, @Nullable String[] language, @Nullable FilterLevel filterLevel) {
        this.count = count;
        this.follow = follow;
        this.track = track;
        this.locations = locations;
        this.language = language;
        this.filterLevel = filterLevel;
    }


    /**
     * Creates a new FilterQuery with specified values
     *
     * @param count Indicates the number of previous statuses to stream before transitioning to the live stream.
     * @return new FilterQuery instance
     */
    public FilterQuery count(int count) {
        return new FilterQuery(count, this.follow, this.track, this.locations, this.language, this.filterLevel);
    }

    /**
     * Creates a new FilterQuery with specified values
     *
     * @param follow Specifies the users, by ID, to receive public tweets from.
     * @return FilterQuery
     */
    public static FilterQuery ofFollow(long... follow) {
        return new FilterQuery(0, follow, null, null, null, null);
    }

    /**
     * Creates a new FilterQuery with specified values
     *
     * @param follow Specifies the users, by ID, to receive public tweets from.
     * @return FilterQuery
     */
    public FilterQuery follow(long... follow) {
        return new FilterQuery(this.count, follow, this.track, this.locations, this.language, this.filterLevel);
    }

    /**
     * Creates a new FilterQuery
     *
     * @param track Specifies keywords to track.
     * @return new FilterQuery instance
     * @since Twitter4J 4.0.4
     */
    public static FilterQuery ofTrack(@NotNull String... track) {
        return new FilterQuery(0, null, track, null, null, null);
    }

    /**
     * Creates a new FilterQuery
     *
     * @param track Specifies keywords to track.
     * @return new FilterQuery instance
     * @since Twitter4J 4.0.4
     */
    public FilterQuery track(@NotNull String... track) {
        return new FilterQuery(this.count, this.follow, track, this.locations, this.language, this.filterLevel);
    }


    /**
     * Creates a new FilterQuery with specified values
     *
     * @param locations Specifies the locations to track. 2D array
     * @return new FilterQuery instance
     */
    @SuppressWarnings("unused")
    public static FilterQuery ofLocations(double[] @NotNull [] locations) {
        return new FilterQuery(0, null, null, locations, null, null);
    }

    /**
     * Creates a new FilterQuery with specified values
     *
     * @param locations Specifies the locations to track. 2D array
     * @return new FilterQuery instance
     */
    public FilterQuery locations(double[] @NotNull [] locations) {
        return new FilterQuery(this.count, this.follow, this.track, locations, this.language, this.filterLevel);
    }

    /**
     * Sets language
     *
     * @param language Specifies languages to track.
     * @return new FilterQuery instance
     */
    public FilterQuery language(@NotNull String... language) {
        return new FilterQuery(this.count, this.follow, this.track, this.locations, language, this.filterLevel);
    }

    /**
     * The filter level limits what tweets appear in the stream to those with
     * a minimum filter_level attribute value.
     *
     * @param filterLevel one of either none, low, or medium.
     * @return new FilterQuery instance
     */
    @SuppressWarnings("unused")
    public FilterQuery filterLevel(@NotNull FilterLevel filterLevel) {
        return new FilterQuery(this.count, this.follow, this.track, this.locations, this.language, filterLevel);
    }

    /**
     * filter level
     */
    public enum FilterLevel {
        /**
         * none
         */
        NONE,
        /**
         * low
         */
        LOW,
        /**
         * medium
         */
        MEDIUM
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilterQuery that = (FilterQuery) o;

        if (count != that.count) return false;
        if (!Arrays.equals(follow, that.follow)) return false;
        if (!Arrays.equals(track, that.track)) return false;
        if (!Arrays.equals(language, that.language)) return false;
        return Objects.equals(filterLevel, that.filterLevel);
    }

    @Override
    public int hashCode() {
        int result = count;
        result = 31 * result + (follow != null ? Arrays.hashCode(follow) : 0);
        result = 31 * result + (track != null ? Arrays.hashCode(track) : 0);
        result = 31 * result + (language != null ? Arrays.hashCode(language) : 0);
        result = 31 * result + (filterLevel != null ? filterLevel.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FilterQuery{" +
                "count=" + count +
                ", follow=" + Arrays.toString(follow) +
                ", track=" + (track == null ? null : Arrays.asList(track)) +
                ", locations=" + (locations == null ? null : Arrays.asList(locations)) +
                ", language=" + (language == null ? null : Arrays.asList(language)) +
                ", filter_level=" + filterLevel +
                '}';
    }
}
