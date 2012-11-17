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

package twitter4j.internal.json;

import twitter4j.Location;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import static twitter4j.internal.json.z_T4JInternalParseUtil.*;

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

    /*package*/
    static ResponseList<Location> createLocationList(HttpResponse res, Configuration conf) throws TwitterException {
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
        }
        return createLocationList(res.asJSONArray(), conf.isJSONStoreEnabled());
    }

    /*package*/
    static ResponseList<Location> createLocationList(JSONArray list, boolean storeJSON) throws TwitterException {
        try {
            int size = list.length();
            ResponseList<Location> locations =
                    new ResponseListImpl<Location>(size, null);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                Location location = new LocationJSONImpl(json);
                locations.add(location);
                if (storeJSON) {
                    DataObjectFactoryUtil.registerJSONObject(location, json);
                }
            }
            if (storeJSON) {
                DataObjectFactoryUtil.registerJSONObject(locations, list);
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
    @Override
    public int getWoeid() {
        return woeid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCountryName() {
        return countryName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlaceName() {
        return placeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPlaceCode() {
        return placeCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
