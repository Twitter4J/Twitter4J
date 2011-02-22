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

package twitter4j.internal.async;

import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.2
 */
public final class DispatcherFactory {
    private String dispatcherImpl;
    private Configuration conf;

    public DispatcherFactory(Configuration conf) {
        dispatcherImpl = conf.getDispatcherImpl();
        this.conf = conf;
    }

    public DispatcherFactory() {
        this(ConfigurationContext.getInstance());
    }

    /**
     * returns a Dispatcher instance.
     *
     * @return dispatcher instance
     */
    public Dispatcher getInstance() {
        try {
            return (Dispatcher) Class.forName(dispatcherImpl)
                    .getConstructor(Configuration.class).newInstance(conf);
        } catch (InstantiationException e) {
            throw new AssertionError(e);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (ClassNotFoundException e) {
            throw new AssertionError(e);
        } catch (ClassCastException e) {
            throw new AssertionError(e);
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new AssertionError(e);
        }
    }
}
