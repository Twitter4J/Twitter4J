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

package twitter4j.util;

import junit.framework.TestCase;

import java.util.Calendar;
import java.util.Locale;

public class TimeSpanConverterTest extends TestCase {
    public TimeSpanConverterTest(String name) {
        super(name);
    }

    final int second = 1000;
    final int minute = second * 60;
    final int hour = minute * 60;

    TimeSpanConverter converter;

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private void assertTimeSpanString(String expected, long time) {
        assertEquals(expected, converter.toTimeSpanString(time));

    }

    // Beware the 'month' argument follows the Java Calendar standard and is 0-based.
    private long getSpecificLocalDateInMillis(int month, int day) {
        return this.getSpecificLocalDateInMillis((int) this.getCurrentYear(), month, day);
    }

    // Beware the 'month' argument follows the Java Calendar standard and is 0-based.
    private long getSpecificLocalDateInMillis(int year, int month, int day) {
        // Re-create the instance in case these tests are multi-threaded.
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        return cal.getTimeInMillis();
    }

    private long getCurrentYear() {
        // Re-create the instance in case these tests are multi-threaded.
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    public void testItalian() throws Exception {
        converter = new TimeSpanConverter(Locale.ITALIAN);
        assertTimeSpanString("Ora", System.currentTimeMillis() - second);
        assertTimeSpanString("4 secondi fa", System.currentTimeMillis() - second * 4);
        assertTimeSpanString("1 minuto fa", System.currentTimeMillis() - second * 61);
        assertTimeSpanString("3 minuti fa", System.currentTimeMillis() - minute * 3);
        assertTimeSpanString("1 ora fa", System.currentTimeMillis() - hour);
        assertTimeSpanString("3 ore fa", System.currentTimeMillis() - hour * 3);
        assertTimeSpanString("18 dic 09", getSpecificLocalDateInMillis(2009, 11, 18));
    }

    public void testSpanish() throws Exception {
        Locale[] locales = Locale.getAvailableLocales();
        Locale locale = null;
        for (Locale loc : locales) {
            if ("es".equals(loc.getLanguage())) {
                locale = loc;
            }
        }
        converter = new TimeSpanConverter(locale);
        assertTimeSpanString("Ahora", System.currentTimeMillis() - second);
        assertTimeSpanString("hace 4 segundos", System.currentTimeMillis() - second * 4);
        assertTimeSpanString("hace 1 munito", System.currentTimeMillis() - second * 61);
        assertTimeSpanString("hace 3 munitos", System.currentTimeMillis() - minute * 3);
        assertTimeSpanString("hace 1 hora", System.currentTimeMillis() - hour);
        assertTimeSpanString("hace 3 horas", System.currentTimeMillis() - hour * 3);
        assertTimeSpanString("18 dic 09", getSpecificLocalDateInMillis(2009, 11, 18));
    }

    public void testEnglish() throws Exception {
        converter = new TimeSpanConverter(Locale.ENGLISH);
        assertTimeSpanString("now", System.currentTimeMillis() - second);
        assertTimeSpanString("4 seconds ago", System.currentTimeMillis() - second * 4);
        assertTimeSpanString("58 seconds ago", System.currentTimeMillis() - second * 58);
        assertTimeSpanString("1 minute ago", System.currentTimeMillis() - second * 61);
        assertTimeSpanString("3 minutes ago", System.currentTimeMillis() - minute * 3);
        assertTimeSpanString("1 hour ago", System.currentTimeMillis() - hour);
        assertTimeSpanString("3 hours ago", System.currentTimeMillis() - hour * 3);
        assertTimeSpanString("18 Dec 09", getSpecificLocalDateInMillis(2009, 11, 18));
    }

    public void testFrench() throws Exception {
        converter = new TimeSpanConverter(Locale.FRENCH);
        assertTimeSpanString("Maintenant", System.currentTimeMillis() - second);
        assertTimeSpanString("Il y a 4 secondes", System.currentTimeMillis() - second * 4);
        assertTimeSpanString("Il y a 1 minute", System.currentTimeMillis() - second * 61);
        assertTimeSpanString("Il y a 3 minutes", System.currentTimeMillis() - minute * 3);
        assertTimeSpanString("Il y a 1 heure", System.currentTimeMillis() - hour);
        assertTimeSpanString("Il y a 3 heures", System.currentTimeMillis() - hour * 3);
        assertTimeSpanString("18 déc. 09", getSpecificLocalDateInMillis(2009, 11, 18));
    }

    public void testGerman() throws Exception {
        converter = new TimeSpanConverter(Locale.GERMAN);
        assertTimeSpanString("Jetzt", System.currentTimeMillis() - second);
        assertTimeSpanString("vor 4 Sekunden", System.currentTimeMillis() - second * 4);
        assertTimeSpanString("vor 1 Minute", System.currentTimeMillis() - second * 61);
        assertTimeSpanString("vor 3 Minuten", System.currentTimeMillis() - minute * 3);
        assertTimeSpanString("vor 1 Stunde", System.currentTimeMillis() - hour);
        assertTimeSpanString("vor 3 Stunden", System.currentTimeMillis() - hour * 3);
        assertTimeSpanString("18 Dez 09", getSpecificLocalDateInMillis(2009, 11, 18));
    }

    public void testJapanese() throws Exception {
        converter = new TimeSpanConverter(Locale.JAPANESE);
        assertTimeSpanString("今", System.currentTimeMillis() - second);
        assertTimeSpanString("4秒前", System.currentTimeMillis() - second * 4);
        assertTimeSpanString("58秒前", System.currentTimeMillis() - second * 58);
        assertTimeSpanString("1分前", System.currentTimeMillis() - second * 61);
        assertTimeSpanString("3分前", System.currentTimeMillis() - minute * 3);
        assertTimeSpanString("1時間前", System.currentTimeMillis() - hour);
        assertTimeSpanString("3時間前", System.currentTimeMillis() - hour * 3);
        assertTimeSpanString("09年12月18日", getSpecificLocalDateInMillis(2009, 11, 18));
    }
}
