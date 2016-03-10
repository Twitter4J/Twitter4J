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

import twitter4j.auth.Authorization;
import twitter4j.auth.AuthorizationFactory;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;

import static twitter4j.media.MediaProvider.*;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.12
 */
public class ImageUploadFactory {
    private final Configuration conf;
    private final MediaProvider defaultMediaProvider;
    private final String apiKey;

    /**
     * Creates an ImageUploadFactory with default configuration
     */
    public ImageUploadFactory() {
        this(ConfigurationContext.getInstance());
    }

    /**
     * Creates an ImageUploadFactory with the specified configuration
     * @param conf configuration to be associated
     */
    public ImageUploadFactory(Configuration conf) {
        String mediaProvider = conf.getMediaProvider().toLowerCase();
        if ("twitter".equals(mediaProvider)) {
            defaultMediaProvider = TWITTER;
        } else if ("imgly".equals(mediaProvider) || "img_ly".equals(mediaProvider)) {
            defaultMediaProvider = IMG_LY;
        } else if ("twipple".equals(mediaProvider)) {
            defaultMediaProvider = TWIPPLE;
        } else if ("yfrog".equals(mediaProvider)) {
            defaultMediaProvider = YFROG;
        } else if ("mobypicture".equals(mediaProvider)) {
            defaultMediaProvider = MOBYPICTURE;
        } else {
            throw new IllegalArgumentException("unsupported media provider:" + mediaProvider);
        }
        this.conf = conf;
        apiKey = conf.getMediaProviderAPIKey();
    }

    /**
     * Returns an ImageUpload instance associated with the default media provider
     *
     * @return ImageUpload
     */
    public ImageUpload getInstance() {
        return getInstance(defaultMediaProvider);
    }

    /**
     * Returns an ImageUpload instance associated with the default media provider
     *
     * @param authorization authorization
     * @return ImageUpload
     * @since Twitter4J 2.1.11
     */
    public ImageUpload getInstance(Authorization authorization) {
        return getInstance(defaultMediaProvider, authorization);
    }

    /**
     * Returns an ImageUploader instance associated with the specified media provider
     *
     * @param mediaProvider media provider
     * @return ImageUploader
     */
    public ImageUpload getInstance(MediaProvider mediaProvider) {
        Authorization authorization = AuthorizationFactory.getInstance(conf);
        return getInstance(mediaProvider, authorization);
    }

    /**
     * Returns an ImageUpload instance associated with the specified media provider
     *
     * @param mediaProvider media provider
     * @param authorization authorization
     * @return ImageUpload
     * @since Twitter4J 2.1.11
     */
    public ImageUpload getInstance(MediaProvider mediaProvider, Authorization authorization) {
        if (!(authorization instanceof OAuthAuthorization)) {
            throw new IllegalArgumentException("OAuth authorization is required.");
        }
        OAuthAuthorization oauth = (OAuthAuthorization) authorization;
        if (mediaProvider == TWITTER) {
            return new TwitterUpload(conf, oauth);
        } else if (mediaProvider == IMG_LY) {
            return new ImgLyUpload(conf, oauth);
        } else if (mediaProvider == TWIPPLE) {
            return new TwippleUpload(conf, oauth);
        } else if (mediaProvider == YFROG) {
            return new YFrogUpload(conf, oauth);
        } else if (mediaProvider == MOBYPICTURE) {
            return new MobypictureUpload(conf, apiKey, oauth);
        } else {
            throw new AssertionError("Unknown provider");
        }
    }
}
