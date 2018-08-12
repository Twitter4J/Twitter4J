package twitter4j;

/*package*/ class EntitiesParseUtil {

    /*package*/ static UserMentionEntity[] getUserMentions(JSONObject entities) throws JSONException, TwitterException {
        if (!entities.isNull("user_mentions")) {
            JSONArray userMentionsArray = entities.getJSONArray("user_mentions");
            int len = userMentionsArray.length();
            UserMentionEntity[] userMentionEntities = new UserMentionEntity[len];
            for (int i = 0; i < len; i++) {
                userMentionEntities[i] = new UserMentionEntityJSONImpl(userMentionsArray.getJSONObject(i));
            }
            return userMentionEntities;
        } else {
            return null;
        }
    }

    /*package*/ static URLEntity[] getUrls(JSONObject entities) throws JSONException, TwitterException {
        if (!entities.isNull("urls")) {
            JSONArray urlsArray = entities.getJSONArray("urls");
            int len = urlsArray.length();
            URLEntity[] urlEntities = new URLEntity[len];
            for (int i = 0; i < len; i++) {
                urlEntities[i] = new URLEntityJSONImpl(urlsArray.getJSONObject(i));
            }
            return urlEntities;
        } else {
            return null;
        }
    }

    /*package*/ static HashtagEntity[] getHashtags(JSONObject entities) throws JSONException, TwitterException {
        if (!entities.isNull("hashtags")) {
            JSONArray hashtagsArray = entities.getJSONArray("hashtags");
            int len = hashtagsArray.length();
            HashtagEntity[] hashtagEntities = new HashtagEntity[len];
            for (int i = 0; i < len; i++) {
                hashtagEntities[i] = new HashtagEntityJSONImpl(hashtagsArray.getJSONObject(i));
            }
            return hashtagEntities;
        } else {
            return null;
        }
    }

    /*package*/ static SymbolEntity[] getSymbols(JSONObject entities) throws JSONException, TwitterException {
        if (!entities.isNull("symbols")) {
            JSONArray symbolsArray = entities.getJSONArray("symbols");
            int len = symbolsArray.length();
            SymbolEntity[] symbolEntities = new SymbolEntity[len];
            for (int i = 0; i < len; i++) {
                // HashtagEntityJSONImpl also implements SymbolEntities
                symbolEntities[i] = new HashtagEntityJSONImpl(symbolsArray.getJSONObject(i));
            }
            return symbolEntities;
        } else {
            return null;
        }
    }

    /*package*/ static MediaEntity[] getMedia(JSONObject entities) throws JSONException, TwitterException {
        if (!entities.isNull("media")) {
            JSONArray mediaArray = entities.getJSONArray("media");
            int len = mediaArray.length();
            MediaEntity[] mediaEntities = new MediaEntity[len];
            for (int i = 0; i < len; i++) {
                mediaEntities[i] = new MediaEntityJSONImpl(mediaArray.getJSONObject(i));
            }
            return mediaEntities;
        } else {
            return null;
        }
    }
}
