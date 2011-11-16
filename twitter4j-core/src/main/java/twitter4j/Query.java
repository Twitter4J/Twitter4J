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

import java.util.ArrayList;
import java.util.List;

/**
 * A data class represents search query.<br>
 * An instance of this class is NOT thread safe.<br>
 * Instances can be shared across threads, but should not be mutated while a search is ongoing.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see <a href="https://dev.twitter.com/docs/api/1/get/search">GET search | Twitter Developers</a>
 * @see <a href="http://search.twitter.com/operators">Twitter API / Search Operators</a>
 */
public final class Query implements java.io.Serializable {
    private String query = null;
    private String lang = null;
    private String locale = null;
    private long maxId = -1l;
    private int rpp = -1;
    private int page = -1;
    private String since = null;
    private long sinceId = -1;
    private String geocode = null;
    private String until = null;
    private String resultType = null;
    private static final long serialVersionUID = -8108425822233599808L;

    public Query() {
    }

    public Query(String query) {
        this.query = query;
    }

    /**
     * Returns the specified query
     *
     * @return query
     */
    public String getQuery() {
        return query;
    }

    /**
     * Sets the query string
     *
     * @param query the query string
     * @see <a href="https://dev.twitter.com/docs/api/1/get/search">GET search | Twitter Developers</a>
     * @see <a href="http://search.twitter.com/operators">Twitter API / Search Operators</a>
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * Sets the query string
     *
     * @param query the query string
     * @return the instance
     * @see <a href="https://dev.twitter.com/docs/api/1/get/search">GET search | Twitter Developers</a>
     * @see <a href="http://search.twitter.com/operators">Twitter API / Search Operators</a>
     * @since Twitter4J 2.1.0
     */
    public Query query(String query) {
        setQuery(query);
        return this;
    }

    /**
     * Returns the lang
     *
     * @return lang
     */
    public String getLang() {
        return lang;
    }

    /**
     * restricts tweets to the given language, given by an <a href="http://en.wikipedia.org/wiki/ISO_639-1">ISO 639-1 code</a>
     *
     * @param lang an <a href="http://en.wikipedia.org/wiki/ISO_639-1">ISO 639-1 code</a>
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * restricts tweets to the given language, given by an <a href="http://en.wikipedia.org/wiki/ISO_639-1">ISO 639-1 code</a>
     *
     * @param lang an <a href="http://en.wikipedia.org/wiki/ISO_639-1">ISO 639-1 code</a>
     * @return the instance
     * @since Twitter4J 2.1.0
     */
    public Query lang(String lang) {
        setLang(lang);
        return this;
    }

    /**
     * Returns the language of the query you are sending (only ja is currently effective). This is intended for language-specific clients and the default should work in the majority of cases.
     *
     * @return locale
     * @since Twitter4J 2.1.1
     */
    public String getLocale() {
        return locale;
    }

    /**
     * Specify the language of the query you are sending (only ja is currently effective). This is intended for language-specific clients and the default should work in the majority of cases.
     *
     * @param locale the locale
     * @since Twitter4J 2.1.1
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * Specify the language of the query you are sending (only ja is currently effective). This is intended for language-specific clients and the default should work in the majority of cases.
     *
     * @param locale the locale
     * @return the instance
     * @since Twitter4J 2.1.1
     */
    public Query locale(String locale) {
        setLocale(locale);
        return this;
    }

    /**
     * Returns tweets with status ids less than the given id.
     *
     * @return maxId
     * @since Twitter4J 2.1.1
     */
    public long getMaxId() {
        return maxId;
    }

    /**
     * If specified, returns tweets with status ids less than the given id.
     *
     * @param maxId maxId
     * @since Twitter4J 2.1.1
     */
    public void setMaxId(long maxId) {
        this.maxId = maxId;
    }

    /**
     * If specified, returns tweets with status ids less than the given id.
     *
     * @param maxId maxId
     * @return this instance
     * @since Twitter4J 2.1.1
     */
    public Query maxId(long maxId) {
        setMaxId(maxId);
        return this;
    }

