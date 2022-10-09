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

package examples.lambda;

import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * example code to explain lambda expression for handling rate limits
 */
public class RateLimitLambda {
    /**
     * example code to explain lambda expression for handling rate limits
     *
     * @param args not used
     */
    public static void main(String... args) {
        var timelines = Twitter.newBuilder()
                .onRateLimitStatus(e -> System.out.println("rate limit remaining: " + e.getRateLimitStatus().getRemaining()))
                .build().v1().timelines();
        for (int i = 0; i < 20; i++) {
            try {
                System.out.println(timelines.getHomeTimeline());
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }
    }
}
