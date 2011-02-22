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

package twitter4j.auth;

import twitter4j.internal.http.HttpRequest;

import java.io.ObjectStreamException;

/**
 * An interface represents credentials.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class NullAuthorization implements Authorization, java.io.Serializable {
    private static NullAuthorization SINGLETON = new NullAuthorization();
    private static final long serialVersionUID = -8748173338942663960L;

    private NullAuthorization() {

    }

    public static NullAuthorization getInstance() {
        return SINGLETON;
    }

    public String getAuthorizationHeader(HttpRequest req) {
        return null;
    }

    public boolean isEnabled() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        return SINGLETON == o;
    }

    @Override
    public String toString() {
        return "NullAuthentication{SINGLETON}";
    }

    private Object readResolve() throws ObjectStreamException {
        return SINGLETON;
    }

}
