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
