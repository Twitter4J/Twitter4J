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
package twitter4j.media;

import twitter4j.Twitter;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;
import twitter4j.http.Authorization;
import twitter4j.http.OAuthAuthorization;

import static twitter4j.media.ImageUploader.*;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.8
 */
public final class ImageUploaderFactory {
    private final Provider defaultProvider;
    private final String apiKey;

    /**
     * Creates an ImageUploaderFactory with default configuration
     */
    public ImageUploaderFactory() {
        this(ConfigurationContext.getInstance());
    }

    /**
     * Creates an ImageUploaderFactory with the specified configuration
     */
    public ImageUploaderFactory(Configuration conf) {
        String mediaProvider = conf.getMediaProvider().toLowerCase();
        if ("imgly".equals(mediaProvider)) {
            defaultProvider = IMG_LY;
        } else if ("twipple".equals(mediaProvider)) {
            defaultProvider = TWIPPLE;
        } else if ("twitgoo".equals(mediaProvider)) {
            defaultProvider = TWITGOO;
        } else if ("twitpic".equals(mediaProvider)) {
            defaultProvider = TWITPIC;
        } else if ("yfrog".equals(mediaProvider)) {
            defaultProvider = YFROG;
        } else {
            throw new IllegalArgumentException("unsupported media provider:" + mediaProvider);
        }
        apiKey = conf.getMediaProviderAPIKey();
    }

    /**
     *
     * @param twitter
     * @return
     */
    public ImageUploader getInstance(Twitter twitter) {
        return getInstance(twitter, defaultProvider);
    }

    /**
     *
     * @param twitter
     * @param provider
     * @return
     */
    public ImageUploader getInstance(Twitter twitter, Provider provider) {
        Authorization authorization = twitter.getAuthorization();
        if (!(authorization instanceof OAuthAuthorization)) {
            throw new IllegalArgumentException("OAuth authorized instance is required.");
        }
        OAuthAuthorization oauth = (OAuthAuthorization) authorization;

        if (provider == IMG_LY) {
            return new ImgLyUploader(oauth);
        } else if (provider == TWEET_PHOTO) {
            return new TweetPhotoUploader(apiKey, oauth);
        } else if (provider == TWIPPLE) {
            return new TwippleUploader(oauth);
        } else if (provider == TWITGOO) {
            return new TwitgooUploader(oauth);
        } else if (provider == TWITPIC) {
            return new TwitpicUploader(apiKey, oauth);
        } else if (provider == YFROG) {
            return new YFrogUploader(oauth);
        } else {
            throw new AssertionError("Unknown provider");
        }
    }
}
