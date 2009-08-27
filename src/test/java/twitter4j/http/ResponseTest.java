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
package twitter4j.http;

import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class ResponseTest extends TestCase {
    public ResponseTest(String name){
        super(name);
    }
    @Override
    protected void setUp() throws Exception{
        super.setUp();
    } 
    protected void tearDown() throws Exception{
        super.tearDown();
    }
    boolean failed = false;
    public void testUnescape() {
        String unescaped = Response.unescape("Twitter4J &#22312;android&#19978;&#19981;&#33021;&#26174;&#31034;&#20013;&#25991;&#65292;&#36824;&#26159;UTF8&#26684;&#24335;&#65292;&#24456;&#22855;&#24618;&#65292;&#20294;&#32431;JAVA&#30340;&#27809;&#38382;&#39064;");
        String expected = "Twitter4J 在android上不能显示中文，还是UTF8格式，很奇怪，但纯JAVA的没问题";
        assertEquals(expected, unescaped);
    }
    public void testMultithreaded() throws Exception{
        failed = false;
        InputStream is = new FileInputStream("response_samples/public_timeline.xml");
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuffer buf = new StringBuffer();
        String line;
        while (null != (line = br.readLine())) {
            buf.append(line).append("\n");
        }
        is.close();
        String content = buf.toString();
        int threadCount = 2;
        Thread[] threads = new Thread[threadCount];
        for(int i=0; i<threads.length;i++){
            threads[i] = new Runner(content);
            threads[i].setDaemon(true);
            threads[i].start();
        }
        while(waiting != threadCount){
            Thread.sleep(100);
        }
        synchronized(lock){
            lock.notifyAll();
        }
        while(waiting != 0){
            Thread.sleep(100);
        }
        assertFalse(failed);
    }
    Object lock = new Object();
    int waiting = 0;

    class Runner extends Thread{
        String str;
        Runner(String str){
            this.str = str;
        }
        int count = 100;

        public void run() {
            waiting++;
            synchronized(lock){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                }
            }
            for (int i = count; i > 0; i--) {
                try {
                    new Response(str).asDocument();
                } catch (Throwable e) {
                    e.printStackTrace();
                    failed = true;
                }
            }
            waiting--;
        }
    }

}
