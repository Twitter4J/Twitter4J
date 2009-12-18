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

import java.util.Properties;
import java.io.FileInputStream;

/**
 * @author Perry Sakkaris - psakkaris at gmail.com
 *         Date: Dec 9, 2009
 */
public class RelationshipTest extends TestCase {

    protected Twitter twitterAPI1;
    protected Properties p = new Properties();
    protected String id1, followsOneWay, bestFriend1, bestFriend2;
    protected String pass1;

    public RelationshipTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        p.load(new FileInputStream("test.properties"));
        id1 = p.getProperty("id1");
        pass1 = p.getProperty("pass1");
        twitterAPI1 = new Twitter(id1, pass1);

        followsOneWay = p.getProperty("followsOneWay");
        bestFriend1 = p.getProperty("bestFriend1");
        bestFriend2 = p.getProperty("bestFriend2");
    }

    protected void tearDown() throws Exception {

        super.tearDown();
    }

    public void testRelationship() throws Exception {
        //  TESTING PRECONDITIONS:
        //  1) id1 is followed by "followsOneWay", but not following "followsOneWay"
        Relationship rel1 = twitterAPI1.showFriendship(id1, followsOneWay);

        // test second precondition
        assertNotNull(rel1);
        assertTrue(rel1.isSourceFollowedByTarget());
        assertFalse(rel1.isSourceFollowingTarget());
        assertTrue(rel1.isTargetFollowingSource());
        assertFalse(rel1.isTargetFollowedBySource());

        //  2) best_friend1 is following and followed by best_friend2
        Relationship rel2 = twitterAPI1.showFriendship(bestFriend1, bestFriend2);

        // test second precondition
        assertNotNull(rel2);
        assertTrue(rel2.isSourceFollowedByTarget());
        assertTrue(rel2.isSourceFollowingTarget());
        assertTrue(rel2.isTargetFollowingSource());
        assertTrue(rel2.isTargetFollowedBySource());

        // test equality
        Relationship rel3 = twitterAPI1.showFriendship(id1, followsOneWay);
        assertEquals(rel1, rel3);
        assertFalse(rel1.equals(rel2));
    }

}