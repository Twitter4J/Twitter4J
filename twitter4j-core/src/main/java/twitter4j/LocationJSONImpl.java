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

import static twitter4j.internal.util.ParseUtil.getInt;
import static twitter4j.internal.util.ParseUtil.getRawString;
import static twitter4j.internal.util.ParseUtil.getUnescapedString;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
/*package*/ final class LocationJSONImpl implements Location {
    private final int woeid;
    private final String countryName;
    private final String countryCode;
    private final String placeName;
    private final int placeCode;
    private final String name;
    private final String url;
    private static final long serialVersionUID = 7095092358530897222L;

    /*package*/ LocationJSONImpl(JSONObject location) throws TwitterException {
        try {
            woeid = getInt("woeid", location);
            countryName = getUnescapedString("country", location);
            countryCode = getRawString("countryCode", location);
            if (!location.isNull("placeType")) {
                JSONObject placeJSON = location.getJSONObject("placeType");
                placeName = getUnescapedString("name", placeJSON);
                placeCode = getInt("code", placeJSON);
            } else {
                placeName = null;
                placeCode = -1;
            }
            name = getUnescapedString("name", location);
            url = getUnescapedString("url", location);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    /*package*/ static ResponseList<Location> createLocationList(HttpResponse res) throws TwitterException {
        return createLocationList(res.asJSONArray(), res);
    }

    /*package*/ static ResponseList<Location> createLocationList(JSONArray list, HttpResponse res) throws TwitterException {
        try {
            int size = list.length();
            ResponseList<Location> locations =
                    new ResponseListImpl<Location>(size, null);
            for (int i = 0; i < size; i++) {
                locations.add(new LocationJSONImpl(list.getJSONObject(i)));
            }
            return locations;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getWoeid() {
        return woeid;
    }

    /**
     * {@inheritDoc}
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * {@inheritDoc}
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * {@inheritDoc}
     */
    public String getPlaceName() {
        return placeName;
    }

    /**
     * {@inheritDoc}
     */
    public int getPlaceCode() {
        return placeCode;
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    public String getURL() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationJSONImpl)) return false;

        LocationJSONImpl that = (LocationJSONImpl) o;

        if (woeid != that.woeid) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return woeid;
    }

    @Override
    public String toString() {
        return "LocationJSONImpl{" +
                "woeid=" + woeid +
                ", countryName='" + countryName + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", placeName='" + placeName + '\'' +
                ", placeCode='" + placeCode + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
