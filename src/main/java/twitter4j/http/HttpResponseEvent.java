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
package twitter4j.http;

/**
 * @author Andrew Hedges - andrew.hedges at gmail.com
 */
public class HttpResponseEvent {

    private HttpRequest request;

    private HttpClient source;

    private HttpResponse response;

    /* package */

    HttpResponseEvent(HttpClient source, HttpRequest request, HttpResponse response) {
        this.source = source;
        this.request = request;
        this.response = response;
    }

    /**
     * returns the request associated with the event
     * @return the request associated with the event
     */
    public HttpRequest getRequest() {
        return request;
    }

    /**
     * returns the response associated with the event
     * @return the response associated with the event
     */
    public HttpResponse getResponse() {
        return response;
    }

    public boolean isAuthenticated() {
        return request.authorization.isEnabled();
    }

    public HttpClient getSource() {
        return source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HttpResponseEvent that = (HttpResponseEvent) o;

        if (request != null ? !request.equals(that.request) : that.request != null)
            return false;
        if (response != null ? !response.equals(that.response) : that.response != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = request != null ? request.hashCode() : 0;
        result = 31 * result + (response != null ? response.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HttpResponseEvent{" +
                "request=" + request +
                ", response=" + response +
                '}';
    }
}
