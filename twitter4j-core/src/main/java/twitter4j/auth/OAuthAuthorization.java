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

package twitter4j.auth;

import twitter4j.*;
import twitter4j.conf.Configuration;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see <a href="http://oauth.net/core/1.0a/">OAuth Core 1.0a</a>
 */
public class OAuthAuthorization implements Authorization, java.io.Serializable, OAuthSupport {
    private static final long serialVersionUID = -886869424811858868L;
    private final Configuration conf;
    private transient static HttpClient http;

    private static final String HMAC_SHA1 = "HmacSHA1";
    private static final HttpParameter OAUTH_SIGNATURE_METHOD = new HttpParameter("oauth_signature_method", "HMAC-SHA1");
    private static final Logger logger = Logger.getLogger(OAuthAuthorization.class);
    private String consumerKey = "";
    private String consumerSecret;

    private String realm = null;

    private OAuthToken oauthToken = null;

    // constructors

    /**
     * @param conf configuration
     */
    public OAuthAuthorization(Configuration conf) {
        this.conf = conf;
        http = HttpClientFactory.getInstance(conf.getHttpClientConfiguration());
        setOAuthConsumer(conf.getOAuthConsumerKey(), conf.getOAuthConsumerSecret());
        if (conf.getOAuthAccessToken() != null && conf.getOAuthAccessTokenSecret() != null) {
            setOAuthAccessToken(new AccessToken(conf.getOAuthAccessToken(), conf.getOAuthAccessTokenSecret()));
        }
    }

    // implementations for Authorization
    @Override
    public String getAuthorizationHeader(HttpRequest req) {
        return generateAuthorizationHeader(req.getMethod().name(), req.getURL(), req.getParameters(), oauthToken);
    }

