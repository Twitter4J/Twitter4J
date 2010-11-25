/*
Copyright (c) 2007-2010, Yusuke Yamamoto
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
package twitter4j.examples.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.PropertyConfiguration;
import twitter4j.http.AccessToken;
import twitter4j.util.ImageUpload;

/**
 * Sample of the ImageUpload utility class. Uploads an image to YFrog with OAuth credentials specified in a properties file.
 * Usage: java twitter4j.examples.YFrogOAuthImageUpload <location of the twitter4j properties file> <image file to upload>.
 *
 * @author Rémy Rakic - remy.rakic at gmail.com
 * @since Twitter4J 2.1.3
 */
public class YFrogOAuthImageUpload {
    public static void main(String[] args) throws TwitterException, FileNotFoundException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Usage java twitter4j.examples.YFrogOAuthImageUpload <location of the twitter4j properties file with oauth properties> <image file to upload>");
        }

        String propertiesFile = args[0];
        if (!new File(propertiesFile).exists()) {
            throw new FileNotFoundException("The twitter4j properties file " + propertiesFile + " can't be found");
        }

        File image = new File(args[1]);
        if (!image.exists()) {
            throw new FileNotFoundException("The image to upload " + image.getAbsolutePath() + " does not exist");
        }

        Configuration conf = new PropertyConfiguration(new FileInputStream(propertiesFile));

        Twitter twitter = new TwitterFactory().getOAuthAuthorizedInstance(conf.getOAuthConsumerKey(), conf.getOAuthConsumerSecret(),
                new AccessToken(conf.getOAuthAccessToken(), conf.getOAuthAccessTokenSecret()));

        ImageUpload upload = ImageUpload.getYFrogUploader(twitter);

        String url = upload.upload(image);

        System.out.println("Successfully uploaded image to YFrog at " + url);
    }
}
