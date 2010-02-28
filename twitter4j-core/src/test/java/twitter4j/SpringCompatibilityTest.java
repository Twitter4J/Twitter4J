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
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import twitter4j.conf.ConfigurationContext;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class SpringCompatibilityTest extends TestCase {
    XmlBeanFactory beanFactory;

    public SpringCompatibilityTest(String name) {
        super(name);
    }


    protected void setUp() throws Exception {
        super.setUp();
        writeFile("./twitter4j.properties", "user=one"
                + "\n" + "password=pasword-one");
        Resource res = new ClassPathResource("spring-beans.xml");
        beanFactory = new XmlBeanFactory(res);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        deleteFile("./twitter4j.properties");
    }

    public void testFactoryInstantiation() throws Exception {
        TwitterFactory twitterFactory = (TwitterFactory)beanFactory.getBean("twitterFactory");
        Twitter twitter = twitterFactory.getInstance();
        assertTrue(twitter instanceof Twitter);

        AsyncTwitterFactory asyncTwitterFactory = (AsyncTwitterFactory)beanFactory.getBean("asyncTwitterFactory");
        AsyncTwitter asyncTwitter = asyncTwitterFactory.getInstance();
        assertTrue(asyncTwitter instanceof AsyncTwitter);

        TwitterStreamFactory twitterStreamFactory = (TwitterStreamFactory) beanFactory.getBean("twitterStreamFactory");
        TwitterStream twitterStream = twitterStreamFactory.getInstance("test", "test");
        assertTrue(twitterStream instanceof TwitterStream);
    }

    public void testTwitterInstantiation() throws Exception {
        Twitter twitter = (Twitter) beanFactory.getBean("twitter");
        assertTrue(twitter instanceof Twitter);

        AsyncTwitter asyncTwitter = (AsyncTwitter) beanFactory.getBean("asyncTwitter");
        assertTrue(asyncTwitter instanceof AsyncTwitter);

        try {
            TwitterStream twitterStream = (TwitterStream) beanFactory.getBean("twitterStream");
            assertTrue(twitterStream instanceof TwitterStream);
        } catch (org.springframework.beans.factory.BeanCreationException ignore) {
        }
    }

    private void writeFile(String path, String content) throws IOException {
        File file = new File(path);
        file.delete();
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(content);
        bw.close();
    }

    private void deleteFile(String path) throws IOException {
        File file = new File(path);
        file.delete();
    }
}
