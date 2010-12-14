/*
Copyright (c) 2007-2010, Yusuke Yamamoto
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
 * A data class representing one single of a URL entity.
 *
 * @author Mocel - mocel at guma.jp
 * @since Twitter4J 2.1.9
 */
/* package */ final class URLEntityJSONImpl implements URLEntity {

    private int index = -1;
    private int endIndex = -1;
    private URL url;
    private URL expandedUrl;

    private static final long serialVersionUID = 1165188478018146676L;

    /* package */ URLEntityJSONImpl(JSONObject json) throws TwitterException {
        super();
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        try {
            JSONArray indicesArray = json.getJSONArray("indices");
            this.index = indicesArray.getInt(0);
            this.endIndex = indicesArray.getInt(1);

            try {
                this.url = new URL(json.getString("url"));
            } catch (MalformedURLException ignore) {
            }

            if (! json.isNull("expanded_url")) {
                try {
                    this.expandedUrl = new URL(json.getString("expanded_url"));
                } catch (MalformedURLException ignore) {
                }
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URL getUrl() {
        return url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URL getExpandedUrl() {
        return expandedUrl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIndex() {
        return index;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getEndIndex() {
        return endIndex;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 17;
        result = prime * result + index;
        result = prime * result + endIndex;
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        result = prime * result + ((expandedUrl == null) ? 0 : expandedUrl.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof URLEntityJSONImpl) {
            URLEntityJSONImpl other = (URLEntityJSONImpl) obj;
            if (index != other.index) {
                return false;
            }
            if (endIndex != other.endIndex) {
                return false;
            }
            if (url == null) {
                if (other.url != null) {
                    return false;
                }
            } else if (! url.equals(other.url)) {
                return false;
            }
            if (expandedUrl == null) {
                if (other.expandedUrl != null) {
                    return false;
                }
            } else if (! expandedUrl.equals(other.expandedUrl)) {
                return false;
            }

            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "URLEntityJSONImpl{startIndex=" + index +
                ",endIndex=" + endIndex +
                ", url=" + url +
                ", expandedUrl=" + expandedUrl + '}';
    }
}
