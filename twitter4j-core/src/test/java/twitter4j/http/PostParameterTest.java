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
package twitter4j.http;

import junit.framework.TestCase;
import twitter4j.internal.http.HttpParameter;

import java.io.File;

/**
  * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class PostParameterTest extends TestCase {

    public PostParameterTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    public void testBooleanParameter() throws Exception {
        assertEquals("true", new HttpParameter("test", true).getValue());
        assertEquals("false", new HttpParameter("test", false).getValue());
    }

    public void testgetContentType() throws Exception {
        assertValidContentType("image/jpeg","img.jpeg");
        assertValidContentType("image/jpeg","img.JPEG");
        assertValidContentType("image/jpeg","img.jpg");
        assertValidContentType("image/jpeg","img.JPG");
        assertValidContentType("image/jpeg","img.JpG");
        assertValidContentType("image/jpeg","img.jPg");

        assertValidContentType("image/gif","img.gif");
        assertValidContentType("image/gif","img.GIF");
        assertValidContentType("image/gif","img.GiF");
        assertValidContentType("image/gif","img.gIf");

        assertValidContentType("image/png","img.png");
        assertValidContentType("image/png","img.PNG");
        assertValidContentType("image/png","img.PnG");
        assertValidContentType("image/png","img.pNg");

        assertValidContentType("application/octet-stream","img.jpegjpeg");
        assertValidContentType("application/octet-stream","img.");
        assertValidContentType("application/octet-stream","img.els");
        assertValidContentType("application/octet-stream","img.ai");
        assertValidContentType("application/octet-stream","img.ps");
        assertValidContentType("application/octet-stream","img.txt");
        assertValidContentType("application/octet-stream","img");

    }
    private void assertValidContentType(String expected, String fileName){
        HttpParameter param = new HttpParameter("file", new File(fileName));
        assertEquals(expected, param.getContentType());

    }
}
