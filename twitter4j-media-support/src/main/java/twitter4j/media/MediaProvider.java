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

package twitter4j.media;

import java.io.ObjectStreamException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.8
 */
public class MediaProvider implements java.io.Serializable {
    private static final long serialVersionUID = -258215809702057490L;

    private static final Map<String, MediaProvider> instances = new HashMap<String, MediaProvider>();

    public static MediaProvider TWITTER = new MediaProvider("TWITTER");
    public static MediaProvider IMG_LY = new MediaProvider("IMG_LY");
    public static MediaProvider PLIXI = new MediaProvider("PLIXI");
    public static MediaProvider LOCKERZ = PLIXI;
    public static MediaProvider TWIPPLE = new MediaProvider("TWIPPLE");
    public static MediaProvider TWITGOO = new MediaProvider("TWITGOO");
    public static MediaProvider TWITPIC = new MediaProvider("TWITPIC");
    public static MediaProvider YFROG = new MediaProvider("YFROG");
    public static MediaProvider MOBYPICTURE = new MediaProvider("MOBYPICTURE");
    public static MediaProvider TWIPL = new MediaProvider("TWIPL");
    public static MediaProvider POSTEROUS = new MediaProvider("POSTEROUS");

    private final String name;

    private MediaProvider() {
        throw new AssertionError();
    }

    MediaProvider(String name) {
        this.name = name;
        instances.put(name, this);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MediaProvider device = (MediaProvider) o;

        if (!name.equals(device.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

    private static MediaProvider getInstance(String name) {
        return instances.get(name);
    }

    private Object readResolve() throws ObjectStreamException {
        return getInstance(name);
    }

}
