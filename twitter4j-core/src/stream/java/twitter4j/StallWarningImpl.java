/*
 * Copyright 2007 Yusuke Yamamoto
 * Copyright (C) 2012 Twitter, Inc.
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

import twitter4j.v1.StallWarning;

import java.io.Serializable;
import java.util.Objects;

import static twitter4j.ParseUtil.getInt;
import static twitter4j.ParseUtil.getRawString;

/**
 * @author Yusuke Yamamoto - yusuke at twitter.com
 * @since Twitter4J 3.0.0
 */
@SuppressWarnings("unused")
final class StallWarningImpl implements StallWarning, Serializable {
    private static final long serialVersionUID = -4294628635422470314L;
    /**
     * code
     */
    private final String code;
    /**
     * message
     */
    private final String message;
    /**
     * percentFull
     */
    private final int percentFull;

    StallWarningImpl(JSONObject json) throws JSONException {
        JSONObject warning = json.getJSONObject("warning");
        code = getRawString("code", warning);
        message = getRawString("message", warning);
        percentFull = getInt("percent_full", warning);

    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getPercentFull() {
        return percentFull;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StallWarningImpl that = (StallWarningImpl) o;
        return percentFull == that.percentFull && Objects.equals(code, that.code) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, percentFull);
    }

    @Override
    public String toString() {
        return "StallWarningImpl{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", percentFull=" + percentFull +
                '}';
    }
}
