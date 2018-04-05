package twitter4j;

/**
 * List of {@link DirectMessageEvent}
 *
 * like string cursor version of {@link PagableResponseList}
 *
 * @author Hiroaki TAKEUCHI - takke30 at gmail.com
 * @since Twitter4J 4.0.7
 */
public interface DirectMessageEventList extends ResponseList<DirectMessageEvent> {

    String getNextCursor();
}
