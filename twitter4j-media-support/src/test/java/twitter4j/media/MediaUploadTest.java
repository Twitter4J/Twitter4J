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

package twitter4j.media;

import junit.framework.TestCase;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

/**
 * @author Takao Nakaguchi - takao.nakaguchi at gmail.com
 * @author withgod - noname at withgod.jp
 * @since Twitter4J 2.1.8
 */
public class MediaUploadTest extends TestCase {
    public MediaUploadTest(String name) {
        super(name);

    }

    private String fileName = "t4j.jpeg";
    private String message = "Twitter4J image upload test" + new Date().toString();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    private Configuration getConfiguration(String apiKey) {
        return new ConfigurationBuilder().setMediaProviderAPIKey(apiKey).build();
    }

    public void testProviders() throws Exception {
        Configuration conf;
        ImageUploadFactory factory;
        conf = new ConfigurationBuilder().setMediaProvider(MediaProvider.TWITTER.name()).build();
        factory = new ImageUploadFactory(conf);
        conf = new ConfigurationBuilder().setMediaProvider(MediaProvider.IMG_LY.name()).build();
        factory = new ImageUploadFactory(conf);
        conf = new ConfigurationBuilder().setMediaProvider(MediaProvider.PLIXI.name()).build();
        factory = new ImageUploadFactory(conf);
        conf = new ConfigurationBuilder().setMediaProvider(MediaProvider.TWIPPLE.name()).build();
        factory = new ImageUploadFactory(conf);
        conf = new ConfigurationBuilder().setMediaProvider(MediaProvider.TWITGOO.name()).build();
        factory = new ImageUploadFactory(conf);
        conf = new ConfigurationBuilder().setMediaProvider(MediaProvider.TWITPIC.name()).build();
        factory = new ImageUploadFactory(conf);
        conf = new ConfigurationBuilder().setMediaProvider(MediaProvider.YFROG.name()).build();
        factory = new ImageUploadFactory(conf);
        conf = new ConfigurationBuilder().setMediaProvider(MediaProvider.MOBYPICTURE.name()).build();
        factory = new ImageUploadFactory(conf);
        conf = new ConfigurationBuilder().setMediaProvider(MediaProvider.POSTEROUS.name()).build();
        factory = new ImageUploadFactory(conf);
    }

    public void testNonexistingFileUpload() throws Exception {

        ImageUploadFactory factory = new ImageUploadFactory(getConfiguration("d414e7c05f440c867990fbb08286bdfd"));
        ImageUpload upload = factory.getInstance(MediaProvider.TWITPIC);
        try {
            String url = upload.upload(new File("foobar"));
        } catch (TwitterException te) {
            if (!(te.getCause() instanceof FileNotFoundException)) {
                fail("expecting FileNotFoundException");
            }
        }
    }

    public void testTwitPicOAuthUpload() throws Exception {
        InputStream is = getClass().getResourceAsStream("/" + fileName);
        try {
            ImageUploadFactory factory = new ImageUploadFactory(getConfiguration("d414e7c05f440c867990fbb08286bdfd"));
            ImageUpload upload = factory.getInstance(MediaProvider.TWITPIC);
            String url = upload.upload(fileName, is, message);
            assertTrue(url.length() > 0);
        } finally {
            is.close();
        }
    }

    public void testTwitterUpload() throws Exception {
        InputStream is = getClass().getResourceAsStream("/" + fileName);
        try {
            ImageUploadFactory factory = new ImageUploadFactory();
            ImageUpload upload = factory.getInstance(MediaProvider.TWITTER);
            String url = upload.upload(fileName, is, message);
            assertTrue(url.length() > 0);
        } finally {
            is.close();
        }
    }

    public void testYFrogUpload() throws Exception {
        InputStream is = getClass().getResourceAsStream("/" + fileName);
        try {
            ImageUploadFactory factory = new ImageUploadFactory(getConfiguration(null));
            ImageUpload upload = factory.getInstance(MediaProvider.YFROG);
            String url = upload.upload(fileName, is, message);
            assertTrue(url.length() > 0);
        } finally {
            is.close();
        }
    }

    public void testPlixiUpload() throws Exception {
        InputStream is = getClass().getResourceAsStream("/" + fileName);
        try {
            ImageUploadFactory factory = new ImageUploadFactory(getConfiguration("b30d6580-46ce-49a1-b469-31777a326938"));
            ImageUpload upload = factory.getInstance(MediaProvider.PLIXI);
            String url = upload.upload(fileName, is, message);
            assertTrue(url.length() > 0);
        } finally {
            is.close();
        }
    }

    public void testImgLyUpload() throws Exception {
        InputStream is = getClass().getResourceAsStream("/" + fileName);
        try {
            ImageUploadFactory factory = new ImageUploadFactory(getConfiguration(null));
            ImageUpload upload = factory.getInstance(MediaProvider.IMG_LY);
            String url = upload.upload(fileName, is, message);
            assertTrue(url.length() > 0);
        } finally {
            is.close();
        }
    }

    public void testTwitgooUpload() throws Exception {
        InputStream is = getClass().getResourceAsStream("/" + fileName);
        try {
            ImageUploadFactory factory = new ImageUploadFactory(getConfiguration(null));
            ImageUpload upload = factory.getInstance(MediaProvider.TWITGOO);
            String url = upload.upload(fileName, is, message);
            assertTrue(url.length() > 0);
        } finally {
            is.close();
        }
    }

    public void testTwippleUpload() throws Exception {
        InputStream is = getClass().getResourceAsStream("/" + fileName);
        try {
            ImageUploadFactory factory = new ImageUploadFactory(getConfiguration(null));
            ImageUpload upload = factory.getInstance(MediaProvider.TWIPPLE);
            String url = upload.upload(fileName, is);
            assertTrue(url.length() > 0);
        } finally {
            is.close();
        }
    }

    public void testMobypictureUpload() throws Exception {
        InputStream is = getClass().getResourceAsStream("/" + fileName);
        try {
            ImageUploadFactory factory = new ImageUploadFactory(getConfiguration("IOUxMoqc8Snms9nU"));
            ImageUpload upload = factory.getInstance(MediaProvider.MOBYPICTURE);
            String url = upload.upload(fileName, is);
            assertTrue(url.length() > 0);
        } finally {
            is.close();
        }
    }

    public void testPosterousUpload() throws Exception {
        InputStream is = getClass().getResourceAsStream("/" + fileName);
        try {
            ImageUploadFactory factory = new ImageUploadFactory(getConfiguration(null));
            ImageUpload upload = factory.getInstance(MediaProvider.POSTEROUS);
            String url = upload.upload(fileName, is);
            assertTrue(url.length() > 0);
        } finally {
            is.close();
        }
    }

    public void testFromConfigurationUpload() throws Exception {
        InputStream is = getClass().getResourceAsStream("/" + fileName);
        try {
            ImageUploadFactory factory = new ImageUploadFactory();
            ImageUpload upload = factory.getInstance();
            System.out.println(upload);
            String url = upload.upload(fileName, is);
            assertTrue(url.length() > 0);
        } finally {
            is.close();
        }
    }
}
