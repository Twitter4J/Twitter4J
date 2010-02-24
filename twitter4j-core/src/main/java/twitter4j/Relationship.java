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


/**
 * A data interface that has detailed information about a relationship between two users
 * @author Perry Sakkaris - psakkaris at gmail.com
 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-friendships-show">REST API DOCUMENTATION</a>
 * @since Twitter4J 2.1.0
 */
 public interface Relationship extends TwitterResponse, java.io.Serializable {
    /**
     * Returns the source user id
     *
     * @return the source user id
     */
    int getSourceUserId();

    /**
     * Returns the target user id
     *
     * @return target user id
     */
    int getTargetUserId();

    /**
     * Returns if the source user is blocking the target user
     *
     * @return if the source is blocking the target
     */
    boolean isSourceBlockingTarget();

    /**
     * Returns the source user screen name
     *
     * @return returns the source user screen name
     */
    String getSourceUserScreenName();

    /**
     * Returns the target user screen name
     *
     * @return the target user screen name
     */
    String getTargetUserScreenName();

    /**
     * Checks if source user is following target user
     *
     * @return true if source user is following target user
     */
    boolean isSourceFollowingTarget();

    /**
     * Checks if target user is following source user.<br>
     * This method is equivalent to isSourceFollowedByTarget().
     *
     * @return true if target user is following source user
     */
    boolean isTargetFollowingSource();

    /**
     * Checks if source user is being followed by target user
     *
     * @return true if source user is being followed by target user
     */
    boolean isSourceFollowedByTarget();

    /**
     * Checks if target user is being followed by source user.<br>
     * This method is equivalent to isSourceFollowingTarget().
     *
     * @return true if target user is being followed by source user
     */
    boolean isTargetFollowedBySource();

    /**
     * Checks if the source user has enabled notifications for updates of the target user
     *
     * @return true if source user enabled notifications for target user
     */
    boolean isSourceNotificationsEnabled();
    
}
