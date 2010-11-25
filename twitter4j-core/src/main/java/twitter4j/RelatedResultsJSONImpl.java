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
/*package*/ final class RelatedResultsJSONImpl implements RelatedResults, java.io.Serializable {

    private static final String TWEETS_WITH_CONVERSATION = "TweetsWithConversation";
    private static final String TWEETS_WITH_REPLY = "TweetsWithReply";
    private static final String TWEETS_FROM_USER = "TweetsFromUser";

    private Map<String, ResponseList<Status>> tweetsMap;

    /* package */ public RelatedResultsJSONImpl(HttpResponse res) throws TwitterException {
        DataObjectFactoryUtil.clearThreadLocalMap();
        JSONArray list = res.asJSONArray();
        Map<String, ResponseList<Status>> tweetsMap = new HashMap<String, ResponseList<Status>>(2);
        try {
            for (int i = 0, listLen = list.length(); i < listLen; ++i) {
                JSONObject o = list.getJSONObject(i);
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
                    DataObjectFactoryUtil.registerJSONObject(status, json);
                    statuses.add(status);
                }
                DataObjectFactoryUtil.registerJSONObject(statuses, results);
            }
//            DataObjectFactoryUtil.registerJSONObject(this, list);
            this.tweetsMap = tweetsMap;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    @Override
    public ResponseList<Status> getTweetsWithConversation() {
        return this.tweetsMap.get(TWEETS_WITH_CONVERSATION);
    }

    @Override
    public ResponseList<Status> getTweetsWithReply() {
        return this.tweetsMap.get(TWEETS_WITH_REPLY);
    }

    @Override
    public ResponseList<Status> getTweetsFromUser() {
        return this.tweetsMap.get(TWEETS_FROM_USER);
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
