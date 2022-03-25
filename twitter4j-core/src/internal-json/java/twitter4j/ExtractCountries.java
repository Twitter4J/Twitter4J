package twitter4j;

public class ExtractCountries extends TwitterResponseImpl{

    public ExtractCountries() {
        super();
    }

    public ExtractCountries(HttpResponse httpResponse) {
        super(httpResponse);
    }

    public String[] getWithHeldInCountries(JSONObject json) {
        String[] withheldInCountries = null;
        if (!json.isNull("withheld_in_countries")) {
            JSONArray withheld_in_countries = json.getJSONArray("withheld_in_countries");
            int length = withheld_in_countries.length();
            withheldInCountries = new String[length];
            for (int i = 0 ; i < length; i ++) {
                withheldInCountries[i] = withheld_in_countries.getString(i);
            }
        }
        return withheldInCountries;
    }
}
