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

package twitter4j.v1;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Controls pagination.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
@SuppressWarnings({"unused", "SameParameterValue", "UnusedReturnValue"})
public final class Paging implements java.io.Serializable {
    private static final long serialVersionUID = -7226113618341047983L;
    /**
     * page
     */
    public final int page;
    /**
     * count
     */
    public final int count;
    /**
     * since id
     */
    public final long sinceId;
    /**
     * max id
     */
    public final long maxId;

    /**
     * @param page page
     * @return Paging instance
     */
    @NotNull
    public static Paging ofPage(int page) {
        if (page < 1) {
            throw new IllegalArgumentException("page should be positive integer. passed:" + page);
        }
        return new Paging(page, -1,-1,-1);
    }

    /**
     * creates a new Paging instance with the specified page
     * @param page page
     * @return new Paging instance
     */
    @NotNull
    public Paging withPage(int page) {
        if (page < 1) {
            throw new IllegalArgumentException("page should be positive integer. passed:" + page);
        }
        return new Paging(page, this.count, this.sinceId, this.maxId);
    }


    /**
     * creates a new Paging instance with the specified count
     * @param count count
     * @return new Paging instance
     */
    @NotNull
    public static Paging ofCount(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("count should be positive integer. passed:" + count);
        }
        return new Paging(-1, count,-1,-1);
    }

    /**
     * creates a new Paging instance with the specified count
     * @param count count
     * @return new Paging instance
     */
    @NotNull
    public Paging count(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("count should be positive integer. passed:" + count);
        }
        return new Paging(this.page, count, this.sinceId, this.maxId);
    }

    /**
     * creates a new Paging instance with the specified sinceId
     * @param sinceId page
     * @return new Paging instance
     */
    @NotNull
    public static Paging ofSinceId(long sinceId) {
        if (sinceId < 1) {
            throw new IllegalArgumentException("since_id should be positive integer. passed:" + sinceId);
        }
        return new Paging(-1, -1,sinceId,-1);
    }

    /**
     * creates a new Paging instance with the specified sinceId
     * @param sinceId page
     * @return new Paging instance
     */
    @NotNull
    public Paging sinceId(long sinceId) {
        if (sinceId < 1) {
            throw new IllegalArgumentException("since_id should be positive integer. passed:" + sinceId);
        }
        return new Paging(this.page, this.count, sinceId, this.maxId);
    }


    /**
     * creates a new Paging instance with the specified maxId
     * @param maxId maxId
     * @return new Paging instance
     */
    @NotNull
    public static Paging ofMaxId(long maxId) {
        if (maxId < 1) {
            throw new IllegalArgumentException("max_id should be positive integer. passed:" + maxId);
        }
        return new Paging(-1, -1,-1,maxId);
    }
    /**
     * creates a new Paging instance with the specified maxId
     * @param maxId maxId
     * @return new Paging instance
     */
    @NotNull
    public Paging maxId(long maxId) {
        if (maxId < 1) {
            throw new IllegalArgumentException("max_id should be positive integer. passed:" + maxId);
        }
        return new Paging(this.page, this.count, this.sinceId, maxId);
    }

    private Paging(int page, int count, long sinceId, long maxId) {
        this.page = page;
        this.count = count;
        this.sinceId = sinceId;
        this.maxId = maxId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paging paging = (Paging) o;
        return page == paging.page && count == paging.count && sinceId == paging.sinceId && maxId == paging.maxId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(page, count, sinceId, maxId);
    }

    @Override
    public String toString() {
        return "Paging{" +
                "page=" + page +
                ", count=" + count +
                ", sinceId=" + sinceId +
                ", maxId=" + maxId +
                '}';
    }
}
