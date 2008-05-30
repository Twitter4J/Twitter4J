package twitter4j.http;

import twitter4j.User;

/**
 * A data class representing HTTP Post parameter
 */
public class PostParameter implements java.io.Serializable {
    String name;
    String value;
    public PostParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }
    public String getName(){
        return name;
    }
    public String getValue(){
        return value;
    }

    @Override public int hashCode() {
        return name.hashCode() + value.hashCode();
    }

    @Override public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof PostParameter) {
            PostParameter that = (PostParameter) obj;
            return this.name.equals(that.name) &&
                this.value.equals(that.value);
        }
        return false;
    }
}
