/*
Copyright (c) 2007-2009, Yusuke Yamamoto
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

import java.util.List;
import java.util.Properties;
import java.io.FileInputStream;

public class ListAPITest extends TestCase {
    protected Twitter twitterAPI1 = null;
    protected Properties p = new Properties();

    public ListAPITest(String name) {
        super(name);
    }
    protected String id1,pass1;
    protected void setUp() throws Exception {
        super.setUp();
        p.load(new FileInputStream("test.properties"));
        id1 = p.getProperty("id1");
        pass1 = p.getProperty("pass1");
        twitterAPI1 = new Twitter(id1, pass1);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    public void testList() throws Exception {
        twitter4j.List list = twitterAPI1.createUserList(id1, "testpoint1", null, null);
        assertNotNull(list);
        assertEquals("testpoint1", list.getName());
        
        list = twitterAPI1.updateUserList(id1, list.getId(), "testpoint2", null, null);
        assertNotNull(list);
        assertEquals("testpoint2", list.getName());
        
        ResponseList<twitter4j.List> lists = twitterAPI1.getUserLists(id1, -1l);
        assertFalse(lists.size() == 0);

        list = twitterAPI1.showUserList(id1, list.getId());
        assertNotNull(list);

        List<Status> statuses = twitterAPI1.getUserListStatuses(id1, list.getId(), new Paging());
        assertNotNull(statuses);

        lists = twitterAPI1.getUserListMemberships(id1, -1l);
        assertNotNull(lists);

        lists = twitterAPI1.getUserListSubscriptions(id1, -1l);
        assertNotNull(lists);
        
        list = twitterAPI1.deleteUserList(id1, list.getId());
        assertNotNull(list);
    }
}
