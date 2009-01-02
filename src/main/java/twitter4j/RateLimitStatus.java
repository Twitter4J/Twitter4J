package twitter4j;

import org.w3c.dom.Element;

import java.util.Date;

/**
 * A data class representing Twitter rate limit status
 */
public class RateLimitStatus extends TwitterResponse {
    private int remainingHits;
    private int hourlyLimit;
    private int resetTimeInSeconds;
    private Date dateTime;
    private static final long serialVersionUID = -2326597301406666858L;

    /* package */ RateLimitStatus(Element elem) throws TwitterException {
        super();
        remainingHits = getChildInt("remaining-hits", elem);
        hourlyLimit = getChildInt("hourly-limit", elem);
        resetTimeInSeconds = getChildInt("reset-time-in-seconds", elem);
        dateTime = getChildDate("reset-time", elem, "yyyy-M-d'T'HH:mm:ss+00:00");
    }

    public int getRemainingHits() {
        return remainingHits;
    }

    public int getHourlyLimit() {
        return hourlyLimit;
    }

    public int getResetTimeInSeconds() {
        return resetTimeInSeconds;
    }

    public Date getDateTime() {
        return dateTime;
    }
}
