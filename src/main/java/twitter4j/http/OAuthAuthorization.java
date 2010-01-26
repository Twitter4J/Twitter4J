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

import twitter4j.conf.Configuration;
import twitter4j.TwitterException;
import twitter4j.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see <a href="http://oauth.net/core/1.0a/">OAuth Core 1.0a</a>
 */
public final class OAuthAuthorization implements Authorization, java.io.Serializable, OAuthSupport {
    private final Configuration conf;
    private transient static HttpClientWrapper http;

    private static final String HMAC_SHA1 = "HmacSHA1";
    private static final HttpParameter OAUTH_SIGNATURE_METHOD = new HttpParameter("oauth_signature_method", "HMAC-SHA1");
    private static final Logger logger = Logger.getLogger();
    static final long serialVersionUID = -4368426677157998618L;
    private String consumerKey = "";
    private String consumerSecret;

    private OAuthToken oauthToken = null;

    // constructors

    public OAuthAuthorization(Configuration conf, String consumerKey, String consumerSecret) {
        this.conf = conf;
        init(consumerKey, consumerSecret);
    }

    public OAuthAuthorization(Configuration conf, String consumerKey, String consumerSecret, AccessToken accessToken) {
        this.conf = conf;
        init(consumerKey, consumerSecret, accessToken);
    }

    private void init(String consumerKey, String consumerSecret){
        http = new HttpClientWrapper(conf);
        setConsumerKey(consumerKey);
        setConsumerSecret(consumerSecret);
    }

    private void init(String consumerKey, String consumerSecret, AccessToken accessToken){
        init(consumerKey, consumerSecret);
        setOAuthAccessToken(accessToken);
    }

    // implementations for Authorization
    public void setAuthorizationHeader(String method, String url, HttpParameter[] params, HttpURLConnection con) {
        String authorization = generateAuthorizationHeader(method, url, params, oauthToken);
        logger.debug("Authorization: " + authorization);
        con.addRequestProperty("Authorization", authorization);
    }

    private void ensureTokenIsAvailable() {
        if (null == oauthToken) {
            throw new IllegalStateException("No Token available.");
        }
    }

    public boolean isEnabled() {
        return null != oauthToken && oauthToken instanceof AccessToken;
    }

    // implementation for OAuthSupport interface
    /**
     * {@inheritDoc}
     */
    public RequestToken getOAuthRequestToken() throws TwitterException {
        return getOAuthRequestToken(null);
    }

    /**
     * {@inheritDoc}
     */
    public RequestToken getOAuthRequestToken(String callbackURL) throws TwitterException {
        HttpParameter[] params = null != callbackURL ? new HttpParameter[]{new HttpParameter("oauth_callback", callbackURL)} : new HttpParameter[0];
        oauthToken = new RequestToken(http.post(conf.getOAuthRequestTokenURL(), params, this), this);
        return (RequestToken) oauthToken;
    }

    /**
     * {@inheritDoc}
     */
    public AccessToken getOAuthAccessToken() throws TwitterException {
        ensureTokenIsAvailable();
        try {
            oauthToken = new AccessToken(http.post(conf.getOAuthAccessTokenURL(), this));
            return (AccessToken) oauthToken;
        } catch (TwitterException te) {
            throw new TwitterException("The user has not given access to the account.", te, te.getStatusCode());
        }
    }

    /**
     * {@inheritDoc}
     */
    public AccessToken getOAuthAccessToken(String oauthVerifier) throws TwitterException {
        ensureTokenIsAvailable();
        try {
            oauthToken = new AccessToken(http.post(conf.getOAuthAccessTokenURL()
                    , new HttpParameter[]{new HttpParameter("oauth_verifier", oauthVerifier)}, this));
            return (AccessToken) oauthToken;
        } catch (TwitterException te) {
            throw new TwitterException("The user has not given access to the account.", te, te.getStatusCode());
        }
    }

    /**
     * {@inheritDoc}
     */
    public AccessToken getOAuthAccessToken(RequestToken requestToken) throws TwitterException {
        this.oauthToken = requestToken;
        return getOAuthAccessToken();
    }

