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
package twitter4j.internal.http;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.2
 */
public interface HttpResponseCode {
    int OK = 200;// OK: Success!
    int MULTIPLE_CHOICES = 300;// 
    int NOT_MODIFIED = 304;// Not Modified: There was no new data to return.
    int BAD_REQUEST = 400;// Bad Request: The request was invalid. An accompanying error message will explain why. This is the status code will be returned during rate limiting.
    int UNAUTHORIZED = 401;// Not Authorized: Authentication credentials were missing or incorrect.
    int FORBIDDEN = 403;// Forbidden: The request is understood, but it has been refused.  An accompanying error message will explain why.
    int NOT_FOUND = 404;// Not Found: The URI requested is invalid or the resource requested, such as a user, does not exists.
    int NOT_ACCEPTABLE = 406;// Not Acceptable: Returned by the Search API when an invalid format is specified in the request.
    /**
     * @see <a href="http://groups.google.com/group/twitter-api-announce/browse_thread/thread/3f3b0fd38deb9b0f?hl=en">Search API: new HTTP response code 420 for rate limiting starting 1/18/2010</a>
     */
    int ENHANCE_YOUR_CLAIM = 420;// Enhance Your Calm: Returned by the Search and Trends API  when you are being rate limited. Not registered in RFC.
    int INTERNAL_SERVER_ERROR = 500;// Internal Server Error: Something is broken. Please post to the group so the Twitter team can investigate.
    int BAD_GATEWAY = 502;// Bad Gateway: Twitter is down or being upgraded.
    int SERVICE_UNAVAILABLE = 503;// Service Unavailable: The Twitter servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.
}
