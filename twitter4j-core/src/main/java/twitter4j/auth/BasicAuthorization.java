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

import twitter4j.internal.http.BASE64Encoder;
import twitter4j.internal.http.HttpRequest;

/**
 * An authentication implementation implements Basic authentication
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class BasicAuthorization implements Authorization, java.io.Serializable {

    private String userId;

    private String password;
    private String basic;
    private static final long serialVersionUID = -5861104407848415060L;

    public BasicAuthorization(String userId, String password) {
        this.userId = userId;
        this.password = password;
        this.basic = encodeBasicAuthenticationString();
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    private String encodeBasicAuthenticationString() {
        if (userId != null && password != null) {
            return "Basic " + BASE64Encoder.encode((userId + ":" + password).getBytes());
        }
        return null;
    }

    public String getAuthorizationHeader(HttpRequest req) {
        return basic;
    }

    /**
     * #{inheritDoc}
     */
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasicAuthorization)) return false;

        BasicAuthorization that = (BasicAuthorization) o;

        return basic.equals(that.basic);

    }

    @Override
    public int hashCode() {
        return basic.hashCode();
    }

    @Override
    public String toString() {
        return "BasicAuthorization{" +
                "userId='" + userId + '\'' +
                ", password='**********'\'" +
                '}';
    }

}
