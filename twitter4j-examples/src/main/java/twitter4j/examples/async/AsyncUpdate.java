/*
Copyright (c) 2007-2010, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package twitter4j.examples.async;

import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.Status;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterMethod;

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
     *
     * @param args String[] TwitterID TwitterPassword StatusString
     * @throws InterruptedException
     */

    static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 1) {
            System.out.println("Usage: java twitter4j.examples.AsyncUpdate text");
            System.exit(-1);
        }
        AsyncTwitterFactory factory = new AsyncTwitterFactory(new TwitterAdapter() {
            @Override
            public void updatedStatus(Status status) {
                System.out.println("Successfully updated the status to [" +
                        status.getText() + "].");
                synchronized (lock) {
                    lock.notify();
                }
            }

            @Override
            public void onException(TwitterException e, TwitterMethod method) {
                if (method == UPDATE_STATUS) {
                    e.printStackTrace();
                    synchronized (lock) {
                        lock.notify();
                    }
                } else {
                    synchronized (lock) {
                        lock.notify();
                    }
                    throw new AssertionError("Should not happen");
                }
            }
        });
        AsyncTwitter twitter = factory.getInstance();
        twitter.updateStatus(args[0]);
        synchronized (lock) {
            lock.wait();
        }
    }

}
