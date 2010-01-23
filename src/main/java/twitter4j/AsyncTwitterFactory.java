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
import twitter4j.http.Authorization;

/**
 * A factory class for AsyncTwitter.<br>
 * An instance of this class is completely thread safe and can be re-used and used concurrently.<br>
 * Note that currently AsyncTwitter is NOT compatible with Google App Engine as it is maintaining threads internally.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.0
 */
public final class AsyncTwitterFactory extends TwitterFactoryOAuthSupportBase<AsyncTwitter> implements java.io.Serializable {
    private TwitterListener listener;
    private static final long serialVersionUID = -2565686715640816219L;

    /**
     * Creates a AsyncTwitterFactory with the root configuration, with no listener. AsyncTwitter instances will not perform callbacks when using this constructor.
     */
    public AsyncTwitterFactory() {
        super();
        this.listener = new TwitterAdapter();
    }

    /**
     * Creates a AsyncTwitterFactory with the root configuration, with given listener
     * @param listener listener
     */
    public AsyncTwitterFactory(TwitterListener listener) {
        super();
        this.listener = listener;
    }

    /**
     * Creates a AsyncTwitterFactory with a specified config tree, with given listener
     * @param configTreePath the path
     * @param listener listener
     */
    public AsyncTwitterFactory(String configTreePath, TwitterListener listener) {
        super(configTreePath);
        this.listener = listener;
    }

    /**
     * {@inheritDoc}
     */
    protected AsyncTwitter getInstance(Configuration conf, Authorization auth) {
        return new AsyncTwitter(conf, auth, listener);
    }
    /**
     * {@inheritDoc}
     */
    protected AsyncTwitter getOAuthSupportInstance(Configuration conf, Authorization auth){
        return new AsyncTwitter(conf, auth, listener);
    }
}
