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
@SuppressWarnings("rawtypes")
public class OAuthAuthorization implements Authorization, java.io.Serializable {
    private static final long serialVersionUID = -886869424811858868L;

    private static final String HMAC_SHA1 = "HmacSHA1";
    private static final HttpParameter OAUTH_SIGNATURE_METHOD = new HttpParameter("oauth_signature_method", "HMAC-SHA1");
    private static final Logger logger = Logger.getLogger();
    private final String consumerKey;
    private final String consumerSecret;
    private final String realm;
    private OAuthToken oauthToken = null;
    private final HttpClient http;
    private final String oAuthRequestTokenURL;
    private final String oAuthAccessTokenURL;
    private final String oAuthInvalidateTokenURL;

    private final String oAuthAuthorizationURL;
    private final String oAuthAuthenticationURL;
    // constructors

    /**
     * @param conf configuration
     */
    OAuthAuthorization(Configuration conf) {
        http = conf.http;
        this.oAuthRequestTokenURL = conf.oAuthRequestTokenURL;
        this.oAuthAccessTokenURL = conf.oAuthAccessTokenURL;
        this.oAuthInvalidateTokenURL = conf.oAuthInvalidateTokenURL;
        this.oAuthAuthorizationURL = conf.oAuthAuthorizationURL;
        this.oAuthAuthenticationURL = conf.oAuthAuthenticationURL;
        this.consumerKey = conf.oAuthConsumerKey != null ? conf.oAuthConsumerKey : "";
        this.consumerSecret = conf.oAuthConsumerSecret != null ? conf.oAuthConsumerSecret : "";
        if (conf.oAuthAccessToken != null && conf.oAuthAccessTokenSecret != null) {
            this.oauthToken = new AccessToken(conf.oAuthAccessToken, conf.oAuthAccessTokenSecret);
        }
        this.realm = conf.oAuthRealm;
    }

    public static OAuthAuthorizationBuilder newBuilder() {
        return new OAuthAuthorizationBuilder();
    }

    /**
     * Equivalent to OAuthAuthorization.newBuilder().oAuthConsumer(key, secret).build();
     *
     * @param consumerKey    consumer key
     * @param consumerSecret consumer secret
     * @return OAuthAuthorization
     */
    public static OAuthAuthorization getInstance(String consumerKey, String consumerSecret) {
        return newBuilder().oAuthConsumer(consumerKey, consumerSecret).build();
    }

