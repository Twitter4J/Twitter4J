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

import twitter4j.internal.http.HttpResponse;

import java.util.ArrayList;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.3
 */
class ResponseListImpl<T> extends ArrayList<T> implements ResponseList<T> {
    private transient RateLimitStatus rateLimitStatus = null;
    private transient RateLimitStatus featureSpecificRateLimitStatus = null;
    private transient int accessLevel;
    private static final long serialVersionUID = 5646617841989265312L;

    ResponseListImpl(int size, HttpResponse res) {
        super(size);
        this.rateLimitStatus = RateLimitStatusJSONImpl.createFromResponseHeader(res);
        this.featureSpecificRateLimitStatus = RateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(res);
        accessLevel = TwitterResponseImpl.toAccessLevel(res);
    }

    /**
     * {@inheritDoc}
     */
    public RateLimitStatus getRateLimitStatus() {
        return rateLimitStatus;
    }

    /**
     * {@inheritDoc}
     */
    public RateLimitStatus getFeatureSpecificRateLimitStatus() {
        return featureSpecificRateLimitStatus;
    }

    /**
     * {@inheritDoc}
     */
    public int getAccessLevel() {
        return accessLevel;
    }
}
