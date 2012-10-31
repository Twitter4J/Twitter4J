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
package twitter4j.api;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @author Mocel - mocel at guma.jp
 * @since 3.0.0
 */
public interface UndocumentedResourcesAsync {
    /**
     * If available, returns an array of replies and mentions related to the specified Tweet. There is no guarantee there will be any replies or mentions in the response. This method is only available to users who have access to #newtwitter.
     * <br>This method calls http://api.twitter.com/1.1/related_results/show/:id
     *
     * @param statusId the numerical ID of the status you're trying to retrieve
     * @see <a href="http://groups.google.com/group/twitter-api-announce/msg/34909da7c399169e">#newtwitter and the API - Twitter API Announcements | Google Group</a>
     * @since Twitter4J 2.1.8
     */
    void getRelatedResults(long statusId);
}
