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

import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.conf.PropertyConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

/**
 * @author Takao Nakaguchi - takao.nakaguchi at gmail.com
 * @author withgod - noname at withgod.jp
 * @since Twitter4J 2.1.8
 */
public class MediaUploadTest extends TwitterTestBase {
    public MediaUploadTest(String name) {
        super(name);

    }

    private final String fileName = "t4j.jpeg";
    private final String message = "Twitter4J image upload test" + new Date().toString();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    private Configuration getConfiguration(String apiKey) {
        return new ConfigurationBuilder().setMediaProviderAPIKey(apiKey).build();
    }

    public void testProviders() throws Exception {
        Configuration conf;
        conf = new ConfigurationBuilder().setMediaProvider(MediaProvider.TWITTER.name()).build();
        new ImageUploadFactory(conf);
    }

    public void testNonexistingFileUpload() throws Exception {

        ImageUploadFactory factory = new ImageUploadFactory(getConfiguration("d414e7c05f440c867990fbb08286bdfd"));
        ImageUpload upload = factory.getInstance(MediaProvider.IMG_LY);
        try {
            upload.upload(new File("foobar"));
        } catch (TwitterException te) {
            if (!(te.getCause() instanceof FileNotFoundException)) {
                fail("expecting FileNotFoundException");
            }
        }
    }

    public void testTwitterUpload() throws Exception {
        InputStream is = getClass().getResourceAsStream("/" + fileName);
        try {
            ImageUploadFactory factory = new ImageUploadFactory(conf1);
            ImageUpload upload = factory.getInstance(MediaProvider.TWITTER);
            String url = upload.upload(fileName, is, message);
            assertTrue(url.length() > 0);
        } finally {
            is.close();
        }
    }

}
