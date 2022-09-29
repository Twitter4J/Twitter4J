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

/**
 * A factory class for TwitterFactory.<br>
 * An instance of this class is completely thread safe and can be re-used and used concurrently.<br>
 * Note that TwitterStream is NOT compatible with Google App Engine as GAE is not capable of handling requests longer than 30 seconds.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.0
 */
public final class TwitterStreamFactory implements java.io.Serializable {
    private static final long serialVersionUID = -5181136070759074681L;
    private final Configuration conf;
    private static final TwitterStream SINGLETON = new TwitterStreamImpl(Configuration.getInstance());

    /**
     * Creates a TwitterStreamFactory with the root configuration.
     */
    public TwitterStreamFactory() {
        this(Configuration.getInstance());
    }

    /**
     * Creates a TwitterStreamFactory with the given configuration.
     *
     * @param conf the configuration to use
     * @since Twitter4J 2.1.1
     */
    public TwitterStreamFactory(Configuration conf) {
        this.conf = conf;
    }

    // implementations for BasicSupportFactory

    /**
     * Returns an instance associated with the configuration bound to this factory.
     *
     * @return default instance
     */
    public TwitterStream getInstance() {
        return getInstance(conf);
    }


    private TwitterStream getInstance(Configuration conf) {
        return new TwitterStreamImpl(conf);
    }

    /**
     * Returns default singleton TwitterStream instance.
     *
     * @return default singleton TwitterStream instance
     * @since Twitter4J 2.2.4
     */
    public static TwitterStream getSingleton() {
        return SINGLETON;
    }
}
