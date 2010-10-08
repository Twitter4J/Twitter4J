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

import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import java.util.Arrays;

import static twitter4j.internal.util.ParseUtil.getRawString;
import static twitter4j.internal.util.ParseUtil.getUnescapedString;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.1
 */
final class PlaceJSONImpl extends TwitterResponseImpl implements Place, java.io.Serializable {
    private String name;
    private String streetAddress;
    private String countryCode;
    private String id;
    private String country;
    private String placeType;
    private String url;
    private String fullName;
    private String boundingBoxType;
    private GeoLocation[][] boundingBoxCoordinates;
    private String geometryType;
    private GeoLocation[][] geometryCoordinates;
    private Place[] containedWithIn;
    private static final long serialVersionUID = -2873364341474633812L;

    /*package*/ PlaceJSONImpl(HttpResponse res) throws TwitterException {
        super(res);
        init(res.asJSONObject());
    }
    PlaceJSONImpl(JSONObject json, HttpResponse res) throws TwitterException {
        super(res);
        init(json);
    }
    private void init(JSONObject json) throws TwitterException{
        try {
            name = getUnescapedString("name", json);
            streetAddress = getUnescapedString("street_address", json);
            countryCode = getRawString("country_code", json);
            id = getRawString("id", json);
            country = getRawString("country", json);
            placeType = getRawString("place_type", json);
            url = getRawString("url", json);
            fullName = getRawString("full_name", json);
            if (!json.isNull("bounding_box")) {
                JSONObject boundingBoxJSON = json.getJSONObject("bounding_box");
                boundingBoxType = getRawString("type", boundingBoxJSON);
                JSONArray array = boundingBoxJSON.getJSONArray("coordinates");
                boundingBoxCoordinates = GeoLocation.coordinatesAsGeoLocationArray(array);
            } else {
                boundingBoxType = null;
                boundingBoxCoordinates = null;
            }

            if(!json.isNull("geometry")){
                JSONObject geometryJSON = json.getJSONObject("geometry");
                geometryType = getRawString("type", geometryJSON);
                JSONArray array = geometryJSON.getJSONArray("coordinates");
                if(geometryType.equals("Point")){
                  geometryCoordinates = new GeoLocation[1][1];
                  geometryCoordinates[0][0] = new GeoLocation(array.getDouble(0), array.getDouble(1));
                }else if (geometryType.equals("Polygon")){
                  geometryCoordinates = GeoLocation.coordinatesAsGeoLocationArray(array);
                }else{
                  // MultiPolygon currently unsupported.
                  geometryType = null;
                  geometryCoordinates = null;
                }
            }else{
                geometryType = null;
                geometryCoordinates = null;
            }

            if(!json.isNull("contained_within")){
                JSONArray containedWithInJSON = json.getJSONArray("contained_within");
                containedWithIn = new Place[containedWithInJSON.length()];
                for(int i=0;i<containedWithInJSON.length();i++){
                containedWithIn[i] = new PlaceJSONImpl(containedWithInJSON.getJSONObject(i), null);
                }
            }else{
                containedWithIn = null;
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), jsone);
        }
    }

    public int compareTo(Place that) {
        return this.id.compareTo(that.getId());
    }

    /*package*/ static ResponseList<Place> createPlaceList(HttpResponse res) throws TwitterException {
        JSONObject json = null;
        try {
            json = res.asJSONObject();
            return createPlaceList(json.getJSONObject("result").getJSONArray("places"), res);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), jsone);
        }
    }

    /*package*/ static ResponseList<Place> createPlaceList(JSONArray list, HttpResponse res) throws TwitterException {
        try {
            int size = list.length();
            ResponseList<Place> places =
                    new ResponseListImpl<Place>(size, res);
            for (int i = 0; i < size; i++) {
                places.add(new PlaceJSONImpl(list.getJSONObject(i), null));
            }
            return places;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }

    public String getName(){
        return name;
    }
    public String getStreetAddress(){
        return streetAddress;
    }
    public String getCountryCode(){
        return countryCode;
    }
    public String getId(){
        return id;
    }
    public String getCountry(){
        return country;
    }
    public String getPlaceType(){
        return placeType;
    }
    public String getURL(){
        return url;
    }
    public String getFullName(){
        return fullName;
    }
    public String getBoundingBoxType(){
        return boundingBoxType;
    }
    public GeoLocation[][] getBoundingBoxCoordinates(){
        return boundingBoxCoordinates;
    }
    public String getGeometryType(){
        return geometryType;
    }
    public GeoLocation[][] getGeometryCoordinates(){
        return geometryCoordinates;
    }
    public Place[] getContainedWithIn(){
        return containedWithIn;
    }


    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return obj instanceof Place && ((Place) obj).getId().equals(this.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "PlaceJSONImpl{" +
                "name='" + name + '\'' +
                ", streetAddress='" + streetAddress + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", id='" + id + '\'' +
                ", country='" + country + '\'' +
                ", placeType='" + placeType + '\'' +
                ", url='" + url + '\'' +
                ", fullName='" + fullName + '\'' +
                ", boundingBoxType='" + boundingBoxType + '\'' +
                ", boundingBoxCoordinates=" + (boundingBoxCoordinates == null ? null : Arrays.asList(boundingBoxCoordinates)) +
                ", geometryType='" + geometryType + '\'' +
                ", geometryCoordinates=" + (geometryCoordinates == null ? null : Arrays.asList(geometryCoordinates)) +
                ", containedWithIn=" + (containedWithIn == null ? null : Arrays.asList(containedWithIn)) +
                '}';
    }
}
