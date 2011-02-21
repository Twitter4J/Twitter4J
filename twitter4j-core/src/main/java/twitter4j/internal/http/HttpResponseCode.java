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

package twitter4j.internal.http;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.2
 */
public interface HttpResponseCode {
    int OK = 200;// OK: Success!
    int MULTIPLE_CHOICES = 300;//
    int FOUND = 302;//
    int NOT_MODIFIED = 304;// Not Modified: There was no new data to return.
    int BAD_REQUEST = 400;// Bad Request: The request was invalid. An accompanying error message will explain why. This is the status code will be returned during rate limiting.
    int UNAUTHORIZED = 401;// Not Authorized: Authentication credentials were missing or incorrect.
    int FORBIDDEN = 403;// Forbidden: The request is understood, but it has been refused.  An accompanying error message will explain why.
    int NOT_FOUND = 404;// Not Found: The URI requested is invalid or the resource requested, such as a user, does not exists.
    int NOT_ACCEPTABLE = 406;// Not Acceptable: Returned by the Search API when an invalid format is specified in the request.
    int TOO_LONG = 413;// Not Acceptable: Returned by the Search API when an invalid format is specified in the request.
    /**
     * @see <a href="http://groups.google.com/group/twitter-api-announce/browse_thread/thread/3f3b0fd38deb9b0f?hl=en">Search API: new HTTP response code 420 for rate limiting starting 1/18/2010</a>
     */
    int ENHANCE_YOUR_CLAIM = 420;// Enhance Your Calm: Returned by the Search and Trends API  when you are being rate limited. Not registered in RFC.
    int INTERNAL_SERVER_ERROR = 500;// Internal Server Error: Something is broken. Please post to the group so the Twitter team can investigate.
    int BAD_GATEWAY = 502;// Bad Gateway: Twitter is down or being upgraded.
    int SERVICE_UNAVAILABLE = 503;// Service Unavailable: The Twitter servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.
}
