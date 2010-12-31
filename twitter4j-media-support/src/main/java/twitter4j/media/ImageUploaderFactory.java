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

import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;
import twitter4j.http.Authorization;
import twitter4j.http.AuthorizationFactory;
import twitter4j.http.OAuthAuthorization;

import static twitter4j.media.MediaProvider.*;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.8
 */
public final class ImageUploaderFactory {
    private final Configuration conf;
    private final MediaProvider defaultMediaProvider;
    private final String apiKey;
    private final OAuthAuthorization oauth;

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
        if ("imgly".equals(mediaProvider) || "img_ly".equals(mediaProvider)) {
            defaultMediaProvider = IMG_LY;
        } else if ("plixi".equals(mediaProvider)) {
            defaultMediaProvider = PLIXI;
        } else if ("twipple".equals(mediaProvider)) {
            defaultMediaProvider = TWIPPLE;
        } else if ("twitgoo".equals(mediaProvider)) {
            defaultMediaProvider = TWITGOO;
        } else if ("twitpic".equals(mediaProvider)) {
            defaultMediaProvider = TWITPIC;
        } else if ("yfrog".equals(mediaProvider)) {
            defaultMediaProvider = YFROG;
        } else {
            throw new IllegalArgumentException("unsupported media provider:" + mediaProvider);
        }
        this.conf = conf;
        apiKey = conf.getMediaProviderAPIKey();
        Authorization authorization = AuthorizationFactory.getInstance(conf, true);
        if (!(authorization instanceof OAuthAuthorization)) {
            throw new IllegalArgumentException("OAuth authorization is required.");
        }
        oauth = (OAuthAuthorization) authorization;
    }

    /**
     * Returns an ImageUploader instance associated with the default media provider
     * @return ImageUploader
     */
    public ImageUpload getInstance() {
        return getInstance(defaultMediaProvider);
    }

    /**
     * Returns an ImageUploader instance associated with the specified media provider
     * @param mediaProvider media provider
     * @return ImageUploader
     */
    public ImageUpload getInstance(MediaProvider mediaProvider) {

        if (mediaProvider == IMG_LY) {
            return new ImgLyUpload(conf, oauth);
        } else if (mediaProvider == PLIXI) {
            return new PlixiUpload(conf, apiKey, oauth);
        } else if (mediaProvider == TWIPPLE) {
            return new TwippleUpload(conf, oauth);
        } else if (mediaProvider == TWITGOO) {
            return new TwitgooUpload(conf, oauth);
        } else if (mediaProvider == TWITPIC) {
            return new TwitpicUpload(conf, apiKey, oauth);
        } else if (mediaProvider == YFROG) {
            return new YFrogUpload(conf, oauth);
        } else {
            throw new AssertionError("Unknown provider");
        }
    }
}
