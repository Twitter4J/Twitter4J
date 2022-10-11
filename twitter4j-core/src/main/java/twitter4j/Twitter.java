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

import org.jetbrains.annotations.NotNull;
import twitter4j.v1.RawStreamListener;
import twitter4j.v1.StatusAdapter;
import twitter4j.v1.StreamListener;
import twitter4j.v1.TwitterV1;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.0
 */
@SuppressWarnings("unused")
public interface Twitter extends java.io.Serializable {

    /**
     * returns new Builder instance
     *
     * @return Builder instance
     */
    static TwitterBuilder newBuilder() {
        return new TwitterBuilder();
    }

    /**
     * equivalent to calling newBuilder().build();
     *
     * @return Twitter Instance
     */
    static Twitter getInstance() {
        return newBuilder().build();
    }

    /**
     * @return returns Twitter API v1.1 interfaces
     */
    TwitterV1 v1();





    /**
     * builder for Twitter
     */
    class TwitterBuilder extends Configuration<TwitterBuilder> {
        private TwitterBuilder() {
        }

        /**
         * @return constructs Twitter instance
         */
        public Twitter build() {
            return new TwitterImpl(buildConfiguration());
        }
        final List<ConnectionLifeCycleListener> connectionLifeCycleListeners = new ArrayList<>();

        final List<StreamListener> streamListeners = new ArrayList<>();
        final List<RawStreamListener> rawStreamListeners = new ArrayList<>();

        /**
         * @param listener listener
         * @return this instance
         */
        public Twitter.TwitterBuilder connectionLifeCycleListener(@NotNull ConnectionLifeCycleListener listener) {
            this.connectionLifeCycleListeners.add(listener);
            return this;
        }


        /**
         * @param streamListener adds listener
         * @return this instance
         */
        public Twitter.TwitterBuilder listener(@NotNull StreamListener streamListener) {
            this.streamListeners.add(streamListener);
            return this;
        }

        /**
         * @param rawStreamListener listener
         * @return this instance
         */
        public Twitter.TwitterBuilder listener(@NotNull RawStreamListener rawStreamListener) {
            this.rawStreamListeners.add(rawStreamListener);
            return this;
        }


        /**
         * @param onStatus listener
         * @return this instance
         */
        public Twitter.TwitterBuilder onStatus(@NotNull Consumer<Status> onStatus) {
            this.streamListeners.add(new StatusAdapter() {
                @Override
                public void onStatus(Status status) {
                    onStatus.accept(status);
                }

            });
            return this;
        }

        /**
         * @param onException listener
         * @return this instance
         */
        public Twitter.TwitterBuilder onException(Consumer<Exception> onException) {
            this.streamListeners.add(new StatusAdapter() {
                @Override
                public void onException(Exception ex) {
                    onException.accept(ex);
                }

            });
            return this;
        }

    }
}
