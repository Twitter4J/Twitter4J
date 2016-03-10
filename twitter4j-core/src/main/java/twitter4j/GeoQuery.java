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

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.1
 */
public final class GeoQuery implements java.io.Serializable {

    private static final long serialVersionUID = 5434503339001056634L;
    private GeoLocation location;
    private String query = null;
    private String ip = null;
    private String accuracy = null;
    private String granularity = null;
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

    public GeoLocation getLocation() {
        return location;
    }

    /**
     * Gets the query to filter Place results from geo/search
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getIp() {
        return ip;
    }

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

    public GeoQuery accuracy(String accuracy) {
        setAccuracy(accuracy);
        return this;
    }

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

    public GeoQuery granularity(String granularity) {
        setGranularity(granularity);
        return this;
    }

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

    public GeoQuery maxResults(int maxResults) {
        setMaxResults(maxResults);
        return this;
    }

    /*package*/ HttpParameter[] asHttpParameterArray() {
        ArrayList<HttpParameter> params = new ArrayList<HttpParameter>();
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

    private void appendParameter(String name, int value, List<HttpParameter> params) {
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
        if (accuracy != null ? !accuracy.equals(geoQuery.accuracy) : geoQuery.accuracy != null)
            return false;
        if (granularity != null ? !granularity.equals(geoQuery.granularity) : geoQuery.granularity != null)
            return false;
        if (ip != null ? !ip.equals(geoQuery.ip) : geoQuery.ip != null)
            return false;
        if (location != null ? !location.equals(geoQuery.location) : geoQuery.location != null)
            return false;

        return true;
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
