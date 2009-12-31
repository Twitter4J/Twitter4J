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

/**
 * A data interface representing Twitter REST API's rate limit status
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see <a href="http://apiwiki.twitter.com/Rate-limiting">Twitter API Wiki / Rate limiting</a>
 */
public class RateLimitStatusEvent extends java.util.EventObject {

    private RateLimitStatus rateLimitStatus;

    private boolean isAccountRateLimitStatus;
    private static final long serialVersionUID = -2332507741769177298L;

    RateLimitStatusEvent(Object source, RateLimitStatus rateLimitStatus, boolean isAccountRateLimitStatus){
        super(source);
        this.rateLimitStatus = rateLimitStatus;
        this.isAccountRateLimitStatus = isAccountRateLimitStatus;
    }

    public RateLimitStatus getRateLimitStatus() {
        return rateLimitStatus;
    }

    public boolean isAccountRateLimitStatus() {
        return isAccountRateLimitStatus;
    }
    public boolean isIPRateLimitStatus() {
        return !isAccountRateLimitStatus;
    }

}
