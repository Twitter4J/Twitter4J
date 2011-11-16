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

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.Configuration;

import java.io.File;
import java.io.InputStream;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.5
 */
class TwitterUpload implements ImageUpload {
    private Twitter twitter;

    public TwitterUpload(Configuration conf, OAuthAuthorization oauth) {
        twitter = new TwitterFactory(conf).getInstance(oauth);
    }

    @Override
    public String upload(File image, String message) throws TwitterException {
        return twitter.updateStatus(new StatusUpdate(message).media(image)).getText();
    }

    @Override
    public String upload(File image) throws TwitterException {
        return twitter.updateStatus(new StatusUpdate("").media(image)).getText();
    }

    @Override
    public String upload(String imageFileName, InputStream imageBody) throws TwitterException {
        return twitter.updateStatus(new StatusUpdate("").media(imageFileName, imageBody)).getText();
    }

    @Override
    public String upload(String imageFileName, InputStream imageBody, String message) throws TwitterException {
        return twitter.updateStatus(new StatusUpdate(message).media(imageFileName, imageBody)).getText();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TwitterUpload that = (TwitterUpload) o;

        if (twitter != null ? !twitter.equals(that.twitter) : that.twitter != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return twitter != null ? twitter.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TwitterUpload{" +
                "twitter=" + twitter +
                '}';
    }
}
