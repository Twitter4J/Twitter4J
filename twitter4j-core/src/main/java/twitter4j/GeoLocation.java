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

/**
 * A data class representing geo location.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class GeoLocation implements java.io.Serializable {

    private static final long serialVersionUID = 6353721071298376949L;
    private double latitude;
    private double longitude;

    /**
     * Creates a GeoLocation instance
     *
     * @param latitude  the latitude
     * @param longitude the longitude
     */
    public GeoLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /* For serialization purposes only. */
    /* package */ GeoLocation() {

    }

    /**
     * returns the latitude of the geo location
     *
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * returns the longitude of the geo location
     *
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeoLocation)) return false;

        GeoLocation that = (GeoLocation) o;

        if (Double.compare(that.getLatitude(), latitude) != 0) return false;
        if (Double.compare(that.getLongitude(), longitude) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "GeoLocation{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
