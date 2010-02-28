/*
Copyright (c) 2007-2010, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package twitter4j;

import junit.framework.TestCase;
import twitter4j.internal.http.HttpParameter;

public class PagingTest extends TestCase {

    public PagingTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
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
    private void assertContains(java.util.List<HttpParameter> params, String name, long value){
        boolean contains = false;
        for(HttpParameter param : params){
            if(param.getName().equals(name) && param.getValue().equals(String.valueOf(value))){
                contains = true;
                break;
            }
        }
        if(!contains){
            fail("expected parameter " + name + ":" + value + " not found.");
        }

    }
}
