package twitter4j.api;

import twitter4j.*;

import java.io.InputStream;

/**
 * @author Norbert Bartels - n.bartels at phpmonkeys.de
 */
public interface DirectMessageEventsResources {

    PagableResponseList<DirectMessageEvent> getDirectMessageEvents()
            throws TwitterException;

    PagableResponseList<DirectMessageEvent> getDirectMessageEvents(Paging paging)
            throws TwitterException;

    /**
     * Returns a single direct message, specified by an id parameter.
     * <br>This method calls https://api.twitter.com/1.1/direct_messages/show/:id.json
     *
     * @param id message id
     * @return DirectMessageEvent
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://developer.twitter.com/en/docs/direct-messages/sending-and-receiving/api-reference/get-event">GET direct_messages/events/show/:id | Twitter Developers</a>
     * @since Twitter4J 2.1.9
     */
    DirectMessageEvent showDirectMessageEvent(long id) throws TwitterException;

    /**
     * Destroys the direct message specified in the required ID parameter.  The authenticating user must be the recipient of the specified direct message.
     * <br>This method calls https://api.twitter.com/1.1/direct_messages/events/destroy
     *
     * @param id the ID of the direct message to destroy
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://developer.twitter.com/en/docs/direct-messages/sending-and-receiving/api-reference/delete-message-event">POST direct_messages/events/destroy/:id | Twitter Developers</a>
     */
    void destroyDirectMessageEvent(long id)
            throws TwitterException;

    /**
     * Sends a new direct message to the specified user from the authenticating user.  Requires both the userid and text parameters below.
     * <br>This method calls https://api.twitter.com/1.1/direct_messages/events/new
     *
     * @param userId the user id of the user to whom send the direct message
     * @param text   The text of your direct message.
     * @return DirectMessageEvent
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://developer.twitter.com/en/docs/direct-messages/sending-and-receiving/api-reference/new-event">POST direct_messages/events/new | Twitter Developers</a>
     */
    DirectMessageEvent sendDirectMessageEvent(long userId, String text)
            throws TwitterException;

    /**
     * Returns a stream of the image included in direct messages.
     *
     * @param url image url
     * @return InputStream
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="https://dev.twitter.com/discussions/24255">Access media shared in direct messages | Twitter Developers</a>
     * @since Twitter4J 3.0.6
     */
    InputStream getDMImageAsStream(String url)
            throws TwitterException;
}
