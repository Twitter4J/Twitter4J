package twitter4j;

import java.util.Arrays;

public class DerivedJSONImpl implements Derived {

    private DerivedLocation[] locations;


    /*package*/DerivedJSONImpl(JSONObject json) throws TwitterException {
        super();
        init(json);
    }

    /* For serialization purposes only. */
    /* package */ DerivedJSONImpl() {

    }

    private void init(JSONObject json) throws TwitterException {
        try {
            if (!json.isNull("locations")) {
                JSONArray locationsArray = json.getJSONArray("locations");
                locations = new DerivedLocation[locationsArray.length()];
                for (int i = 0; i < locationsArray.length(); i++) {
                    locations[i] = new DerivedLocationJSONImpl(locationsArray.getJSONObject(i));
                }
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), jsone);
        }
    }


    @Override
    public DerivedLocation[] getLocations() {
        return locations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DerivedJSONImpl that = (DerivedJSONImpl) o;

        if (locations != null ? !locations.equals(that.locations) : that.locations != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (locations != null ? locations.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DerivedJSONImpl{" +
                "locations=" + Arrays.toString(locations) +
                '}';
    }
}