    /**
     * {@inheritDoc}
     */
    public AccessToken getOAuthAccessToken(RequestToken requestToken, String oauthVerifier) throws TwitterException {
        this.oauthToken = requestToken;
        return getOAuthAccessToken(oauthVerifier);
    }

    /**
     * {@inheritDoc}
     */
    public void setOAuthAccessToken(AccessToken accessToken) {
        this.oauthToken = accessToken;
    }

    /*package*/

    String generateAuthorizationHeader(String method, String url, HttpParameter[] params, String nonce, String timestamp, OAuthToken otoken) {
        if (null == params) {
            params = new HttpParameter[0];
        }
        List<HttpParameter> oauthHeaderParams = new ArrayList<HttpParameter>(5);
        oauthHeaderParams.add(new HttpParameter("oauth_consumer_key", consumerKey));
        oauthHeaderParams.add(OAUTH_SIGNATURE_METHOD);
        oauthHeaderParams.add(new HttpParameter("oauth_timestamp", timestamp));
        oauthHeaderParams.add(new HttpParameter("oauth_nonce", nonce));
        oauthHeaderParams.add(new HttpParameter("oauth_version", "1.0"));
        if (null != otoken) {
            oauthHeaderParams.add(new HttpParameter("oauth_token", otoken.getToken()));
        }
        List<HttpParameter> signatureBaseParams = new ArrayList<HttpParameter>(oauthHeaderParams.size() + params.length);
        signatureBaseParams.addAll(oauthHeaderParams);
        if (!HttpParameter.containsFile(params)) {
            signatureBaseParams.addAll(toParamList(params));
        }
        parseGetParameters(url, signatureBaseParams);
        StringBuffer base = new StringBuffer(method).append("&")
                .append(encode(constructRequestURL(url))).append("&");
        base.append(encode(normalizeRequestParameters(signatureBaseParams)));
        String oauthBaseString = base.toString();
        logger.debug("OAuth base string: ", oauthBaseString);
        String signature = generateSignature(oauthBaseString, otoken);
        logger.debug("OAuth signature: ", signature);

        oauthHeaderParams.add(new HttpParameter("oauth_signature", signature));
        return "OAuth " + encodeParameters(oauthHeaderParams, ",", true);
    }

    private void parseGetParameters(String url, List<HttpParameter> signatureBaseParams) {
        int queryStart = url.indexOf("?");
        if (-1 != queryStart) {
            String[] queryStrs = url.substring(queryStart + 1).split("&");
            try {
                for (String query : queryStrs) {
                    String[] split = query.split("=");
                    if (split.length == 2) {
                        signatureBaseParams.add(
                                new HttpParameter(URLDecoder.decode(split[0],
                                        "UTF-8"), URLDecoder.decode(split[1],
                                        "UTF-8")));
                    } else {
                        signatureBaseParams.add(
                                new HttpParameter(URLDecoder.decode(split[0],
                                        "UTF-8"), ""));
                    }
                }
            } catch (UnsupportedEncodingException ignore) {
            }

        }

    }

    private static Random RAND = new Random();

    /**
     * @return generated authorization header
     * @see <a href="http://oauth.net/core/1.0a/#rfc.section.5.4.1">OAuth Core - 5.4.1.  Authorization Header</a>
     */
    /*package*/ String generateAuthorizationHeader(String method, String url, HttpParameter[] params, OAuthToken token) {
        long timestamp = System.currentTimeMillis() / 1000;
        long nonce = timestamp + RAND.nextInt();
        return generateAuthorizationHeader(method, url, params, String.valueOf(nonce), String.valueOf(timestamp), token);
    }


