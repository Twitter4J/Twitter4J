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

import javax.annotation.Generated;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.9
 */
@Generated(
        value = "generate-lazy-objects.sh",
        comments = "This is Tool Generated Code. DO NOT EDIT",
        date = "2011-07-13"
)
final class LazyAccountSettings implements twitter4j.AccountSettings {
    private final HttpResponse res;
    private final ObjectFactory factory;
    private AccountSettings target = null;

    LazyAccountSettings(HttpResponse res, ObjectFactory factory) {
        this.res = res;
        this.factory = factory;
    }

    private AccountSettings getTarget() {
        if (target == null) {
            try {
                target = factory.createAccountSettings(res);
            } catch (TwitterException e) {
                throw new TwitterRuntimeException(e);
            }
        }
        return target;
    }

    /**
     * Returns true if the user enabled sleep time.
     *
     * @return true if the user enabled sleep time
     */
    public boolean isSleepTimeEnabled() {
        return getTarget().isSleepTimeEnabled();
    }


    /**
     * Returns sleep start time.
     *
     * @return sleep start time
     */
    public String getSleepStartTime() {
        return getTarget().getSleepStartTime();
    }


    /**
     * Returns sleep end time.
     *
     * @return sleep end time
     */
    public String getSleepEndTime() {
        return getTarget().getSleepEndTime();
    }


    /**
     * Return the user's trend locations
     *
     * @return the user's trend locations
     */
    public Location[] getTrendLocations() {
        return getTarget().getTrendLocations();
    }


    /**
     * Return true if the user is enabling geo location
     *
     * @return true if the user is enabling geo location
     */
    public boolean isGeoEnabled() {
        return getTarget().isGeoEnabled();
    }


    /**
     * Returns the timezone configured for this user.
     *
     * @return the timezone (formated as a Rails TimeZone name)
     */
    public TimeZone getTimeZone() {
        return getTarget().getTimeZone();
    }


    /**
     * Returns the language used to render Twitter's UII for this user.
     *
     * @return the language ISO 639-1 representation
     */
    public String getLanguage() {
        return getTarget().getLanguage();
    }


    /**
     * Returns true if the user is discoverable by email.
     *
     * @return true if the user is discoverable by email
     */
    public boolean isDiscoverableByEmail() {
        return getTarget().isDiscoverableByEmail();
    }


    /**
     * Returns true if the wants to always access twitter using HTTPS.
     *
     * @return true if the wants to always access twitter using HTTPS
     */
    public boolean isAlwaysUseHttps() {
        return getTarget().isAlwaysUseHttps();
    }

    /**
     * Returns the user's screen name
     *
     * @return the user's screen name
     */
    @Override
    public String getScreenName() {
        return getTarget().getScreenName();
    }

    public RateLimitStatus getRateLimitStatus() {
        return getTarget().getRateLimitStatus();
    }

    public int getAccessLevel() {
        return getTarget().getAccessLevel();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountSettings)) return false;
        return getTarget().equals(o);
    }

    @Override
    public int hashCode() {
        return getTarget().hashCode();
    }

    @Override
    public String toString() {
        return "LazyAccountSettings{" +
                "target=" + getTarget() +
                "}";
    }
}
