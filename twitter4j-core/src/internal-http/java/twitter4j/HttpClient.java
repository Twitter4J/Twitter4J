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

import twitter4j.auth.Authorization;

import java.util.Map;

/**
 * A utility class to handle HTTP request/response.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public interface HttpClient {

    void addDefaultRequestHeader(String name, String value);

    Map<String, String> getRequestHeaders();

    HttpResponse request(HttpRequest req) throws TwitterException;

    HttpResponse request(HttpRequest req, HttpResponseListener listener) throws TwitterException;

    HttpResponse get(String url, HttpParameter[] parameters
            , Authorization authorization, HttpResponseListener listener) throws TwitterException;

    HttpResponse get(String url) throws TwitterException;

    HttpResponse post(String url, HttpParameter[] parameters
            , Authorization authorization, HttpResponseListener listener) throws TwitterException;

    HttpResponse post(String url) throws TwitterException;

    HttpResponse delete(String url, HttpParameter[] parameters
            , Authorization authorization, HttpResponseListener listener) throws TwitterException;

    HttpResponse delete(String url) throws TwitterException;

    HttpResponse head(String url) throws TwitterException;

    HttpResponse put(String url, HttpParameter[] parameters
            , Authorization authorization, HttpResponseListener listener) throws TwitterException;

    HttpResponse put(String url) throws TwitterException;
}
