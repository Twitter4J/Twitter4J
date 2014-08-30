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

import twitter4j.conf.ConfigurationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.2
 */
public final class HttpClientFactory {
    private static final Constructor<?> HTTP_CLIENT_CONSTRUCTOR;
    private static final String HTTP_CLIENT_IMPLEMENTATION = "twitter4j.http.httpClient";

    static {
        Class<?> clazz = null;
        //-Dtwitter4j.http.httpClient=twitter4j.HttpClient
        String httpClientImpl = System.getProperty(HTTP_CLIENT_IMPLEMENTATION);
        if (httpClientImpl != null) {
            try {
                clazz = Class.forName(httpClientImpl);
            } catch (ClassNotFoundException ignore) {
            }
        }
        if (null == clazz) {
            try {
                clazz = Class.forName("twitter4j.AlternativeHttpClientImpl");
            } catch (ClassNotFoundException ignore) {
            }
        }
        if (null == clazz) {
            try {
                clazz = Class.forName("twitter4j.HttpClientImpl");
            } catch (ClassNotFoundException cnfe) {
                throw new AssertionError(cnfe);
            }
        }
        try {
            HTTP_CLIENT_CONSTRUCTOR = clazz.getConstructor(HttpClientConfiguration.class);
        } catch (NoSuchMethodException nsme) {
            throw new AssertionError(nsme);
        }
    }

    private final static HashMap<HttpClientConfiguration, HttpClient> confClientMap = new HashMap<HttpClientConfiguration, HttpClient>();

    public static HttpClient getInstance() {
        return getInstance(ConfigurationContext.getInstance().getHttpClientConfiguration());
    }

    public static HttpClient getInstance(HttpClientConfiguration conf) {
        HttpClient client = confClientMap.get(conf);
        try {
            if (client == null) {
                client = (HttpClient) HTTP_CLIENT_CONSTRUCTOR.newInstance(conf);
                confClientMap.put(conf, client);
            }
        } catch (InstantiationException e) {
            throw new AssertionError(e);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new AssertionError(e);
        }
        return client;
    }
}
