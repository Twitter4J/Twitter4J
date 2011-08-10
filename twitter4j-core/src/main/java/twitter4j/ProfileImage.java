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

import java.io.ObjectStreamException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.7
 */
public interface ProfileImage extends TwitterResponse, java.io.Serializable {
    String getURL();

    ImageSize BIGGER = new ImageSize("bigger");
    ImageSize NORMAL = new ImageSize("normal");
    ImageSize MINI = new ImageSize("mini");
    ImageSize ORIGINAL = new ImageSize("original");

    static class ImageSize implements java.io.Serializable {

        private static final Map<String, ImageSize> instances = new HashMap<String, ImageSize>();

        private static final long serialVersionUID = 3363026523372848987L;

        private final String name;

        private ImageSize() {
            throw new AssertionError();
        }

        private ImageSize(String name) {
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

            ImageSize imageSize = (ImageSize) o;

            if (!name.equals(imageSize.name)) return false;

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

        private static ImageSize getInstance(String name) {
            return instances.get(name);
        }

        private Object readResolve() throws ObjectStreamException {
            return getInstance(name);
        }
    }
}
