/*
 * Copyright 2007 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package twitter4j;

import twitter4j.v1.HelpResources;
import twitter4j.v1.ResponseList;


/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.3
 */
class LanguageJSONImpl implements HelpResources.Language {
    private static final long serialVersionUID = 7494362811767097342L;
    private String name;
    private String code;
    private String status;

    LanguageJSONImpl(JSONObject json) throws TwitterException {
        super();
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        try {
            name = json.getString("name");
            code = json.getString("code");
            status = json.getString(("status"));

        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json, jsone);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getStatus() {
        return status;
    }

    static ResponseList<HelpResources.Language> createLanguageList(HttpResponse res, boolean jsonStoreEnabled) throws TwitterException {
        return createLanguageList(res.asJSONArray(), res, jsonStoreEnabled);
    }

    /*package*/
    static ResponseList<HelpResources.Language> createLanguageList(JSONArray list, HttpResponse res
            , boolean jsonStoreEnabled) throws TwitterException {
        if (jsonStoreEnabled) {
            TwitterObjectFactory.clearThreadLocalMap();
        }
        try {
            int size = list.length();
            ResponseList<HelpResources.Language> languages =
                    new ResponseListImpl<>(size, res);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                HelpResources.Language language = new LanguageJSONImpl(json);
                languages.add(language);
                if (jsonStoreEnabled) {
                    TwitterObjectFactory.registerJSONObject(language, json);
                }
            }
            if (jsonStoreEnabled) {
                TwitterObjectFactory.registerJSONObject(languages, list);
            }
            return languages;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }
}