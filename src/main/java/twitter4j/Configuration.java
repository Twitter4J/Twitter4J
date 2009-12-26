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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.AccessControlException;
import java.util.Properties;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class Configuration {
    public static final String DEBUG = "twitter4j.debug";
    public static final String SOURCE = "twitter4j.source";
    public static final String CLIENT_URL = "twitter4j.clientURL";
    public static final String HTTP_USER_AGENT = "twitter4j.http.userAgent";
    public static final String USER = "twitter4j.user";
    public static final String PASSWORD = "twitter4j.password";
    public static final String HTTP_USE_SSL = "twitter4j.http.useSSL";
    public static final String HTTP_PROXY_HOST = "twitter4j.http.proxyHost";
    public static final String HTTP_PROXY_HOST_FALLBACK = "twitter4j.http.proxyHost.fallback";
    public static final String HTTP_PROXY_USER = "twitter4j.http.proxyUser";
    public static final String HTTP_PROXY_PASSWORD = "twitter4j.http.proxyPassword";
    public static final String HTTP_PROXY_PORT = "twitter4j.http.proxyPort";
    public static final String HTTP_PROXY_PORT_FALLBACK = "twitter4j.http.proxyPort.fallback";
    public static final String HTTP_CONNECTION_TIMEOUT = "twitter4j.http.connectionTimeout";
    public static final String HTTP_READ_TIMEOUT = "twitter4j.http.readTimeout";
    public static final String HTTP_RETRY_COUNT = "twitter4j.http.retryCount";
    public static final String HTTP_RETRY_INTERVAL_SECS = "twitter4j.http.retryIntervalSecs";
    public static final String OAUTH_CONSUMER_KEY = "twitter4j.oauth.consumerKey";
    public static final String OAUTH_CONSUMER_SECRET = "twitter4j.oauth.consumerSecret";
    public static final String ASYNC_NUM_THREADS = "twitter4j.async.numThreads";
    public static final String CLIENT_VERSION = "twitter4j.clientVersion";
    public static final String DALVIK = "twitter4j.dalvik";

    private static Properties defaultProperty = new Properties();

    static {
        init();
    }

    /*package*/ static void init() {
        defaultProperty.setProperty(DEBUG, "false");
        defaultProperty.setProperty(SOURCE, "Twitter4J");
        defaultProperty.setProperty(CLIENT_URL, "http://yusuke.homeip.net/twitter4j/en/twitter4j-{twitter4j.clientVersion}.xml");
        defaultProperty.setProperty(HTTP_USER_AGENT, "twitter4j http://yusuke.homeip.net/twitter4j/ /{twitter4j.clientVersion}");
        //defaultProperty.setProperty(USER,"");
        //defaultProperty.setProperty(PASSWORD,"");
        defaultProperty.setProperty(HTTP_USE_SSL, "false");
        //defaultProperty.setProperty(HTTP_PROXY_HOST,"");
        defaultProperty.setProperty(HTTP_PROXY_HOST_FALLBACK, "http.proxyHost");
        //defaultProperty.setProperty(HTTP_PROXY_USER,"");
        //defaultProperty.setProperty(HTTP_PROXY_PASSWORD,"");
        //defaultProperty.setProperty(HTTP_PROXY_PORT,"");
        defaultProperty.setProperty(HTTP_PROXY_PORT_FALLBACK, "http.proxyPort");
        defaultProperty.setProperty(HTTP_CONNECTION_TIMEOUT, "20000");
        defaultProperty.setProperty(HTTP_READ_TIMEOUT, "120000");
        defaultProperty.setProperty(HTTP_RETRY_COUNT, "3");
        defaultProperty.setProperty(HTTP_RETRY_INTERVAL_SECS, "10");
        //defaultProperty.setProperty(OAUTH_CONSUMER_KEY,"");
        //defaultProperty.setProperty(OAUTH_CONSUMER_SECRET,"");
        defaultProperty.setProperty(ASYNC_NUM_THREADS, "1");
        defaultProperty.setProperty(CLIENT_VERSION, Version.getVersion());
        try{
            // Android platform should have dalvik.system.VMRuntime in the classpath.
            // @see http://developer.android.com/reference/dalvik/system/VMRuntime.html
            Class.forName("dalvik.system.VMRuntime");
            defaultProperty.setProperty(DALVIK, "true");
        }catch(ClassNotFoundException cnfe){
            defaultProperty.setProperty(DALVIK, "false");
        }
        IS_DALVIK = getBoolean(DALVIK);

        final String TWITTER4J_PROPERTIES = "twitter4j.properties";
        boolean loaded = loadProperties(defaultProperty, "." + File.separatorChar + TWITTER4J_PROPERTIES) ||
                loadProperties(defaultProperty, Configuration.class.getResourceAsStream("/WEB-INF/" + TWITTER4J_PROPERTIES)) ||
                loadProperties(defaultProperty, Configuration.class.getResourceAsStream("/" + TWITTER4J_PROPERTIES));
    }

    private static boolean loadProperties(Properties props, String path) {
        try {
            File file = new File(path);
            if(file.exists() && file.isFile()){
                props.load(new FileInputStream(file));
                return true;
            }
        } catch (Exception ignore) {
        }
        return false;
    }

    private static boolean loadProperties(Properties props, InputStream is) {
        try {
            props.load(is);
            return true;
        } catch (Exception ignore) {
        }
        return false;
    }

    private static boolean IS_DALVIK;


    public static boolean isDalvik() {
        return IS_DALVIK;
    }

    public static boolean useSSL() {
        return getBoolean(HTTP_USE_SSL);
    }
    public static String getScheme(){
        return useSSL() ? "https://" : "http://";
    }

    public static String getCilentVersion(){
        return getProperty(CLIENT_VERSION);
    }
    public static String getCilentVersion(String clientVersion){
        return getProperty(CLIENT_VERSION, clientVersion);
    }
    public static String getSource(){
        return getProperty(SOURCE);
    }
    public static String getSource(String source){
        return getProperty(SOURCE, source);
    }
    public static String getProxyHost(){
        return getProperty(HTTP_PROXY_HOST);
    }
    public static String getProxyHost(String proxyHost){
        return getProperty(HTTP_PROXY_HOST, proxyHost);
    }
    public static String getProxyUser(){
        return getProperty(HTTP_PROXY_USER);
    }
    public static String getProxyUser(String user){
        return getProperty(HTTP_PROXY_USER, user);
    }
    public static String getClientURL(){
        return getProperty(CLIENT_URL);
    }
    public static String getClientURL(String clientURL){
        return getProperty(CLIENT_URL, clientURL);
    }

    public static String getProxyPassword(){
        return getProperty(HTTP_PROXY_PASSWORD);
    }
    public static String getProxyPassword(String password){
        return getProperty(HTTP_PROXY_PASSWORD, password);
    }
    public static int getProxyPort(){
        return getIntProperty(HTTP_PROXY_PORT);
    }
    public static int getProxyPort(int port){
        return getIntProperty(HTTP_PROXY_PORT, port);
    }
    public static int getConnectionTimeout(){
        return getIntProperty(HTTP_CONNECTION_TIMEOUT);
    }
    public static int getConnectionTimeout(int connectionTimeout){
        return getIntProperty(HTTP_CONNECTION_TIMEOUT, connectionTimeout);
    }
    public static int getReadTimeout(){
        return getIntProperty(HTTP_READ_TIMEOUT);
    }
    public static int getReadTimeout(int readTimeout){
        return getIntProperty(HTTP_READ_TIMEOUT, readTimeout);
    }
    public static int getRetryCount(){
        return getIntProperty(HTTP_RETRY_COUNT);
    }
    public static int getRetryCount(int retryCount){
        return getIntProperty(HTTP_RETRY_COUNT, retryCount);
    }
    public static int getRetryIntervalSecs(){
        return getIntProperty(HTTP_RETRY_INTERVAL_SECS);
    }
    public static int getRetryIntervalSecs(int retryIntervalSecs){
        return getIntProperty(HTTP_RETRY_INTERVAL_SECS, retryIntervalSecs);
    }

    public static String getUser() {
        return getProperty(USER);
    }
    public static String getUser(String userId) {
        return getProperty(USER, userId);
    }

    public static String getPassword() {
        return getProperty(PASSWORD);
    }
    public static String getPassword(String password) {
        return getProperty(PASSWORD, password);
    }

    public static String getUserAgent() {
        return getProperty(HTTP_USER_AGENT);
    }
    public static String getUserAgent(String userAgent) {
        return getProperty(HTTP_USER_AGENT, userAgent);
    }

    public static String getOAuthConsumerKey() {
        return getProperty(OAUTH_CONSUMER_KEY);
    }
    public static String getOAuthConsumerKey(String consumerKey) {
        return getProperty(OAUTH_CONSUMER_KEY, consumerKey);
    }

    public static String getOAuthConsumerSecret() {
        return getProperty(OAUTH_CONSUMER_SECRET);
    }
    public static String getOAuthConsumerSecret(String consumerSecret) {
        return getProperty(OAUTH_CONSUMER_SECRET, consumerSecret);
    }

    public static int getNumberOfAsyncThreads() {
        return getIntProperty(ASYNC_NUM_THREADS);
    }

    public static boolean getDebug() {
        return getBoolean(DEBUG);

    }

    private static boolean getBoolean(String name) {
        String value = getProperty(name);
        return Boolean.valueOf(value);
    }

    private static int getIntProperty(String name) {
        String value = getProperty(name);
        try{
            return Integer.parseInt(value);
        }catch(NumberFormatException nfe){
            return -1;
        }
    }
    private static int getIntProperty(String name, int fallbackValue) {
        String value = getProperty(name, String.valueOf(fallbackValue));
        try{
            return Integer.parseInt(value);
        }catch(NumberFormatException nfe){
            return -1;
        }
    }
    private static long getLongProperty(String name) {
        String value = getProperty(name);
        try{
            return Long.parseLong(value);
        }catch(NumberFormatException nfe){
            return -1;
        }
    }

    private static String getProperty(String name) {
        return getProperty(name, null);
    }
    private static String getProperty(String name, String fallbackValue) {
        String value;
        try {
            value = System.getProperty(name, fallbackValue);
            if (null == value) {
                value = defaultProperty.getProperty(name);
            }
            if (null == value) {
                String fallback = defaultProperty.getProperty(name + ".fallback");
                if (null != fallback) {
                    value = System.getProperty(fallback);
                }
            }
        } catch (AccessControlException ace) {
            // Unsigned applet cannot access System properties
            value = fallbackValue;
        }
        return replace(value);
    }
    private static String replace(String value){
        if(null == value){
            return value;
        }
        String newValue = value;
        int openBrace = 0;
        if(-1 != (openBrace = value.indexOf("{", openBrace))){
            int closeBrace = value.indexOf("}", openBrace);
            if(closeBrace > (openBrace+1)){
                String name = value.substring(openBrace + 1, closeBrace);
                if(name.length() > 0){
                    newValue = value.substring(0,openBrace) + getProperty(name)
                            + value.substring(closeBrace + 1);
                    
                }
            }
        }
        if (newValue.equals(value)) {
            return value;
        } else {
            return replace(newValue);
        }
    }
}
