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
    private GeoLocation location;
    /**
     * query
     */
    private String query = null;
    /**
     * ip
     */
    private String ip = null;
    /**
     * accuracy
     */
    private String accuracy = null;
    /**
     * granularity
     */
    private String granularity = null;
    /**
     * maxResults
     */
    private int maxResults = -1;

    /**
     * Creates a GeoQuery with the specified location
     *
     * @param location geo location
     */
    public GeoQuery(GeoLocation location) {
        this.location = location;
    }

    /**
     * Creates a GeoQuery with the specified IP address
     *
     * @param ip IP address
     */
    public GeoQuery(String ip) {
        this.ip = ip;
    }

    /**
     * Creates a GeoQuery with the specified query, ip and location
     *
     * @param query    free-form text to match
     * @param ip       IP address
     * @param location geo location
     */
    public GeoQuery(String query, String ip, GeoLocation location) {
        this.query = query;
        this.ip = ip;
        this.location = location;
    }

    /**
     * @return geo location
     */
    public GeoLocation getLocation() {
        return location;
    }

    /**
     * Gets the query to filter Place results from geo/search
     *
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    /**
     * @param query query
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * @return ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @return accuracy
     */
    public String getAccuracy() {
        return accuracy;
    }

    /**
     * Sets a hint on the "region" in which to search.  If a number, then this is a radius in meters, but it can also take a string that is suffixed with ft to specify feet.  If this is not passed in, then it is assumed to be 0m.  If coming from a device, in practice, this value is whatever accuracy the device has measuring its location (whether it be coming from a GPS, WiFi triangulation, etc.).
     *
     * @param accuracy a hint on the "region" in which to search.
     */
    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }


    /**
     * Sets a hint on the "region" in which to search.  If a number, then this is a radius in meters, but it can also take a string that is suffixed with ft to specify feet.  If this is not passed in, then it is assumed to be 0m.  If coming from a device, in practice, this value is whatever accuracy the device has measuring its location (whether it be coming from a GPS, WiFi triangulation, etc.).
     *
     * @param accuracy a hint on the "region" in which to search.
     * @return this instance
     */
    public GeoQuery accuracy(String accuracy) {
        setAccuracy(accuracy);
        return this;
    }

    /**
     * returns granularity
     * @return granularity
     */
    public String getGranularity() {
        return granularity;
    }

    /**
     * Sets the minimal granularity of data to return.  If this is not passed in, then neighborhood is assumed.  city can also be passed.
     *
     * @param granularity the minimal granularity of data to return
     */
    public void setGranularity(String granularity) {
        this.granularity = granularity;
    }


    /**
     * Sets the minimal granularity of data to return.  If this is not passed in, then neighborhood is assumed.  city can also be passed.
     *
     * @param granularity the minimal granularity of data to return
     * @return this instance
     */
    public GeoQuery granularity(String granularity) {
        setGranularity(granularity);
        return this;
    }

    /**
     * @return max results
     */
    public int getMaxResults() {
        return maxResults;
    }

    /**
     * Sets a hint as to the number of results to return.  This does not guarantee that the number of results returned will equal max_results, but instead informs how many "nearby" results to return.  Ideally, only pass in the number of places you intend to display to the user here.
     *
     * @param maxResults A hint as to the number of results to return.
     */
    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    /**
     * Sets a hint as to the number of results to return.  This does not guarantee that the number of results returned will equal max_results, but instead informs how many "nearby" results to return.  Ideally, only pass in the number of places you intend to display to the user here.
     *
     * @param maxResults A hint as to the number of results to return.
     * @return this instance
     */
    public GeoQuery maxResults(int maxResults) {
        setMaxResults(maxResults);
        return this;
    }

    /*package*/ HttpParameter[] asHttpParameterArray() {
        ArrayList<HttpParameter> params = new ArrayList<>();
        if (location != null) {
            appendParameter("lat", location.getLatitude(), params);
            appendParameter("long", location.getLongitude(), params);

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
