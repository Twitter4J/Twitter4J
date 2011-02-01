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
import twitter4j.http.Authorization;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.8
 * @deprecated use {@link ImageUploadFactory} instead
 */
public final class ImageUploaderFactory {
    ImageUploadFactory delegate;

    /**
     * Creates an ImageUploaderFactory with default configuration
     *
     * @deprecated use {@link ImageUploadFactory} instead
     */
    public ImageUploaderFactory() {
        delegate = new ImageUploadFactory();
    }

    /**
     * Creates an ImageUploaderFactory with the specified configuration
     *
     * @deprecated use {@link ImageUploadFactory} instead
     */
    public ImageUploaderFactory(Configuration conf) {
        delegate = new ImageUploadFactory(conf);
    }

    /**
     * Returns an ImageUpload instance associated with the default media provider
     *
     * @return ImageUpload
     * @deprecated use {@link twitter4j.media.ImageUploadFactory#getInstance()} instead
     */
    public ImageUpload getInstance() {
        return delegate.getInstance();
    }

    /**
     * Returns an ImageUpload instance associated with the default media provider
     *
     * @param authorization authorization
     * @return ImageUpload
     * @since Twitter4J 2.1.11
     * @deprecated use {@link twitter4j.media.ImageUploadFactory#getInstance(twitter4j.http.Authorization)} instead
     */
    public ImageUpload getInstance(Authorization authorization) {
        return delegate.getInstance(authorization);
    }

    /**
     * Returns an ImageUploader instance associated with the specified media provider
     *
     * @param mediaProvider media provider
     * @return ImageUploader
     * @deprecated use {@link twitter4j.media.ImageUploadFactory#getInstance(MediaProvider)} instead
     */
    public ImageUpload getInstance(MediaProvider mediaProvider) {
        return delegate.getInstance(mediaProvider);
    }

    /**
     * Returns an ImageUpload instance associated with the specified media provider
     *
     * @param mediaProvider media provider
     * @param authorization authorization
     * @return ImageUpload
     * @since Twitter4J 2.1.11
     * @deprecated use {@link twitter4j.media.ImageUploadFactory#getInstance(MediaProvider, twitter4j.http.Authorization)} instead
     */
    public ImageUpload getInstance(MediaProvider mediaProvider, Authorization authorization) {
        return delegate.getInstance(mediaProvider, authorization);
    }
}
