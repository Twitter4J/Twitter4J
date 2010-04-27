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
package twitter4j.examples;

import twitter4j.*;

/**
 * <p>This is a code example of Twitter4J Streaming API - sample method support.<br>
 * Usage: java twitter4j.examples.PrintSampleStream [<i>TwitterScreenName</i> <i>TwitterPassword</i>]<br>
 * </p>
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class PrintSampleStream extends StatusAdapter {
    /**
     * Main entry of this application.
     * @param args String[] TwitterID TwitterPassword
     */
    public static void main(String[] args)throws TwitterException {
        PrintSampleStream printSampleStream = new PrintSampleStream(args);
        printSampleStream.startConsuming();
    }

    TwitterStream twitterStream;

    PrintSampleStream(String[] args) {
        try {
            twitterStream = new TwitterStreamFactory(this).getInstance();
        } catch (IllegalStateException is) {
            // screen name / password combination is not in twitter4j.properties
            if (args.length < 2) {
                System.out.println(
                        "Usage: java twitter4j.examples.PrintSampleStream [ScreenName Password]");
                System.exit(-1);
            }
            twitterStream = new TwitterStreamFactory().getInstance(args[0], args[1]);
        }
    }
    private void startConsuming() throws TwitterException {
        // sample() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
        twitterStream.setStatusListener(this);
        twitterStream.sample();
    }

    public void onStatus(Status status) {
        System.out.println(status.getUser().getName() + " : " + status.getText());
    }

    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
    }

    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
    }

    public void onException(Exception ex) {
        ex.printStackTrace();
    }
}
