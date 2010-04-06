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
package twitter4j;

import twitter4j.internal.http.HttpResponse;

/**
 * An exception class that will be thrown when TwitterAPI calls are failed.<br>
 * In case the Twitter server returned HTTP error code, you can get the HTTP status code using getStatusCode() method.
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class TwitterException extends Exception implements TwitterResponse {
    private int statusCode = -1;
    private int retryAfter;
    private RateLimitStatus rateLimitStatus;
    private static final long serialVersionUID = -2623309261327598087L;

    public TwitterException(String msg) {
        super(msg);
        rateLimitStatus = null;
    }

    public TwitterException(Exception cause) {
        super(cause);
        rateLimitStatus = null;
    }

    public TwitterException(String msg, HttpResponse res) {
        super(msg);
        this.statusCode = res.getStatusCode();
        this.rateLimitStatus = RateLimitStatusJSONImpl.createFromResponseHeader(res);
    }

    public TwitterException(String msg, int retryAfter, HttpResponse res) {
        this(msg,res);
        this.retryAfter = retryAfter;
    }

    public TwitterException(String msg, Exception cause) {
        super(msg, cause);
    }

    public TwitterException(String msg, Exception cause, int statusCode) {
        super(msg, cause);
        this.statusCode = statusCode;

    }

    public int getStatusCode() {
        return this.statusCode;
    }

    /**
     * {@inheritDoc}
     * @since Twitter4J 2.1.2
     */
    public RateLimitStatus getRateLimitStatus(){
        return rateLimitStatus;
    }

    /**
     * Returns int value of "Retry-After" response header.
     * An application that exceeds the rate limitations of the Search API will receive HTTP 503 response codes to requests.<br>
     * It is a best practice to watch for this error condition and honor the Retry-After header that instructs the application when it is safe to continue. The Retry-After header's value is the number of seconds your application should wait before submitting another query.<br>
     *  (for example: Retry-After: 67).<br>
     * Check if getStatusCode() == 503 before calling this method to ensure that you are actually exceeding rate limitation with query apis.<br>
     * Otherwise, you'll get an IllegalStateException if "Retry-After" response header was not included in the response.<br>
     * @return instructs the application when it is safe to continue in seconds
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Rate-limiting">Rate limiting</a>
     */
    public int getRetryAfter() {
        if(this.statusCode != 503 && this.statusCode != 420){
            throw new IllegalStateException("Rate limitation is not exceeded");
        }
        return retryAfter;
    }
}