    /**
     * Equivalent to OAuthAuthorization.newBuilder().build();
     *
     * @return OAuthAuthorization
     */
    public static OAuthAuthorization getInstance() {
        return newBuilder().build();
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

    /**
     * Retrieves a request token
     *
     * @return generated request token.
     * @throws TwitterException      when Twitter service or network is unavailable
     * @throws IllegalStateException access token is already available
     * @see <a href="https://dev.twitter.com/docs/auth/oauth/faq">OAuth FAQ | Twitter Developers</a>
     * @see <a href="http://oauth.net/core/1.0a/#auth_step1">OAuth Core 1.0a - 6.1.  Obtaining an Unauthorized Request Token</a>
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/oauth/request_token">POST oauth/request_token | Twitter Developers</a>
     * @since Twitter4J 2.0.0
     */
    public RequestToken getOAuthRequestToken() throws TwitterException {
        return getOAuthRequestToken(null);
    }

    /**
     * Retrieves a request token
     *
     * @param callbackURL callback URL
     * @return generated request token
     * @throws TwitterException      when Twitter service or network is unavailable
     * @throws IllegalStateException access token is already available
     * @see <a href="https://dev.twitter.com/docs/auth/oauth/faq">OAuth FAQ | Twitter Developers</a>
     * @see <a href="http://oauth.net/core/1.0a/#auth_step1">OAuth Core 1.0a - 6.1.  Obtaining an Unauthorized Request Token</a>
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/oauth/request_token">POST oauth/request_token | Twitter Developers</a>
     * @since Twitter4J 2.0.0
     */
    public RequestToken getOAuthRequestToken(String callbackURL) throws TwitterException {
        if (oauthToken instanceof AccessToken) {
            throw new IllegalStateException("Access token already available.");
        }
        List<HttpParameter> params = new ArrayList<>();
        if (callbackURL != null) {
            params.add(new HttpParameter("oauth_callback", callbackURL));
        }
        oauthToken = new RequestToken(http.post(oAuthRequestTokenURL, params.toArray(new HttpParameter[0]),
                this, null), oAuthAuthorizationURL, oAuthAuthenticationURL);
        return (RequestToken) oauthToken;
    }

    /**
     * Returns an access token associated with this instance.<br>
     * If no access token is associated with this instance, this will retrieve a new access token.
     *
     * @return access token
     * @throws TwitterException      when Twitter service or network is unavailable, or the user has not authorized
     * @throws IllegalStateException when RequestToken has never been acquired
     * @see <a href="https://dev.twitter.com/docs/auth/oauth/faq">OAuth FAQ | dev.twitter.com - How long does an access token last?</a>
     * @see <a href="http://oauth.net/core/1.0a/#auth_step2">OAuth Core 1.0a - 6.2.  Obtaining User Authorization</a>
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/oauth/access_token">POST oauth/access_token | Twitter Developers</a>
     * @since Twitter4J 2.0.0
     */
    public AccessToken getOAuthAccessToken() throws TwitterException {
        ensureTokenIsAvailable();
        if (oauthToken instanceof AccessToken) {
            return (AccessToken) oauthToken;
        }
        oauthToken = new AccessToken(http.post(oAuthAccessTokenURL, null, this, null));
        return (AccessToken) oauthToken;
    }

    /**
     * Retrieves an access token.
     *
     * @param oauthVerifier OAuth verifier. AKA pin.
     * @return access token
     * @throws TwitterException when Twitter service or network is unavailable, or the user has not authorized
     * @see <a href="https://dev.twitter.com/docs/auth/oauth/faq">OAuth FAQ | dev.twitter.com - How long does an access token last?</a>
     * @see <a href="http://oauth.net/core/1.0a/#auth_step2">OAuth Core 1.0a - 6.2.  Obtaining User Authorization</a>
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/oauth/access_token">POST oauth/access_token | Twitter Developers</a>
     * @since Twitter4J 2.0.0
     */
    public AccessToken getOAuthAccessToken(String oauthVerifier) throws TwitterException {
        ensureTokenIsAvailable();
        oauthToken = new AccessToken(http.post(oAuthAccessTokenURL
                , new HttpParameter[]{new HttpParameter("oauth_verifier", oauthVerifier)}, this, null));
        return (AccessToken) oauthToken;
    }

    /**
     * Retrieves an access token associated with the supplied request token and sets userId.
     *
     * @param requestToken the request token
     * @return access token associated with the supplied request token.
     * @throws TwitterException when Twitter service or network is unavailable, or the user has not authorized
     * @see <a href="https://dev.twitter.com/docs/auth/oauth/faq">OAuth FAQ | dev.twitter.com - How long does an access token last?</a>
     * @see <a href="http://oauth.net/core/1.0a/#auth_step2">OAuth Core 1.0a - 6.2.  Obtaining User Authorization</a>
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/oauth/access_token">POST oauth/access_token | Twitter Developers</a>
     * @since Twitter4J 2.0.0
     */
    public AccessToken getOAuthAccessToken(RequestToken requestToken) throws TwitterException {
        this.oauthToken = requestToken;
        return getOAuthAccessToken();
    }

    /**
     * Retrieves an access token associated with the supplied request token and sets userId.
     *
     * @param requestToken  the request token
     * @param oauthVerifier OAuth verifier. AKA pin.
     * @return access token associated with the supplied request token.
     * @throws TwitterException when Twitter service or network is unavailable, or the user has not authorized
     * @see <a href="http://oauth.net/core/1.0a/#auth_step2">OAuth Core 1.0a - 6.2.  Obtaining User Authorization</a>
     * @see <a href="https://dev.twitter.com/docs/api/1.1/post/oauth/access_token">POST oauth/access_token | Twitter Developers</a>
     * @since Twitter 2.1.1
     */
    public AccessToken getOAuthAccessToken(RequestToken requestToken, String oauthVerifier) throws TwitterException {
        this.oauthToken = requestToken;
        return getOAuthAccessToken(oauthVerifier);
    }

    /**
     * Invalidates the OAuth token
     * <p>
     * On success, sets oauthToken to null
     *
     * @throws TwitterException when Twitter service or network is unavailable, or the user has not authorized
     */
    @SuppressWarnings("unused")
    public void invalidateOAuthToken() throws TwitterException {
        if (oauthToken == null) {
            throw new IllegalStateException("OAuth Token is not available.");
        }

        HttpParameter[] params = new HttpParameter[1];
        params[0] = new HttpParameter("access_token", getOAuthAccessToken().getToken());

        OAuthToken _token = oauthToken;
        boolean succeed = false;

        try {
            HttpResponse res = http.post(oAuthInvalidateTokenURL, params, this, null);
            if (res.getStatusCode() != 200) {
                throw new TwitterException("Invalidating OAuth Token failed.", res);
            }

            succeed = true;

        } finally {
            oauthToken = null;
            if (!succeed) {
                oauthToken = _token;
            }
        }
    }

    /*package*/ String generateAuthorizationHeader(String method, String url, HttpParameter[] params, String nonce, String timestamp, OAuthToken otoken) {
        if (null == params) {
            params = new HttpParameter[0];
        }
        List<HttpParameter> oauthHeaderParams = new ArrayList<>(5);
        oauthHeaderParams.add(new HttpParameter("oauth_consumer_key", consumerKey));
        oauthHeaderParams.add(OAUTH_SIGNATURE_METHOD);
        oauthHeaderParams.add(new HttpParameter("oauth_timestamp", timestamp));
        oauthHeaderParams.add(new HttpParameter("oauth_nonce", nonce));
        oauthHeaderParams.add(new HttpParameter("oauth_version", "1.0"));
        if (otoken != null) {
            oauthHeaderParams.add(new HttpParameter("oauth_token", otoken.getToken()));
        }
        List<HttpParameter> signatureBaseParams = new ArrayList<>(oauthHeaderParams.size() + params.length);
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
            String[] queryStrs = url.substring(queryStart + 1).split("&");
            try {
                for (String query : queryStrs) {
                    String[] split = query.split("=");
                    if (split.length == 2) {
                        signatureBaseParams.add(
                                new HttpParameter(URLDecoder.decode(split[0],
                                        "UTF-8"), URLDecoder.decode(split[1], "UTF-8"))
                        );
                    } else {
                        signatureBaseParams.add(
                                new HttpParameter(URLDecoder.decode(split[0], "UTF-8"), "")
                        );
                    }
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
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

    /**
     * Computes RFC 2104-compliant HMAC signature.
     *
     * @param data  the data to be signed
     * @param token the token
     * @return signature
     * @see <a href="http://oauth.net/core/1.0a/#rfc.section.9.2.1">OAuth Core - 9.2.1.  Generating Signature</a>
     */
    /*package*/ String generateSignature(String data, OAuthToken token) {
        byte[] byteHMAC;
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
        List<HttpParameter> paramList = new ArrayList<>(params.length);
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
            if (!param.isFile() && !param.isJson()) {
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
    @SuppressWarnings("JavadocLinkAsPlainText")
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

//    public void setOAuthConsumer(String consumerKey, String consumerSecret) {
//        this.consumerKey = consumerKey != null ? consumerKey : "";
//        this.consumerSecret = consumerSecret != null ? consumerSecret : "";
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OAuthAuthorization that = (OAuthAuthorization) o;
        return Objects.equals(consumerKey, that.consumerKey) && Objects.equals(consumerSecret, that.consumerSecret) && Objects.equals(realm, that.realm) && Objects.equals(oauthToken, that.oauthToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(consumerKey, consumerSecret, realm, oauthToken);
    }

    @Override
    public String toString() {
        return "OAuthAuthorization{" +
                "consumerKey='" + consumerKey + '\'' +
                ", consumerSecret='******************************************'" +
                ", oauthToken=" + oauthToken +
                '}';
    }

    static class OAuthAuthorizationBuilder extends Configuration<OAuthAuthorizationBuilder> {
        private OAuthAuthorizationBuilder() {
        }

        public OAuthAuthorization build() {
            return new OAuthAuthorization(buildConfiguration());
        }
    }
}
