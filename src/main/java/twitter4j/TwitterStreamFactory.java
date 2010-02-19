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

import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;
import twitter4j.http.Authorization;
import twitter4j.http.AuthorizationFactory;

/**
 * A factory class for TwitterFactory.<br>
 * An instance of this class is completely thread safe and can be re-used and used concurrently.<br>
 * Note that TwitterStream is NOT compatible with Google App Engine as GAE is not capable of handling requests longer than 30 seconds.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.0
 */
public final class TwitterStreamFactory implements java.io.Serializable{
    private static final long serialVersionUID = 8146074704915782233L;
    private StatusListener listener = null;
    private final Configuration conf;

    /**
     * Creates a TwitterStreamFactory with the root configuration.
     */
    public TwitterStreamFactory() {
        this.conf = ConfigurationContext.getInstance();
    }

    /**
     * Creates a TwitterStreamFactory with a specified status listener
     * @param listener the listener
     */
    public TwitterStreamFactory(StatusListener listener) {
        this.conf = ConfigurationContext.getInstance();
        this.listener = listener;
    }


    /**
     * Creates a TwitterStreamFactory with a specified config tree
     * @param configTreePath the path
     */
    public TwitterStreamFactory(String configTreePath) {
        this.conf = ConfigurationContext.getInstance(configTreePath);
    }

    /**
     * Creates a TwitterStreamFactory with a specified config tree and a listener
     * @param configTreePath the path
     */
    public TwitterStreamFactory(String configTreePath, StatusListener listener) {
        this(configTreePath);
        this.listener = listener;
    }

    // implementations for BasicSupportFactory

    /**
     * Returns a instance.
     *
     * @return default singleton instance
     */
    public TwitterStream getInstance(){
        return getInstance(conf);
    }

    /**
     * Returns a Basic Authenticated instance.
     *
     * @param screenName screen name
     * @param password password
     * @return an instance
     */
    public TwitterStream getInstance(String screenName, String password){
        return getInstance(AuthorizationFactory
                .getBasicAuthorizationInstance(screenName, password));
    }

    /**
     * {@inheritDoc}
     */
    public TwitterStream getInstance(Authorization auth){
        return getInstance(conf,auth);
    }
    private TwitterStream getInstance(Configuration conf, Authorization auth) {
            return new TwitterStream(conf, auth, listener);
    }
    private TwitterStream getInstance(Configuration conf) {
            return new TwitterStream(conf, AuthorizationFactory.getInstance(conf, false), listener);
    }
}
