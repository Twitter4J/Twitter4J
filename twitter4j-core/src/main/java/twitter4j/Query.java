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
@SuppressWarnings("unused")
public final class Query implements java.io.Serializable {
    private static final long serialVersionUID = 7196404519192910019L;
    @NotNull
    public final String query;
    public final String lang;
    public final String locale;
    public final long maxId;
    public final int count;
    @Nullable
    public final String since;
    public final long sinceId;
    @Nullable
    public final String geocode;
    @Nullable
    public final String until;
    @Nullable
    public final ResultType resultType;
    @Nullable
    final String nextPageQuery;

    @NotNull
    public Query withLang(@NotNull String lang) {
        return new Query(query, lang, locale, maxId, count, since, sinceId, geocode, until, resultType, nextPageQuery);
    }

    @NotNull
    public Query withLocale(@NotNull String locale) {
        return new Query(query, lang, locale, maxId, count, since, sinceId, geocode, until, resultType, nextPageQuery);
    }

    @NotNull
    public Query withMaxId(long maxId) {
        return new Query(query, lang, locale, maxId, count, since, sinceId, geocode, until, resultType, nextPageQuery);
    }
    @NotNull
    public Query withCount(int count) {
        return new Query(query, lang, locale, maxId, count, since, sinceId, geocode, until, resultType, nextPageQuery);
    }
    @NotNull
    public Query withSince(@NotNull String since) {
        return new Query(query, lang, locale, maxId, count, since, sinceId, geocode, null, resultType, nextPageQuery);
    }
    @NotNull
    public Query withSinceId(long sinceId) {
        return new Query(query, lang, locale, maxId, count, since, sinceId, geocode, until, resultType, nextPageQuery);
    }
    @NotNull
    public Query withGeoCode(@NotNull GeoLocation location, double radius
                , Unit unit) {
          String geocode = location.getLatitude() + "," + location.getLongitude() + "," + radius + unit.name();
        return new Query(query, lang, locale, maxId, count, since, sinceId, geocode, until, resultType, nextPageQuery);
    }
    @NotNull
    public Query withUntil(@NotNull String until) {
        return new Query(query, lang, locale, maxId, count, since, sinceId, geocode, until, resultType, nextPageQuery);
    }
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

    @NotNull
    public static Query of(@NotNull String query) {
        return new Query(query, null, null, -1, -1, null, -1, null, null, null, null);
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

        String query = params.getOrDefault("q", "");
        String lang = params.getOrDefault("lang", null);
        String locale = params.getOrDefault("locale", null);
        long maxId = Long.parseLong(params.getOrDefault("max_id", "-1"));
        int count = Integer.parseInt(params.getOrDefault("count", "-1"));
        String geoLocation = null;
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
            if (unit == null) {
                throw new IllegalArgumentException("unrecognized geocode radius: " + radiusstr);
            }
            geoLocation = latitude + "," + longitude + "," + radius + unit.name();

        }
        ResultType resultType1 = null;
        if (params.containsKey("result_type")) {
            resultType1 = Query.ResultType.valueOf(params.get("result_type"));
        }
        // We don't pull out since, until -- they get pushed into the query
        return new Query(query, lang, locale, maxId, count, null, -1, geoLocation, null, resultType1, nextPageQuery);
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
}
