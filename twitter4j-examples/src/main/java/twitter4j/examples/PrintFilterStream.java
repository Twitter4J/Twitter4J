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
 * <p>This is a code example of Twitter4J Streaming API - filter method support.<br>
 * Usage: java twitter4j.examples.PrintFilterStream [<i>TwitterScreenName</i> <i>TwitterPassword</i>] follow(comma separated) track(comma separated)]<br>
 * </p>
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class PrintFilterStream extends StatusAdapter {
    /**
     * Main entry of this application.
     * @param args [ScreenName Password] follow(comma separated user ids) track(comma separated filter terms)z
     */
    public static void main(String[] args)throws TwitterException {
        PrintFilterStream printFilterStream = new PrintFilterStream(args);
        printFilterStream.startConsuming();
    }

    TwitterStream twitterStream;
    int[] filterArray;
    String[] trackArray;

    private PrintFilterStream(String[] args) {
        String filter;
        String track;
        try {
            twitterStream = new TwitterStreamFactory(this).getInstance();
            if (args.length < 2) {
                printUsageAndExit();
            }
            filter = args[0];
            track = args[1];
        } catch (IllegalStateException is) {
            // screen name / password combination is not in twitter4j.properties
            if (args.length < 4) {
                printUsageAndExit();
            }
            twitterStream = new TwitterStreamFactory().getInstance(args[0], args[1]);
            filter = args[2];
            track = args[3];
        }
        String[] filterSplit = filter.split(",");
        filterArray = new int[filterSplit.length];
        for(int i=0; i< filterSplit.length; i++){
            filterArray[i] = Integer.parseInt(filterSplit[i]);

        }
        trackArray = track.split(",");
    }

    private void printUsageAndExit() {
            System.out.println(
                    "Usage: java twitter4j.examples.PrintFilterStream [ScreenName Password] follow(comma separated user ids) track(comma separated filter terms)");
            System.exit(-1);
    }

    private void startConsuming() throws TwitterException {
        // filter() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
        twitterStream.setStatusListener(this);
        twitterStream.filter(0, filterArray, trackArray);
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