    private void ensureTokenIsAvailable() {
        if (null == oauthToken) {
            throw new IllegalStateException("No Token available.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled() {
        return oauthToken != null && oauthToken instanceof AccessToken;
    }

    // implementation for OAuthSupport interface

    @Override
    public RequestToken getOAuthRequestToken() throws TwitterException {
        return getOAuthRequestToken(null, null, null);
    }

    @Override
    public RequestToken getOAuthRequestToken(String callbackURL) throws TwitterException {
        return getOAuthRequestToken(callbackURL, null, null);
    }

    @Override
    public RequestToken getOAuthRequestToken(String callbackURL, String xAuthAccessType) throws TwitterException {
        return getOAuthRequestToken(callbackURL, xAuthAccessType, null);
    }

    @Override
    public RequestToken getOAuthRequestToken(String callbackURL, String xAuthAccessType, String xAuthMode) throws TwitterException {
        if (oauthToken instanceof AccessToken) {
            throw new IllegalStateException("Access token already available.");
        }
        List<HttpParameter> params = new ArrayList<HttpParameter>();
        if (callbackURL != null) {
            params.add(new HttpParameter("oauth_callback", callbackURL));
        }
        if (xAuthAccessType != null) {
            params.add(new HttpParameter("x_auth_access_type", xAuthAccessType));
        }
        if (xAuthMode != null) {
            params.add(new HttpParameter("x_auth_mode", xAuthMode));
        }
        oauthToken = new RequestToken(http.post(conf.getOAuthRequestTokenURL(), params.toArray(new HttpParameter[params.size()]), this, null), this);
        return (RequestToken) oauthToken;
    }

    @Override
    public AccessToken getOAuthAccessToken() throws TwitterException {
        ensureTokenIsAvailable();
        if (oauthToken instanceof AccessToken) {
            return (AccessToken) oauthToken;
        }
        oauthToken = new AccessToken(http.post(conf.getOAuthAccessTokenURL(), null, this, null));
        return (AccessToken) oauthToken;
    }

    @Override
    public AccessToken getOAuthAccessToken(String oauthVerifier) throws TwitterException {
        ensureTokenIsAvailable();
        oauthToken = new AccessToken(http.post(conf.getOAuthAccessTokenURL()
                , new HttpParameter[]{new HttpParameter("oauth_verifier", oauthVerifier)}, this, null));
        return (AccessToken) oauthToken;
    }

    @Override
    public AccessToken getOAuthAccessToken(RequestToken requestToken) throws TwitterException {
        this.oauthToken = requestToken;
        return getOAuthAccessToken();
    }

    @Override
    public AccessToken getOAuthAccessToken(RequestToken requestToken, String oauthVerifier) throws TwitterException {
        this.oauthToken = requestToken;
        return getOAuthAccessToken(oauthVerifier);
    }

    @Override
    public AccessToken getOAuthAccessToken(String screenName, String password) throws TwitterException {
        try {
            String url = conf.getOAuthAccessTokenURL();
            if (0 == url.indexOf("http://")) {
                // SSL is required
                // @see https://dev.twitter.com/docs/oauth/xauth
                url = "https://" + url.substring(7);
            }
            oauthToken = new AccessToken(http.post(url, new HttpParameter[]{
                    new HttpParameter("x_auth_username", screenName),
                    new HttpParameter("x_auth_password", password),
                    new HttpParameter("x_auth_mode", "client_auth")
            }, this, null));
            return (AccessToken) oauthToken;
        } catch (TwitterException te) {
            throw new TwitterException("The screen name / password combination seems to be invalid.", te, te.getStatusCode());
        }
    }

    @Override
    public void setOAuthAccessToken(AccessToken accessToken) {
        this.oauthToken = accessToken;
    }

    /**
     * Sets the OAuth realm
     *
     * @param realm OAuth realm
     * @since Twitter 2.1.4
     */
    public void setOAuthRealm(String realm) {
        this.realm = realm;
    }


    /*package*/ String generateAuthorizationHeader(String method, String url, HttpParameter[] params, String nonce, String timestamp, OAuthToken otoken) {
        if (null == params) {
            params = new HttpParameter[0];
        }
        List<HttpParameter> oauthHeaderParams = new ArrayList<HttpParameter>(5);
        oauthHeaderParams.add(new HttpParameter("oauth_consumer_key", consumerKey));
        oauthHeaderParams.add(OAUTH_SIGNATURE_METHOD);
        oauthHeaderParams.add(new HttpParameter("oauth_timestamp", timestamp));
        oauthHeaderParams.add(new HttpParameter("oauth_nonce", nonce));
        oauthHeaderParams.add(new HttpParameter("oauth_version", "1.0"));
        if (otoken != null) {
            oauthHeaderParams.add(new HttpParameter("oauth_token", otoken.getToken()));
        }
        List<HttpParameter> signatureBaseParams = new ArrayList<HttpParameter>(oauthHeaderParams.size() + params.length);
        signatureBaseParams.addAll(oauthHeaderParams);
        if (!HttpParameter.containsFile(params)) {
            signatureBaseParams.addAll(toParamList(params));
        }
        parseGetParameters(url, signatureBaseParams);
        StringBuilder base = new StringBuilder(method).append("&")
                .append(HttpParameter.encode(constructRequestURL(url))).append("&");
        base.append(HttpParameter.encode(normalizeRequestParameters(signatureBaseParams)));
        String oauthBaseString = base.toString();
        logger.debug("OAuth base string: ", oauthBaseString);
        String signature = generateSignature(oauthBaseString, otoken);
        logger.debug("OAuth signature: ", signature);

        oauthHeaderParams.add(new HttpParameter("oauth_signature", signature));

        // http://oauth.net/core/1.0/#rfc.section.9.1.1
        if (realm != null) {
            oauthHeaderParams.add(new HttpParameter("realm", realm));
        }
        return "OAuth " + encodeParameters(oauthHeaderParams, ",", true);
    }

    private void parseGetParameters(String url, List<HttpParameter> signatureBaseParams) {
        int queryStart = url.indexOf("?");
        if (-1 != queryStart) {
            url.split("&");
            String[] queryStrs = url.substring(queryStart + 1).split("&");
            try {
                for (String query : queryStrs) {
                    String[] split = query.split("=");
                    if (split.length == 2) {
                        signatureBaseParams.add(
                                new HttpParameter(URLDecoder.decode(split[0],
                                        "UTF-8"), URLDecoder.decode(split[1],
                                        "UTF-8"))
                        );
                    } else {
                        signatureBaseParams.add(
                                new HttpParameter(URLDecoder.decode(split[0],
                                        "UTF-8"), "")
                        );
                    }
                }
            } catch (UnsupportedEncodingException ignore) {
            }

        }

    }

    private static final Random RAND = new Random();

    /**
     * @return generated authorization header
     * @see <a href="http://oauth.net/core/1.0a/#rfc.section.5.4.1">OAuth Core - 5.4.1.  Authorization Header</a>
     */
    /*package*/ String generateAuthorizationHeader(String method, String url, HttpParameter[] params, OAuthToken token) {
        long timestamp = System.currentTimeMillis() / 1000;
        long nonce = timestamp + RAND.nextInt();
        return generateAuthorizationHeader(method, url, params, String.valueOf(nonce), String.valueOf(timestamp), token);
    }

    public List<HttpParameter> generateOAuthSignatureHttpParams(String method, String url) {
        long timestamp = System.currentTimeMillis() / 1000;
        long nonce = timestamp + RAND.nextInt();

        List<HttpParameter> oauthHeaderParams = new ArrayList<HttpParameter>(5);
        oauthHeaderParams.add(new HttpParameter("oauth_consumer_key", consumerKey));
        oauthHeaderParams.add(OAUTH_SIGNATURE_METHOD);
        oauthHeaderParams.add(new HttpParameter("oauth_timestamp", timestamp));
        oauthHeaderParams.add(new HttpParameter("oauth_nonce", nonce));
        oauthHeaderParams.add(new HttpParameter("oauth_version", "1.0"));
        if (oauthToken != null) {
            oauthHeaderParams.add(new HttpParameter("oauth_token", oauthToken.getToken()));
        }

        List<HttpParameter> signatureBaseParams = new ArrayList<HttpParameter>(oauthHeaderParams.size());
        signatureBaseParams.addAll(oauthHeaderParams);
        parseGetParameters(url, signatureBaseParams);

        StringBuilder base = new StringBuilder(method).append("&")
                .append(HttpParameter.encode(constructRequestURL(url))).append("&");
        base.append(HttpParameter.encode(normalizeRequestParameters(signatureBaseParams)));

        String oauthBaseString = base.toString();
        String signature = generateSignature(oauthBaseString, oauthToken);

        oauthHeaderParams.add(new HttpParameter("oauth_signature", signature));

        return oauthHeaderParams;
    }

    /**
     * Computes RFC 2104-compliant HMAC signature.
     *
     * @param data  the data to be signed
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
                String oauthSignature = HttpParameter.encode(consumerSecret) + "&";
                spec = new SecretKeySpec(oauthSignature.getBytes(), HMAC_SHA1);
            } else {
                spec = token.getSecretKeySpec();
                if (null == spec) {
                    String oauthSignature = HttpParameter.encode(consumerSecret) + "&" + HttpParameter.encode(token.getTokenSecret());
                    spec = new SecretKeySpec(oauthSignature.getBytes(), HMAC_SHA1);
                    token.setSecretKeySpec(spec);
                }
            }
            mac.init(spec);
            byteHMAC = mac.doFinal(data.getBytes());
        } catch (InvalidKeyException ike) {
            logger.error("Failed initialize \"Message Authentication Code\" (MAC)", ike);
            throw new AssertionError(ike);
        } catch (NoSuchAlgorithmException nsae) {
            logger.error("Failed to get HmacSHA1 \"Message Authentication Code\" (MAC)", nsae);
            throw new AssertionError(nsae);
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
     * @return normalized and concatenated parameters
     * @see <a href="http://oauth.net/core/1.0#rfc.section.9.1.1">OAuth Core - 9.1.1.  Normalize Request Parameters</a>
     */
    static String normalizeRequestParameters(HttpParameter[] params) {
        return normalizeRequestParameters(toParamList(params));
    }

    private static String normalizeRequestParameters(List<HttpParameter> params) {
        Collections.sort(params);
        return encodeParameters(params);
    }

    private static List<HttpParameter> toParamList(HttpParameter[] params) {
        List<HttpParameter> paramList = new ArrayList<HttpParameter>(params.length);
        paramList.addAll(Arrays.asList(params));
        return paramList;
    }

    /**
     * @param httpParams parameters to be encoded and concatenated
     * @return encoded string
     * @see <a href="http://wiki.oauth.net/TestCases">OAuth / TestCases</a>
     * @see <a href="http://groups.google.com/group/oauth/browse_thread/thread/a8398d0521f4ae3d/9d79b698ab217df2?hl=en&lnk=gst&q=space+encoding#9d79b698ab217df2">Space encoding - OAuth | Google Groups</a>
     */
    public static String encodeParameters(List<HttpParameter> httpParams) {
        return encodeParameters(httpParams, "&", false);
    }

    public static String encodeParameters(List<HttpParameter> httpParams, String splitter, boolean quot) {
        StringBuilder buf = new StringBuilder();
        for (HttpParameter param : httpParams) {
            if (!param.isFile()) {
                if (buf.length() != 0) {
                    if (quot) {
                        buf.append("\"");
                    }
                    buf.append(splitter);
                }
                buf.append(HttpParameter.encode(param.getName())).append("=");
                if (quot) {
                    buf.append("\"");
                }
                buf.append(HttpParameter.encode(param.getValue()));
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
    static String constructRequestURL(String url) {
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

    @Override
    public void setOAuthConsumer(String consumerKey, String consumerSecret) {
        this.consumerKey = consumerKey != null ? consumerKey : "";
        this.consumerSecret = consumerSecret != null ? consumerSecret : "";
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
                ", consumerSecret='******************************************\'" +
                ", oauthToken=" + oauthToken +
                '}';
    }
}
