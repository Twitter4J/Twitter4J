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
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.conf.ConfigurationContext;
import twitter4j.conf.PropertyConfiguration;
import twitter4j.internal.http.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.*;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class OAuthTest extends TwitterTestBase {


    public OAuthTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testDeterministic() throws Exception {
        ArrayList list1 = new ArrayList();
        ArrayList list2 = new ArrayList();
        assertEquals(list1, list2);
        Twitter twitter1 = new TwitterFactory().getInstance();
        twitter1.setOAuthConsumer(browserConsumerKey, browserConsumerSecret);
        Twitter twitter2 = new TwitterFactory().getInstance();
        twitter2.setOAuthConsumer(browserConsumerKey, browserConsumerSecret);
        assertTrue(twitter1.equals(twitter2));
        assertEquals(twitter1, twitter2);
    }

    public void testOAuth() throws Exception {
        ConfigurationBuilder build = new ConfigurationBuilder();
        String oAuthAccessToken = p.getProperty("id1.oauth.accessToken");
        String oAuthAccessTokenSecret = p.getProperty("id1.oauth.accessTokenSecret");
        String oAuthConsumerKey = p.getProperty("oauth.consumerKey");
        String oAuthConsumerSecret = p.getProperty("oauth.consumerSecret");
        build.setOAuthAccessToken(oAuthAccessToken);
        build.setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
        build.setOAuthConsumerKey(oAuthConsumerKey);
        build.setOAuthConsumerSecret(oAuthConsumerSecret);
        OAuthAuthorization auth = new OAuthAuthorization(build.build());
        Twitter twitter = new TwitterFactory().getInstance(auth);
        twitter.verifyCredentials();
    }

    public void testDesktopClient() throws Exception {
        RequestToken rt;
        Twitter twitter = new TwitterFactory().getInstance();
        HttpClientImpl http;
        HttpResponse response;
        String resStr;
        String authorizeURL;
        HttpParameter[] params;
        AccessToken at;
        String cookie;
        http = new HttpClientImpl();

        // desktop client - requiring pin
        Twitter unauthenticated = new TwitterFactory().getInstance();
        unauthenticated.setOAuthConsumer(desktopConsumerKey, desktopConsumerSecret);
        rt = unauthenticated.getOAuthRequestToken();
        rt.hashCode();
        // trying to get an access token without permitting the request token.
        try {
            unauthenticated.getOAuthAccessToken();
            fail();
        } catch (TwitterException te) {
            assertEquals(401, te.getStatusCode());
        }
        twitter.setOAuthConsumer(desktopConsumerKey, desktopConsumerSecret);
        rt = twitter.getOAuthRequestToken(null, "read");
        // trying to get an access token without permitting the request token.
        try {
            twitter.getOAuthAccessToken(rt.getToken(), rt.getTokenSecret());
            fail();
        } catch (TwitterException te) {
            assertEquals(401, te.getStatusCode());
        }
        Map<String, String> props = new HashMap<String, String>();
        response = http.get(rt.getAuthorizationURL());
        cookie = response.getResponseHeader("Set-Cookie");
//        http.setRequestHeader("Cookie", cookie);
        props.put("Cookie", cookie);
        resStr = response.asString();
        authorizeURL = catchPattern(resStr, "<form action=\"", "\" id=\"oauth_form\"");
        params = new HttpParameter[4];
        params[0] = new HttpParameter("authenticity_token"
                , catchPattern(resStr, "\"authenticity_token\" type=\"hidden\" value=\"", "\" />"));
        params[1] = new HttpParameter("oauth_token",
                catchPattern(resStr, "name=\"oauth_token\" type=\"hidden\" value=\"", "\" />"));
        params[2] = new HttpParameter("session[username_or_email]", numberId);
        params[3] = new HttpParameter("session[password]", numberPass);
        response = http.request(new HttpRequest(RequestMethod.POST, authorizeURL, params, null, props));
        resStr = response.asString();
        String pin = catchPattern(resStr, "<kbd aria-labelledby=\"code-desc\"><code>", "</code></kbd>");
        at = twitter.getOAuthAccessToken(rt, pin);
        try {
            twitter.getOAuthRequestToken();
        } catch (TwitterException te) {
            fail("expecting IllegalStateException as access token is already available.");
        } catch (IllegalStateException expected) {
        }


        assertEquals(at.getScreenName(), numberId);
        assertEquals(at.getUserId(), 96154916);
        AccessToken at1 = twitter.getOAuthAccessToken();
        assertEquals(at, at1);
        TwitterResponse res = twitter.getLanguages();
        assertEquals(TwitterResponse.READ, res.getAccessLevel());

    }

    public void testIllegalStatus() throws Exception {
        try {
            new TwitterFactory().getInstance().getOAuthAccessToken();
            fail("should throw IllegalStateException since request token hasn't been acquired.");
        } catch (IllegalStateException ignore) {
        }
    }

    public void testSigninWithTwitter() throws Exception {
        RequestToken rt;
        Twitter twitter = new TwitterFactory().getInstance();
        HttpClientImpl http;
        HttpResponse response;
        String resStr;
        String authorizeURL;
        HttpParameter[] params;
        AccessToken at;
        String cookie;
        http = new HttpClientImpl();

        // browser client - not requiring pin
        twitter.setOAuthConsumer(browserConsumerKey, browserConsumerSecret);
        rt = twitter.getOAuthRequestToken();

        Map<String, String> props = new HashMap<String, String>();
        response = http.get(rt.getAuthenticationURL());
        cookie = response.getResponseHeader("Set-Cookie");
//        http.setRequestHeader("Cookie", cookie);
        props.put("Cookie", cookie);

        resStr = response.asString();
        authorizeURL = catchPattern(resStr, "<form action=\"", "\" id=\"oauth_form\"");
        params = new HttpParameter[4];
        params[0] = new HttpParameter("authenticity_token"
                , catchPattern(resStr, "\"authenticity_token\" type=\"hidden\" value=\"", "\" />"));
        params[1] = new HttpParameter("oauth_token",
                catchPattern(resStr, "name=\"oauth_token\" type=\"hidden\" value=\"", "\" />"));
        params[2] = new HttpParameter("session[username_or_email]", id1.screenName);
        params[3] = new HttpParameter("session[password]", id1.password);
        response = http.request(new HttpRequest(RequestMethod.POST, authorizeURL, params, null, props));

//        response = http.post(authorizeURL, params);
        at = twitter.getOAuthAccessToken(rt);
        assertEquals(at.getScreenName(), id1.screenName);
        assertEquals(at.getUserId(), 6358482);

    }

    public void testBrowserClient() throws Exception {
        RequestToken rt;
        Twitter twitter = new TwitterFactory().getInstance();
        HttpClientImpl http;
        HttpResponse response;
        String resStr;
        String authorizeURL;
        HttpParameter[] params;
        AccessToken at;
        String cookie;
        http = new HttpClientImpl();

        // browser client - not requiring pin
        twitter.setOAuthConsumer(browserConsumerKey, browserConsumerSecret);
        rt = twitter.getOAuthRequestToken();

        response = http.get(rt.getAuthorizationURL());
        Map<String, String> props = new HashMap<String, String>();
        cookie = response.getResponseHeader("Set-Cookie");
        props.put("Cookie", cookie);
        resStr = response.asString();
        authorizeURL = catchPattern(resStr, "<form action=\"", "\" id=\"oauth_form\"");
        params = new HttpParameter[4];
        params[0] = new HttpParameter("authenticity_token"
                , catchPattern(resStr, "\"authenticity_token\" type=\"hidden\" value=\"", "\" />"));
        params[1] = new HttpParameter("oauth_token",
                catchPattern(resStr, "name=\"oauth_token\" type=\"hidden\" value=\"", "\" />"));
        params[2] = new HttpParameter("session[username_or_email]", id1.screenName);
        params[3] = new HttpParameter("session[password]", id1.password);
        response = http.request(new HttpRequest(RequestMethod.POST, authorizeURL, params, null, props));
        at = twitter.getOAuthAccessToken(rt);
        assertEquals(at.getScreenName(), id1.screenName);
        assertEquals(at.getUserId(), 6358482);


    }

    public void testBrowserClientWithCustomCallback() throws Exception {
        RequestToken rt;
        Twitter twitter = new TwitterFactory().getInstance();
        HttpClientImpl http;
        HttpResponse response;
        String resStr;
        String authorizeURL;
        HttpParameter[] params;
        AccessToken at;
        String cookie;
        http = new HttpClientImpl();

        // browser client - not requiring pin / overriding callback url
        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(browserConsumerKey, browserConsumerSecret);
        rt = twitter.getOAuthRequestToken("http://yusuke.homeip.net/twitter4j/custom_callback");
        http = new HttpClientImpl();

        System.out.println("AuthorizationURL: " + rt.getAuthorizationURL());
        response = http.get(rt.getAuthorizationURL());
        Map<String, String> props = new HashMap<String, String>();
        cookie = response.getResponseHeader("Set-Cookie");
//        http.setRequestHeader("Cookie", cookie);
        props.put("Cookie", cookie);
        resStr = response.asString();
        authorizeURL = catchPattern(resStr, "<form action=\"", "\" id=\"oauth_form\"");
        params = new HttpParameter[4];
        params[0] = new HttpParameter("authenticity_token"
                , catchPattern(resStr, "\"authenticity_token\" type=\"hidden\" value=\"", "\" />"));
        params[1] = new HttpParameter("oauth_token",
                catchPattern(resStr, "name=\"oauth_token\" type=\"hidden\" value=\"", "\" />"));
        params[2] = new HttpParameter("session[username_or_email]", id1.screenName);
        params[3] = new HttpParameter("session[password]", id1.password);
        response = http.request(new HttpRequest(RequestMethod.POST, authorizeURL, params, null, props));
//        response = http.post(authorizeURL, params);
        resStr = response.asString();
        String oauthVerifier = catchPattern(resStr, "&oauth_verifier=", "\">");

        at = twitter.getOAuthAccessToken(rt, oauthVerifier);
        assertEquals(at.getScreenName(), id1.screenName);
        assertEquals(at.getUserId(), 6358482);
    }


    private static String catchPattern(String body, String before, String after) {
        int beforeIndex = body.indexOf(before);
        int afterIndex = body.indexOf(after, beforeIndex);
        return body.substring(beforeIndex + before.length(), afterIndex);
    }

    public void testAccessToken() {
        AccessToken at = new AccessToken("oauth_token=6377362-kW0YV1ymaqEUCSHP29ux169mDeA4kQfhEuqkdvHk&oauth_token_secret=ghoTpd7LuMLHtJDyHkhYo40Uq5bWSxeCyOUAkbsOoOY&user_id=6377362&screen_name=twit4j2");
        assertEquals("6377362-kW0YV1ymaqEUCSHP29ux169mDeA4kQfhEuqkdvHk", at.getToken());
        assertEquals("ghoTpd7LuMLHtJDyHkhYo40Uq5bWSxeCyOUAkbsOoOY", at.getTokenSecret());
        assertEquals("twit4j2", at.getScreenName());
        assertEquals(6377362, at.getUserId());
    }


    public void testSign() throws Exception {
        String baseStr = "GET&http%3A%2F%2Fphotos.example.net%2Fphotos&file%3Dvacation.jpg%26oauth_consumer_key%3Ddpf43f3p2l4k3l03%26oauth_nonce%3Dkllo9940pd9333jh%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1191242096%26oauth_token%3Dnnch734d00sl2jdk%26oauth_version%3D1.0%26size%3Doriginal";
        OAuthAuthorization oauth = new OAuthAuthorization(ConfigurationContext.getInstance());
        oauth.setOAuthConsumer("dpf43f3p2l4k3l03", "kd94hf93k423kf44");
        trySerializable(oauth);
        //http://wiki.oauth.net/TestCases
        assertEquals("tR3+Ty81lMeYAr/Fid0kMTYa/WM=", oauth.generateSignature(baseStr, new RequestToken("nnch734d00sl2jdk", "pfkkdhi9sl3r4s00")));
        oauth = new OAuthAuthorization(ConfigurationContext.getInstance());
        oauth.setOAuthConsumer(desktopConsumerKey, "cs");
        assertEquals("egQqG5AJep5sJ7anhXju1unge2I=", oauth.generateSignature("bs", new RequestToken("nnch734d00sl2jdk", "")));
        assertEquals("VZVjXceV7JgPq/dOTnNmEfO0Fv8=", oauth.generateSignature("bs", new RequestToken("nnch734d00sl2jdk", "ts")));
        oauth = new OAuthAuthorization(ConfigurationContext.getInstance());
        oauth.setOAuthConsumer(desktopConsumerKey, "kd94hf93k423kf44");

        assertEquals("tR3+Ty81lMeYAr/Fid0kMTYa/WM=", oauth.generateSignature("GET&http%3A%2F%2Fphotos.example.net%2Fphotos&file%3Dvacation.jpg%26oauth_consumer_key%3Ddpf43f3p2l4k3l03%26oauth_nonce%3Dkllo9940pd9333jh%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1191242096%26oauth_token%3Dnnch734d00sl2jdk%26oauth_version%3D1.0%26size%3Doriginal", new RequestToken("nnch734d00sl2jdk", "pfkkdhi9sl3r4s00")));
    }

    public void testHeader() throws Exception {
        HttpParameter[] params = new HttpParameter[2];
        params[0] = new HttpParameter("file", "vacation.jpg");
        params[1] = new HttpParameter("size", "original");
        OAuthAuthorization oauth = new OAuthAuthorization(ConfigurationContext.getInstance());
        oauth.setOAuthConsumer("dpf43f3p2l4k3l03", "kd94hf93k423kf44");
        String expected = "OAuth oauth_consumer_key=\"dpf43f3p2l4k3l03\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"1191242096\",oauth_nonce=\"kllo9940pd9333jh\",oauth_version=\"1.0\",oauth_token=\"nnch734d00sl2jdk\",oauth_signature=\"tR3%2BTy81lMeYAr%2FFid0kMTYa%2FWM%3D\"";
        assertEquals(expected, oauth.generateAuthorizationHeader("GET", "http://photos.example.net/photos", params, "kllo9940pd9333jh", "1191242096", new RequestToken("nnch734d00sl2jdk", "pfkkdhi9sl3r4s00")));

    }

    public void testEncodeParameter() throws Exception {
        //http://wiki.oauth.net/TestCases
        assertEquals("abcABC123", HttpParameter.encode("abcABC123"));
        assertEquals("-._~", HttpParameter.encode("-._~"));
        assertEquals("%25", HttpParameter.encode("%"));
        assertEquals("%2B", HttpParameter.encode("+"));
        assertEquals("%26%3D%2A", HttpParameter.encode("&=*"));
        assertEquals("%0A", HttpParameter.encode("\n"));
        assertEquals("%20", HttpParameter.encode("\u0020"));
        assertEquals("%7F", HttpParameter.encode("\u007F"));
        assertEquals("%C2%80", HttpParameter.encode("\u0080"));
        assertEquals("%E3%80%81", HttpParameter.encode("\u3001"));

        String unreserved = "abcdefghijklmnopqrstuvwzyxABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-._~";
        assertEquals(unreserved, HttpParameter.encode(unreserved));
    }

    public void testNormalizeRequestParameters() throws Exception {
        // a=1, c=hi%20there, f=25, f=50, f=a, z=p, z=t
        HttpParameter[] params = new HttpParameter[]{
                new HttpParameter("a", "1"),
                new HttpParameter("c", "hi there"),
                new HttpParameter("f", "50"),
                new HttpParameter("f", "25"),
                new HttpParameter("z", "t"),
                new HttpParameter("z", "p"),
                new HttpParameter("f", "a"),
        };
        assertEquals("a=1&c=hi%20there&f=25&f=50&f=a&z=p&z=t", OAuthAuthorization.normalizeRequestParameters(params));

        // test cases from http://wiki.oauth.net/TestCases - Normalize Request Parameters (section 9.1.1)
        params = new HttpParameter[]{
                new HttpParameter("name", ""),
        };
        assertEquals("name=", OAuthAuthorization.normalizeRequestParameters(params));


        params = new HttpParameter[]{
                new HttpParameter("a", "b"),
        };
        assertEquals("a=b", OAuthAuthorization.normalizeRequestParameters(params));

        params = new HttpParameter[]{
                new HttpParameter("a", "b"),
                new HttpParameter("c", "d"),
        };
        assertEquals("a=b&c=d", OAuthAuthorization.normalizeRequestParameters(params));

        params = new HttpParameter[]{
                new HttpParameter("a", "x!y"),
                new HttpParameter("a", "x y"),
        };
        assertEquals("a=x%20y&a=x%21y", OAuthAuthorization.normalizeRequestParameters(params));

        params = new HttpParameter[]{
                new HttpParameter("x!y", "a"),
                new HttpParameter("x", "a"),
        };
        assertEquals("x=a&x%21y=a", OAuthAuthorization.normalizeRequestParameters(params));


    }

    public void testConstructRequestURL() throws Exception {
        //http://oauth.net/core/1.0#rfc.section.9.1.2
        assertEquals("http://example.com/resource", OAuthAuthorization.constructRequestURL("HTTP://Example.com:80/resource?id=123"));
        assertEquals("http://example.com:8080/resource", OAuthAuthorization.constructRequestURL("HTTP://Example.com:8080/resource?id=123"));
        assertEquals("http://example.com/resource", OAuthAuthorization.constructRequestURL("HTTP://Example.com/resource?id=123"));
        assertEquals("https://example.com/resource", OAuthAuthorization.constructRequestURL("HTTPS://Example.com:443/resource?id=123"));
        assertEquals("https://example.com:8443/resource", OAuthAuthorization.constructRequestURL("HTTPS://Example.com:8443/resource?id=123"));
        assertEquals("https://example.com/resource", OAuthAuthorization.constructRequestURL("HTTPS://Example.com/resource?id=123"));
    }

    public void testXAuth() throws Exception {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(desktopConsumerKey);
        builder.setOAuthConsumerSecret(desktopConsumerSecret);
        Twitter twitter = new TwitterFactory(builder.build()).getInstance();
        try {
            twitter.getOAuthAccessToken(id1.screenName, id2.password);
            fail("expecting TwitterException");
        } catch (TwitterException te) {
            // id1 doesn't have access to xAuth
            assertEquals(401, te.getStatusCode());
        }
        InputStream is = OAuthTest.class.getResourceAsStream("/xauth-test.properties");
        if (null == is) {
            System.out.println("xauth-test.properties not found. skipping xAuth test.");
        } else {
            Properties props = new Properties();
            props.load(is);
            Configuration conf = new PropertyConfiguration(props);
            twitter = new TwitterFactory(conf).getInstance();
            AccessToken at = twitter.getOAuthAccessToken(id1.screenName, id1.password);
            twitter.updateStatus(new Date() + ": xAuth test.");

            twitter = new TwitterFactory().getInstance();
            twitter.setOAuthConsumer(conf.getOAuthConsumerKey(), conf.getOAuthConsumerSecret());
            twitter.getOAuthAccessToken(id1.screenName, id1.password);
        }

    }

    private void trySerializable(Object obj) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new ByteArrayOutputStream());
        oos.writeObject(obj);
    }
}
