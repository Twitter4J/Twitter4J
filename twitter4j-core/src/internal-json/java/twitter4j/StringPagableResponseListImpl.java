package twitter4j;

public class StringPagableResponseListImpl<T extends TwitterResponse> extends ResponseListImpl<T> implements StringPageableResponseList<T> {

    private final String previousCursor;
    private final String nextCursor;

    StringPagableResponseListImpl(RateLimitStatus rateLimitStatus, int accessLevel) {
        super(rateLimitStatus, accessLevel);
        previousCursor = "";
        nextCursor = "";
    }

    StringPagableResponseListImpl(int size, JSONObject json, HttpResponse res) {
        super(size, res);
        this.previousCursor = ParseUtil.getRawString("previous_cursor", json);
        this.nextCursor = ParseUtil.getRawString("next_cursor", json);
    }

    @Override
    public boolean hasPrevious() {
        return "".equals(previousCursor);
    }

    @Override
    public String getPreviousCursor() {
        return previousCursor;
    }

    @Override
    public boolean hasNext() {
        return "".equals(nextCursor);
    }

    @Override
    public String getNextCursor() {
        return nextCursor;
    }
}
