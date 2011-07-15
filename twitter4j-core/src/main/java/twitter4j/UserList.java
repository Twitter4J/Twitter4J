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

package twitter4j;

import java.net.URI;

/**
 * A data interface representing Basic list information element
 *
 * @author Dan Checkoway - dcheckoway at gmail.com
 */
public interface UserList extends Comparable<UserList>, TwitterResponse, java.io.Serializable {
    /**
     * Returns the id of the list
     *
     * @return the id of the list
     */
    int getId();

    /**
     * Returns the name of the list
     *
     * @return the name of the list
     */
    String getName();

    /**
     * Returns the full name of the list
     *
     * @return the full name of the list
     */
    String getFullName();

    /**
     * Returns the slug of the list
     *
     * @return the slug of the list
     */
    String getSlug();

    /**
     * Returns the description of the list
     *
     * @return the description of the list
     */
    String getDescription();

    /**
     * Returns the subscriber count of the list
     *
     * @return the subscriber count of the list
     */
    int getSubscriberCount();

    /**
     * Returns the member count of the list
     *
     * @return the member count of the list
     */
    int getMemberCount();

    /**
     * Returns the uri of the list
     *
     * @return the uri of the list
     */
    URI getURI();

    /**
     * tests if the list is public
     *
     * @return if the list is public
     */
    boolean isPublic();

    /**
     * Returns the user of the list
     *
     * @return the user of the list
     */
    User getUser();

    /**
     * Returns if the authenticated user is following the list
     *
     * @return if the authenticated user is following the list
     */
    boolean isFollowing();
}