    /**
     * Computes RFC 2104-compliant HMAC signature.
     *
     * @param data the data to be signed
     * @param token the token
     * @return signature
     * @see <a href="http://oauth.net/core/1.0a/#rfc.section.9.2.1">OAuth Core - 9.2.1.  Generating Signature</a>
     */
    /*package*/ String generateSignature(String data, OAuthToken token) {
        byte[] byteHMAC = null;
        try {
            Mac mac = Mac.getInstance(HMAC_SHA1);
            SecretKeySpec spec;
            if (null == token) {
                String oauthSignature = encode(consumerSecret) + "&";
                spec = new SecretKeySpec(oauthSignature.getBytes(), HMAC_SHA1);
            } else {
                spec = token.getSecretKeySpec();
                if (null == spec) {
                    String oauthSignature = encode(consumerSecret) + "&" + encode(token.getTokenSecret());
                    spec = new SecretKeySpec(oauthSignature.getBytes(), HMAC_SHA1);
                    token.setSecretKeySpec(spec);
                }
            }
            mac.init(spec);
            byteHMAC = mac.doFinal(data.getBytes());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException ignore) {
            // should never happen
        }
        return BASE64Encoder.encode(byteHMAC);
    }

    /*package*/

    String generateSignature(String data) {
        return generateSignature(data, null);
    }


    /**
     * The request parameters are collected, sorted and concatenated into a normalized string:<br>
     * •	Parameters in the OAuth HTTP Authorization header excluding the realm parameter.<br>
     * •	Parameters in the HTTP POST request body (with a content-type of application/x-www-form-urlencoded).<br>
     * •	HTTP GET parameters added to the URLs in the query part (as defined by [RFC3986] section 3).<br>
     * <br>
     * The oauth_signature parameter MUST be excluded.<br>
     * The parameters are normalized into a single string as follows:<br>
     * 1.	Parameters are sorted by name, using lexicographical byte value ordering. If two or more parameters share the same name, they are sorted by their value. For example:<br>
     * 2.	                    a=1, c=hi%20there, f=25, f=50, f=a, z=p, z=t<br>
     * 3.	<br>
     * 4.	Parameters are concatenated in their sorted order into a single string. For each parameter, the name is separated from the corresponding value by an ‘=’ character (ASCII code 61), even if the value is empty. Each name-value pair is separated by an ‘&’ character (ASCII code 38). For example:<br>
     * 5.	                    a=1&c=hi%20there&f=25&f=50&f=a&z=p&z=t<br>
     * 6.	<br>
     *
     * @param params parameters to be normalized and concatenated
     * @return nomarized and concatenated parameters
     * @see <a href="http://oauth.net/core/1.0#rfc.section.9.1.1">OAuth Core - 9.1.1.  Normalize Request Parameters</a>
     */
    public static String normalizeRequestParameters(HttpParameter[] params) {
        return normalizeRequestParameters(toParamList(params));
    }

    public static String normalizeRequestParameters(List<HttpParameter> params) {
        Collections.sort(params);
        return encodeParameters(params);
    }

    public static String normalizeAuthorizationHeaders(List<HttpParameter> params) {
        Collections.sort(params);
        return encodeParameters(params);
    }

    public static List<HttpParameter> toParamList(HttpParameter[] params) {
        List<HttpParameter> paramList = new ArrayList<HttpParameter>(params.length);
        paramList.addAll(Arrays.asList(params));
        return paramList;
    }

    /**
     * @param httpParams parameters to be enocded and concatenated
     * @return eoncoded string
     * @see <a href="http://wiki.oauth.net/TestCases">OAuth / TestCases</a>
     * @see <a href="http://groups.google.com/group/oauth/browse_thread/thread/a8398d0521f4ae3d/9d79b698ab217df2?hl=en&lnk=gst&q=space+encoding#9d79b698ab217df2">Space encoding - OAuth | Google Groups</a>
     */
    public static String encodeParameters(List<HttpParameter> httpParams) {
        return encodeParameters(httpParams, "&", false);
    }

    public static String encodeParameters(List<HttpParameter> httpParams, String splitter, boolean quot) {
        StringBuffer buf = new StringBuffer();
        for (HttpParameter param : httpParams) {
            if (!param.isFile()) {
                if (buf.length() != 0) {
                    if (quot) {
                        buf.append("\"");
                    }
                    buf.append(splitter);
                }
                buf.append(encode(param.getName())).append("=");
                if (quot) {
                    buf.append("\"");
                }
                buf.append(encode(param.getValue()));
            }
        }
        if (buf.length() != 0) {
            if (quot) {
                buf.append("\"");
            }
        }
        return buf.toString();
    }

