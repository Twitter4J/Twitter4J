package twitter4j.http;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import twitter4j.TwitterException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private static final long serialVersionUID = 6190279542077827227L;
    private SAXException saxe = null;

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
        } catch (NullPointerException ignore) {
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

    public Document asDocument() throws TwitterException{
        if(null == saxe && null == response){
            try {
                this.response = builder.parse(new ByteArrayInputStream(
                        responseString.getBytes("UTF-8")));
            } catch (SAXException saxe) {
                this.saxe = saxe;
            } catch (IOException ioe) {
                //should never reach here
                throw new TwitterException("Twitter returned a non-XML response", ioe);
            }
        }
        if(null != saxe){
            throw new TwitterException("Twitter returned a non-XML response", saxe);
        }
        return response;
    }

    public InputStreamReader asReader() {
        try {
            return new InputStreamReader(is, "UTF-8");
        } catch (java.io.UnsupportedEncodingException uee) {
            return new InputStreamReader(is);
        }
    }

    @Override
    public String toString(){
        return responseString;
    }
}
