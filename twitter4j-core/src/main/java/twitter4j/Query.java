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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A data class represents search query.<br>
 * An instance of this class is NOT thread safe.<br>
 * Instances can be shared across threads, but should not be mutated while a search is ongoing.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see <a href="https://dev.twitter.com/docs/api/1.1/get/search">GET search | Twitter Developers</a>
 * @see <a href="http://search.twitter.com/operators">Twitter API / Search Operators</a>
 */
public final class Query implements java.io.Serializable {
    private static final long serialVersionUID = 7196404519192910019L;
    @NotNull
    final String query;
    String lang;
    String locale;
    long maxId;
    int count;
    @Nullable
    String since;
    long sinceId;
    @Nullable
    String geocode;
    @Nullable
    String until;
    @Nullable
    ResultType resultType;
    String nextPageQuery;

    Query(@NotNull String query, String lang, String locale, long maxId, int count, @Nullable String since,
          long sinceId, @Nullable String geocode, @Nullable String until, @Nullable ResultType resultType, String nextPageQuery) {
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


    public static QueryBuilder newBuilder(@NotNull String query) {
        return new QueryBuilder(query);
    }

    /**
     * Equivalent to calling newBuilder(query).build();
     * @param query query
     * @return Query
     */
    public static Query getInstance(String query) {
        return new QueryBuilder(query).build();
    }

    /* package */
    static Query createWithNextPageQuery(@NotNull String nextPageQuery) {

        String nextPageParameters = nextPageQuery.substring(1);

        Map<String, String> params = new LinkedHashMap<>();
        for (HttpParameter param : HttpParameter.decodeParameters(nextPageParameters)) {
            // Yes, we'll overwrite duplicate parameters, but we should not
            // get duplicate parameters from this endpoint.
            params.put(param.getName(), param.getValue());
        }

        QueryBuilder query = new QueryBuilder(params.getOrDefault("q", ""));
        if (params.containsKey("lang"))
            query.lang(params.get("lang"));
        if (params.containsKey("locale"))
            query.locale(params.get("locale"));
        if (params.containsKey("max_id"))
            query.maxId(Long.parseLong(params.get("max_id")));
        if (params.containsKey("count"))
            query.count(Integer.parseInt(params.get("count")));
        if (params.containsKey("geocode")) {
            String[] parts = params.get("geocode").split(",");
            double latitude = Double.parseDouble(parts[0]);
            double longitude = Double.parseDouble(parts[1]);

            double radius = 0.0;
            Query.Unit unit = null;
            String radiusstr = parts[2];
            for (Query.Unit value : Query.Unit.values())
                if (radiusstr.endsWith(value.name())) {
                    radius = Double.parseDouble(radiusstr.substring(0, radiusstr.length() - 2));
                    unit = value;
                    break;
                }
            if (unit == null)
                throw new IllegalArgumentException("unrecognized geocode radius: " + radiusstr);

            query.geoCode(new GeoLocation(latitude, longitude), radius, unit);
        }
        if (params.containsKey("result_type"))
            query.resultType(Query.ResultType.valueOf(params.get("result_type")));

        query.nextPageQuery = nextPageQuery;
        // We don't pull out since, until -- they get pushed into the query
        return query.build();
    }


    @SuppressWarnings("unused")
    public static final Unit MILES = Unit.mi;
    public static final Unit KILOMETERS = Unit.km;

    public enum Unit {
        mi, km
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

    public enum ResultType {
        popular, mixed, recent
    }


    private static final HttpParameter WITH_TWITTER_USER_ID = new HttpParameter("with_twitter_user_id", "true");

    /*package*/ HttpParameter[] asHttpParameterArray() {
        ArrayList<HttpParameter> params = new ArrayList<>(12);
        appendParameter("q", query, params);
        appendParameter("lang", lang, params);
        appendParameter("locale", locale, params);
        appendParameter("max_id", maxId, params);
        appendParameter("count", count, params);
        appendParameter("since", since, params);
        appendParameter("since_id", sinceId, params);
        appendParameter("geocode", geocode, params);
        appendParameter("until", until, params);
        if (resultType != null) {
            params.add(new HttpParameter("result_type", resultType.name()));
        }
        params.add(WITH_TWITTER_USER_ID);
        HttpParameter[] paramArray = new HttpParameter[params.size()];
        return params.toArray(paramArray);
    }

    private void appendParameter(String name, String value, List<HttpParameter> params) {
        if (value != null) {
            params.add(new HttpParameter(name, value));
        }
    }

    private void appendParameter(String name, long value, List<HttpParameter> params) {
        if (0 <= value) {
            params.add(new HttpParameter(name, String.valueOf(value)));
        }
    }

    /*package*/ String nextPage() {
        return nextPageQuery;
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

    @SuppressWarnings("UnusedReturnValue")
    public static class QueryBuilder {
        private final String query;
        private String lang = null;
        private String locale = null;
        private long maxId = -1L;
        private int count = -1;
        private String since = null;
        private long sinceId = -1;
        private String geocode = null;
        private String until = null;
        private ResultType resultType = null;
        private String nextPageQuery = null;

        private QueryBuilder(String query) {
            this.query = query;
        }

        /**
         * restricts tweets to the given language, given by an <a href="http://en.wikipedia.org/wiki/ISO_639-1">ISO 639-1 code</a>
         *
         * @param lang an <a href="http://en.wikipedia.org/wiki/ISO_639-1">ISO 639-1 code</a>
         * @return the instance
         * @since Twitter4J 2.1.0
         */
        public QueryBuilder lang(String lang) {
            this.lang = lang;
            return this;
        }

        /**
         * Specify the language of the query you are sending (only ja is currently effective). This is intended for language-specific clients and the default should work in the majority of cases.
         *
         * @param locale the locale
         * @return the instance
         * @since Twitter4J 2.1.1
         */
        public QueryBuilder locale(String locale) {
            this.locale = locale;
            return this;
        }

        /**
         * If specified, returns tweets with status ids less than the given id.
         *
         * @param maxId maxId
         * @return this instance
         * @since Twitter4J 2.1.1
         */
        public QueryBuilder maxId(long maxId) {
            this.maxId = maxId;
            return this;
        }

        /**
         * sets the number of tweets to return per page, up to a max of 100
         *
         * @param count the number of tweets to return per page
         * @return the instance
         * @since Twitter4J 2.1.0
         */
        public QueryBuilder count(int count) {
            this.count = count;
            return this;
        }

        /**
         * If specified, returns tweets with since the given date.  Date should be formatted as YYYY-MM-DD
         *
         * @param since since
         * @return since
         * @since Twitter4J 2.1.1
         */
        public QueryBuilder since(String since) {
            this.since = since;
            return this;
        }

        /**
         * returns tweets with status ids greater than the given id.
         *
         * @param sinceId returns tweets with status ids greater than the given id
         * @return the instance
         * @since Twitter4J 2.1.0
         */
        public QueryBuilder sinceId(long sinceId) {
            this.sinceId = sinceId;
            return this;
        }

        /**
         * returns tweets by users located within a given radius of the given latitude/longitude, where the user's location is taken from their Twitter profile
         *
         * @param location geo location
         * @param radius   radius
         * @param unit     Query.MILES or Query.KILOMETERS
         * @return the instance
         * @since Twitter4J 4.0.7
         */
        public QueryBuilder geoCode(GeoLocation location, double radius
                , Unit unit) {
            this.geocode = location.getLatitude() + "," + location.getLongitude() + "," + radius + unit.name();
            return this;
        }

        /**
         * If specified, returns tweets with generated before the given date.  Date should be formatted as YYYY-MM-DD
         *
         * @param until until
         * @return the instance
         * @since Twitter4J 2.1.1
         */
        public QueryBuilder until(String until) {
            this.until = until;
            return this;
        }


        /**
         * If specified, returns tweets included popular or real time or both in the response
         *
         * @param resultType resultType
         * @return the instance
         * @since Twitter4J 2.1.3
         */
        public QueryBuilder resultType(ResultType resultType) {
            this.resultType = resultType;
            return this;
        }

        public Query build() {
            return new Query(query, lang, locale, maxId, count, since, sinceId, geocode, until, resultType, nextPageQuery);
        }

    }
}
