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

import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

/**
 * example code to explain lambda expression. Prints tweets containing twitter4j or #twitter4j.
 */
public class TwitterStreamLambda {
    public static void main(String... args) {
        // Twitter4j 4.0.4+
        TwitterStreamFactory.getSingleton()
                .onStatus(e -> System.out.println(String.format("@%s %s", e.getUser().getScreenName(), e.getText())))
                .onException(e -> e.printStackTrace())
                .filter("twitter4j", "#twitter4j");

    }

    public static void oldTraditionalDullBoringImplementation(String... dummy){
        // Twitter4J 4.0.3 or earlier
        TwitterStream stream = TwitterStreamFactory.getSingleton();
        stream.addListener(new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                String.format("@%s %s", status.getUser().getScreenName(), status.getText());
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        });
        stream.filter(new FilterQuery(new String[]{"twitter4j", "#twitter4j"}));
    }
}
