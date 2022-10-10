package twitter4j.v1;

/**
 * stall warning
 */
public interface StallWarning {
    /**
     * @return code
     */
    String getCode();

    /**
     * @return message
     */
    String getMessage();

    /**
     * @return percent full
     */
    int getPercentFull();
}
