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

import junit.framework.TestCase;

public class PagingTest extends TestCase {

    public PagingTest(String name) {
        super(name);
    }

    public void testPaging() throws Exception {
        java.util.List<HttpParameter> params;
        Paging paging = new Paging();
        params = paging.asPostParameterList();
        assertEquals(0, params.size());
        // parameter validation test
        params = paging.asPostParameterList(Paging.S);
        assertEquals(0, params.size());

        // setter validation test
        try {
            paging.setSinceId(-1l);
            fail("sinceId should not accept negative value");
        } catch (IllegalArgumentException ignore) {
        }
        // parameter validation test
        try {
            paging.asPostParameterList(Paging.S);
        } catch (IllegalStateException ise) {
            fail("IllegalStateException should not be thrown.");
        }
        params = paging.asPostParameterList();
        assertEquals(0, params.size());
        paging.setSinceId(2000l);
        params = paging.asPostParameterList();
        assertEquals(1, params.size());
        assertContains(params, "since_id", 2000l);

        // setter validation test
        try {
            paging.setPage(-1);
            fail("page should not accept negative value");
        } catch (IllegalArgumentException ignore) {
        }
        paging.setPage(10);
        params = paging.asPostParameterList();
        assertEquals(2, params.size());
        assertContains(params, "page", 10);
        assertContains(params, "since_id", 2000l);

        // setter validation test
        try {
            paging.setMaxId(-1l);
            fail("maxId should not accept negative value");
        } catch (IllegalArgumentException ignore) {
        }
        // parameter validation test
        try {
            paging.asPostParameterList(Paging.S);
            fail("should accept only since_id parameter");
        } catch (IllegalStateException ignore) {
        }
        params = paging.asPostParameterList();
        assertEquals(2, params.size());
        paging.setMaxId(1000l);
        params = paging.asPostParameterList();
        assertEquals(3, params.size());
        assertContains(params, "page", 10);
        assertContains(params, "max_id", 1000l);
        assertContains(params, "since_id", 2000l);


        // setter validation test
        try {
            paging.setCount(-1);
            fail("count should not accept negative value");
        } catch (IllegalArgumentException ignore) {
        }
        // parameter validation test
        try {
            paging.asPostParameterList(Paging.S);
            fail("should accept only since_id parameter");
        } catch (IllegalStateException ignore) {
        }
        params = paging.asPostParameterList();
        assertEquals(3, params.size());
        paging.setCount(3000);
        params = paging.asPostParameterList();
        assertEquals(4, params.size());
        assertContains(params, "page", 10);
        assertContains(params, "max_id", 1000l);
        assertContains(params, "since_id", 2000l);
        assertContains(params, "count", 3000);

    }

    private void assertContains(java.util.List<HttpParameter> params, String name, long value) {
        boolean contains = false;
        for (HttpParameter param : params) {
            if (param.getName().equals(name) && param.getValue().equals(String.valueOf(value))) {
                contains = true;
                break;
            }
        }
        if (!contains) {
            fail("expected parameter " + name + ":" + value + " not found.");
        }

    }
}
