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

import java.util.Arrays;

/**
 * A data class representing array of numeric IDs.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
/*package*/ final class IDsJSONImpl extends TwitterResponseImpl implements IDs {

    private int[] ids;
    private long previousCursor = -1;
    private long nextCursor = -1;
    private static final long serialVersionUID = -6585026560164704953L;

    private IDsJSONImpl(HttpResponse res) throws TwitterException {
        super(res);
    }
    /*package*/ static IDs getFriendsIDs(HttpResponse res) throws TwitterException {
        IDsJSONImpl friendsIDs = new IDsJSONImpl(res);
        JSONObject json = res.asJSONObject();
        JSONArray idList;
        try {
            idList = json.getJSONArray("ids");
            friendsIDs.ids = new int[idList.length()];
            for (int i = 0; i < idList.length(); i++) {
                try {
                    friendsIDs.ids[i] = Integer.parseInt(idList.getString(i));
                } catch (NumberFormatException nfe) {
                    throw new TwitterException("Twitter API returned malformed response: " + json, nfe);
                }
            }
            friendsIDs.previousCursor = ParseUtil.getLong("previous_cursor", json);
            friendsIDs.nextCursor = ParseUtil.getLong("next_cursor", json);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
        return friendsIDs;
    }


    /*package*/ static IDs getBlockIDs(HttpResponse res) throws TwitterException {
        IDsJSONImpl blockIDs = new IDsJSONImpl(res);
        JSONArray idList = null;
        try {
            idList = res.asJSONArray();
            blockIDs.ids = new int[idList.length()];
            for (int i = 0; i < idList.length(); i++) {
                try {
                    blockIDs.ids[i] = Integer.parseInt(idList.getString(i));
                } catch (NumberFormatException nfe) {
                    throw new TwitterException("Twitter API returned malformed response: " + idList, nfe);
                }
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
        return blockIDs;
    }

    /**
     * {@inheritDoc}
     */
    public int[] getIDs() {
        return ids;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasPrevious(){
        return 0 != previousCursor;
    }

    /**
     * {@inheritDoc}
     */
    public long getPreviousCursor() {
        return previousCursor;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasNext(){
        return 0 != nextCursor;
    }

    /**
     * {@inheritDoc}
     */
    public long getNextCursor() {
        return nextCursor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IDs)) return false;

        IDs iDs = (IDs) o;

        if (!Arrays.equals(ids, iDs.getIDs())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return ids != null ? Arrays.hashCode(ids) : 0;
    }

    @Override
    public String toString() {
        return "IDsJSONImpl{" +
                "ids=" + ids +
                ", previousCursor=" + previousCursor +
                ", nextCursor=" + nextCursor +
                '}';
    }
}