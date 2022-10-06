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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
@Execution(ExecutionMode.CONCURRENT)
class OAuthTest extends TwitterTestBase {

    @Test
    void testDeterministic() {
        Twitter twitter1 = Twitter.newBuilder().load(subProperty(p, "id1")).build();
        Twitter twitter2 = Twitter.newBuilder().load(subProperty(p, "id1")).build();
        assertEquals(twitter1, twitter2);
    }

    @Test
    void testOAuth() throws Exception {
        String oAuthAccessToken = p.getProperty("id1.oauth.accessToken");
        String oAuthAccessTokenSecret = p.getProperty("id1.oauth.accessTokenSecret");
        String oAuthConsumerKey = p.getProperty("oauth.consumerKey");
        String oAuthConsumerSecret = p.getProperty("oauth.consumerSecret");
        Twitter twitter = Twitter.newBuilder()
                .oAuthAccessToken(oAuthAccessToken, oAuthAccessTokenSecret)
                .oAuthConsumer(oAuthConsumerKey, oAuthConsumerSecret).build();
        twitter.users().verifyCredentials();
    }

    @Disabled
    @Test
    void testDesktopClient() throws Exception {

        // desktop client - requiring pin
        OAuthAuthorization oAuthAuthorization = OAuthAuthorization.getInstance(browserConsumerKey, browserConsumerSecret);
        RequestToken rt = oAuthAuthorization.getOAuthRequestToken();
        //noinspection ResultOfMethodCallIgnored
        rt.hashCode();
        // trying to get an access token without permitting the request token.
        try {
            oAuthAuthorization.getOAuthAccessToken();
            fail();
        } catch (TwitterException te) {
            assertEquals(401, te.getStatusCode());
        }
        OAuthAuthorization oAuthAuthorization2 = OAuthAuthorization.getInstance(desktopConsumerKey, desktopConsumerSecret);
        AccessToken at = getAccessToken(oAuthAuthorization2, rt.getAuthorizationURL(), rt, numberId, numberPass, true);
        try {
            oAuthAuthorization2.getOAuthRequestToken();
        } catch (TwitterException te) {
            fail("expecting IllegalStateException as access token is already available.");
        } catch (IllegalStateException ignored) {
        }


        assertEquals(at.getScreenName(), numberId);
        assertEquals(at.getUserId(), numberIdId);
        AccessToken at1 = oAuthAuthorization2.getOAuthAccessToken();
        assertEquals(at, at1);
    }

    @Test
    void testIllegalStatus() throws Exception {
        try {
            OAuthAuthorization.getInstance().getOAuthAccessToken();
            fail("should throw IllegalStateException since request token hasn't been acquired.");
        } catch (IllegalStateException ignore) {
        }
    }

    @Disabled
    @Test
    void testSigninWithTwitter() throws Exception {
        // browser client - not requiring pin
        OAuthAuthorization oauth = OAuthAuthorization.getInstance(browserConsumerKey, browserConsumerSecret);
        RequestToken rt = oauth.getOAuthRequestToken("http://twitter4j.org/ja/index.html");

        AccessToken at = getAccessToken(oauth, rt.getAuthenticationURL(), rt, id1.screenName, id1.password, false);

        assertEquals(at.getScreenName(), id1.screenName);
        assertEquals(at.getUserId(), id1.id);

    }

    @Disabled
    @Test
    void testBrowserClient() throws Exception {
        // browser client - not requiring pin
        OAuthAuthorization oauth = OAuthAuthorization.getInstance(browserConsumerKey, browserConsumerSecret);
        RequestToken rt = oauth.getOAuthRequestToken("http://twitter4j.org/ja/index.html");

        AccessToken at = getAccessToken(oauth, rt.getAuthorizationURL(), rt, id1.screenName, id1.password, false);
        assertEquals(at.getScreenName(), id1.screenName);
        assertEquals(at.getUserId(), id1.id);
    }

    private AccessToken getAccessToken(OAuthAuthorization twitter, @SuppressWarnings("unused") String url, RequestToken rt, String screenName, String password, boolean pinRequired) throws TwitterException {

        BrowserVersion browserVersion = BrowserVersion.getDefault();
        HtmlUnitDriver driver = new HtmlUnitDriver(browserVersion);
        driver.get(rt.getAuthorizationURL());
        driver.findElement(By.name("session[username_or_email]")).sendKeys(screenName);
        driver.findElement(By.name("session[password]")).sendKeys(password);
        driver.findElement(By.id("allow")).click();

        if (pinRequired) {
            return twitter.getOAuthAccessToken(rt, driver.findElements(By.tagName("code")).get(0).getText());

        } else {
            String oauthVerifier = driver.getCurrentUrl().replaceFirst(".*&oauth_verifier=([a-zA-Z0-9]+)$", "$1");
            return twitter.getOAuthAccessToken(rt, oauthVerifier);

        }
    }

    @Test
    void testAccessToken() {
        AccessToken at = new AccessToken("oauth_token=6377362-kW0YV1ymaqEUCSHP29ux169mDeA4kQfhEuqkdvHk&oauth_token_secret=ghoTpd7LuMLHtJDyHkhYo40Uq5bWSxeCyOUAkbsOoOY&user_id=6377362&screen_name=twit4j2");
        assertEquals("6377362-kW0YV1ymaqEUCSHP29ux169mDeA4kQfhEuqkdvHk", at.getToken());
        assertEquals("ghoTpd7LuMLHtJDyHkhYo40Uq5bWSxeCyOUAkbsOoOY", at.getTokenSecret());
        assertEquals("twit4j2", at.getScreenName());
        assertEquals(6377362, at.getUserId());
    }


