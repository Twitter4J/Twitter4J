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

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 3.0.2
 * @see <a href="http://oembed.com">oEmded</a>
 */
@Generated(
        value = "generate-lazy-objects.sh",
        comments = "This is Tool Generated Code. DO NOT EDIT",
        date = "2012-11-29"
)
final class LazyOEmbed implements twitter4j.OEmbed {
    private twitter4j.internal.http.HttpResponse res;
    private z_T4JInternalFactory factory;
    private OEmbed target = null;

    LazyOEmbed(twitter4j.internal.http.HttpResponse res, z_T4JInternalFactory factory) {
        this.res = res;
        this.factory = factory;
    }

    private OEmbed getTarget() {
        if (target == null) {
            try {
                target = factory.createOEmbed(res);
            } catch (TwitterException e) {
                throw new TwitterRuntimeException(e);
            }
        }
        return target;
    }

    /**
     * The HTML required to display the resource. The HTML should have no padding or margins. Consumers may wish to load the HTML in an off-domain iframe to avoid XSS vulnerabilities. The markup should be valid XHTML 1.0 Basic.
     * @return The HTML required to display the resource.
     */
    public String getHtml() {
        return getTarget().getHtml();
    }


    /**
     * The name of the author/owner of the resource.
     * @return The name of the author/owner of the resource.
     */
    public String getAuthorName() {
        return getTarget().getAuthorName();
    }


    /**
     * The url of the resource provider.<br>
     * The source URL of the image. Consumers should be able to insert this URL into an &lt;img&gt; element. Only HTTP and HTTPS URLs are valid.
     * @return The url of the resource provider.
     */
    public String getURL() {
        return getTarget().getURL();
    }


    /**
     * The oEmbed version number.
     * @return The oEmbed version number.
     */
    public String getVersion() {
        return getTarget().getVersion();
    }

    /**
     * The suggested cache lifetime for this resource, in seconds. Consumers may choose to use this value or not.
     * @return The suggested cache lifetime for this resource, in seconds. Consumers may choose to use this value or not.
     */
    public long getCacheAge() {
        return getTarget().getCacheAge();
    }


    /**
     * A URL for the author/owner of the resource.
     * @return A URL for the author/owner of the resource.
     */
    public String getAuthorURL() {
        return getTarget().getAuthorURL();
    }


    /**
     * The width in pixels of the image specified in the url parameter.
     * @return The width in pixels of the image specified in the url parameter.
     */
    public int getWidth() {
        return getTarget().getWidth();
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
        if (!(o instanceof OEmbed)) return false;
        return getTarget().equals(o);
    }

    @Override
    public int hashCode() {
        return getTarget().hashCode();
    }

    @Override
    public String toString() {
        return "LazyOEmbed{" +
                "target=" + getTarget() +
                "}";
    }
}