    /**
     * Returns the number of tweets to return per page, up to a max of 100
     *
     * @return rpp
     */
    public int getRpp() {
        return rpp;
    }

    /**
     * sets the number of tweets to return per page, up to a max of 100
     *
     * @param rpp the number of tweets to return per page
     */
    public void setRpp(int rpp) {
        this.rpp = rpp;
    }

    /**
     * sets the number of tweets to return per page, up to a max of 100
     *
     * @param rpp the number of tweets to return per page
     * @return the instance
     * @since Twitter4J 2.1.0
     */
    public Query rpp(int rpp) {
        setRpp(rpp);
        return this;
    }

    /**
     * Returns the page number (starting at 1) to return, up to a max of roughly 1500 results
     *
     * @return the page number (starting at 1) to return
     */
    public int getPage() {
        return page;
    }

    /**
     * sets the page number (starting at 1) to return, up to a max of roughly 1500 results
     *
     * @param page the page number (starting at 1) to return
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * sets the page number (starting at 1) to return, up to a max of roughly 1500 results
     *
     * @param page the page number (starting at 1) to return
     * @return the instance
     * @since Twitter4J 2.1.0
     */
    public Query page(int page) {
        setPage(page);
        return this;
    }

    /**
     * Returns tweets with since the given date.  Date should be formatted as YYYY-MM-DD
     *
     * @return since
     * @since Twitter4J 2.1.1
     */
    public String getSince() {
        return since;
    }

    /**
     * If specified, returns tweets with since the given date.  Date should be formatted as YYYY-MM-DD
     *
     * @param since since
     * @since Twitter4J 2.1.1
     */
    public void setSince(String since) {
        this.since = since;
    }

    /**
     * If specified, returns tweets with since the given date.  Date should be formatted as YYYY-MM-DD
     *
     * @param since since
     * @return since
     * @since Twitter4J 2.1.1
     */
    public Query since(String since) {
        setSince(since);
        return this;
    }

    /**
     * returns sinceId
     *
     * @return sinceId
     */
    public long getSinceId() {
        return sinceId;
    }

    /**
     * returns tweets with status ids greater than the given id.
     *
     * @param sinceId returns tweets with status ids greater than the given id
     */
    public void setSinceId(long sinceId) {
        this.sinceId = sinceId;
    }

    /**
     * returns tweets with status ids greater than the given id.
     *
     * @param sinceId returns tweets with status ids greater than the given id
     * @return the instance
     * @since Twitter4J 2.1.0
     */
    public Query sinceId(long sinceId) {
        setSinceId(sinceId);
        return this;
    }

    /**
     * Returns the specified geocode
     *
     * @return geocode
     */
    public String getGeocode() {
        return geocode;
    }

    public static final String MILES = "mi";
    public static final String KILOMETERS = "km";

    /**
     * returns tweets by users located within a given radius of the given latitude/longitude, where the user's location is taken from their Twitter profile
     *
     * @param location geo location
     * @param radius   radius
     * @param unit     Query.MILES or Query.KILOMETERS
     */
    public void setGeoCode(GeoLocation location, double radius
            , String unit) {
        this.geocode = location.getLatitude() + "," + location.getLongitude() + "," + radius + unit;
    }

    /**
     * returns tweets by users located within a given radius of the given latitude/longitude, where the user's location is taken from their Twitter profile
     *
     * @param location geo location
     * @param radius   radius
     * @param unit     Query.MILES or Query.KILOMETERS
     * @return the instance
     * @since Twitter4J 2.1.0
     */
    public Query geoCode(GeoLocation location, double radius
            , String unit) {
        setGeoCode(location, radius, unit);
        return this;
    }

    /**
     * Returns until
     *
     * @return until
     * @since Twitter4J 2.1.1
     */
    public String getUntil() {
        return until;
    }

    /**
     * If specified, returns tweets with generated before the given date.  Date should be formatted as YYYY-MM-DD
     *
     * @param until until
     * @since Twitter4J 2.1.1
     */
    public void setUntil(String until) {
        this.until = until;
    }

