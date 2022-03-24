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
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
abstract class LazyPagableResponseList<T extends TwitterResponse> extends LazyResponseList<T> implements PagableResponseList<T> {
    private static final long serialVersionUID = -3478264948215362741L;

    public boolean hasPrevious() {
        return ((PagableResponseList<T>) getTarget()).hasPrevious();
    }

    public long getPreviousCursor() {
        return ((PagableResponseList<T>) getTarget()).getPreviousCursor();
    }

    public boolean hasNext() {
        return ((PagableResponseList<T>) getTarget()).hasNext();
    }

    public long getNextCursor() {
        return ((PagableResponseList<T>) getTarget()).getNextCursor();
    }

}
