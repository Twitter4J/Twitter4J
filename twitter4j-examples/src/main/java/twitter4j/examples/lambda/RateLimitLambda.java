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

package twitter4j.examples.lambda;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * example code to explain lambda expression for handling rate limits
 */
public class RateLimitLambda {
    public static void main(String... args) {
        Twitter twitter = TwitterFactory.getSingleton();
        twitter.onRateLimitStatus(e -> System.out.println("rate limit remaining: " + e.getRateLimitStatus().getRemaining()));
        for (int i = 0; i < 20; i++) {
            try {
                System.out.println(twitter.getHomeTimeline());
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }
    }
}
