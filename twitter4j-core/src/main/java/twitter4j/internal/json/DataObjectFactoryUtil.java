/*
Copyright (c) 2007-2011, Yusuke Yamamoto
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
package twitter4j.internal.json;

import twitter4j.json.DataObjectFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * provides public access to package private methods of twitter4j.json.DataObjectFactory class.<br>
 * This class is intended to
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.7
 */
public class DataObjectFactoryUtil {
    private DataObjectFactoryUtil(){
        throw new AssertionError("not intended to be instantiated.");
    }
    private static final Method CLEAR_THREAD_LOCAL_MAP;
    private static final Method REGISTER_JSON_OBJECT;
    static{
        Method[] methods = DataObjectFactory.class.getDeclaredMethods();
        Method clearThreadLocalMap = null;
        Method registerJSONObject = null;
        for (Method method : methods){
            if(method.getName().equals("clearThreadLocalMap")){
                clearThreadLocalMap = method;
                clearThreadLocalMap.setAccessible(true);
            }else if(method.getName().equals("registerJSONObject")){
                registerJSONObject = method;
                registerJSONObject.setAccessible(true);
            }
        }
        if(null == clearThreadLocalMap || null == registerJSONObject){
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
//            Class[] clazz = REGISTER_JSON_OBJECT.getParameterTypes();
            return (T) REGISTER_JSON_OBJECT.invoke(null, key, json);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new AssertionError(e);
        }
    }
}
