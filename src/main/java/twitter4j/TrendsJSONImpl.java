/*
Copyright (c) 2007-2009, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package twitter4j;

import twitter4j.http.HttpResponse;
import twitter4j.org.json.JSONArray;
import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static twitter4j.ParseUtil.*;

/**
 * A data class representing Trends.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.0.2
 */
/*package*/ final class TrendsJSONImpl implements Trends, java.io.Serializable {
    private Date asOf;
    private Date trendAt;
    private Trend[] trends;
    private static final long serialVersionUID = -7151479143843312309L;

    public int compareTo(Trends that) {
        return this.trendAt.compareTo(that.getTrendAt());
    }

    /*package*/

    TrendsJSONImpl(Date asOf, Date trendAt, Trend[] trends)
            throws TwitterException {
        this.asOf = asOf;
        this.trendAt = trendAt;
        this.trends = trends;
    }

    /*package*/

    static List<Trends> createTrendsList(HttpResponse res) throws
            TwitterException {
        JSONObject json = res.asJSONObject();
        List<Trends> trends;
        try {
            Date asOf = parseTrendsDate(json.getString("as_of"));
            JSONObject trendsJson = json.getJSONObject("trends");
            trends = new ArrayList<Trends>(trendsJson.length());
            Iterator ite = trendsJson.keys();
            while (ite.hasNext()) {
                String key = (String) ite.next();
                JSONArray array = trendsJson.getJSONArray(key);
                Trend[] trendsArray = jsonArrayToTrendArray(array);
                if (key.length() == 19) {
                    // current trends
                    trends.add(new TrendsJSONImpl(asOf, getDate(key
                            , "yyyy-MM-dd HH:mm:ss"), trendsArray));
                } else if (key.length() == 16) {
                    // daily trends
                    trends.add(new TrendsJSONImpl(asOf, getDate(key
                            , "yyyy-MM-dd HH:mm"), trendsArray));
                } else if (key.length() == 10) {
                    // weekly trends
                    trends.add(new TrendsJSONImpl(asOf, getDate(key
                            , "yyyy-MM-dd"), trendsArray));
                }
            }
            Collections.sort(trends);
            return trends;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + res.asString(), jsone);
        }
    }

    /*package*/

    static Trends createTrends(HttpResponse res) throws TwitterException {
        JSONObject json = res.asJSONObject();
        try {
            Date asOf = parseTrendsDate(json.getString("as_of"));
            JSONArray array = json.getJSONArray("trends");
            Trend[] trendsArray = jsonArrayToTrendArray(array);
            return new TrendsJSONImpl(asOf, asOf, trendsArray);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + res.asString(), jsone);
        }
    }

    private static Date parseTrendsDate(String asOfStr) throws TwitterException {
        Date parsed;
        if (asOfStr.length() == 10) {
            parsed = new Date(Long.parseLong(asOfStr) * 1000);
        } else {
            parsed = getDate(asOfStr, "EEE, d MMM yyyy HH:mm:ss z");
        }
        return parsed;
    }

    private static Trend[] jsonArrayToTrendArray(JSONArray array) throws JSONException {
        Trend[] trends = new Trend[array.length()];
        for (int i = 0; i < array.length(); i++) {
            JSONObject trend = array.getJSONObject(i);
            trends[i] = new TrendJSONImpl(trend);
        }
        return trends;
    }

    /**
     * {@inheritDoc}
     */
    public Trend[] getTrends() {
        return this.trends;
    }

    /**
     * {@inheritDoc}
     */
    public Date getAsOf() {
        return asOf;
    }

    /**
     * {@inheritDoc}
     */
    public Date getTrendAt() {
        return trendAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trends)) return false;

        Trends trends1 = (Trends) o;

        if (asOf != null ? !asOf.equals(trends1.getAsOf()) : trends1.getAsOf() != null)
            return false;
        if (trendAt != null ? !trendAt.equals(trends1.getTrendAt()) : trends1.getTrendAt() != null)
            return false;
        if (!Arrays.equals(trends, trends1.getTrends())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = asOf != null ? asOf.hashCode() : 0;
        result = 31 * result + (trendAt != null ? trendAt.hashCode() : 0);
        result = 31 * result + (trends != null ? Arrays.hashCode(trends) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TrendsJSONImpl{" +
                "asOf=" + asOf +
                ", trendAt=" + trendAt +
                ", trends=" + (trends == null ? null : Arrays.asList(trends)) +
                '}';
    }
}
