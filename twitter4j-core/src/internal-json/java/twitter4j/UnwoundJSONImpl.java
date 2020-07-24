package twitter4j;

/**
 * A data class representing the inwound of a single URL entity
 *
 * @author Vincent Demay - vincent at demay-fr.net
 * @since Twitter4J 4.0.7
 */
public class UnwoundJSONImpl implements Unwound {

    private static final long serialVersionUID = 7333552738058031624L;
    private String url;
    private int status;
    private String title;
    private String description;


    /* package */ UnwoundJSONImpl(JSONObject json) throws TwitterException {
        super();
        init(json);
    }

    /* package */ UnwoundJSONImpl(String url, int status, String title, String description) {
        super();
        this.url = url;
        this.status = status;
        this.title = title;
        this.description = description;
    }

    /* For serialization purposes only. */
    /* package */ UnwoundJSONImpl() {

    }


    private void init(JSONObject json) throws TwitterException {
        try {

            if (!json.isNull("url")) {
                this.url = json.getString("url");
            }

            if (!json.isNull("status")) {
                this.status = json.getInt("status");
            }

            if (!json.isNull("title")) {
                this.title = json.getString("title");
            }

            if (!json.isNull("description")) {
                this.description = json.getString("description");
            }

        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }


    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnwoundJSONImpl that = (UnwoundJSONImpl) o;

        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (!(status == that.status)) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + status;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UnwoundJSONImpl{" +
                "url='" + url + '\'' +
                ", status=" + status  +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
