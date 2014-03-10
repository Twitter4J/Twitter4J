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

import twitter4j.json.DataObjectFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * provides public access to package private methods of twitter4j.json.DataObjectFactory class.<br>
 * This class is not intended to be used by Twitter4J client.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.7
 */
public class DataObjectFactoryUtil {
    private DataObjectFactoryUtil() {
        throw new AssertionError("not intended to be instantiated.");
    }

    private static final Method CLEAR_THREAD_LOCAL_MAP;
    private static final Method REGISTER_JSON_OBJECT;

    static {
        Method[] methods = DataObjectFactory.class.getDeclaredMethods();
        Method clearThreadLocalMap = null;
        Method registerJSONObject = null;
        for (Method method : methods) {
            if (method.getName().equals("clearThreadLocalMap")) {
                clearThreadLocalMap = method;
                clearThreadLocalMap.setAccessible(true);
            } else if (method.getName().equals("registerJSONObject")) {
                registerJSONObject = method;
                registerJSONObject.setAccessible(true);
            }
        }
        if (null == clearThreadLocalMap || null == registerJSONObject) {
            throw new AssertionError();
        }
        CLEAR_THREAD_LOCAL_MAP = clearThreadLocalMap;
        REGISTER_JSON_OBJECT = registerJSONObject;
    }

    /**
     * provides a public access to {DAOFactory#clearThreadLocalMap}
     */
    public static void clearThreadLocalMap() {
        try {
            CLEAR_THREAD_LOCAL_MAP.invoke(null);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * provides a public access to {DAOFactory#registerJSONObject}
     */
    public static <T> T registerJSONObject(T key, Object json) {
        try {
            return (T) REGISTER_JSON_OBJECT.invoke(null, key, json);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new AssertionError(e);
        }
    }
}
