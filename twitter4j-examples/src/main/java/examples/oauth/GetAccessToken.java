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

package examples.oauth;

import twitter4j.AccessToken;
import twitter4j.OAuthAuthorization;
import twitter4j.RequestToken;
import twitter4j.TwitterException;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * Acquire access token
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.7
 */
public class GetAccessToken {
    /**
     * Usage: java  twitter4j.examples.oauth.GetAccessToken [consumer key] [consumer secret]
     *
     * @param args message
     */
    public static void main(String[] args) {
        File file = new File("twitter4j.properties");
        Properties prop = new Properties();
        try (InputStream is = new FileInputStream(file)) {
            if (file.exists()) {
                prop.load(is);
            }
            if (args.length < 2) {
                if (null == prop.getProperty("oauth.consumerKey")
                        && null == prop.getProperty("oauth.consumerSecret")) {
                    // consumer key/secret are not set in twitter4j.properties
                    System.out.println(
                            "Usage: java twitter4j.examples.oauth.GetAccessToken [consumer key] [consumer secret]");
                    System.exit(-1);
                }
            } else {
                prop.setProperty("oauth.consumerKey", args[0]);
                prop.setProperty("oauth.consumerSecret", args[1]);
                try (OutputStream os = new FileOutputStream("twitter4j.properties")) {
                    prop.store(os, "twitter4j.properties");
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(-1);
        }
        try {
            OAuthAuthorization oAuthAuthorization = OAuthAuthorization.getInstance();
            RequestToken requestToken = oAuthAuthorization.getOAuthRequestToken();
            System.out.println("Got request token.");
            System.out.println("Request token: " + requestToken.getToken());
            System.out.println("Request token secret: " + requestToken.getTokenSecret());
            AccessToken accessToken = null;

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (null == accessToken) {
                System.out.println("Open the following URL and grant access to your account:");
                System.out.println(requestToken.getAuthorizationURL());
                try {
                    Desktop.getDesktop().browse(new URI(requestToken.getAuthorizationURL()));
                } catch (UnsupportedOperationException | IOException ignore) {
                } catch (URISyntaxException e) {
                    throw new AssertionError(e);
                }
                System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
                String pin = br.readLine();
                try {
                    if (pin.length() > 0) {
                        accessToken = oAuthAuthorization.getOAuthAccessToken(requestToken, pin);
                    } else {
                        accessToken = oAuthAuthorization.getOAuthAccessToken(requestToken);
                    }
                } catch (TwitterException te) {
                    if (401 == te.getStatusCode()) {
                        System.out.println("Unable to get the access token.");
                    } else {
                        te.printStackTrace();
                    }
                }
            }
            System.out.println("Got access token.");
            System.out.println("Access token: " + accessToken.getToken());
            System.out.println("Access token secret: " + accessToken.getTokenSecret());

            try (OutputStream os = new FileOutputStream(file)) {
                prop.setProperty("oauth.accessToken", accessToken.getToken());
                prop.setProperty("oauth.accessTokenSecret", accessToken.getTokenSecret());
                prop.store(os, "twitter4j.properties");
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.exit(-1);
            }
            System.out.println("Successfully stored access token to " + file.getAbsolutePath() + ".");
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get accessToken: " + te.getMessage());
            System.exit(-1);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Failed to read the system input.");
            System.exit(-1);
        }
    }
}