    /**
     * If specified, returns tweets with generated before the given date.  Date should be formatted as YYYY-MM-DD
     *
     * @param until until
     * @return the instance
     * @since Twitter4J 2.1.1
     */
    public Query until(String until) {
        setUntil(until);
        return this;
    }

    /**
     * mixed: Include both popular and real time results in the response.
     * recent: return only the most recent results in the response
     * popular: return only the most popular results in the response.
     */
    public final static String MIXED = "mixed";
    public final static String POPULAR = "popular";
    public final static String RECENT = "recent";

    /**
     * Returns resultType
     *
     * @return the resultType
     * @since Twitter4J 2.1.3
     */
    public String getResultType() {
        return resultType;
    }

    /**
     * Default value is Query.MIXED if parameter not specified
     *
     * @param resultType Query.MIXED or Query.POPULAR or Query.RECENT
     * @since Twitter4J 2.1.3
     */
    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    /**
     * If specified, returns tweets included popular or real time or both in the responce
     *
     * @param resultType resultType
     * @return the instance
     * @since Twitter4J 2.1.3
     */
    public Query resultType(String resultType) {
        setResultType(resultType);
        return this;
    }

    private static HttpParameter WITH_TWITTER_USER_ID = new HttpParameter("with_twitter_user_id", "true");

    /*package*/ HttpParameter[] asHttpParameterArray(HttpParameter includeEntities) {
        ArrayList<HttpParameter> params = new ArrayList<HttpParameter>();
        appendParameter("q", query, params);
        appendParameter("lang", lang, params);
        appendParameter("locale", locale, params);
        appendParameter("max_id", maxId, params);
        appendParameter("rpp", rpp, params);
        appendParameter("page", page, params);
        appendParameter("since", since, params);
        appendParameter("since_id", sinceId, params);
        appendParameter("geocode", geocode, params);
        appendParameter("until", until, params);
        appendParameter("result_type", resultType, params);
        params.add(WITH_TWITTER_USER_ID);
        params.add(includeEntities);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Query query1 = (Query) o;

        if (maxId != query1.maxId) return false;
        if (page != query1.page) return false;
        if (rpp != query1.rpp) return false;
        if (sinceId != query1.sinceId) return false;
        if (geocode != null ? !geocode.equals(query1.geocode) : query1.geocode != null)
            return false;
        if (lang != null ? !lang.equals(query1.lang) : query1.lang != null)
            return false;
        if (locale != null ? !locale.equals(query1.locale) : query1.locale != null)
            return false;
        if (query != null ? !query.equals(query1.query) : query1.query != null)
            return false;
        if (since != null ? !since.equals(query1.since) : query1.since != null)
            return false;
        if (until != null ? !until.equals(query1.until) : query1.until != null)
            return false;
        if (resultType != null ? !resultType.equals(query1.resultType) : query1.resultType != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = query != null ? query.hashCode() : 0;
        result = 31 * result + (lang != null ? lang.hashCode() : 0);
        result = 31 * result + (locale != null ? locale.hashCode() : 0);
        result = 31 * result + (int) (maxId ^ (maxId >>> 32));
        result = 31 * result + rpp;
        result = 31 * result + page;
        result = 31 * result + (since != null ? since.hashCode() : 0);
        result = 31 * result + (int) (sinceId ^ (sinceId >>> 32));
        result = 31 * result + (geocode != null ? geocode.hashCode() : 0);
        result = 31 * result + (until != null ? until.hashCode() : 0);
        result = 31 * result + (resultType != null ? resultType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Query{" +
                "query='" + query + '\'' +
                ", lang='" + lang + '\'' +
                ", locale='" + locale + '\'' +
                ", maxId=" + maxId +
                ", rpp=" + rpp +
                ", page=" + page +
                ", since='" + since + '\'' +
                ", sinceId=" + sinceId +
                ", geocode='" + geocode + '\'' +
                ", until='" + until + '\'' +
                ", resultType='" + resultType + '\'' +
                '}';
    }
}
