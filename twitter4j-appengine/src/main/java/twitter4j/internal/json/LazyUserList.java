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
import java.net.URI;

/**
 * A data class representing Basic list information element
 *
 * @author Dan Checkoway - dcheckoway at gmail.com
 */
@Generated(
        value = "generate-lazy-objects.sh",
        comments = "This is Tool Generated Code. DO NOT EDIT",
        date = "2011-07-13"
)
final class LazyUserList implements twitter4j.UserList {
    private twitter4j.internal.http.HttpResponse res;
    private z_T4JInternalFactory factory;
    private UserList target = null;

    LazyUserList(twitter4j.internal.http.HttpResponse res, z_T4JInternalFactory factory) {
        this.res = res;
        this.factory = factory;
    }

    private UserList getTarget() {
        if (target == null) {
            try {
                target = factory.createAUserList(res);
            } catch (TwitterException e) {
                throw new TwitterRuntimeException(e);
            }
        }
        return target;
    }

    /**
     * Returns the id of the list
     *
     * @return the id of the list
     */
    public int getId() {
        return getTarget().getId();
    }


    /**
     * Returns the name of the list
     *
     * @return the name of the list
     */
    public String getName() {
        return getTarget().getName();
    }


    /**
     * Returns the full name of the list
     *
     * @return the full name of the list
     */
    public String getFullName() {
        return getTarget().getFullName();
    }


    /**
     * Returns the slug of the list
     *
     * @return the slug of the list
     */
    public String getSlug() {
        return getTarget().getSlug();
    }


    /**
     * Returns the description of the list
     *
     * @return the description of the list
     */
    public String getDescription() {
        return getTarget().getDescription();
    }


    /**
     * Returns the subscriber count of the list
     *
     * @return the subscriber count of the list
     */
    public int getSubscriberCount() {
        return getTarget().getSubscriberCount();
    }


    /**
     * Returns the member count of the list
     *
     * @return the member count of the list
     */
    public int getMemberCount() {
        return getTarget().getMemberCount();
    }


    /**
     * Returns the uri of the list
     *
     * @return the uri of the list
     */
    public URI getURI() {
        return getTarget().getURI();
    }


    /**
     * tests if the list is public
     *
     * @return if the list is public
     */
    public boolean isPublic() {
        return getTarget().isPublic();
    }


    /**
     * Returns the user of the list
     *
     * @return the user of the list
     */
    public User getUser() {
        return getTarget().getUser();
    }


    /**
     * Returns if the authenticated user is following the list
     *
     * @return if the authenticated user is following the list
     */
    public boolean isFollowing() {
        return getTarget().isFollowing();
    }

    public RateLimitStatus getRateLimitStatus() {
        return getTarget().getRateLimitStatus();
    }

    public int getAccessLevel() {
        return getTarget().getAccessLevel();
    }

    public int compareTo(UserList target) {
        return getTarget().compareTo(target);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserList)) return false;
        return getTarget().equals(o);
    }

    @Override
    public int hashCode() {
        return getTarget().hashCode();
    }

    @Override
    public String toString() {
        return "LazyUserList{" +
                "target=" + getTarget() +
                "}";
    }
}
