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

package twitter4j;

import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.http.HttpResponseCode;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * An exception class that will be thrown when TwitterAPI calls are failed.<br>
 * In case the Twitter server returned HTTP error code, you can get the HTTP status code using getStatusCode() method.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class TwitterException extends Exception implements TwitterResponse, HttpResponseCode {
    private int statusCode = -1;
    private int retryAfter;
    private RateLimitStatus rateLimitStatus;
    private RateLimitStatus featureSpecificRateLimitStatus = null;
    private static final long serialVersionUID = -2623309261327598087L;
    private Map<String, List<String>> responseHeaderFields = null;
    private ExceptionDiagnosis exceptionDiagnosis = null;

    public TwitterException(String message) {
        super(decode(message));
        rateLimitStatus = null;
    }

    public TwitterException(Exception cause) {
        super(decode(cause.getMessage()), cause);
        if (cause instanceof TwitterException) {
            ((TwitterException) cause).setNested();
        }
        rateLimitStatus = null;
    }

    public TwitterException(String message, HttpResponse res) {
        super(getCause(res) + "\n" + decode(message));
        if (res.getStatusCode() == ENHANCE_YOUR_CLAIM) {
            // application exceeded the rate limitation
            // Search API returns Retry-After header that instructs the application when it is safe to continue.
            // @see <a href="http://apiwiki.twitter.com/Rate-limiting">Rate limiting</a>
            try {
                String retryAfterStr = res.getResponseHeader("Retry-After");
                if (null != retryAfterStr) {
                    this.retryAfter = Integer.valueOf(retryAfterStr);
                }
            } catch (NumberFormatException ignore) {
                this.retryAfter = -1;
            }
        }
        this.responseHeaderFields = res.getResponseHeaderFields();
        this.statusCode = res.getStatusCode();
        this.rateLimitStatus = RateLimitStatusJSONImpl.createFromResponseHeader(res);
        this.featureSpecificRateLimitStatus = RateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(res);
    }

    /**
     * @param message                        message
     * @param retryAfter                     retry-after
     * @param responseHeaderFields           response header fields
     * @param statusCode                     status code
     * @param rateLimitStatus                rate limit status
     * @param featureSpecificLateLimitStatus feature specific rate limit status
     * @since Twitter4J 2.1.9
     */
    public TwitterException(String message, int retryAfter
            , Map<String, List<String>> responseHeaderFields
            , int statusCode, RateLimitStatus rateLimitStatus
            , RateLimitStatus featureSpecificLateLimitStatus) {
        super(decode(message));
        this.retryAfter = retryAfter;
        this.responseHeaderFields = responseHeaderFields;
        this.statusCode = statusCode;
        this.rateLimitStatus = rateLimitStatus;
        this.featureSpecificRateLimitStatus = featureSpecificLateLimitStatus;
    }

    public TwitterException(String msg, Exception cause) {
        super(decode(msg), cause);
    }

    public TwitterException(String msg, Exception cause, int statusCode) {
        super(decode(msg), cause);
        this.statusCode = statusCode;

    }

    private static String decode(String str) {
        StringBuffer value = new StringBuffer(str.length());
        try {
            JSONObject json = new JSONObject(str);
            if (!json.isNull("error")) {
                value.append("error - ").append(json.getString("error"))
                        .append("\n");
            }
            if (!json.isNull("request")) {
                value.append("request - ").append(json.getString("request"))
                        .append("\n");
            }
        } catch (JSONException e) {
            value.append(str);
        }
        return value.toString();
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getResponseHeader(String name) {
        String value = null;
        if (null != responseHeaderFields) {
            List<String> header = responseHeaderFields.get(name);
            if (header.size() > 0) {
                value = header.get(0);
            }
        }
        return value;
    }

    /**
     * {@inheritDoc}
     *
     * @since Twitter4J 2.1.2
     */
    public RateLimitStatus getRateLimitStatus() {
        return rateLimitStatus;
    }

    /**
     * Returns the current feature-specific rate limit status if available.<br>
     * This method is available in conjunction with Twitter#searchUsers()<br>
     *
     * @return current rate limit status
     * @see <a href="http://apiwiki.twitter.com/Rate-limiting">Rate limiting</a>
     * @since Twitter4J 2.1.2
     */
    public RateLimitStatus getFeatureSpecificRateLimitStatus() {
        return featureSpecificRateLimitStatus;
    }

    /**
     * Returns int value of "Retry-After" response header (Search API) or seconds_until_reset (REST API).
     * An application that exceeds the rate limitations of the Search API will receive HTTP 420 response codes to requests. It is a best
     * practice to watch for this error condition and honor the Retry-After header that instructs the application when it is safe to
     * continue. The Retry-After header's value is the number of seconds your application should wait before submitting another query (for
     * example: Retry-After: 67).<br>
     * Check if getStatusCode() == 503 before calling this method to ensure that you are actually exceeding rate limitation with query
     * apis.<br>
     * Otherwise, you'll get an IllegalStateException if "Retry-After" response header was not included in the response.<br>
     *
     * @return instructs the application when it is safe to continue in seconds
     * @see <a href="http://apiwiki.twitter.com/Rate-limiting">Rate limiting</a>
     * @since Twitter4J 2.1.0
     */
    public int getRetryAfter() {
        int retryAfter = -1;
        if (this.statusCode == 400) {
            if (null != rateLimitStatus) {
                retryAfter = rateLimitStatus.getSecondsUntilReset();
            }
        } else if (this.statusCode == 420) {
            retryAfter = this.retryAfter;
        }
        if (retryAfter == -1) {
            throw new IllegalStateException("Rate limitation is not exceeded");
        }
        return retryAfter;
    }

    /**
     * Tests if the exception is caused by network issue
     *
     * @return if the exception is caused by network issue
     * @since Twitter4J 2.1.2
     */
    public boolean isCausedByNetworkIssue() {
        return getCause() instanceof java.io.IOException;
    }

    /**
     * Tests if the exception is caused by rate limitation exceed
     *
     * @return if the exception is caused by rate limitation exceed
     * @see <a href="http://apiwiki.twitter.com/Rate-limiting">Rate limiting</a>
     * @since Twitter4J 2.1.2
     */
    public boolean exceededRateLimitation() {
        return (statusCode == 400 && null != rateLimitStatus) // REST API
                || (statusCode == 420); // Search API
    }

    /**
     * Tests if the exception is caused by non-existing resource
     *
     * @return if the exception is caused by non-existing resource
     * @since Twitter4J 2.1.2
     */
    public boolean resourceNotFound() {
        return statusCode == 404;
    }

    private final static String[] FILTER = new String[]{"twitter4j"};

    /**
     * Returns a hexadecimal representation of this exception stacktrace.<br>
     * An exception code is a hexadecimal representation of the stacktrace which enables it easier to Google known issues.<br>
     * Format : XXXXXXXX:YYYYYYYY[ XX:YY]<br>
     * Where XX is a hash code of stacktrace without line number<br>
     * YY is a hash code of stacktrace excluding line number<br>
     * [-XX:YY] will appear when this instance a root cause
     *
     * @return a hexadecimal representation of this exception stacktrace
     */
    public String getExceptionCode() {
        return getExceptionDiagnosis().asHexString();
    }

    private ExceptionDiagnosis getExceptionDiagnosis() {
        if (null == exceptionDiagnosis) {
            exceptionDiagnosis = new ExceptionDiagnosis(this, FILTER);
        }
        return exceptionDiagnosis;
    }

    boolean nested = false;

    void setNested() {
        nested = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TwitterException that = (TwitterException) o;

        if (retryAfter != that.retryAfter) return false;
        if (statusCode != that.statusCode) return false;
        if (rateLimitStatus != null ? !rateLimitStatus.equals(that.rateLimitStatus) : that.rateLimitStatus != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = statusCode;
        result = 31 * result + retryAfter;
        result = 31 * result + (rateLimitStatus != null ? rateLimitStatus.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getMessage() + (nested ? "" : "Relevant discussions can be on the Internet at:\n"
                + "\thttp://www.google.co.jp/search?q=" + getExceptionDiagnosis().getStackLineHashAsHex()
                + " or\n\thttp://www.google.co.jp/search?q=" + getExceptionDiagnosis().getLineNumberHashAsHex())
                + "\nTwitterException{" + (nested ? "" : "exceptionCode=[" + getExceptionCode() + "], ") +
                "statusCode=" + statusCode +
                ", retryAfter=" + retryAfter +
                ", rateLimitStatus=" + rateLimitStatus +
                ", version=" + Version.getVersion() +
                '}';
    }

    private static String getCause(HttpResponse res) {
        int statusCode = res.getStatusCode();
        String cause;
        // http://apiwiki.twitter.com/HTTP-Response-Codes-and-Errors
        switch (statusCode) {
            case NOT_MODIFIED:
                cause = "There was no new data to return.";
                break;
            case BAD_REQUEST:
                cause = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
                break;
            case UNAUTHORIZED:
                cause = "Authentication credentials (http://dev.twitter.com/pages/auth) were missing or incorrect. Ensure that you have set valid conumer key/secret, access token/secret, and the system clock in in sync.";
                break;
            case FORBIDDEN:
                cause = "The request is understood, but it has been refused. An accompanying error message will explain why. This code is used when requests are being denied due to update limits (http://support.twitter.com/forums/10711/entries/15364).";
                break;
            case NOT_FOUND:
                cause = "The URI requested is invalid or the resource requested, such as a user, does not exist.";
                break;
            case NOT_ACCEPTABLE:
                cause = "Returned by the Search API when an invalid format is specified in the request.\n" +
                        "Returned by the Streaming API when one or more of the parameters are not suitable for the resource. The track parameter, for example, would throw this error if:\n" +
                        " The track keyword is too long or too short.\n" +
                        " The bounding box specified is invalid.\n" +
                        " No predicates defined for filtered resource, for example, neither track nor follow parameter defined.\n" +
                        " Follow userid cannot be read.";
                break;
            case TOO_LONG:
                cause = "A parameter list is too long. The track parameter, for example, would throw this error if:\n" +
                        " Too many track tokens specified for role; contact API team for increased access.\n" +
                        " Too many bounding boxes specified for role; contact API team for increased access.\n" +
                        " Too many follow userids specified for role; contact API team for increased access.";
                break;
            case ENHANCE_YOUR_CLAIM:
                cause = "Returned by the Search and Trends API when you are being rate limited (http://dev.twitter.com/pages/rate-limiting).\n"
                        + "Returned by the Streaming API:\n Too many login attempts in a short period of time.\n" +
                        " Running too many copies of the same application authenticating with the same account name.";
                break;
            case INTERNAL_SERVER_ERROR:
                cause = "Something is broken. Please post to the group (http://dev.twitter.com/pages/support) so the Twitter team can investigate.";
                break;
            case BAD_GATEWAY:
                cause = "Twitter is down or being upgraded.";
                break;
            case SERVICE_UNAVAILABLE:
                cause = "The Twitter servers are up, but overloaded with requests. Try again later.";
                break;
            default:
                cause = "";
        }
        return statusCode + ":" + cause;
    }
}
