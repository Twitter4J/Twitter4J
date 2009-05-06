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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class SizeofStatusChecker {
    public static void main(String[] args) throws Exception {
        String statusXML = "<status>  <created_at>Thu Oct 30 10:51:24 +0000 2008</created_at>  <id>981972359</id>  <text>test</text>  <source>&lt;a href=&quot;http://yusuke.homeip.net/twitter4j/&quot;&gt;Twitter4J&lt;/a&gt;</source>  <truncated>false</truncated>  <in_reply_to_status_id></in_reply_to_status_id>  <in_reply_to_user_id></in_reply_to_user_id>  <favorited>false</favorited>  <user>    <id>6358482</id>    <name>twit4j</name>    <screen_name>twit4j</screen_name>    <location>location:Thu Oct 30 19:51:21 J</location>    <description></description>    <profile_image_url>http://static.twitter.com/images/default_profile_normal.png</profile_image_url>    <url></url>    <protected>false</protected>    <followers_count>3</followers_count>  </user></status>";
        DocumentBuilder builder = null;
        builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        int count = 10000;
        Status[] statuses = new Status[count];
        long before;
        System.gc();
        before = Runtime.getRuntime().freeMemory();
        for (int i = 0; i < count; i++) {
            Document doc = builder.parse(new ByteArrayInputStream(statusXML.getBytes()));
            Element elem = doc.getDocumentElement();
//            statuses[i] = new Status(elem, null);
        }
        System.out.println((before - Runtime.getRuntime().freeMemory()) / count);
    }
}
