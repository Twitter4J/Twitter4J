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

import java.util.function.Consumer;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.0
 */
public interface TwitterBase {
    /**
     * Registers a RateLimitStatusListener for account associated rate limits
     *
     * @param listener the listener to be added
     * @see <a href="https://dev.twitter.com/docs/rate-limiting">Rate Limiting | Twitter Developers</a>
     * @since Twitter4J 2.1.12
     */
    void addRateLimitStatusListener(RateLimitStatusListener listener);

    /**
     * Registers a lambda action for account associated rate limits
     *
     * @param action the action to be added
     * @see <a href="https://dev.twitter.com/docs/rate-limiting">Rate Limiting | Twitter Developers</a>
     * @since Twitter4J 4.0.4
     */
    void onRateLimitStatus(Consumer<RateLimitStatusEvent> action);

    /**
     * Registers a RateLimitStatusListener for account associated rate limits
     *
     * @param action the action to be added
     * @see <a href="https://dev.twitter.com/docs/rate-limiting">Rate Limiting | Twitter Developers</a>
     * @since Twitter4J 4.0.4
     */
    @SuppressWarnings("unused")
    void onRateLimitReached(Consumer<RateLimitStatusEvent> action);

    /**
     * Returns the authorization scheme for this instance.<br>
     * The returned type will be either of BasicAuthorization, OAuthAuthorization, or NullAuthorization
     *
     * @return the authorization scheme for this instance
     */
    Authorization getAuthorization();

}
