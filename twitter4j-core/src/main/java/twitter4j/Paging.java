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

import java.util.ArrayList;
import java.util.List;

/**
 * Controls pagination.<br>
 * It is possible to use the same Paging instance in a multi-threaded
 * context only if the instance is treated immutably.<br>
 * But basically instance of this class is NOT thread safe.
 * A client should instantiate Paging class per thread.<br>
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class Paging implements java.io.Serializable {
    private static final long serialVersionUID = -7226113618341047983L;
    private Integer page = null;
    private Integer count = null;
    private Long sinceId = null;
    private Long maxId = null;
    private String cursor = null;

    // since only
    static final char[] S = new char[]{'s'};

    // since
    static final char[] SC = new char[]{'s', 'c'};
    // since, max_id, count, page
    static final char[] SMCP = new char[]{'s', 'm', 'c', 'p'};

    static final String COUNT = "count";
    // somewhat GET list statuses requires "per_page" instead of "count"
    // @see <a href="https://dev.twitter.com/docs/api/1.1/get/:user/lists/:id/statuses">GET :user/lists/:id/statuses | Twitter Developers</a>
    static final String PER_PAGE = "per_page";

    // with the changes of August 2018, direct messages can be pages by using a special cursor String
    // @see <a href="https://developer.twitter.com/en/docs/direct-messages/sending-and-receiving/api-reference/list-events">GET direct_messages/events/list | Twitter Developers</a>
    static final String CURSOR = "cursor";

    static final String SINCE_ID = "since_id";

    /*package*/ List<HttpParameter> asPostParameterList() {
        return asPostParameterList(SMCP, COUNT);
    }

    private static final HttpParameter[] NULL_PARAMETER_ARRAY = new HttpParameter[0];

    /*package*/ HttpParameter[] asPostParameterArray() {
        List<HttpParameter> list = asPostParameterList(SMCP, COUNT);
        if (list.size() == 0) {
            return NULL_PARAMETER_ARRAY;
        }
        return list.toArray(new HttpParameter[list.size()]);
    }

    /*package*/ HttpParameter[] asPostParameterArrayForDMevents() {
        List<HttpParameter> list = asPostParameterList(SC, COUNT, CURSOR);
        if (list.size() == 0) {
            return NULL_PARAMETER_ARRAY;
        }
        return list.toArray(new HttpParameter[list.size()]);
    }

    /*package*/ List<HttpParameter> asPostParameterList(char[] supportedParams) {
        return asPostParameterList(supportedParams, COUNT);
    }


    private static final List<HttpParameter> NULL_PARAMETER_LIST = new ArrayList<HttpParameter>(0);


    /*package*/ List<HttpParameter> asPostParameterList(char[] supportedParams, String perPageParamName) {
        return asPostParameterList(supportedParams, perPageParamName, SINCE_ID);
    }

    /**
     * Converts the pagination parameters into a List of PostParameter.<br>
     * This method also Validates the preset parameters, and throws
     * IllegalStateException if any unsupported parameter is set.
     *
     * @param supportedParams  char array representation of supported parameters
     * @param perPageParamName name used for per-page parameter. getUserListStatuses() requires "per_page" instead of "count".
     * @return list of PostParameter
     */
    /*package*/ List<HttpParameter> asPostParameterList(char[] supportedParams, String perPageParamName, String sinceParamName) {
        java.util.List<HttpParameter> pagingParams = new ArrayList<HttpParameter>(supportedParams.length);
        if (SINCE_ID.equals(sinceParamName)) {
            addPostParameter(supportedParams, 's', pagingParams, SINCE_ID, sinceId);
        } else {
            addPostParameter(supportedParams, 's', pagingParams, CURSOR, cursor);
        }
        addPostParameter(supportedParams, 'm', pagingParams, "max_id", maxId);
        addPostParameter(supportedParams, 'c', pagingParams, perPageParamName, count);
        addPostParameter(supportedParams, 'p', pagingParams, "page", page);
        if (pagingParams.size() == 0) {
            return NULL_PARAMETER_LIST;
        } else {
            return pagingParams;
        }
    }

    /*package*/ HttpParameter[] asPostParameterArray(char[] supportedParams, String perPageParamName) {
        return asPostParameterArray(supportedParams, perPageParamName, SINCE_ID);
    }

    /**
     * Converts the pagination parameters into a List of PostParameter.<br>
     * This method also Validates the preset parameters, and throws
     * IllegalStateException if any unsupported parameter is set.
     *
     * @param supportedParams  char array representation of supported parameters
     * @param perPageParamName name used for per-page parameter. getUserListStatuses() requires "per_page" instead of "count".
     * @return list of PostParameter
     */
    /*package*/ HttpParameter[] asPostParameterArray(char[] supportedParams, String perPageParamName, String sinceParamName) {
        java.util.List<HttpParameter> pagingParams = new ArrayList<HttpParameter>(supportedParams.length);
        if (SINCE_ID.equals(sinceParamName)) {
            addPostParameter(supportedParams, 's', pagingParams, SINCE_ID, sinceId);
        } else {
            addPostParameter(supportedParams, 's', pagingParams, CURSOR, cursor);
        }
        addPostParameter(supportedParams, 'm', pagingParams, "max_id", maxId);
        addPostParameter(supportedParams, 'c', pagingParams, perPageParamName, count);
        addPostParameter(supportedParams, 'p', pagingParams, "page", page);
        if (pagingParams.size() == 0) {
            return NULL_PARAMETER_ARRAY;
        } else {
            return pagingParams.toArray(new HttpParameter[pagingParams.size()]);
        }
    }

    private void addPostParameter(char[] supportedParams, char paramKey
            , List<HttpParameter> pagingParams, String paramName, Object paramValue) {
        boolean supported = false;
        for (char supportedParam : supportedParams) {
            if (supportedParam == paramKey) {
                supported = true;
                break;
            }
        }
        if (!supported && null != paramValue) {
            throw new IllegalStateException("Paging parameter [" + paramName
                    + "] is not supported with this operation.");
        }
        if (null != paramValue) {
            pagingParams.add(new HttpParameter(paramName, String.valueOf(paramValue)));
        }
    }

    public Paging() {
    }

    public Paging(String cursor) {
        setCursor(cursor);
    }

    public Paging(String cursor, int count) {
        this(cursor);
        setCount(count);
    }

    public Paging(int page) {
        setPage(page);
    }

    public Paging(long sinceId) {
        setSinceId(sinceId);
    }

    public Paging(int page, int count) {
        this(page);
        setCount(count);
    }

    public Paging(int page, long sinceId) {
        this(page);
        setSinceId(sinceId);
    }

    public Paging(int page, int count, long sinceId) {
        this(page, count);
        setSinceId(sinceId);
    }

    public Paging(int page, int count, long sinceId, long maxId) {
        this(page, count, sinceId);
        setMaxId(maxId);
    }

    public int getPage() {
        if (page == null) {
            return -1;
        } else {
            return page;
        }
    }

    public void setPage(int page) {
        if (page < 1) {
            throw new IllegalArgumentException("page should be positive integer. passed:" + page);
        }
        this.page = page;
    }

    public int getCount() {
        if (count == null) {
            return -1;
        } else {
            return count;
        }
    }

    public void setCount(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("count should be positive integer. passed:" + count);
        }
        this.count = count;
    }

    public Paging count(int count) {
        setCount(count);
        return this;
    }

    public long getSinceId() {
        if (sinceId == null) {
            return -1;
        } else {
            return sinceId;
        }
    }

    public void setSinceId(long sinceId) {
        if (sinceId < 1) {
            throw new IllegalArgumentException("since_id should be positive integer. passed:" + sinceId);
        }
        this.sinceId = sinceId;
    }

    public Paging sinceId(long sinceId) {
        setSinceId(sinceId);
        return this;
    }

    public long getMaxId() {
        if (maxId == null) {
            return -1;
        } else {
            return maxId;
        }
    }

    public void setMaxId(long maxId) {
        if (maxId < 1) {
            throw new IllegalArgumentException("max_id should be positive integer. passed:" + maxId);
        }
        this.maxId = maxId;
    }

    public Paging maxId(long maxId) {
        setMaxId(maxId);
        return this;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paging)) return false;

        Paging paging = (Paging) o;

        if (count != paging.count) return false;
        if (maxId != paging.maxId) return false;
        if (page != paging.page) return false;
        if (sinceId != paging.sinceId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = page;
        result = 31 * result + count;
        result = 31 * result + (int) (sinceId ^ (sinceId >>> 32));
        result = 31 * result + (int) (maxId ^ (maxId >>> 32));
        return result;
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
