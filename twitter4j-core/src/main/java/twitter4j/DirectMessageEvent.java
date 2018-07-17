package twitter4j;

import java.util.Date;

/**
 * A data interface representing sent/received direct message event.
 *
 * @author Norbert Bartels - n.bartels at phpmonkeys.de
 */
public interface DirectMessageEvent extends TwitterResponse, EntitySupport, java.io.Serializable {

    long getId();

    String getText();

    long getSenderId();

    long getRecipientId();

    Date getCreatedAt();

}
