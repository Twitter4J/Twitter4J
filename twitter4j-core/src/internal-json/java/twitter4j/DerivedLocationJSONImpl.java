package twitter4j;

public class DerivedLocationJSONImpl implements DerivedLocation {
    private String country;
    private String countryCode;
    private String locality;
    private String region;
    private String subRegion;
    private String fullName;

    public static String getRawString(String name, JSONObject json) {
        try {
            if (json.isNull(name)) {
                return null;
            } else {
                return json.getString(name);
            }
        } catch (JSONException jsone) {
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    /*package*/ DerivedLocationJSONImpl(JSONObject json) throws TwitterException {
        super();
        init(json);
    }

    /* For serialization purposes only. */
    /* package */ DerivedLocationJSONImpl() {

    }

    private void init(JSONObject json) throws TwitterException {
        try {
            country = getRawString("country", json);
            countryCode = getRawString("country_code", json);
            locality = getRawString("locality", json);
            region = getRawString("region", json);
            subRegion = getRawString("sub_region", json);
            fullName = getRawString("full_name", json);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), jsone);
        }
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public String getCountryCode() {
        return countryCode;
    }

    @Override
    public String getLocality() {
        return locality;
    }

    @Override
    public String getRegion() {
        return region;
    }

    @Override
    public String getSubRegion() {
        return subRegion;
    }

    @Override
    public String getFullName() {
        return fullName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DerivedLocationJSONImpl that = (DerivedLocationJSONImpl) o;

        if (country != null ? !country.equals(that.country) : that.country != null) return false;
        if (countryCode != null ? !countryCode.equals(that.countryCode) : that.countryCode != null) return false;
        if (locality != null ? !locality.equals(that.locality) : that.locality != null) return false;
        if (region != null ? !region.equals(that.region) : that.region != null) return false;
        if (subRegion != null ? !subRegion.equals(that.subRegion) : that.subRegion != null) return false;
        if (fullName != null ? !fullName.equals(that.region) : that.fullName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = country != null ? country.hashCode() : 0;
        result = 31 * result + countryCode != null ? countryCode.hashCode() : 0;
        result = 31 * result + locality != null ? locality.hashCode() : 0;
        result = 31 * result + region != null ? region.hashCode() : 0;
        result = 31 * result + subRegion != null ? subRegion.hashCode() : 0;
        result = 31 * result + fullName != null ? fullName.hashCode() : 0;
        return result;
    }

    @Override
    public String toString() {
        return "DerivedLocationJSONImpl{" +
                "country='" + country + '\'' +
                ", countryCode=" + countryCode  +
                ", locality=" + locality  +
                ", region=" + region  +
                ", subRegion=" + subRegion  +
                ", fullName=" + fullName  +
                '}';
    }
}
