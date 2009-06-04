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

import java.util.Properties;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class Configuration {
    private static Properties defaultProperty = new Properties();
    static{
        String defaultPropertiesPath = "/META-INF/twitter4j.properties";
        try {
            defaultProperty.load(Configuration.class.getResourceAsStream(defaultPropertiesPath));
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Error while reading "+ defaultPropertiesPath + ".");
        }
        defaultProperty.setProperty("twitter4j.clientVersion", Version.getVersion());
    }

    public static String getCilentVersion(){
        return getProperty("twitter4j.clientVersion");
    }
    public static String getCilentVersion(String clientVersion){
        return getProperty("twitter4j.clientVersion", clientVersion);
    }
    public static String getSource(){
        return getProperty("twitter4j.source");
    }
    public static String getSource(String source){
        return getProperty("twitter4j.source", source);
    }
    public static String getProxyHost(){
        return getProperty("twitter4j.http.proxyHost");
    }
    public static String getProxyHost(String proxyHost){
        return getProperty("twitter4j.http.proxyHost", proxyHost);
    }
    public static String getProxyUser(){
        return getProperty("twitter4j.http.proxyUser");
    }
    public static String getProxyUser(String user){
        return getProperty("twitter4j.http.proxyUser", user);
    }
    public static String getClientURL(){
        return getProperty("twitter4j.clientURL");
    }
    public static String getClientURL(String clientURL){
        return getProperty("twitter4j.clientURL", clientURL);
    }

    public static String getProxyPassword(){
        return getProperty("twitter4j.http.proxyPassword");
    }
    public static String getProxyPassword(String password){
        return getProperty("twitter4j.http.proxyPassword", password);
    }
    public static int getProxyPort(){
        return getIntProperty("twitter4j.http.proxyPort");
    }
    public static int getProxyPort(int port){
        return getIntProperty("twitter4j.http.proxyPort", port);
    }
    public static int getConnectionTimeout(){
        return getIntProperty("twitter4j.http.connectionTimeout");
    }
    public static int getConnectionTimeout(int connectionTimeout){
        return getIntProperty("twitter4j.http.connectionTimeout", connectionTimeout);
    }
    public static int getReadTimeout(){
        return getIntProperty("twitter4j.http.readTimeout");
    }
    public static int getReadTimeout(int readTimeout){
        return getIntProperty("twitter4j.http.readTimeout", readTimeout);
    }
    public static int getRetryCount(){
        return getIntProperty("twitter4j.http.retryCount");
    }
    public static int getRetryCount(int retryCount){
        return getIntProperty("twitter4j.http.retryCount", retryCount);
    }
    public static int getRetryIntervalSecs(){
        return getIntProperty("twitter4j.http.retryIntervalSecs");
    }
    public static int getRetryIntervalSecs(int retryIntervalSecs){
        return getIntProperty("twitter4j.http.retryIntervalSecs", retryIntervalSecs);
    }

    public static String getUser() {
        return getProperty("twitter4j.user");
    }
    public static String getUser(String userId) {
        return getProperty("twitter4j.user", userId);
    }

    public static String getPassword() {
        return getProperty("twitter4j.password");
    }
    public static String getPassword(String password) {
        return getProperty("twitter4j.password", password);
    }

    public static String getUserAgent() {
        return getProperty("twitter4j.http.userAgent");
    }
    public static String getUserAgent(String userAgent) {
        return getProperty("twitter4j.http.userAgent", userAgent);
    }

    public static String getOAuthConsumerKey() {
        return getProperty("twitter4j.oauth.consumerKey");
    }
    public static String getOAuthConsumerKey(String consumerKey) {
        return getProperty("twitter4j.oauth.consumerKey", consumerKey);
    }

    public static String getOAuthConsumerSecret() {
        return getProperty("twitter4j.oauth.consumerSecret");
    }
    public static String getOAuthConsumerSecret(String consumerSecret) {
        return getProperty("twitter4j.oauth.consumerSecret", consumerSecret);
    }

    public static boolean getBoolean(String name) {
        String value = getProperty(name);
        return Boolean.valueOf(value);
    }

    public static int getIntProperty(String name) {
        String value = getProperty(name);
        try{
            return Integer.parseInt(value);
        }catch(NumberFormatException nfe){
            return -1;
        }
    }
    public static int getIntProperty(String name, int fallbackValue) {
        String value = getProperty(name, String.valueOf(fallbackValue));
        try{
            return Integer.parseInt(value);
        }catch(NumberFormatException nfe){
            return -1;
        }
    }
    public static long getLongProperty(String name) {
        String value = getProperty(name);
        try{
            return Long.parseLong(value);
        }catch(NumberFormatException nfe){
            return -1;
        }
    }

    public static String getProperty(String name) {
        return getProperty(name, null);
    }
    public static String getProperty(String name, String fallbackValue) {
        String value = System.getProperty(name, fallbackValue);
        if(null == value){
            value = defaultProperty.getProperty(name);
        }
        if(null == value){
            String fallback = defaultProperty.getProperty(name + ".fallback");
            if(null != fallback){
                value = System.getProperty(fallback);
            }
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

    public static int getNumberOfAsyncThreads() {
        return getIntProperty("twitter4j.async.numThreads");
    }

    public static boolean getDebug() {
        return getBoolean("twitter4j.debug");

    }
}
