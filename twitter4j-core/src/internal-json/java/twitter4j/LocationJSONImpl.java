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

import twitter4j.conf.Configuration;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
/*package*/ final class LocationJSONImpl implements Location {
    private static final long serialVersionUID = -1312752311160422264L;
    private final int woeid;
    private final String countryName;
    private final String countryCode;
    private final String placeName;
    private final int placeCode;
    private final String name;
    private final String url;

    /*package*/ LocationJSONImpl(JSONObject location) throws TwitterException {
        try {
            woeid = ParseUtil.getInt("woeid", location);
            countryName = ParseUtil.getUnescapedString("country", location);
            countryCode = ParseUtil.getRawString("countryCode", location);
            if (!location.isNull("placeType")) {
                JSONObject placeJSON = location.getJSONObject("placeType");
                placeName = ParseUtil.getUnescapedString("name", placeJSON);
                placeCode = ParseUtil.getInt("code", placeJSON);
            } else {
                placeName = null;
                placeCode = -1;
            }
            name = ParseUtil.getUnescapedString("name", location);
            url = ParseUtil.getUnescapedString("url", location);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    /*package*/
    static ResponseList<Location> createLocationList(HttpResponse res, Configuration conf) throws TwitterException {
        if (conf.isJSONStoreEnabled()) {
            TwitterObjectFactory.clearThreadLocalMap();
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
                    TwitterObjectFactory.registerJSONObject(location, json);
                }
            }
            if (storeJSON) {
                TwitterObjectFactory.registerJSONObject(locations, list);
            }
            return locations;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    @Override
    public int getWoeid() {
        return woeid;
    }

    @Override
    public String getCountryName() {
        return countryName;
    }

    @Override
    public String getCountryCode() {
        return countryCode;
    }

    @Override
    public String getPlaceName() {
        return placeName;
    }

    @Override
    public int getPlaceCode() {
        return placeCode;
    }

    @Override
    public String getName() {
        return name;
    }

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
