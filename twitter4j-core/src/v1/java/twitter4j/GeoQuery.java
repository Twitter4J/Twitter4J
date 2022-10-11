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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.1
 */
@SuppressWarnings("unused")
public final class GeoQuery implements java.io.Serializable {

    private static final long serialVersionUID = 5434503339001056634L;
    /**
     * location
     */
    @Nullable
    public final GeoLocation location;
    /**
     * query
     */
    public final String query;
    /**
     * ip
     */
    @Nullable
    public final String ip;
    /**
     * accuracy
     */
    @Nullable
    public final String accuracy;
    /**
     * granularity
     */
    @Nullable
    public final String granularity;
    /**
     * maxResults
     */
    public final int maxResults;

    private GeoQuery(@Nullable GeoLocation location, String query, @Nullable String ip, @Nullable String accuracy, @Nullable String granularity, int maxResults) {
        this.location = location;
        this.query = query;
        this.ip = ip;
        this.accuracy = accuracy;
        this.granularity = granularity;
        this.maxResults = maxResults;
    }

    /**
     * Creates a GeoQuery with the specified location
     *
     * @param location geo location
     * @return GeoQuery
     */
    public static GeoQuery ofGeoLocation(GeoLocation location) {
        return new GeoQuery(location, null, null, null, null, -1);
    }

    /**
     * Creates a GeoQuery with the specified location
     *
     * @param location geo location
     * @return GeoQuery
     */
    public GeoQuery geoLocation(GeoLocation location) {
        return new GeoQuery(location, this.query, this.ip, this.accuracy, this.granularity, this.maxResults);
    }

/*
        return new GeoQuery(this.location,this.query,this.ip,this.accuracy,this.granularity,this.maxResults);

 */

    /**
     * Creates a GeoQuery with the specified IP address
     *
     * @param ip IP address
     * @return GeoQuery
     */
    public static GeoQuery ofIP(String ip) {
        return new GeoQuery(null, null, ip, null, null, -1);
    }

    /**
     * @param ip IP
     * @return GeoQuery
     */
    public GeoQuery ip(String ip) {
        return new GeoQuery(this.location, this.query, ip, this.accuracy, this.granularity, this.maxResults);
    }

    /**
     * Creates a GeoQuery with the specified query, ip and location
     *
     * @param query free-form text to match
     * @return GeoQuery
     */
    public static GeoQuery ofQuery(@NotNull String query) {
        return new GeoQuery(null, query, null, null, null, -1);
    }

    /**
     * Creates a GeoQuery with the specified query, ip and location
     *
     * @param query free-form text to match
     * @return GeoQuery
     */
    public GeoQuery query(@NotNull String query) {
        return new GeoQuery(this.location, query, this.ip, this.accuracy, this.granularity, this.maxResults);
    }

    /**
     * Sets a hint on the "region" in which to search.  If a number, then this is a radius in meters, but it can also take a string that is suffixed with ft to specify feet.  If this is not passed in, then it is assumed to be 0m.  If coming from a device, in practice, this value is whatever accuracy the device has measuring its location (whether it be coming from a GPS, WiFi triangulation, etc.).
     *
     * @param accuracy a hint on the "region" in which to search.
     * @return this instance
     */
    public GeoQuery accuracy(@NotNull String accuracy) {
        return new GeoQuery(this.location, this.query, this.ip, accuracy, this.granularity, this.maxResults);
    }


    /**
     * Sets the minimal granularity of data to return.  If this is not passed in, then neighborhood is assumed.  city can also be passed.
     *
     * @param granularity the minimal granularity of data to return
     * @return GeoQuery
     */
    public GeoQuery granularity(@NotNull String granularity) {
        return new GeoQuery(this.location, this.query, this.ip, this.accuracy, granularity, this.maxResults);
    }

    /**
     * Sets a hint as to the number of results to return.  This does not guarantee that the number of results returned will equal max_results, but instead informs how many "nearby" results to return.  Ideally, only pass in the number of places you intend to display to the user here.
     *
     * @param maxResults A hint as to the number of results to return.
     * @return this instance
     */
    public GeoQuery maxResults(int maxResults) {
        return new GeoQuery(this.location, this.query, this.ip, this.accuracy, this.granularity, maxResults);
    }

    /*package*/ HttpParameter[] asHttpParameterArray() {
        ArrayList<HttpParameter> params = new ArrayList<>();
        if (location != null) {
            appendParameter("lat", location.latitude, params);
            appendParameter("long", location.longitude, params);

        }
        if (ip != null) {
            appendParameter("ip", ip, params);

        }
        appendParameter("accuracy", accuracy, params);
        appendParameter("query", query, params);
        appendParameter("granularity", granularity, params);
        appendParameter("max_results", maxResults, params);
        HttpParameter[] paramArray = new HttpParameter[params.size()];
        return params.toArray(paramArray);
    }

    private void appendParameter(String name, String value, List<HttpParameter> params) {
        if (value != null) {
            params.add(new HttpParameter(name, value));
        }
    }

    private void appendParameter(@SuppressWarnings("SameParameterValue") String name, int value, List<HttpParameter> params) {
        if (0 < value) {
            params.add(new HttpParameter(name, String.valueOf(value)));
        }
    }

    private void appendParameter(String name, double value, List<HttpParameter> params) {
        params.add(new HttpParameter(name, String.valueOf(value)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeoQuery geoQuery = (GeoQuery) o;

        if (maxResults != geoQuery.maxResults) return false;
        if (!Objects.equals(accuracy, geoQuery.accuracy))
            return false;
        if (!Objects.equals(granularity, geoQuery.granularity))
            return false;
        if (!Objects.equals(ip, geoQuery.ip))
            return false;
        return Objects.equals(location, geoQuery.location);
    }

    @Override
    public int hashCode() {
        int result = location != null ? location.hashCode() : 0;
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        result = 31 * result + (accuracy != null ? accuracy.hashCode() : 0);
        result = 31 * result + (granularity != null ? granularity.hashCode() : 0);
        result = 31 * result + maxResults;
        return result;
    }

    @Override
    public String toString() {
        return "GeoQuery{" +
                "location=" + location +
                ", query='" + query + '\'' +
                ", ip='" + ip + '\'' +
                ", accuracy='" + accuracy + '\'' +
                ", granularity='" + granularity + '\'' +
                ", maxResults=" + maxResults +
                '}';
    }
}
