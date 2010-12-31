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

import java.util.HashMap;
import java.util.Map;

import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

/**
 * A data class representing related_results API response
 * @author Mocel - mocel at guma.jp
 */
/*package*/ final class RelatedResultsJSONImpl extends TwitterResponseImpl implements RelatedResults, java.io.Serializable {

    private static final String TWEETS_WITH_CONVERSATION = "TweetsWithConversation";
    private static final String TWEETS_WITH_REPLY = "TweetsWithReply";
    private static final String TWEETS_FROM_USER = "TweetsFromUser";
    private static final long serialVersionUID = -7417061781993004083L;

    private Map<String, ResponseList<Status>> tweetsMap;

    /* package */ RelatedResultsJSONImpl(HttpResponse res) throws TwitterException {
        super(res);
        DataObjectFactoryUtil.clearThreadLocalMap();
        JSONArray jsonArray = res.asJSONArray();
        init(jsonArray, res, true);

    }
    /* package */ RelatedResultsJSONImpl(JSONArray jsonArray) throws TwitterException {
        super();
        init(jsonArray, null, false);

    }

    private void init(JSONArray jsonArray, HttpResponse res, boolean registerRawJSON) throws TwitterException {
        tweetsMap = new HashMap<String, ResponseList<Status>>(2);
        try {
            for (int i = 0, listLen = jsonArray.length(); i < listLen; ++i) {
                JSONObject o = jsonArray.getJSONObject(i);
                if (! "Tweet".equals(o.getString("resultType"))) {
                    continue;
                }

                String groupName = o.getString("groupName");
                if (groupName.length() == 0
                        || ! (groupName.equals(TWEETS_WITH_CONVERSATION) || groupName.equals(TWEETS_WITH_REPLY) || groupName.equals(TWEETS_FROM_USER))) {
                    continue;
                }

                JSONArray results = o.getJSONArray("results");
                ResponseList<Status> statuses = tweetsMap.get(groupName);
                if (statuses == null) {
                    statuses = new ResponseListImpl<Status>(results.length(), res);
                    tweetsMap.put(groupName, statuses);
                }

                for (int j = 0, resultsLen = results.length(); j < resultsLen; ++j) {
                    JSONObject json = results.getJSONObject(j).getJSONObject("value");
                    Status status = new StatusJSONImpl(json);
                    if(registerRawJSON){
                        DataObjectFactoryUtil.registerJSONObject(status, json);
                    }
                    statuses.add(status);
                }
                if(registerRawJSON){
                    DataObjectFactoryUtil.registerJSONObject(statuses, results);
                }
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getTweetsWithConversation() {
        ResponseList<Status> statuses = this.tweetsMap.get(TWEETS_WITH_CONVERSATION);
        if (null != statuses) {
            return statuses;
        } else {
            return new ResponseListImpl<Status>(0, null);
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getTweetsWithReply() {
        ResponseList<Status> statuses = this.tweetsMap.get(TWEETS_WITH_REPLY);
        if (null != statuses) {
            return statuses;
        } else {
            return new ResponseListImpl<Status>(0, null);
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getTweetsFromUser() {
        ResponseList<Status> statuses = this.tweetsMap.get(TWEETS_FROM_USER);
        if (null != statuses) {
            return statuses;
        } else {
            return new ResponseListImpl<Status>(0, null);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + tweetsMap.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RelatedResultsJSONImpl) {
            RelatedResultsJSONImpl other = (RelatedResultsJSONImpl) obj;
            if (tweetsMap == null) {
                if (other.tweetsMap != null)
                    return false;
            } else if (!tweetsMap.equals(other.tweetsMap))
                return false;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "RelatedResultsJSONImpl {tweetsMap=" + tweetsMap + "}";
    }
}
