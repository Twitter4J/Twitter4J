package twitter4j;

/**
 * List of {@link DirectMessage}
 *
 * like string cursor version of {@link PagableResponseList}
 *
 * @author Hiroaki TAKEUCHI - takke30 at gmail.com
 * @since Twitter4J 4.0.7
 */
public interface DirectMessageList extends ResponseList<DirectMessage> {

    String getNextCursor();
}
