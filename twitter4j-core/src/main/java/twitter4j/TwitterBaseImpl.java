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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static twitter4j.HttpResponseCode.*;

/**
 * Base class of Twitter / AsyncTwitter / TwitterStream supports OAuth.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
@SuppressWarnings("rawtypes")
abstract class TwitterBaseImpl implements java.io.Serializable, HttpResponseListener {
    private static final String WWW_DETAILS = "See https://twitter4j.org/en/configuration.html for details. See and register at https://apps.twitter.com/";
    private static final long serialVersionUID = -7824361938865528554L;

    Configuration conf;

    transient HttpClient http;

    ObjectFactory factory;

    Authorization auth;

    /*package*/ TwitterBaseImpl(Configuration conf) {
        this.conf = conf;
        this.auth = conf.getAuthorization();
        http = HttpClient.getInstance(conf.getHttpClientConfiguration());
        setFactory();
    }

    void setFactory() {
        factory = new JSONImplFactory(conf);
    }


    @Override
    public void httpResponseReceived(HttpResponseEvent event) {
        if (conf.rateLimitStatusListeners.size() != 0) {
            HttpResponse res = event.getResponse();
            TwitterException te = event.getTwitterException();
            RateLimitStatus rateLimitStatus;
            int statusCode;
            if (te != null) {
                rateLimitStatus = te.getRateLimitStatus();
                statusCode = te.getStatusCode();
            } else {
                rateLimitStatus = JSONImplFactory.createRateLimitStatusFromResponseHeader(res);
                statusCode = res.getStatusCode();
            }
            if (rateLimitStatus != null) {
                RateLimitStatusEvent statusEvent
                        = new RateLimitStatusEvent(this, rateLimitStatus, event.isAuthenticated());
                if (statusCode == ENHANCE_YOUR_CLAIM
                        || statusCode == SERVICE_UNAVAILABLE
                        || statusCode == TOO_MANY_REQUESTS) {
                    // EXCEEDED_RATE_LIMIT_QUOTA is returned by Rest API
                    // SERVICE_UNAVAILABLE is returned by Search API
                    //noinspection unchecked
                    for (Consumer<RateLimitStatusEvent> listener : (List<Consumer<RateLimitStatusEvent>>)conf.rateLimitStatusListeners) {
                        listener.accept(statusEvent);
                    }
                    //noinspection unchecked
                    for (Consumer<RateLimitStatusEvent> listener : (List<Consumer<RateLimitStatusEvent>>)conf.rateLimitReachedListeners) {
                        listener.accept(statusEvent);
                    }
                } else {
                    //noinspection unchecked
                    for (Consumer<RateLimitStatusEvent> listener : (List<Consumer<RateLimitStatusEvent>>)conf.rateLimitStatusListeners) {
                        listener.accept(statusEvent);
                    }
                }
            }
        }
    }

    final void ensureAuthorizationEnabled() {
        if (!auth.isEnabled()) {
            throw new IllegalStateException(
                    "Authentication credentials are missing. " + WWW_DETAILS);
        }
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        // http://docs.oracle.com/javase/6/docs/platform/serialization/spec/output.html#861
        out.putFields();
        out.writeFields();

        out.writeObject(conf);
        out.writeObject(auth);
    }

    private void readObject(ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        // http://docs.oracle.com/javase/6/docs/platform/serialization/spec/input.html#2971
        stream.readFields();

        conf = (Configuration) stream.readObject();
        auth = (Authorization) stream.readObject();
        http = HttpClient.getInstance(conf.getHttpClientConfiguration());
        setFactory();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TwitterBaseImpl that = (TwitterBaseImpl) o;
        return Objects.equals(conf, that.conf) && Objects.equals(http, that.http) && Objects.equals(factory, that.factory) && Objects.equals(auth, that.auth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conf, http, factory, auth);
    }

    @Override
    public String toString() {
        return "TwitterBaseImpl{" +
                "conf=" + conf +
                ", http=" + http +
                ", factory=" + factory +
                ", auth=" + auth +
                '}';
    }
}
