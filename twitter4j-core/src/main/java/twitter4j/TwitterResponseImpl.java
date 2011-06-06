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


/**
 * Super interface of Twitter Response data interfaces which indicates that rate limit status is available.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see twitter4j.DirectMessage
 * @see twitter4j.Status
 * @see twitter4j.User
 */
/*package*/ abstract class TwitterResponseImpl implements TwitterResponse, java.io.Serializable {

    private transient RateLimitStatus rateLimitStatus = null;
    private static final long serialVersionUID = -7284708239736552059L;
    private transient int accessLevel;

    public TwitterResponseImpl() {
        accessLevel = NONE;
    }

    public TwitterResponseImpl(HttpResponse res) {
        this.rateLimitStatus = RateLimitStatusJSONImpl.createFromResponseHeader(res);
        accessLevel = toAccessLevel(res);

    }

    /* package */
    static int toAccessLevel(HttpResponse res) {
        if (null == res) {
            return -1;
        }
        String xAccessLevel = res.getResponseHeader("X-Access-Level");
        int accessLevel;
        if (null == xAccessLevel) {
            accessLevel = NONE;
        } else {
            // https://dev.twitter.com/pages/application-permission-model-faq#how-do-we-know-what-the-access-level-of-a-user-token-is
            switch (xAccessLevel.length()) {
                // “read” (Read-only)
                case 4:
                    accessLevel = READ;
                    break;
                case 10:
                    // “read-write” (Read & Write)
                    accessLevel = READ_WRITE;
                    break;
                case 25:
                    // “read-write-directmessages” (Read, Write, & Direct Message)
                    accessLevel = READ_WRITE_DIRECTMESSAGES;
                    break;
                case 26:
                    // “read-write-privatemessages” (Read, Write, & Direct Message)
                    accessLevel = READ_WRITE_DIRECTMESSAGES;
                    break;
                default:
                    accessLevel = NONE;
                    // unknown access level;
            }
        }
        return accessLevel;
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
    public int getAccessLevel() {
        return accessLevel;
    }
}
