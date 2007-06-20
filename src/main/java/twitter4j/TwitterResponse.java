package twitter4j;

import org.w3c.dom.Element;

/**
 * Super class of Twitter Response objects.
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
            throw new TwitterException("Unexpected root node name:"+elem.getNodeName()+". Expected:"+rootName+". Check Twitter service availability.");
        }
    }
    protected static void ensureRootNodeNameIs(String rootName,Element elem) throws TwitterException{
        if(!rootName.equals(elem.getNodeName())){
            throw new TwitterException("Unexpected root node name:"+elem.getNodeName()+". Expected:"+rootName+". Check Twitter service availability.");
        }
    }

    protected String getChildText(String str) {
        return elem.getElementsByTagName(str).item(0).getTextContent();
    }

    protected int getChildInt(String str) {
        return Integer.valueOf(elem.getElementsByTagName(str).item(0).
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
