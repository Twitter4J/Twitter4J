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
package twitter4j.util;

import junit.framework.TestCase;

import java.util.Locale;

public class TimeSpanConverterTest extends TestCase {
    public TimeSpanConverterTest(String name) {
        super(name);
    }

    int second = 1000;
    int minute = second * 60;
    int hour = minute * 60;

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

    public void testItalian() throws Exception {
        converter = new TimeSpanConverter(Locale.ITALIAN);
        assertTimeSpanString("Ora", System.currentTimeMillis() - second * 1);
        assertTimeSpanString("4 secondi fa", System.currentTimeMillis() - second * 4);
        assertTimeSpanString("1 minuto fa", System.currentTimeMillis() - second * 61);
        assertTimeSpanString("3 minuti fa", System.currentTimeMillis() - minute * 3);
        assertTimeSpanString("1 ora fa", System.currentTimeMillis() - hour * 1);
        assertTimeSpanString("3 ore fa", System.currentTimeMillis() - hour * 3);
        assertTimeSpanString("4 gen", 1294068301798l);
        assertTimeSpanString("1 gen", 1293809388354l);
        assertTimeSpanString("18 dic 09", 1261143946000l);
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
        assertTimeSpanString("Ahora", System.currentTimeMillis() - second * 1);
        assertTimeSpanString("hace 4 segundos", System.currentTimeMillis() - second * 4);
        assertTimeSpanString("hace 1 munito", System.currentTimeMillis() - second * 61);
        assertTimeSpanString("hace 3 munitos", System.currentTimeMillis() - minute * 3);
        assertTimeSpanString("hace 1 hora", System.currentTimeMillis() - hour * 1);
        assertTimeSpanString("hace 3 horas", System.currentTimeMillis() - hour * 3);
        assertTimeSpanString("4 ene", 1294068301798l);
        assertTimeSpanString("1 ene", 1293809388354l);
        assertTimeSpanString("18 dic 09", 1261143946000l);
    }

    public void testEnglish() throws Exception {
        converter = new TimeSpanConverter(Locale.ENGLISH);
        assertTimeSpanString("now", System.currentTimeMillis() - second * 1);
        assertTimeSpanString("4 seconds ago", System.currentTimeMillis() - second * 4);
        assertTimeSpanString("58 seconds ago", System.currentTimeMillis() - second * 58);
        assertTimeSpanString("1 minute ago", System.currentTimeMillis() - second * 61);
        assertTimeSpanString("3 minutes ago", System.currentTimeMillis() - minute * 3);
        assertTimeSpanString("1 hour ago", System.currentTimeMillis() - hour * 1);
        assertTimeSpanString("3 hours ago", System.currentTimeMillis() - hour * 3);
        assertTimeSpanString("4 Jan", 1294068301798l);
        assertTimeSpanString("1 Jan", 1293809388354l);
        assertTimeSpanString("18 Dec 09", 1261143946000l);
    }

    public void testFrench() throws Exception {
        converter = new TimeSpanConverter(Locale.FRENCH);
        assertTimeSpanString("Maintenant", System.currentTimeMillis() - second * 1);
        assertTimeSpanString("Il y a 4 secondes", System.currentTimeMillis() - second * 4);
        assertTimeSpanString("Il y a 1 minute", System.currentTimeMillis() - second * 61);
        assertTimeSpanString("Il y a 3 minutes", System.currentTimeMillis() - minute * 3);
        assertTimeSpanString("Il y a 1 heure", System.currentTimeMillis() - hour * 1);
        assertTimeSpanString("Il y a 3 heures", System.currentTimeMillis() - hour * 3);
        assertTimeSpanString("4 janv.", 1294068301798l);
        assertTimeSpanString("1 janv.", 1293809388354l);
        assertTimeSpanString("18 déc. 09", 1261143946000l);
    }

    public void testGerman() throws Exception {
        converter = new TimeSpanConverter(Locale.GERMAN);
        assertTimeSpanString("Jetzt", System.currentTimeMillis() - second * 1);
        assertTimeSpanString("vor 4 Sekunden", System.currentTimeMillis() - second * 4);
        assertTimeSpanString("vor 1 Minute", System.currentTimeMillis() - second * 61);
        assertTimeSpanString("vor 3 Minuten", System.currentTimeMillis() - minute * 3);
        assertTimeSpanString("vor 1 Stunde", System.currentTimeMillis() - hour * 1);
        assertTimeSpanString("vor 3 Stunden", System.currentTimeMillis() - hour * 3);
        assertTimeSpanString("4 Jan", 1294068301798l);
        assertTimeSpanString("1 Jan", 1293809388354l);
        assertTimeSpanString("18 Dez 09", 1261143946000l);
    }

    public void testJapanese() throws Exception {
        converter = new TimeSpanConverter(Locale.JAPANESE);
        assertTimeSpanString("今", System.currentTimeMillis() - second * 1);
        assertTimeSpanString("4秒前", System.currentTimeMillis() - second * 4);
        assertTimeSpanString("58秒前", System.currentTimeMillis() - second * 58);
        assertTimeSpanString("1分前", System.currentTimeMillis() - second * 61);
        assertTimeSpanString("3分前", System.currentTimeMillis() - minute * 3);
        assertTimeSpanString("1時間前", System.currentTimeMillis() - hour * 1);
        assertTimeSpanString("3時間前", System.currentTimeMillis() - hour * 3);
        assertTimeSpanString("1月4日", 1294068301798l);
        assertTimeSpanString("1月1日", 1293809388354l);
        assertTimeSpanString("09年12月18日", 1261143946000l);
    }
}
