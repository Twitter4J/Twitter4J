package twitter4j;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Super class of Twitter Response objects.
 *
 * @see twitter4j.DirectMessage
 * @see twitter4j.Status
 * @see twitter4j.User
 * @see twitter4j.UserWithStatus
 */
public class TwitterResponse implements java.io.Serializable {
    private Map<String,SimpleDateFormat> formatMap = new HashMap<String,SimpleDateFormat>();
    private static final long serialVersionUID = 3519962197957449562L;

    public TwitterResponse() {
    }

    protected void ensureRootNodeNameIs(String rootName, Element elem) throws TwitterException {
        if (!rootName.equals(elem.getNodeName())) {
            throw new TwitterException("Unexpected root node name:" + elem.getNodeName() + ". Expected:" + rootName + ". Check Twitter service availability.\n" + toString(elem));
        }
    }

    protected void ensureRootNodeNameIs(String[] rootNames, Element elem) throws TwitterException {
        String actualRootName = elem.getNodeName();
        for (String rootName : rootNames) {
            if (rootName.equals(actualRootName)) {
                return;
            }
        }
        String expected = "";
        for (int i = 0; i < rootNames.length; i++) {
            if (i != 0) {
                expected += " or ";
            }
            expected += rootNames[i];
        }
        throw new TwitterException("Unexpected root node name:" + elem.getNodeName() + ". Expected:" + expected + ". Check Twitter service availability.\n" + toString(elem));
    }

    protected static void ensureRootNodeNameIs(String rootName, Document doc) throws TwitterException {
        Element elem = doc.getDocumentElement();
        if (!rootName.equals(elem.getNodeName())) {
            throw new TwitterException("Unexpected root node name:" + elem.getNodeName() + ". Expected:" + rootName + ". Check Twitter service availability.\n" + toString(elem));
        }
    }

    protected static boolean isRootNodeNilClasses(Document doc) {
        String root = doc.getDocumentElement().getNodeName();
        return "nil-classes".equals(root) || "nilclasses".equals(root);
    }

    private static String toString(Element doc) {
        try {
            StringWriter output = new StringWriter();
            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(output));
            return output.toString();
        } catch (TransformerException tfe) {
            return "";
        }
    }

    protected String getChildText(String str, Element elem) {
        return elem.getElementsByTagName(str).item(0).getTextContent();
    }

    protected int getChildInt(String str, Element elem) {
        String str2 = elem.getElementsByTagName(str).item(0).
                getTextContent();
        if (null == str2 || "".equals(str2)) {
            return -1;
        } else {
            return Integer.valueOf(str2);
        }
    }

    protected long getChildLong(String str, Element elem) {
        String str2 = elem.getElementsByTagName(str).item(0).
                getTextContent();
        if (null == str2 || "".equals(str2)) {
            return -1;
        } else {
            return Long.valueOf(str2);
        }
    }

    protected boolean getChildBoolean(String str, Element elem) {
        return Boolean.valueOf(elem.getElementsByTagName(str).item(0).
                getTextContent());
    }

    protected Date getChildDate(String str, Element elem) throws TwitterException {
        return getChildDate(str, elem, "EEE MMM d HH:mm:ss z yyyy");
    }

    protected Date getChildDate(String str, Element elem, String format) throws TwitterException {
        return encodeDate(getChildText(str, elem),format);
    }
    protected Date encodeDate(String str, String format) throws TwitterException{
        SimpleDateFormat sdf = formatMap.get(format);
        if (null == sdf) {
            sdf = new SimpleDateFormat(format, Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            formatMap.put(format, sdf);
        }
        try {
            return sdf.parse(str);
        } catch (ParseException pe) {
            throw new TwitterException("Unexpected format(" + str + ") returned from twitter.com");
        }
    }

}
