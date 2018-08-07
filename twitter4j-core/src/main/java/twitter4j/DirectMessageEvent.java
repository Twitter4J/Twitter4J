package twitter4j;

import java.util.Date;

/**
 * @author Hiroaki TAKEUCHI - takke30 at gmail.com
 * @since Twitter4J 4.0.7
 */
public interface DirectMessageEvent extends TwitterResponse, EntitySupport {

    String getType();

    long getId();

    Date getCreatedTimestamp();

    long getRecipientId();

    long getSenderId();

    String getText();
}
