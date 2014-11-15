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
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.3
 */
class PagableResponseListImpl<T extends TwitterResponse> extends ResponseListImpl<T> implements PagableResponseList<T> {
    private static final long serialVersionUID = -8603601553967559275L;
    private final long previousCursor;
    private final long nextCursor;

    PagableResponseListImpl(RateLimitStatus rateLimitStatus, int accessLevel) {
        super(rateLimitStatus, accessLevel);
        previousCursor = 0;
        nextCursor = 0;
    }

    PagableResponseListImpl(int size, JSONObject json, HttpResponse res) {
        super(size, res);
        this.previousCursor = ParseUtil.getLong("previous_cursor", json);
        this.nextCursor = ParseUtil.getLong("next_cursor", json);
    }

    @Override
    public boolean hasPrevious() {
        return 0 != previousCursor;
    }

    @Override
    public long getPreviousCursor() {
        return previousCursor;
    }

    @Override
    public boolean hasNext() {
        return 0 != nextCursor;
    }

    @Override
    public long getNextCursor() {
        return nextCursor;
    }

}
