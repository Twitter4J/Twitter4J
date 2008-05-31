package twitter4j;

import org.w3c.dom.Element;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

/**
 * Super class of Twitter Response objects.
 *
 * @see twitter4j.DirectMessage
 * @see twitter4j.Status
 * @see twitter4j.User
 * @see twitter4j.UserWithStatus
 */
public class TwitterResponse implements java.io.Serializable {
    protected final Element elem;
    protected Twitter twitter;
    public TwitterResponse(Element elem, Twitter twitter) {
        this.elem = elem;
        this.twitter = twitter;
    }
    protected void ensureRootNodeNameIs(String rootName) throws TwitterException{
        if(!rootName.equals(elem.getNodeName())){
            throw new TwitterException("Unexpected root node name:"+elem.getNodeName()+". Expected:"+rootName+". Check Twitter service availability.\n"+toString(elem));
        }
    }
    protected void ensureRootNodeNameIs(String[] rootNames) throws TwitterException{
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
    protected static void ensureRootNodeNameIs(String rootName,Element elem) throws TwitterException{
        if(!rootName.equals(elem.getNodeName())){
            throw new TwitterException("Unexpected root node name:"+elem.getNodeName()+". Expected:"+rootName+". Check Twitter service availability.\n"+toString(elem));
        }
    }

    private static String toString(Element doc) {
        try{
        StringWriter output = new StringWriter();
        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(output));
        return output.toString();
        }catch(TransformerException tfe){
            return "";
        }
    }

    protected String getChildText(String str) {
        return elem.getElementsByTagName(str).item(0).getTextContent();
    }

    protected int getChildInt(String str) {
        String str2 = elem.getElementsByTagName(str).item(0).
                getTextContent();
        if (null == str2 || "".equals(str2)) {
            return -1;
        } else {
            return Integer.valueOf(str2);
        }
    }
    protected boolean getChildBoolean(String str) {
        return Boolean.valueOf(elem.getElementsByTagName(str).item(0).
                               getTextContent());
    }

    @Override public int hashCode() {
        return elem.hashCode();
    }

    @Override public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof TwitterResponse) {
            ( (TwitterResponse) obj).elem.equals(this.elem);
        }
        return false;
    }
}
