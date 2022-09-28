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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.fail;

public class TwitterTestBase {

    static Twitter twitter1, twitter2, twitter3,
            twitterAPIBestFriend1, twitterAPIBestFriend2,
            rwPrivateMessage, readonly;
    static protected Properties p;

    static protected String numberId, numberPass;
    static protected long numberIdId;
    static protected TestUserInfo id1, id2, id3, bestFriend1, bestFriend2, rwPrivate;
    static protected Configuration conf1, conf2, conf3, bestFriend1Conf, bestFriend2Conf, rwPrivateConf;

    static class TestUserInfo {
        public final String screenName;
        public final String password;
        public final long id;
        public final String accessToken;
        public final String accessTokenSecret;

        TestUserInfo(String screenName) {

            this.screenName = p.getProperty(screenName + ".user");
            this.password = p.getProperty(screenName + ".password");
            long id = -1L;
            try {
                id = Long.parseLong(p.getProperty(screenName + ".id"));
            } catch (NumberFormatException nfe) {
                fail("failed to parse:" + p.getProperty(screenName + ".id"));
            }
            this.id = id;
            this.accessToken = p.getProperty(screenName + ".oauth.accessToken");
            this.accessTokenSecret = p.getProperty(screenName + ".oauth.accessTokenSecret");
        }
    }

    static String desktopConsumerSecret;
    static String desktopConsumerKey;
    static String browserConsumerSecret;
    static String browserConsumerKey;

    private static int currentIndex;
    private static int maxTestPropertyIndex = -1;

    static {
        // set properties in test.properties to System property
        InputStream resource;
        try {
            resource = new FileInputStream("test.properties");
        } catch (FileNotFoundException fnfe) {
            try {
                resource = new FileInputStream("../test.properties");
            } catch (FileNotFoundException fnfe2) {
                resource = TwitterTestBase.class.getResourceAsStream("/test.properties");
            }
        }

        if (resource != null) {
            Properties properties = new Properties();
            try {
                properties.load(resource);
                resource.close();
            } catch (IOException ignore) {
            }
            for (String propertyName : properties.stringPropertyNames()) {
                System.setProperty(propertyName, properties.getProperty(propertyName));
            }
        }
        // look up the number of property sets
        for (int i = 0; i < 100; i++) {
            String propName = i + ".id1.id";
            String envName = "t4j" + propName.replaceAll("\\.", "_");
            if (System.getProperty(propName, System.getenv(envName)) != null) {
                maxTestPropertyIndex = i;
            } else {
                break;
            }
        }
        currentIndex = (int) (System.currentTimeMillis() % (maxTestPropertyIndex + 1));
    }

    /**
     * rotate test property file
     *
     * @return test[index].properties as InputStream
     */
    private static Properties getNextProperty() {
        currentIndex++;
        if (currentIndex > maxTestPropertyIndex) {
            currentIndex = 0;
        }
        Properties props = new Properties();

        String prefix = currentIndex + ".";
        String envPrefix = "t4j" + currentIndex + "_";
        Map<String, String> map = System.getenv();
        for (String key : map.keySet()) {
            if (key.startsWith(envPrefix)) {
                props.setProperty(key.substring(envPrefix.length()).replaceAll("_", "."), map.get(key));
            }
        }
        for (String key : System.getProperties().stringPropertyNames()) {
            if (key.startsWith(prefix)) {
                props.setProperty(key.substring(prefix.length()), System.getProperty(key));
            }
        }
        return props;
    }

    static {
        p = getNextProperty();

        desktopConsumerSecret = p.getProperty("desktop.oauth.consumerSecret");
        desktopConsumerKey = p.getProperty("desktop.oauth.consumerKey");
        browserConsumerSecret = p.getProperty("browser.oauth.consumerSecret");
        browserConsumerKey = p.getProperty("browser.oauth.consumerKey");

        conf1 = new ConfigurationBuilder().load(subProperty(p, "id1")).build();
        id1 = new TestUserInfo("id1");
        conf2 = new ConfigurationBuilder().load(subProperty(p, "id2")).build();
        id2 = new TestUserInfo("id2");
        conf3 = new ConfigurationBuilder().load(subProperty(p, "id3")).build();
        id3 = new TestUserInfo("id3");
        rwPrivateMessage = new TwitterFactory(new ConfigurationBuilder().load(subProperty(p, "rwprivate")).build()).getInstance();
        bestFriend1Conf = new ConfigurationBuilder().load(subProperty(p, "bestFriend1")).build();
        bestFriend1 = new TestUserInfo("bestFriend1");
        bestFriend2Conf = new ConfigurationBuilder().load(subProperty(p, "bestFriend2")).build();
        bestFriend2 = new TestUserInfo("bestFriend2");
        rwPrivate = new TestUserInfo("rwprivate");
        rwPrivateConf = new ConfigurationBuilder().load(subProperty(p, "rwprivate")).build();

        numberId = p.getProperty("numberid.user");
        numberPass = p.getProperty("numberid.password");
//        id1id = Integer.valueOf(p.getProperty("id1id"));
        numberIdId = Long.parseLong(p.getProperty("numberid.id"));

        twitter1 = new TwitterFactory(conf1).getInstance();

        twitter2 = new TwitterFactory(conf2).getInstance();

        twitter3 = new TwitterFactory(conf3).getInstance();

        twitterAPIBestFriend1 = new TwitterFactory(bestFriend1Conf).getInstance();

        twitterAPIBestFriend2 = new TwitterFactory(bestFriend2Conf).getInstance();


        readonly = new TwitterFactory(new ConfigurationBuilder().load(subProperty(p, "readonly")).build()).getInstance();

        System.out.println("twitter1:"+twitter1);
        System.out.println("twitter2:"+twitter2);
        System.out.println("twitter3:"+twitter3);
        System.out.println("twitter readoonly:"+readonly);
    }

    static Properties subProperty(Properties p, String path){
        Properties p1 = new Properties((Properties) p.clone());
        p.keySet().forEach(key -> {
            String ks = (String) key;
            if (ks.startsWith(path+".")) {
                p1.setProperty(ks.replaceFirst("^" + path + "\\.", ""), (String) p.get(key));
            }
        });
        return p1;
    }
}
