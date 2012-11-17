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

import twitter4j.AccountSettings;
import twitter4j.Location;
import twitter4j.TimeZone;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import static twitter4j.internal.json.z_T4JInternalParseUtil.getBoolean;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.9
 */
class AccountSettingsJSONImpl extends TwitterResponseImpl implements AccountSettings, java.io.Serializable {
    private static final long serialVersionUID = 7983363611306383416L;
    private final boolean SLEEP_TIME_ENABLED;
    private final String SLEEP_START_TIME;
    private final String SLEEP_END_TIME;
    private final Location[] TREND_LOCATION;
    private final boolean GEO_ENABLED;
    private final String LANGUAGE;
    private final TimeZone TIMEZONE;
    private final boolean ALWAYS_USE_HTTPS;
    private final boolean DISCOVERABLE_BY_EMAIL;

    private AccountSettingsJSONImpl(HttpResponse res, JSONObject json) throws TwitterException {
        super(res);
        try {
            JSONObject sleepTime = json.getJSONObject("sleep_time");
            SLEEP_TIME_ENABLED = getBoolean("enabled", sleepTime);
            SLEEP_START_TIME = sleepTime.getString("start_time");
            SLEEP_END_TIME = sleepTime.getString("end_time");
            if (json.isNull("trend_location")) {
                TREND_LOCATION = new Location[0];
            } else {
                JSONArray locations = json.getJSONArray("trend_location");
                TREND_LOCATION = new Location[locations.length()];
                for (int i = 0; i < locations.length(); i++) {
                    TREND_LOCATION[i] = new LocationJSONImpl(locations.getJSONObject(i));
                }
            }
            GEO_ENABLED = getBoolean("geo_enabled", json);
            LANGUAGE = json.getString("language");
            ALWAYS_USE_HTTPS = getBoolean("always_use_https", json);
            DISCOVERABLE_BY_EMAIL = getBoolean("discoverable_by_email", json);
            TIMEZONE = new TimeZoneJSONImpl(json.getJSONObject("time_zone"));
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    /*package*/ AccountSettingsJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        this(res, res.asJSONObject());
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(this, res.asJSONObject());
        }
    }

    /*package*/ AccountSettingsJSONImpl(JSONObject json) throws TwitterException {
        this(null, json);
    }

    @Override
    public boolean isSleepTimeEnabled() {
        return SLEEP_TIME_ENABLED;
    }

    @Override
    public String getSleepStartTime() {
        return SLEEP_START_TIME;
    }

    @Override
    public String getSleepEndTime() {
        return SLEEP_END_TIME;
    }

    @Override
    public Location[] getTrendLocations() {
        return TREND_LOCATION;
    }

    @Override
    public boolean isGeoEnabled() {
        return GEO_ENABLED;
    }

    @Override
    public boolean isDiscoverableByEmail() {
        return DISCOVERABLE_BY_EMAIL;
    }

    @Override
    public boolean isAlwaysUseHttps() {
        return ALWAYS_USE_HTTPS;
    }

    @Override
    public String getLanguage() {
        return LANGUAGE;
    }

    @Override
    public TimeZone getTimeZone() {
        return TIMEZONE;
    }
}
