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
import twitter4j.GeoLocation;

import java.util.Objects;

/**
 * A data class represents search query.<br>
 * An instance of this class is NOT thread safe.<br>
 * Instances can be shared across threads, but should not be mutated while a search is ongoing.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see <a href="https://dev.twitter.com/docs/api/1.1/get/search">GET search | Twitter Developers</a>
 * @see <a href="http://search.twitter.com/operators">Twitter API / Search Operators</a>
 */
@SuppressWarnings("unused")
public final class Query implements java.io.Serializable {
    private static final long serialVersionUID = 7196404519192910019L;
    /**
     * query
     */
    @NotNull
    public final String query;
    /**
     * lang
     */
    public final String lang;
    /**
     * locale
     */
    public final String locale;
    /**
     * maxId
     */
    public final long maxId;
    /**
     * count
     */
    public final int count;
    /**
     * since
     */
    @Nullable
    public final String since;
    /**
     * sinceId
     */
    public final long sinceId;
    /**
     * geocode
     */
    @Nullable
    public final String geocode;
    /**
     * until
     */
    @Nullable
    public final String until;
    /**
     * result type
     */
    @Nullable
    public final ResultType resultType;
    /**
     * next page query
     */
    @Nullable
    public final String nextPageQuery;

    /**
     * creates a new Query instance with the specified lang
     *
     * @param lang lang
     * @return new Query instance
     */
    @NotNull
    public Query withLang(@NotNull String lang) {
        return new Query(query, lang, locale, maxId, count, since, sinceId, geocode, until, resultType, nextPageQuery);
    }

    /**
     * creates a new Query instance with the specified locale
     *
     * @param locale locale
     * @return new Query instance
     */
    @NotNull
    public Query withLocale(@NotNull String locale) {
        return new Query(query, lang, locale, maxId, count, since, sinceId, geocode, until, resultType, nextPageQuery);
    }

    /**
     * creates a new Query instance with the specified maxId
     *
     * @param maxId maxId
     * @return new Query instance
     */

    @NotNull
    public Query withMaxId(long maxId) {
        return new Query(query, lang, locale, maxId, count, since, sinceId, geocode, until, resultType, nextPageQuery);
    }

    /**
     * creates a new Query instance with the specified count
     *
     * @param count count
     * @return new Query instance
     */

    @NotNull
    public Query withCount(int count) {
        return new Query(query, lang, locale, maxId, count, since, sinceId, geocode, until, resultType, nextPageQuery);
    }

    /**
     * creates a new Query instance with the specified since
     *
     * @param since since
     * @return new Query instance
     */

    @NotNull
    public Query withSince(@NotNull String since) {
        return new Query(query, lang, locale, maxId, count, since, sinceId, geocode, null, resultType, nextPageQuery);
    }

    /**
     * creates a new Query instance with the specified sinceId
     *
     * @param sinceId sinceId
     * @return new Query instance
     */

    @NotNull
    public Query withSinceId(long sinceId) {
        return new Query(query, lang, locale, maxId, count, since, sinceId, geocode, until, resultType, nextPageQuery);
    }

    /**
     * creates a new Query instance with the specified geolocation
     *
     * @param location geolocation
     * @param radius   radius
     * @param unit     unit
     * @return new Query instance
     */

    @NotNull
    public Query withGeoCode(@NotNull GeoLocation location, double radius
            , Unit unit) {
        String geocode = location.latitude + "," + location.longitude + "," + radius + unit.name();
        return new Query(query, lang, locale, maxId, count, since, sinceId, geocode, until, resultType, nextPageQuery);
    }

    /**
     * creates a new Query instance with the specified until
     *
     * @param until until
     * @return new Query instance
     */

    @NotNull
    public Query withUntil(@NotNull String until) {
        return new Query(query, lang, locale, maxId, count, since, sinceId, geocode, until, resultType, nextPageQuery);
    }

    /**
     * creates a new Query instance with the specified result type
     *
     * @param resultType result type
     * @return new Query instance
     */
    @NotNull
    public Query withResultType(@NotNull ResultType resultType) {
        return new Query(query, lang, locale, maxId, count, since, sinceId, geocode, until, resultType, nextPageQuery);
    }

    Query(@NotNull String query, String lang, String locale, long maxId, int count, @Nullable String since,
          long sinceId, @Nullable String geocode, @Nullable String until, @Nullable ResultType resultType,
          @Nullable String nextPageQuery) {
        this.query = query;
        this.lang = lang;
        this.locale = locale;
        this.maxId = maxId;
        this.count = count;
        this.since = since;
        this.sinceId = sinceId;
        this.geocode = geocode;
        this.until = until;
        this.resultType = resultType;
        this.nextPageQuery = nextPageQuery;
    }

    /**
     * @param query query
     * @return Query
     */
    @NotNull
    public static Query of(@NotNull String query) {
        return new Query(query, null, null, -1, -1, null, -1, null, null, null, null);
    }

    /**
     * miles
     */
    @SuppressWarnings("unused")
    public static final Unit MILES = Unit.mi;
    /**
     * kilo meters
     */
    public static final Unit KILOMETERS = Unit.km;

    /**
     * unit
     */
    public enum Unit {
        /**
         * miles
         */
        mi,
        /**
         * kilo meters
         */
        km
    }


    /**
     * mixed: Include both popular and real time results in the response.
     */
    @SuppressWarnings("unused")
    public final static ResultType MIXED = ResultType.mixed;
    /**
     * popular: return only the most popular results in the response.
     */
    public final static ResultType POPULAR = ResultType.popular;
    /**
     * recent: return only the most recent results in the response
     */
    @SuppressWarnings("unused")
    public final static ResultType RECENT = ResultType.recent;

    /**
     * result type
     */
    public enum ResultType {
        /**
         * popular
         */
        popular,
        /**
         * mixed
         */
        mixed,
        /**
         * recent
         */
        recent
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Query query1 = (Query) o;
        return maxId == query1.maxId && count == query1.count && sinceId == query1.sinceId && query.equals(query1.query) && Objects.equals(lang, query1.lang) && Objects.equals(locale, query1.locale) && Objects.equals(since, query1.since) && Objects.equals(geocode, query1.geocode) && Objects.equals(until, query1.until) && resultType == query1.resultType && Objects.equals(nextPageQuery, query1.nextPageQuery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, lang, locale, maxId, count, since, sinceId, geocode, until, resultType, nextPageQuery);
    }

    @Override
    public String toString() {
        return "Query{" +
                "query='" + query + '\'' +
                ", lang='" + lang + '\'' +
                ", locale='" + locale + '\'' +
                ", maxId=" + maxId +
                ", count=" + count +
                ", since='" + since + '\'' +
                ", sinceId=" + sinceId +
                ", geocode='" + geocode + '\'' +
                ", until='" + until + '\'' +
                ", resultType=" + resultType +
                ", nextPageQuery='" + nextPageQuery + '\'' +
                '}';
    }
}
