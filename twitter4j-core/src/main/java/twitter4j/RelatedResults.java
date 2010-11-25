package twitter4j;

public interface RelatedResults extends java.io.Serializable {

    /**
     * Returns the 8 or less statuses with conversation
     * @return list of statuses with conversation
     */
    ResponseList<Status> getTweetsWithConversation();

    /**
     * Returns the 8 or less statuses with reply.
     * @return list of statuses with reply
     */
    ResponseList<Status> getTweetsWithReply();

    /**
     * Return the 3 or less latest statuses from the user who sent the origin tweet.
     * @return list of latest statuses
     */
    ResponseList<Status> getTweetsFromUser();
}
