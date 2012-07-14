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
package twitter4j.internal.json;

import twitter4j.*;

import javax.annotation.Generated;
import java.util.Map;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.3
 */
@Generated(
        value = "generate-lazy-objects.sh",
        comments = "This is Tool Generated Code. DO NOT EDIT",
        date = "2011-07-13"
)
final class LazyTwitterAPIConfiguration implements twitter4j.TwitterAPIConfiguration {
    private twitter4j.internal.http.HttpResponse res;
    private z_T4JInternalFactory factory;
    private TwitterAPIConfiguration target = null;

    LazyTwitterAPIConfiguration(twitter4j.internal.http.HttpResponse res, z_T4JInternalFactory factory) {
        this.res = res;
        this.factory = factory;
    }

    private TwitterAPIConfiguration getTarget() {
        if (target == null) {
            try {
                target = factory.createTwitterAPIConfiguration(res);
            } catch (TwitterException e) {
                throw new TwitterRuntimeException(e);
            }
        }
        return target;
    }

    public int getPhotoSizeLimit() {
        return getTarget().getPhotoSizeLimit();
    }


    public int getShortURLLength() {
        return getTarget().getShortURLLength();
    }


    public int getShortURLLengthHttps() {
        return getTarget().getShortURLLengthHttps();
    }


    public int getCharactersReservedPerMedia() {
        return getTarget().getCharactersReservedPerMedia();
    }


    public Map<Integer, MediaEntity.Size> getPhotoSizes() {
        return getTarget().getPhotoSizes();
    }


    public String[] getNonUsernamePaths() {
        return getTarget().getNonUsernamePaths();
    }


    public int getMaxMediaPerUpload() {
        return getTarget().getMaxMediaPerUpload();
    }

    public RateLimitStatus getRateLimitStatus() {
        return getTarget().getRateLimitStatus();
    }

    public int getAccessLevel() {
        return getTarget().getAccessLevel();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TwitterAPIConfiguration)) return false;
        return getTarget().equals(o);
    }

    @Override
    public int hashCode() {
        return getTarget().hashCode();
    }

    @Override
    public String toString() {
        return "LazyTwitterAPIConfiguration{" +
                "target=" + getTarget() +
                "}";
    }
}
