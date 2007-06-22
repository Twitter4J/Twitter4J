package twitter4j.http;

import org.w3c.dom.Document;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import org.xml.sax.SAXException;
import java.io.ByteArrayInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.InputStreamReader;

/**
 * A data class representing HTTP Response
 */
public class Response implements java.io.Serializable {
    static DocumentBuilder builder = null;
    static {

        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private int statusCode;
    private Document response = null;
    private String responseString = null;
    private InputStream is;
    public Response(int statusCode, InputStream is) throws IOException {
        this.statusCode = statusCode;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuffer buf = new StringBuffer();
            String line;
            while (null != (line = br.readLine())) {
                buf.append(line).append("\n");
            }
            this.responseString = buf.toString();
            this.is = new ByteArrayInputStream(responseString.getBytes("UTF-8"));
            this.response = builder.parse(new ByteArrayInputStream(
                responseString.getBytes("UTF-8")));
        } catch (SAXException ignore) {
            //twitter returned non-XML response
        } catch (NullPointerException ignore) {
            //twitter returned non-XML response
            throw new IOException(ignore.getMessage());
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String asString() {
        return responseString;
    }

    public InputStream asStream() {
        return is;
    }

    public Document asDocument() {
        return response;
    }

    public InputStreamReader asReader() {
        try {
            return new InputStreamReader(is, "UTF-8");
        } catch (java.io.UnsupportedEncodingException uee) {
            return new InputStreamReader(is);
        }
    }
}
