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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
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

    static Paging empty = new Paging(-1,-1,-1,-1);

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

    // since only
    static final char[] S = new char[]{'s'};
    // since, max_id, count, page
    static final char[] SMCP = new char[]{'s', 'm', 'c', 'p'};

    static final String COUNT = "count";
    // somewhat GET list statuses requires "per_page" instead of "count"
    // @see <a href="https://dev.twitter.com/docs/api/1.1/get/:user/lists/:id/statuses">GET :user/lists/:id/statuses | Twitter Developers</a>
    static final String PER_PAGE = "per_page";

    List<HttpParameter> asPostParameterList() {
        return asPostParameterList(SMCP, COUNT);
    }

    private static final HttpParameter[] NULL_PARAMETER_ARRAY = new HttpParameter[0];

    /*package*/ HttpParameter[] asPostParameterArray() {
        List<HttpParameter> list = asPostParameterList(SMCP, COUNT);
        if (list.size() == 0) {
            return NULL_PARAMETER_ARRAY;
        }
        return list.toArray(new HttpParameter[0]);
    }

    /*package*/ List<HttpParameter> asPostParameterList(char[] supportedParams) {
        return asPostParameterList(supportedParams, COUNT);
    }


    private static final List<HttpParameter> NULL_PARAMETER_LIST = new ArrayList<>(0);

    /**
     * Converts the pagination parameters into a List of PostParameter.<br>
     * This method also Validates the preset parameters, and throws
     * IllegalStateException if any unsupported parameter is set.
     *
     * @param supportedParams  char array representation of supported parameters
     * @param perPageParamName name used for per-page parameter. getUserListStatuses() requires "per_page" instead of "count".
     * @return list of PostParameter
     */
    /*package*/ List<HttpParameter> asPostParameterList(char[] supportedParams, String perPageParamName) {
        List<HttpParameter> pagingParams = new ArrayList<>(supportedParams.length);
        addPostParameter(supportedParams, 's', pagingParams, "since_id", sinceId);
        addPostParameter(supportedParams, 'm', pagingParams, "max_id", maxId);
        addPostParameter(supportedParams, 'c', pagingParams, perPageParamName, count);
        addPostParameter(supportedParams, 'p', pagingParams, "page", page);
        return pagingParams;
    }

    /**
     * Converts the pagination parameters into a List of PostParameter.<br>
     * This method also Validates the preset parameters, and throws
     * IllegalStateException if any unsupported parameter is set.
     *
     * @param supportedParams  char array representation of supported parameters
     * @param perPageParamName name used for per-page parameter. getUserListStatuses() requires "per_page" instead of "count".
     * @return array of PostParameter
     */
    /*package*/ HttpParameter[] asPostParameterArray(char[] supportedParams, String perPageParamName) {
        return asPostParameterList(supportedParams, perPageParamName).toArray(new HttpParameter[0]);
    }

    private void addPostParameter(char[] supportedParams, char paramKey
            , List<HttpParameter> pagingParams, String paramName, long paramValue) {
        boolean supported = false;
        for (char supportedParam : supportedParams) {
            if (supportedParam == paramKey) {
                supported = true;
                break;
            }
        }
        if (!supported && -1 != paramValue) {
            throw new IllegalStateException("Paging parameter [" + paramName
                    + "] is not supported with this operation.");
        }
        if (-1 != paramValue) {
            pagingParams.add(new HttpParameter(paramName, String.valueOf(paramValue)));
        }
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
