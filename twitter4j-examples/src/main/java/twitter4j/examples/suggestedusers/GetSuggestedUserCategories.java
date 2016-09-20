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

package twitter4j.examples.suggestedusers;

import twitter4j.*;

/**
 * Shows authenticated user's suggested user categories.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class GetSuggestedUserCategories {
    /**
     * Usage: java twitter4j.examples.suggestedusers.GetSuggestedUserCategories
     *
     * @param args message
     */
    public static void main(String[] args) {
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            System.out.println("Showing suggested user categories.");
            ResponseList<Category> categories = twitter.getSuggestedUserCategories();
            for (Category category : categories) {
                System.out.println(category.getName() + ":" + category.getSlug());
            }
            System.out.println("done.");
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get suggested categories: " + te.getMessage());
            System.exit(-1);
        }
    }
}
