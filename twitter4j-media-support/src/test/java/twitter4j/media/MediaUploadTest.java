/*
Copyright (c) 2007-2011, Yusuke Yamamoto
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
package twitter4j.media;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import junit.framework.TestCase;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

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
    private String message = "Twitter4J image upload test";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    private Configuration getConfiguration(String apiKey){
        return new ConfigurationBuilder().setMediaProviderAPIKey(apiKey).build();
    }

    public void testProviders() throws Exception{
        Configuration conf;
        ImageUploaderFactory factory;
        conf = new ConfigurationBuilder().setMediaProvider(MediaProvider.IMG_LY.getName()).build();
        factory = new ImageUploaderFactory(conf);
        conf = new ConfigurationBuilder().setMediaProvider(MediaProvider.PLIXI.getName()).build();
        factory = new ImageUploaderFactory(conf);
        conf = new ConfigurationBuilder().setMediaProvider(MediaProvider.TWIPPLE.getName()).build();
        factory = new ImageUploaderFactory(conf);
        conf = new ConfigurationBuilder().setMediaProvider(MediaProvider.TWITGOO.getName()).build();
        factory = new ImageUploaderFactory(conf);
        conf = new ConfigurationBuilder().setMediaProvider(MediaProvider.TWITPIC.getName()).build();
        factory = new ImageUploaderFactory(conf);
        conf = new ConfigurationBuilder().setMediaProvider(MediaProvider.YFROG.getName()).build();
        factory = new ImageUploaderFactory(conf);
    }

    public void testNonexistingFileUpload() throws Exception {

        ImageUploaderFactory factory = new ImageUploaderFactory(getConfiguration("d414e7c05f440c867990fbb08286bdfd"));
        ImageUpload upload = factory.getInstance(MediaProvider.TWITPIC);
        try {
            String url = upload.upload(new File("foobar"));
        } catch (TwitterException te) {
            if(!(te.getCause() instanceof FileNotFoundException)){
                fail("expecting FileNotFoundException");
            }
        }
    }

    public void testTwitPicOAuthUpload() throws Exception {
        InputStream is = getClass().getResourceAsStream("/" + fileName);
        try {
            ImageUploaderFactory factory = new ImageUploaderFactory(getConfiguration("d414e7c05f440c867990fbb08286bdfd"));
            ImageUpload upload = factory.getInstance(MediaProvider.TWITPIC);
            String url = upload.upload(fileName, is, message);
            assertTrue(url.length() > 0);
        } finally {
            is.close();
        }
    }

    public void testYFrogUpload() throws Exception {
        InputStream is = getClass().getResourceAsStream("/" + fileName);
        try {
            ImageUploaderFactory factory = new ImageUploaderFactory(getConfiguration(null));
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
            ImageUploaderFactory factory = new ImageUploaderFactory(getConfiguration("b30d6580-46ce-49a1-b469-31777a326938"));
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
            ImageUploaderFactory factory = new ImageUploaderFactory(getConfiguration(null));
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
            ImageUploaderFactory factory = new ImageUploaderFactory(getConfiguration(null));
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
            ImageUploaderFactory factory = new ImageUploaderFactory(getConfiguration(null));
            ImageUpload upload = factory.getInstance(MediaProvider.TWIPPLE);
            String url = upload.upload(fileName, is);
            assertTrue(url.length() > 0);
        } finally {
            is.close();
        }
    }

}
