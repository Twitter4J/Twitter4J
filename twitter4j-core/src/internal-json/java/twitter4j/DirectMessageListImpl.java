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
 * @author Hiroaki TAKEUCHI - takke30 at gmail.com
 * @since Twitter4J 4.0.7
 */
class DirectMessageListImpl extends ResponseListImpl<DirectMessage> implements DirectMessageList {
    private static final long serialVersionUID = 8150060768287194508L;
    private final String nextCursor;

    DirectMessageListImpl(RateLimitStatus rateLimitStatus, int accessLevel) {
        super(rateLimitStatus, accessLevel);
        nextCursor = null;
    }

    DirectMessageListImpl(int size, JSONObject json, HttpResponse res) {
        super(size, res);
        this.nextCursor = ParseUtil.getRawString("next_cursor", json);
    }
    DirectMessageListImpl(int size, HttpResponse res) {
        super(size, res);
        this.nextCursor = null;
    }

    @Override
    public String getNextCursor() {
        return nextCursor;
    }

}
