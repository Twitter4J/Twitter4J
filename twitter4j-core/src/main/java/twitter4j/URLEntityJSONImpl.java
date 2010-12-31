/*
Copyright (c) 2007-2011, Yusuke Yamamoto
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

import java.net.MalformedURLException;
import java.net.URL;

import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

/**
 * A data class representing one single URL entity.
 *
 * @author Mocel - mocel at guma.jp
 * @since Twitter4J 2.1.9
 */
/* package */ final class URLEntityJSONImpl implements URLEntity {

    private int start = -1;
    private int end = -1;
    private URL url;
    private URL expandedURL;
    private String displayURL;

    private static final long serialVersionUID = 1165188478018146676L;

    /* package */ URLEntityJSONImpl(JSONObject json) throws TwitterException {
        super();
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        try {
            JSONArray indicesArray = json.getJSONArray("indices");
            this.start = indicesArray.getInt(0);
            this.end = indicesArray.getInt(1);

            try {
                this.url = new URL(json.getString("url"));
            } catch (MalformedURLException ignore) {
            }

            if (!json.isNull("expanded_url")) {
                try {
                    this.expandedURL = new URL(json.getString("expanded_url"));
                } catch (MalformedURLException ignore) {
                }
            }
            if (!json.isNull("display_url")) {
                this.displayURL = json.getString("display_url");
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    /**
     * {@inheritDoc}
     */
    public URL getURL() {
        return url;
    }

    /**
     * {@inheritDoc}
     */
    public URL getExpandedURL() {
        return expandedURL;
    }

    /**
     * {@inheritDoc}
     */
    public String getDisplayURL() {
        return displayURL;
    }

    /**
     * {@inheritDoc}
     */
    public int getStart() {
        return start;
    }

    /**
     * {@inheritDoc}
     */
    public int getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        URLEntityJSONImpl that = (URLEntityJSONImpl) o;

        if (end != that.end) return false;
        if (start != that.start) return false;
        if (displayURL != null ? !displayURL.equals(that.displayURL) : that.displayURL != null)
            return false;
        if (expandedURL != null ? !expandedURL.equals(that.expandedURL) : that.expandedURL != null)
            return false;
        if (url != null ? !url.equals(that.url) : that.url != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = start;
        result = 31 * result + end;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (expandedURL != null ? expandedURL.hashCode() : 0);
        result = 31 * result + (displayURL != null ? displayURL.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "URLEntityJSONImpl{" +
                "start=" + start +
                ", end=" + end +
                ", url=" + url +
                ", expandedURL=" + expandedURL +
                ", displayURL=" + displayURL +
                '}';
    }
}
