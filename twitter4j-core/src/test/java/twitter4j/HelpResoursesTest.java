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

import twitter4j.api.HelpResources;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
public class HelpResoursesTest extends TwitterTestBase {
    public HelpResoursesTest(String name) {
        super(name);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testHelpMethods() throws Exception {
        ResponseList<HelpResources.Language> languages = twitter1.getLanguages();
        assertTrue(languages.size() > 5);
        HelpResources.Language language = languages.get(0);
        assertNotNull(language.getCode());
        assertNotNull(language.getName());
        assertNotNull(language.getStatus());

        TwitterAPIConfiguration conf = twitter1.getAPIConfiguration();
        assertEquals(3145728, conf.getPhotoSizeLimit());
        assertEquals(23, conf.getCharactersReservedPerMedia());
        assertEquals(22, conf.getShortURLLength());
        assertEquals(23, conf.getShortURLLengthHttps());
        assertEquals(4, conf.getPhotoSizes().size());
        assertTrue(20 < conf.getNonUsernamePaths().length);
        assertEquals(1, conf.getMaxMediaPerUpload());
    }

    public void testLegalResources() throws Exception {
        assertNotNull(twitter1.getTermsOfService());
        assertNotNull(twitter1.getPrivacyPolicy());
    }
}
