package twitter4j.v1;

/**
 * List of {@link DirectMessage}
 * <p>
 * like string cursor version of {@link PagableResponseList}
 *
 * @author Hiroaki TAKEUCHI - takke30 at gmail.com
 * @since Twitter4J 4.0.7
 */
public interface DirectMessageList extends ResponseList<DirectMessage> {

    /**
     * @return next cursor
     */
    String getNextCursor();
}
