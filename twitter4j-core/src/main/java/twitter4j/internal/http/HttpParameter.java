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
package twitter4j.internal.http;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * A data class representing HTTP Post parameter
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class HttpParameter implements Comparable, java.io.Serializable {
    private String name = null;
    private String value = null;
    private File file = null;
    private InputStream fileBody = null;
    private static final long serialVersionUID = -8708108746980739212L;

    public HttpParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }
    public HttpParameter(String name, File file) {
        this.name = name;
        this.file = file;
    }

    public HttpParameter(String name, String fileName, InputStream fileBody) {
        this.name = name;
        this.file = new File(fileName);
        this.fileBody = fileBody;
    }

    public HttpParameter(String name, int value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public HttpParameter(String name, long value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public HttpParameter(String name, double value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public HttpParameter(String name, boolean value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public String getName(){
        return name;
    }
    public String getValue(){
        return value;
    }

    public File getFile() {
        return file;
    }

    public InputStream getFileBody(){
    	return fileBody;
    }

    public boolean isFile(){
        return null != file;
    }

    public boolean hasFileBody(){
        return null != fileBody;
    }

    private static final String JPEG = "image/jpeg";
    private static final String GIF = "image/gif";
    private static final String PNG = "image/png";
    private static final String OCTET = "application/octet-stream";

    /**
     * 
     * @return content-type
     */
    public String getContentType() {
        if (!isFile()) {
            throw new IllegalStateException("not a file");
        }
        String contentType;
        String extensions = file.getName();
        int index = extensions.lastIndexOf(".");
        if (-1 == index) {
            // no extension
            contentType = OCTET;
        } else {
            extensions = extensions.substring(extensions.lastIndexOf(".") + 1).toLowerCase();
            if (extensions.length() == 3) {
                if ("gif".equals(extensions)) {
                    contentType = GIF;
                } else if ("png".equals(extensions)) {
                    contentType = PNG;
                } else if ("jpg".equals(extensions)) {
                    contentType = JPEG;
                } else {
                    contentType = OCTET;
                }
            } else if (extensions.length() == 4) {
                if ("jpeg".equals(extensions)) {
                    contentType = JPEG;
                } else {
                    contentType = OCTET;
                }
            } else {
                contentType = OCTET;
            }
        }
        return contentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HttpParameter)) return false;

        HttpParameter that = (HttpParameter) o;

        if (file != null ? !file.equals(that.file) : that.file != null)
            return false;
        if (fileBody != null ? !fileBody.equals(that.fileBody) : that.fileBody != null)
            return false;
        if (!name.equals(that.name)) return false;
        if (value != null ? !value.equals(that.value) : that.value != null)
            return false;

        return true;
    }

    public static boolean containsFile(HttpParameter[] params) {
        boolean containsFile = false;
        if(null == params){
            return false;
        }
        for (HttpParameter param : params) {
            if (param.isFile()) {
                containsFile = true;
                break;
            }
        }
        return containsFile;
    }
    /*package*/ static boolean containsFile(List<HttpParameter> params) {
        boolean containsFile = false;
        for (HttpParameter param : params) {
            if (param.isFile()) {
                containsFile = true;
                break;
            }
        }
        return containsFile;
    }

    public static HttpParameter[] getParameterArray(String name, String value) {
        return new HttpParameter[]{new HttpParameter(name,value)};
    }
    public static HttpParameter[] getParameterArray(String name, int value) {
        return getParameterArray(name,String.valueOf(value));
    }

    public static HttpParameter[] getParameterArray(String name1, String value1
            , String name2, String value2) {
        return new HttpParameter[]{new HttpParameter(name1, value1)
                , new HttpParameter(name2, value2)};
    }
    public static HttpParameter[] getParameterArray(String name1, int value1
            , String name2, int value2) {
        return getParameterArray(name1,String.valueOf(value1),name2,String.valueOf(value2));
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (file != null ? file.hashCode() : 0);
        result = 31 * result + (fileBody != null ? fileBody.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PostParameter{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", file=" + file +
                ", fileBody=" + fileBody +
                '}';
    }

    public int compareTo(Object o) {
        int compared;
        HttpParameter that = (HttpParameter) o;
        compared = name.compareTo(that.name);
        if (0 == compared) {
            compared = value.compareTo(that.value);
        }
        return compared;
    }

    public static String encodeParameters(HttpParameter[] httpParams) {
        if (null == httpParams) {
            return "";
        }
        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < httpParams.length; j++) {
            if (httpParams[j].isFile()) {
                throw new IllegalArgumentException("parameter [" + httpParams[j].name + "]should be text");
            }
            if (j != 0) {
                buf.append("&");
            }
            buf.append(encode(httpParams[j].name))
                    .append("=").append(encode(httpParams[j].value));
        }
        return buf.toString();
    }

    /**
     * @param value string to be encoded
     * @return encoded string
     * @see <a href="http://wiki.oauth.net/TestCases">OAuth / TestCases</a>
     * @see <a href="http://groups.google.com/group/oauth/browse_thread/thread/a8398d0521f4ae3d/9d79b698ab217df2?hl=en&lnk=gst&q=space+encoding#9d79b698ab217df2">Space encoding - OAuth | Google Groups</a>
     * @see <a href="http://tools.ietf.org/html/rfc3986#section-2.1">RFC 3986 - Uniform Resource Identifier (URI): Generic Syntax - 2.1. Percent-Encoding</a>
     */
    public static String encode(String value) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
        }
        StringBuffer buf = new StringBuffer(encoded.length());
        char focus;
        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                buf.append("%2A");
            } else if (focus == '+') {
                buf.append("%20");
            } else if (focus == '%' && (i + 1) < encoded.length()
                    && encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
                buf.append('~');
                i += 2;
            } else {
                buf.append(focus);
            }
        }
        return buf.toString();
    }
}