    @Test
    void testSign() throws Exception {

        {
            String baseStr = "GET&http%3A%2F%2Fphotos.example.net%2Fphotos&file%3Dvacation.jpg%26oauth_consumer_key%3Ddpf43f3p2l4k3l03%26oauth_nonce%3Dkllo9940pd9333jh%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1191242096%26oauth_token%3Dnnch734d00sl2jdk%26oauth_version%3D1.0%26size%3Doriginal";
            OAuthAuthorization oauth = OAuthAuthorization.getInstance("dpf43f3p2l4k3l03", "kd94hf93k423kf44");
            trySerializable(oauth);
            //http://wiki.oauth.net/TestCases
            assertEquals("tR3+Ty81lMeYAr/Fid0kMTYa/WM=", oauth.generateSignature(baseStr,
                    new RequestToken("nnch734d00sl2jdk",
                            "pfkkdhi9sl3r4s00"
                            , conf2.oAuthAuthorizationURL, conf2.oAuthAuthorizationURL)));

        }
        {
            OAuthAuthorization oauth = OAuthAuthorization.getInstance(desktopConsumerKey, "cs");
            assertEquals("egQqG5AJep5sJ7anhXju1unge2I=", oauth.generateSignature("bs",
                    new RequestToken("nnch734d00sl2jdk", "",
                            conf2.oAuthAuthenticationURL, conf2.oAuthAuthenticationURL)));
            assertEquals("VZVjXceV7JgPq/dOTnNmEfO0Fv8=", oauth.generateSignature("bs",
                    new RequestToken("nnch734d00sl2jdk", "ts",
                            conf2.oAuthAuthenticationURL, conf2.oAuthAuthenticationURL)));
        }
        {
            OAuthAuthorization oauth = OAuthAuthorization.getInstance(desktopConsumerKey, "kd94hf93k423kf44");
            assertEquals("tR3+Ty81lMeYAr/Fid0kMTYa/WM=", oauth.generateSignature("GET&http%3A%2F%2Fphotos.example.net%2Fphotos&file%3Dvacation.jpg%26oauth_consumer_key%3Ddpf43f3p2l4k3l03%26oauth_nonce%3Dkllo9940pd9333jh%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1191242096%26oauth_token%3Dnnch734d00sl2jdk%26oauth_version%3D1.0%26size%3Doriginal",
                    new RequestToken("nnch734d00sl2jdk", "pfkkdhi9sl3r4s00", conf2.oAuthAuthorizationURL, conf2.oAuthAuthenticationURL)));
        }
    }

    @Test
    void testHeader() {
        HttpParameter[] params = new HttpParameter[2];
        params[0] = new HttpParameter("file", "vacation.jpg");
        params[1] = new HttpParameter("size", "original");
        OAuthAuthorization oauth = OAuthAuthorization.getInstance("dpf43f3p2l4k3l03", "kd94hf93k423kf44");
        String expected = "OAuth oauth_consumer_key=\"dpf43f3p2l4k3l03\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"1191242096\",oauth_nonce=\"kllo9940pd9333jh\",oauth_version=\"1.0\",oauth_token=\"nnch734d00sl2jdk\",oauth_signature=\"tR3%2BTy81lMeYAr%2FFid0kMTYa%2FWM%3D\"";
        assertEquals(expected,
                oauth.generateAuthorizationHeader("GET", "http://photos.example.net/photos", params,
                        "kllo9940pd9333jh", "1191242096",
                        new RequestToken("nnch734d00sl2jdk", "pfkkdhi9sl3r4s00"
                                , conf2.oAuthAuthorizationURL, conf2.oAuthAuthenticationURL)));
    }

    @Test
    void testEncodeParameter() {
        //http://wiki.oauth.net/TestCases
        assertEquals("abcABC123", HttpParameter.encode("abcABC123"));
        assertEquals("-._~", HttpParameter.encode("-._~"));
        assertEquals("%25", HttpParameter.encode("%"));
        assertEquals("%2B", HttpParameter.encode("+"));
        assertEquals("%26%3D%2A", HttpParameter.encode("&=*"));
        assertEquals("%0A", HttpParameter.encode("\n"));
        //noinspection UnnecessaryUnicodeEscape
        assertEquals("%20", HttpParameter.encode("\u0020"));
        assertEquals("%7F", HttpParameter.encode("\u007F"));
        assertEquals("%C2%80", HttpParameter.encode("\u0080"));
        //noinspection UnnecessaryUnicodeEscape
        assertEquals("%E3%80%81", HttpParameter.encode("\u3001"));

        String unreserved = "abcdefghijklmnopqrstuvwzyxABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-._~";
        assertEquals(unreserved, HttpParameter.encode(unreserved));
    }

    @Test
    void testNormalizeRequestParameters() {
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

    @Test
    void testConstructRequestURL() {
        //http://oauth.net/core/1.0#rfc.section.9.1.2
        assertEquals("http://example.com/resource", OAuthAuthorization.constructRequestURL("HTTP://Example.com:80/resource?id=123"));
        assertEquals("http://example.com:8080/resource", OAuthAuthorization.constructRequestURL("HTTP://Example.com:8080/resource?id=123"));
        assertEquals("http://example.com/resource", OAuthAuthorization.constructRequestURL("HTTP://Example.com/resource?id=123"));
        assertEquals("https://example.com/resource", OAuthAuthorization.constructRequestURL("HTTPS://Example.com:443/resource?id=123"));
        assertEquals("https://example.com:8443/resource", OAuthAuthorization.constructRequestURL("HTTPS://Example.com:8443/resource?id=123"));
        assertEquals("https://example.com/resource", OAuthAuthorization.constructRequestURL("HTTPS://Example.com/resource?id=123"));
    }

    private void trySerializable(Object obj) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new ByteArrayOutputStream());
        oos.writeObject(obj);
    }
}
