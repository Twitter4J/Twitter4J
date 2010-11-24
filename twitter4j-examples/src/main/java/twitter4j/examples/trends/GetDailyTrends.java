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
package twitter4j.examples.trends;

import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Shows daily trends.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class GetDailyTrends {
    /**
     * Usage: java twitter4j.examples.trends.GetDailyTrends [yyyy-mm-dd]
     *
     * @param args message
     */
    public static void main(String[] args) {
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            List<Trends> trendsList;
            if (args.length > 0) {
                trendsList = twitter.getDailyTrends(new SimpleDateFormat("yyyy-MM-dd").parse(args[0]), false);

            } else {
                trendsList = twitter.getDailyTrends();
            }
            System.out.println("Showing daily trends");
            for (Trends trends : trendsList) {
                System.out.println("As of : " + trends.getAsOf());
                for (Trend trend : trends.getTrends()) {
                    System.out.println(" " + trend.getName());
                }
            }
            System.out.println("done.");
            System.exit(0);
        } catch (ParseException pe) {
            System.out.println("Usage: java twitter4j.examples.trends.GetDailyTrends [yyyy-mm-dd]");
            System.exit(-1);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get trends: " + te.getMessage());
            System.exit(-1);
        }
    }
}
