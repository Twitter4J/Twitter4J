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
 * Super interface of Twitter Response data interfaces which indicates that rate limit status is available.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see twitter4j.DirectMessage
 * @see twitter4j.Status
 * @see twitter4j.User
 */
/*package*/ abstract class TwitterResponseImpl implements TwitterResponse, java.io.Serializable {

    private static final long serialVersionUID = 7422171124869859808L;
    private transient RateLimitStatus rateLimitStatus = null;
    private final transient int accessLevel;

    public TwitterResponseImpl() {
        accessLevel = NONE;
    }

    public TwitterResponseImpl(HttpResponse res) {
        this.rateLimitStatus = RateLimitStatusJSONImpl.createFromResponseHeader(res);
        accessLevel = ParseUtil.toAccessLevel(res);
    }

    @Override
    public RateLimitStatus getRateLimitStatus() {
        return rateLimitStatus;
    }

    @Override
    public int getAccessLevel() {
        return accessLevel;
    }
}
