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
 * @since Twitter4J 2.1.7
 */
public interface LegalResourcesAsync {
    /**
     * Returns Twitter's' Terms of Service.
     * <br>This method calls http://api.twitter.com/1/legal/tos.json
     *
     * @see <a href="https://dev.twitter.com/docs/api/1/get/legal/tos">GET legal/tos | Twitter Developers</a>
     * @since Twitter4J 2.1.7
     */
    void getTermsOfService();

    /**
     * Returns Twitter's Privacy Policy.
     * <br>This method calls http://api.twitter.com/1/legal/privacy.json
     *
     * @see <a href="https://dev.twitter.com/docs/api/1/get/legal/privacy">GET legal/privacy | Twitter Developers</a>
     * @since Twitter4J 2.1.7
     */
    void getPrivacyPolicy();
}
