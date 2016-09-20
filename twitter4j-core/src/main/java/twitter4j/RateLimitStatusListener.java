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
 * @author Andrew Hedges - andrew.hedges at gmail.com
 */
public interface RateLimitStatusListener {

    /**
     * Called when the response contains rate limit status.
     *
     * @param event rate limit status event.
     */
    public void onRateLimitStatus(RateLimitStatusEvent event);

    /**
     * Called when the account or IP address is hitting the rate limit.<br>
     * onRateLimitStatus will be also called before this event.
     *
     * @param event rate limit status event.
     */
    public void onRateLimitReached(RateLimitStatusEvent event);
}
