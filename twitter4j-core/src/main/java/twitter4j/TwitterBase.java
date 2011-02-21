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
import twitter4j.http.Authorization;
import twitter4j.http.BasicAuthorization;
import twitter4j.http.NullAuthorization;
import twitter4j.http.OAuthAuthorization;

/**
 * Base class of Twitter / AsyncTwitter / TwitterStream supports Basic Authorization.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
abstract class TwitterBase implements java.io.Serializable {
    protected final Configuration conf;

    protected Authorization auth;
    private static final long serialVersionUID = -3812176145960812140L;

    /*package*/ TwitterBase(Configuration conf) {
        this.conf = conf;
        initBasicAuthorization(conf.getUser(), conf.getPassword());
    }

    /*package*/ TwitterBase(Configuration conf, String userId, String password) {
        this.conf = conf;
        initBasicAuthorization(userId, password);
    }

    private void initBasicAuthorization(String screenName, String password) {
        if (null != screenName && null != password) {
            auth = new BasicAuthorization(screenName, password);
        }
        if (null == auth) {
            auth = NullAuthorization.getInstance();
        }
    }

    /*package*/ TwitterBase(Configuration conf, Authorization auth) {
        this.conf = conf;
        this.auth = auth;
    }

    /**
     * tests if the instance is authenticated by Basic
     *
     * @return returns true if the instance is authenticated by Basic
     */
    public final boolean isBasicAuthEnabled() {
        return auth instanceof BasicAuthorization && auth.isEnabled();
    }

    protected final void ensureAuthorizationEnabled() {
        if (!auth.isEnabled()) {
            throw new IllegalStateException(
                    "Authentication credentials are missing. See http://twitter4j.org/configuration.html for the detail.");
        }
    }

    protected final void ensureOAuthEnabled() {
        if (!(auth instanceof OAuthAuthorization)) {
            throw new IllegalStateException(
                    "OAuth required. Authentication credentials are missing. See http://twitter4j.org/configuration.html for the detail.");
        }
    }

    /**
     * Returns the authorization scheme for this instance.<br>
     * The returned type will be either of BasicAuthorization, OAuthAuthorization, or NullAuthorization
     *
     * @return the authorization scheme for this instance
     */
    public final Authorization getAuthorization() {
        return auth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TwitterBase)) return false;

        TwitterBase that = (TwitterBase) o;

        if (!auth.equals(that.auth)) return false;

        return true;
    }

    /**
     * Returns the configuration associated with this instance
     *
     * @return configuration associated with this instance
     * @since Twitter4J 2.1.8
     */
    public Configuration getConfiguration() {
        return this.conf;
    }

    @Override
    public int hashCode() {
        return auth != null ? auth.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TwitterBase{" +
                "auth=" + auth +
                '}';
    }
}
