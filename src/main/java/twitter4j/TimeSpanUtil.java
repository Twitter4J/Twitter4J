/*
Copyright (c) 2007-2009, Yusuke Yamamoto
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
package twitter4j;

import java.util.Date;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 * @since Twitter4J 2.1.0
 */
public class TimeSpanUtil {
    private static int ONE_HOUR_IN_SECONDS = 60 * 60;
    private static int ONE_DAY_IN_SECONDS = 24 * ONE_HOUR_IN_SECONDS;

    private TimeSpanUtil() {
        throw new AssertionError("not intended to be instantiated.");
    }

    public static String toTimeSpanString(Date date) {
        return toTimeSpanString(date.getTime());
    }

    public static String toTimeSpanString(long milliseconds) {
        int deltaInSeconds = (int) ((System.currentTimeMillis() - milliseconds) / 1000);
        return toTimeSpanString(deltaInSeconds);
    }


    public static String toTimeSpanString(int deltaInSeconds) {
        if (deltaInSeconds < 5) {
            return "less than 5 seconds ago";
        } else if (deltaInSeconds < 10) {
            return "less than 10 seconds ago";
        } else if (deltaInSeconds < 20) {
            return "less than 20 seconds ago";
        } else if (deltaInSeconds < 40) {
            return "half a minute ago";
        } else if (deltaInSeconds < 60) {
            return "less than a minute ago";
        }

        if (deltaInSeconds < 45 * 60) {
            int minutes = deltaInSeconds / 60;
            if (minutes == 1) {
                return "1 minute ago";
            }
            return minutes + " minutes ago";
        }

        if (deltaInSeconds < 105 * 60) {// between 0:45 and 1:45 => 1
            return "about an hour ago";
        }
        if (deltaInSeconds < ONE_DAY_IN_SECONDS) {
            int hours = ((deltaInSeconds + 15 * 60) / ONE_HOUR_IN_SECONDS);
            if (hours < 24) {
                return "about " + hours + " hours ago";
            }
        }
        if (deltaInSeconds < 48 * 60 * 60) {
            return "1 day ago";
        }

        int days = deltaInSeconds / ONE_DAY_IN_SECONDS;
        return "" + days + " days ago";
    }
}
