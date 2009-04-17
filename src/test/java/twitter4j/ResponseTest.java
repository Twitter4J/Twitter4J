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
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * <p>Title: Twitter4J</p>
 *
 * <p>Description: </p>
 *
 * @version 0.9
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class ResponseTest extends TestCase {

    public ResponseTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public int count;
    static DocumentBuilder builder = null;
    static {

        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
        }
    }

    public void testUser() throws Exception {
        String responseString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "     <user>"
            + "       <id>6377362</id>"
            + "       <name>twit4j2</name>"
            + "       <screen_name>twit4j2</screen_name>"
            + "       <location></location>"
            + "       <description></description>"
            + "       <profile_image_url>http://assets1.twitter.com/images/default_image.gif?1180256973</profile_image_url>"
            + "       <url></url>"
            + "       <protected>false</protected>"
            + "       <profile_background_color>9ae4e8</profile_background_color>"
            + "       <profile_text_color>000000</profile_text_color>"
            + "       <profile_link_color>0000ff</profile_link_color>"
            + "       <profile_sidebar_fill_color>e0ff92</profile_sidebar_fill_color>"
            + "       <profile_sidebar_border_color>87bc44</profile_sidebar_border_color>"
            + "       <friends_count>1</friends_count>"
            + "       <followers_count>1</followers_count>"
            + "       <favourites_count>0</favourites_count>"
            + "       <statuses_count>0</statuses_count>"
            + "     </user>";
        InputStream is;
        is = new ByteArrayInputStream(responseString.getBytes("UTF-8"));
        Document doc = builder.parse(new ByteArrayInputStream(responseString.getBytes("UTF-8")));
        User user = new User(doc.getDocumentElement(),null);
        assertEquals(6377362, user.getId());
    }

    public void testStatus() throws Exception {
        String responseString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<statuses>"
            + "  <status>"
            + "    <created_at>Sat Jun 02 14:56:38 +0000 2007</created_at>"
            + "    <id>88182692</id>"
            + "    <text>&#30130;&#12428;&#12383;&#12290;&#12502;&#12525;&#12464;&#12399;&#26126;&#26085;&#12395;&#12377;&#12427;&#12290; *Tw*</text>"
            + "    <user>"
            + "      <id>5608852</id>"
            + "      <name>s_d</name>"
            + "      <screen_name>s_d</screen_name>"
            + "      <location>&#26085;&#26412;</location>"
            + "      <description>&#12418;&#12358;COBOL&#12375;&#12394;&#12356;&#12290;Mac&#12399;&#22909;&#12365;&#12384;&#12424;&#12290;HR/HM&#12434;&#33391;&#12367;&#32884;&#12367;&#12290;Jazz&#12418;&#22909;&#12365;&#12290;&#37329;&#34701;&#31995;SE&#12375;&#12390;&#12427;&#12290;&#12513;&#12531;&#12504;&#12521;&#12391;&#12418;&#12354;&#12427;&#12290;</description>"
            + "      <profile_image_url>http://assets1.twitter.com/system/user/profile_image/5608852/normal/Cornell1.jpg?1177784770</profile_image_url>"
            + "      <url>http://blog.so-net.ne.jp/s_d/</url>"
            + "      <protected>false</protected>"
            + "    </user>"
            + "  </status>"
            + "  <status>"
            + "    <created_at>Sat Jun 02 14:56:25 +0000 2007</created_at>"
            + "    <id>88182532</id>"
            + "    <text>Well, praise the Lord that McKenna is ok.  Went through the same thing many years ago.  Not much is scarier.</text>"
            + "    <user>"
            + "      <id>5385192</id>"
            + "      <name>Linda Tyler</name>"
            + "      <screen_name>ginnylin</screen_name>"
            + "      <location></location>"
            + "      <description></description>"
            + "      <profile_image_url>http://assets2.twitter.com/system/user/profile_image/5385192/normal/J.B.BOW3pts.jpg?1177184596</profile_image_url>"
            + "      <url></url>"
            + "      <protected>false</protected>"
            + "    </user>"
            + "  </status>"
            + "  <status>"
            + "    <created_at>Sat Jun 02 14:56:25 +0000 2007</created_at>"
            + "    <id>88182522</id>"
            + "    <text>@misspeter Downloading :)</text>"
            + "    <user>"
            + "      <id>4990471</id>"
            + "      <name>Guy David</name>"
            + "      <screen_name>guy_david</screen_name>"
            + "      <location>Somewhere in Israel</location>"
            + "      <description>I'm Guy David, a musician, a podcaster and a digital artist. I live in Israel with my wife and my son.</description>"
            + "      <profile_image_url>http://assets1.twitter.com/system/user/profile_image/4990471/normal/gd_290x290.jpg?1178210980</profile_image_url>"
            + "      <url>http://www.guydavid.com</url>"
            + "      <protected>false</protected>"
            + "    </user>"
            + "  </status>"
            + "</statuses>";

        Document doc = builder.parse(new ByteArrayInputStream(responseString.getBytes("UTF-8")));
        List<Status> statuses = Status.constructStatuses(doc,null);
        assertEquals(88182692, statuses.get(0).getId());
        assertEquals(88182532, statuses.get(1).getId());
        assertEquals(88182522, statuses.get(2).getId());
        assertEquals("Guy David", statuses.get(2).getUser().getName());
    }

}
