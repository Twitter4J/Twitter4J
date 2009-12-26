/*
Copyright (c) 2007-2009, Yusuke Yamamoto
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
package twitter4j;

import java.net.URI;

/**
 * A data interface representing Basic list information element
 *
 * @author Dan Checkoway - dcheckoway at gmail.com
 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-GET-list-id">REST API Documentation - Basic list information element</a>
 */
public interface UserList extends TwitterResponse, java.io.Serializable {
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

}