    /**
     * @param value string to be encoded
     * @return encoded string
     * @see <a href="http://wiki.oauth.net/TestCases">OAuth / TestCases</a>
     * @see <a href="http://groups.google.com/group/oauth/browse_thread/thread/a8398d0521f4ae3d/9d79b698ab217df2?hl=en&lnk=gst&q=space+encoding#9d79b698ab217df2">Space encoding - OAuth | Google Groups</a>
     * @see <a href="http://tools.ietf.org/html/rfc3986#section-2.1">RFC 3986 - Uniform Resource Identifier (URI): Generic Syntax - 2.1. Percent-Encoding</a>
     */
    public static String encode(String value) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
        }
        StringBuffer buf = new StringBuffer(encoded.length());
        char focus;
        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                buf.append("%2A");
            } else if (focus == '+') {
                buf.append("%20");
            } else if (focus == '%' && (i + 1) < encoded.length()
                    && encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
                buf.append('~');
                i += 2;
            } else {
                buf.append(focus);
            }
        }
        return buf.toString();
    }

    /**
     * The Signature Base String includes the request absolute URL, tying the signature to a specific endpoint. The URL used in the Signature Base String MUST include the scheme, authority, and path, and MUST exclude the query and fragment as defined by [RFC3986] section 3.<br>
     * If the absolute request URL is not available to the Service Provider (it is always available to the Consumer), it can be constructed by combining the scheme being used, the HTTP Host header, and the relative HTTP request URL. If the Host header is not available, the Service Provider SHOULD use the host name communicated to the Consumer in the documentation or other means.<br>
     * The Service Provider SHOULD document the form of URL used in the Signature Base String to avoid ambiguity due to URL normalization. Unless specified, URL scheme and authority MUST be lowercase and include the port number; http default port 80 and https default port 443 MUST be excluded.<br>
     * <br>
     * For example, the request:<br>
     * HTTP://Example.com:80/resource?id=123<br>
     * Is included in the Signature Base String as:<br>
     * http://example.com/resource
     *
     * @param url the url to be normalized
     * @return the Signature Base String
     * @see <a href="http://oauth.net/core/1.0#rfc.section.9.1.2">OAuth Core - 9.1.2.  Construct Request URL</a>
     */
    public static String constructRequestURL(String url) {
        int index = url.indexOf("?");
        if (-1 != index) {
            url = url.substring(0, index);
        }
        int slashIndex = url.indexOf("/", 8);
        String baseURL = url.substring(0, slashIndex).toLowerCase();
        int colonIndex = baseURL.indexOf(":", 8);
        if (-1 != colonIndex) {
            // url contains port number
            if (baseURL.startsWith("http://") && baseURL.endsWith(":80")) {
                // http default port 80 MUST be excluded
                baseURL = baseURL.substring(0, colonIndex);
            } else if (baseURL.startsWith("https://") && baseURL.endsWith(":443")) {
                // http default port 443 MUST be excluded
                baseURL = baseURL.substring(0, colonIndex);
            }
        }
        url = baseURL + url.substring(slashIndex);

        return url;
    }

    private void setConsumerKey(String consumerKey) {
        this.consumerKey = null != consumerKey ? consumerKey : "";
    }

    private void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = null != consumerSecret ? consumerSecret : "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OAuthSupport)) return false;

        OAuthAuthorization that = (OAuthAuthorization) o;

        if (consumerKey != null ? !consumerKey.equals(that.consumerKey) : that.consumerKey != null)
            return false;
        if (consumerSecret != null ? !consumerSecret.equals(that.consumerSecret) : that.consumerSecret != null)
            return false;
        if (oauthToken != null ? !oauthToken.equals(that.oauthToken) : that.oauthToken != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = consumerKey != null ? consumerKey.hashCode() : 0;
        result = 31 * result + (consumerSecret != null ? consumerSecret.hashCode() : 0);
        result = 31 * result + (oauthToken != null ? oauthToken.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OAuthAuthorization{" +
                "consumerKey='" + consumerKey + '\'' +
                ", consumerSecret='" + consumerSecret + '\'' +
                ", oauthToken=" + oauthToken +
                '}';
    }
}
