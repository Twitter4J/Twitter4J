package twitter4j;

import java.util.Date;

/**
 * A data interface representing sent/received direct message event.
 *
 * @author Norbert Bartels - n.bartels at phpmonkeys.de
 */
final class LazyDirectMessageEvent implements twitter4j.DirectMessageEvent {

    private final HttpResponse res;
    private final ObjectFactory factory;
    private DirectMessageEvent target = null;

    LazyDirectMessageEvent(HttpResponse res, ObjectFactory factory) {
        this.res = res;
        this.factory = factory;
    }

    private DirectMessageEvent getTarget() {
        if (target == null) {
            try {
                target = factory.createDirectMessageEvent(res);
            } catch (TwitterException e) {
                throw new TwitterRuntimeException(e);
            }
        }
        return target;
    }

    @Override
    public long getId() {
        return getTarget().getId();
    }

    @Override
    public String getText() {
        return getTarget().getText();
    }

    @Override
    public long getSenderId() {
        return getTarget().getSenderId();
    }

    @Override
    public long getRecipientId() {
        return getTarget().getRecipientId();
    }

    @Override
    public Date getCreatedAt() {
        return getTarget().getCreatedAt();
    }

    @Override
    public RateLimitStatus getRateLimitStatus() {
        return getTarget().getRateLimitStatus();
    }

    @Override
    public int getAccessLevel() {
        return getTarget().getAccessLevel();
    }

    @Override
    public UserMentionEntity[] getUserMentionEntities() {
        return getTarget().getUserMentionEntities();
    }

    @Override
    public URLEntity[] getURLEntities() {
        return getTarget().getURLEntities();
    }

    @Override
    public HashtagEntity[] getHashtagEntities() {
        return getTarget().getHashtagEntities();
    }

    @Override
    public MediaEntity[] getMediaEntities() {
        return getTarget().getMediaEntities();
    }

    @Override
    public SymbolEntity[] getSymbolEntities() {
        return getTarget().getSymbolEntities();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectMessageEvent)) return false;
        return getTarget().equals(o);
    }

    @Override
    public int hashCode() {
        return getTarget().hashCode();
    }

    @Override
    public String toString() {
        return "LazyDirectMessageEvent{" +
                "target=" + getTarget() +
                "}";
    }

}
