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

import java.util.List;

import static twitter4j.ParseUtil.getInt;

/**
 * An exception class that will be thrown when TwitterAPI calls are failed.<br>
 * In case the Twitter server returned HTTP error code, you can get the HTTP status code using getStatusCode() method.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class TwitterException extends Exception implements TwitterResponse, HttpResponseCode {
    private static final long serialVersionUID = 6006561839051121336L;
    private int statusCode = -1;
    private int errorCode = -1;
    private ExceptionDiagnosis exceptionDiagnosis = null;
    private HttpResponse response;
    private String errorMessage = null;

    public TwitterException(String message, Throwable cause) {
        super(message, cause);
        decode(message);
    }

    public TwitterException(String message) {
        this(message, (Throwable) null);
    }


    public TwitterException(Exception cause) {
        this(cause.getMessage(), cause);
        if (cause instanceof TwitterException) {
            ((TwitterException) cause).setNested();
        }
    }

    public TwitterException(String message, HttpResponse res) {
        this(message);
        response = res;
        this.statusCode = res.getStatusCode();
    }

    public TwitterException(String message, Exception cause, int statusCode) {
        this(message, cause);
        this.statusCode = statusCode;
    }

    @Override
    public String getMessage() {
        StringBuilder value = new StringBuilder();
        if (errorMessage != null && errorCode != -1) {
            value.append("message - ").append(errorMessage)
                    .append("\n");
            value.append("code - ").append(errorCode)
                    .append("\n");
        } else {
            value.append(super.getMessage());
        }
        if (statusCode != -1) {
            return getCause(statusCode) + "\n" + value.toString();
        } else {
            return value.toString();
        }
    }

    private void decode(String str) {
        if (str != null && str.startsWith("{")) {
            try {
                JSONObject json = new JSONObject(str);
                if (!json.isNull("errors")) {
                    JSONObject error = json.getJSONArray("errors").getJSONObject(0);
                    this.errorMessage = error.getString("message");
                    this.errorCode = getInt("code", error);
                }
            } catch (JSONException ignore) {
            }
        }
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getResponseHeader(String name) {
        String value = null;
        if (response != null) {
            List<String> header = response.getResponseHeaderFields().get(name);
            if (header.size() > 0) {
                value = header.get(0);
            }
        }
        return value;
    }

    @Override
    public RateLimitStatus getRateLimitStatus() {
        if (null == response) {
            return null;
        }
        return JSONImplFactory.createRateLimitStatusFromResponseHeader(response);
    }

    @Override
    public int getAccessLevel() {
        return ParseUtil.toAccessLevel(response);
    }

    /**
     * Returns int value of "Retry-After" response header (Search API) or seconds_until_reset (REST API).
     * An application that exceeds the rate limitations of the Search API will receive HTTP 420 response codes to requests. It is a best
     * practice to watch for this error condition and honor the Retry-After header that instructs the application when it is safe to
     * continue. The Retry-After header's value is the number of seconds your application should wait before submitting another query (for
     * example: Retry-After: 67).<br>
     * Check if getStatusCode() == 503 before calling this method to ensure that you are actually exceeding rate limitation with query
     * apis.<br>
     *
     * @return instructs the application when it is safe to continue in seconds
     * @see <a href="https://dev.twitter.com/docs/rate-limiting">Rate Limiting | Twitter Developers</a>
     * @since Twitter4J 2.1.0
     */
    public int getRetryAfter() {
        int retryAfter = -1;
        if (this.statusCode == 400) {
            RateLimitStatus rateLimitStatus = getRateLimitStatus();
            if (rateLimitStatus != null) {
                retryAfter = rateLimitStatus.getSecondsUntilReset();
            }
        } else if (this.statusCode == ENHANCE_YOUR_CLAIM) {
            try {
                String retryAfterStr = response.getResponseHeader("Retry-After");
                if (retryAfterStr != null) {
                    retryAfter = Integer.valueOf(retryAfterStr);
                }
            } catch (NumberFormatException ignore) {
            }
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
     * @see <a href="https://dev.twitter.com/docs/rate-limiting">Rate Limiting | Twitter Developers</a>
     * @since Twitter4J 2.1.2
     */
    public boolean exceededRateLimitation() {
        return (statusCode == 400 && getRateLimitStatus() != null) // REST API
                || (statusCode == ENHANCE_YOUR_CLAIM) // Streaming API
                || (statusCode == TOO_MANY_REQUESTS); // API 1.1
    }

    /**
     * Tests if the exception is caused by non-existing resource
     *
     * @return if the exception is caused by non-existing resource
     * @since Twitter4J 2.1.2
     */
    public boolean resourceNotFound() {
        return statusCode == NOT_FOUND;
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

    private boolean nested = false;

    void setNested() {
        nested = true;
    }

    /**
     * Returns error message from the API if available.
     *
     * @return error message from the API
     * @since Twitter4J 2.2.3
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Tests if error message from the API is available
     *
     * @return true if error message from the API is available
     * @since Twitter4J 2.2.3
     */
    public boolean isErrorMessageAvailable() {
        return errorMessage != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TwitterException that = (TwitterException) o;

        if (errorCode != that.errorCode) return false;
        if (nested != that.nested) return false;
        if (statusCode != that.statusCode) return false;
        if (errorMessage != null ? !errorMessage.equals(that.errorMessage) : that.errorMessage != null) return false;
        if (exceptionDiagnosis != null ? !exceptionDiagnosis.equals(that.exceptionDiagnosis) : that.exceptionDiagnosis != null)
            return false;
        if (response != null ? !response.equals(that.response) : that.response != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = statusCode;
        result = 31 * result + errorCode;
        result = 31 * result + (exceptionDiagnosis != null ? exceptionDiagnosis.hashCode() : 0);
        result = 31 * result + (response != null ? response.hashCode() : 0);
        result = 31 * result + (errorMessage != null ? errorMessage.hashCode() : 0);
        result = 31 * result + (nested ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return getMessage() + (nested ? "" : "\nRelevant discussions can be found on the Internet at:\n"
                + "\thttp://www.google.co.jp/search?q=" + getExceptionDiagnosis().getStackLineHashAsHex()
                + " or\n\thttp://www.google.co.jp/search?q=" + getExceptionDiagnosis().getLineNumberHashAsHex())
                + "\nTwitterException{" + (nested ? "" : "exceptionCode=[" + getExceptionCode() + "], ") +
                "statusCode=" + statusCode +
                ", message=" + errorMessage +
                ", code=" + errorCode +
                ", retryAfter=" + getRetryAfter() +
                ", rateLimitStatus=" + getRateLimitStatus() +
                ", version=" + Version.getVersion() +
                '}';
    }

    private static String getCause(int statusCode) {
        String cause;
        // https://dev.twitter.com/docs/error-codes-responses
        switch (statusCode) {
            case NOT_MODIFIED:
                cause = "There was no new data to return.";
                break;
            case BAD_REQUEST:
                cause = "The request was invalid. An accompanying error message will explain why. This is the status code will be returned during version 1.0 rate limiting(https://dev.twitter.com/pages/rate-limiting). In API v1.1, a request without authentication is considered invalid and you will get this response.";
                break;
            case UNAUTHORIZED:
                cause = "Authentication credentials (https://dev.twitter.com/pages/auth) were missing or incorrect. Ensure that you have set valid consumer key/secret, access token/secret, and the system clock is in sync.";
                break;
            case FORBIDDEN:
                cause = "The request is understood, but it has been refused. An accompanying error message will explain why. This code is used when requests are being denied due to update limits (https://support.twitter.com/articles/15364-about-twitter-limits-update-api-dm-and-following).";
                break;
            case NOT_FOUND:
                cause = "The URI requested is invalid or the resource requested, such as a user, does not exists. Also returned when the requested format is not supported by the requested method.";
                break;
            case NOT_ACCEPTABLE:
                cause = "Returned by the Search API when an invalid format is specified in the request.\n" +
                        "Returned by the Streaming API when one or more of the parameters are not suitable for the resource. The track parameter, for example, would throw this error if:\n" +
                        " The track keyword is too long or too short.\n" +
                        " The bounding box specified is invalid.\n" +
                        " No predicates defined for filtered resource, for example, neither track nor follow parameter defined.\n" +
                        " Follow userid cannot be read.";
                break;
            case ENHANCE_YOUR_CLAIM:
                cause = "Returned by the Search and Trends API when you are being rate limited (https://dev.twitter.com/docs/rate-limiting).\n"
                        + "Returned by the Streaming API:\n Too many login attempts in a short period of time.\n" +
                        " Running too many copies of the same application authenticating with the same account name.";
                break;
            case UNPROCESSABLE_ENTITY:
                cause = "Returned when an image uploaded to POST account/update_profile_banner(https://dev.twitter.com/docs/api/1/post/account/update_profile_banner) is unable to be processed.";
                break;
            case TOO_MANY_REQUESTS:
                cause = "Returned in API v1.1 when a request cannot be served due to the application's rate limit having been exhausted for the resource. See Rate Limiting in API v1.1.(https://dev.twitter.com/docs/rate-limiting/1.1)";
                break;
            case INTERNAL_SERVER_ERROR:
                cause = "Something is broken. Please post to the group (https://dev.twitter.com/docs/support) so the Twitter team can investigate.";
                break;
            case BAD_GATEWAY:
                cause = "Twitter is down or being upgraded.";
                break;
            case SERVICE_UNAVAILABLE:
                cause = "The Twitter servers are up, but overloaded with requests. Try again later.";
                break;
            case GATEWAY_TIMEOUT:
                cause = "The Twitter servers are up, but the request couldn't be serviced due to some failure within our stack. Try again later.";
                break;
            default:
                cause = "";
        }
        return statusCode + ":" + cause;
    }
}
