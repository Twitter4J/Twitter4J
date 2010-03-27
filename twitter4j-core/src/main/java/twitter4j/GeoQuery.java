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
import java.util.List;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.1
 */
public final class GeoQuery implements java.io.Serializable {

    private GeoLocation location;
    private String ip;
    private String accuracy;
    private String granularity;
    private int maxResults;
    public static final String NEIGHBORHOOD = "neighborhood";
    public static final String CITY = "city";
    private static final long serialVersionUID = 927081526936169802L;

    /**
     * Creates a GeoQuery with the specified location
     * @param location
     */
    public GeoQuery(GeoLocation location){
        this.location = location;
        this.ip = null;
    }

    /**
     * Creates a GeoQuery with the specified IP address
     * @param ip IP address
     */
    public GeoQuery(String ip){
        this.ip = ip;
        this.location = null;
    }

    public GeoLocation getLocation() {
        return location;
    }

    public String getIp() {
        return ip;
    }

    public String getAccuracy() {
        return accuracy;
    }

    /**
     * Sets a hint on the "region" in which to search.  If a number, then this is a radius in meters, but it can also take a string that is suffixed with ft to specify feet.  If this is not passed in, then it is assumed to be 0m.  If coming from a device, in practice, this value is whatever accuracy the device has measuring its location (whether it be coming from a GPS, WiFi triangulation, etc.).
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
     * @param maxResults A hint as to the number of results to return.
     */
    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }
    public GeoQuery maxResults(int maxResults) {
        setMaxResults(maxResults);
        return this;
    }
    /*package*/ HttpParameter[] asHttpParameterArray(){
        ArrayList<HttpParameter> params = new ArrayList<HttpParameter>();
        if(null != location){
            appendParameter("lat", location.getLatitude(), params);
            appendParameter("long", location.getLongitude(), params);

        }
        if(null != ip){
            appendParameter("ip", ip, params);

        }
        appendParameter("accuracy", accuracy, params);
        appendParameter("granularity", granularity, params);
        appendParameter("max_results", maxResults, params);
        HttpParameter[] paramArray = new HttpParameter[params.size()];
        return params.toArray(paramArray);
    }

    private void appendParameter(String name, String value, List<HttpParameter> params) {
        if (null != value) {
            params.add(new HttpParameter(name, value));
        }
    }

    private void appendParameter(String name, int value, List<HttpParameter> params) {
        if(0 < value){
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
                ", ip='" + ip + '\'' +
                ", accuracy='" + accuracy + '\'' +
                ", granularity='" + granularity + '\'' +
                ", maxResults=" + maxResults +
                '}';
    }
}
