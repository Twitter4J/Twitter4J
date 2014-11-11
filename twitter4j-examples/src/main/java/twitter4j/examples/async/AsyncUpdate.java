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

package twitter4j.examples.async;

import twitter4j.*;

import static twitter4j.TwitterMethod.UPDATE_STATUS;

/**
 * <p>This is a code example of Twitter4J async API.<br>
 * Usage: java twitter4j.examples.AsyncUpdate <i>TwitterID</i> <i>TwitterPassword</i> <i>text</i><br>
 * </p>
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class AsyncUpdate {
    /**
     * Main entry for this application.
     */
    private static final Object LOCK = new Object();

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 1) {
            System.out.println("Usage: java twitter4j.examples.AsyncUpdate text");
            System.exit(-1);
        }
        AsyncTwitterFactory factory = new AsyncTwitterFactory();
        AsyncTwitter twitter = factory.getInstance();
        twitter.addListener(new TwitterAdapter() {
            @Override
            public void updatedStatus(Status status) {
                System.out.println("Successfully updated the status to [" +
                        status.getText() + "].");
                synchronized (LOCK) {
                    LOCK.notify();
                }
            }

            @Override
            public void onException(TwitterException e, TwitterMethod method) {
                if (method == UPDATE_STATUS) {
                    e.printStackTrace();
                    synchronized (LOCK) {
                        LOCK.notify();
                    }
                } else {
                    synchronized (LOCK) {
                        LOCK.notify();
                    }
                    throw new AssertionError("Should not happen");
                }
            }
        });
        twitter.updateStatus(args[0]);
        synchronized (LOCK) {
            LOCK.wait();
        }
    }

}
