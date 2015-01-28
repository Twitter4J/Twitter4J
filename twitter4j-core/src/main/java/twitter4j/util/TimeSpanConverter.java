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

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.0
 */
public final class TimeSpanConverter implements Serializable {
    private static final int ONE_HOUR_IN_SECONDS = 60 * 60;
    private static final int ONE_DAY_IN_SECONDS = 24 * ONE_HOUR_IN_SECONDS;
    private static final int ONE_MONTH_IN_SECONDS = 30 * ONE_DAY_IN_SECONDS;
    private static final long serialVersionUID = 8665013607650804076L;
    private final MessageFormat[] formats = new MessageFormat[6];
    private final SimpleDateFormat dateMonth;
    private final SimpleDateFormat dateMonthYear;

    private static final int NOW = 0;
    private static final int N_SECONDS_AGO = 1;
    private static final int A_MINUTE_AGO = 2;
    private static final int N_MINUTES_AGO = 3;
    private static final int AN_HOUR_AGO = 4;
    private static final int N_HOURS_AGO = 5;

    /**
     * Constructs an instance with default locale
     */
    public TimeSpanConverter() {
        this(Locale.getDefault());
    }

    /**
     * Constructs an instance with the specified locale
     *
     * @param locale locale
     */
    public TimeSpanConverter(Locale locale) {
        String language = locale.getLanguage();
        if ("it".equals(language)) {
            formats[NOW] = new MessageFormat("Ora");
            formats[N_SECONDS_AGO] = new MessageFormat("{0} secondi fa");
            formats[A_MINUTE_AGO] = new MessageFormat("1 minuto fa");
            formats[N_MINUTES_AGO] = new MessageFormat("{0} minuti fa");
            formats[AN_HOUR_AGO] = new MessageFormat("1 ora fa");
            formats[N_HOURS_AGO] = new MessageFormat("{0} ore fa");
            dateMonth = new SimpleDateFormat("d MMM", locale);
            dateMonthYear = new SimpleDateFormat("d MMM yy", locale);
        } else if ("kr".equals(language)) {
            formats[NOW] = new MessageFormat("지금");
            formats[N_SECONDS_AGO] = new MessageFormat("{0}초 전");
            formats[A_MINUTE_AGO] = new MessageFormat("1분 전");
            formats[N_MINUTES_AGO] = new MessageFormat("{0}분 전");
            formats[AN_HOUR_AGO] = new MessageFormat("1시간 전");
            formats[N_HOURS_AGO] = new MessageFormat("{0} ore fa");
            dateMonth = new SimpleDateFormat("M월 d일", locale);
            dateMonthYear = new SimpleDateFormat("yy년 M월 d일", locale);
        } else if ("es".equals(language)) {
            formats[NOW] = new MessageFormat("Ahora");
            formats[N_SECONDS_AGO] = new MessageFormat("hace {0} segundos");
            formats[A_MINUTE_AGO] = new MessageFormat("hace 1 minuto");
            formats[N_MINUTES_AGO] = new MessageFormat("hace {0} minutos");
            formats[AN_HOUR_AGO] = new MessageFormat("hace 1 hora");
            formats[N_HOURS_AGO] = new MessageFormat("hace {0} horas");
            dateMonth = new SimpleDateFormat("d MMM", locale);
            dateMonthYear = new SimpleDateFormat("d MMM yy", locale);
        } else if ("fr".equals(language)) {
            formats[NOW] = new MessageFormat("Maintenant");
            formats[N_SECONDS_AGO] = new MessageFormat("Il y a {0} secondes");
            formats[A_MINUTE_AGO] = new MessageFormat("Il y a 1 minute");
            formats[N_MINUTES_AGO] = new MessageFormat("Il y a {0} minutes");
            formats[AN_HOUR_AGO] = new MessageFormat("Il y a 1 heure");
            formats[N_HOURS_AGO] = new MessageFormat("Il y a {0} heures");
            dateMonth = new SimpleDateFormat("d MMM", locale);
            dateMonthYear = new SimpleDateFormat("d MMM yy", locale);
        } else if ("de".equals(language)) {
            formats[NOW] = new MessageFormat("Jetzt");
            formats[N_SECONDS_AGO] = new MessageFormat("vor {0} Sekunden");
            formats[A_MINUTE_AGO] = new MessageFormat("vor 1 Minute");
            formats[N_MINUTES_AGO] = new MessageFormat("vor {0} Minuten");
            formats[AN_HOUR_AGO] = new MessageFormat("vor 1 Stunde");
            formats[N_HOURS_AGO] = new MessageFormat("vor {0} Stunden");
            dateMonth = new SimpleDateFormat("d MMM", locale);
            dateMonthYear = new SimpleDateFormat("d MMM yy", locale);
        } else if ("ja".equals(language)) {
            formats[NOW] = new MessageFormat("今");
            formats[N_SECONDS_AGO] = new MessageFormat("{0}秒前");
            formats[A_MINUTE_AGO] = new MessageFormat("1分前");
            formats[N_MINUTES_AGO] = new MessageFormat("{0}分前");
            formats[AN_HOUR_AGO] = new MessageFormat("1時間前");
            formats[N_HOURS_AGO] = new MessageFormat("{0}時間前");
            dateMonth = new SimpleDateFormat("M月d日", locale);
            dateMonthYear = new SimpleDateFormat("yy年M月d日", locale);
        } else {
            formats[NOW] = new MessageFormat("now");
            formats[N_SECONDS_AGO] = new MessageFormat("{0} seconds ago");
            formats[A_MINUTE_AGO] = new MessageFormat("1 minute ago");
            formats[N_MINUTES_AGO] = new MessageFormat("{0} minutes ago");
            formats[AN_HOUR_AGO] = new MessageFormat("1 hour ago");
            formats[N_HOURS_AGO] = new MessageFormat("{0} hours ago");
            dateMonth = new SimpleDateFormat("d MMM", Locale.ENGLISH);
            dateMonthYear = new SimpleDateFormat("d MMM yy", Locale.ENGLISH);
        }
    }

    public String toTimeSpanString(Date date) {
        return toTimeSpanString(date.getTime());
    }

    public String toTimeSpanString(long milliseconds) {
        int deltaInSeconds = (int) ((System.currentTimeMillis() - milliseconds) / 1000);
        if (deltaInSeconds >= ONE_DAY_IN_SECONDS) {
            if (deltaInSeconds >= ONE_MONTH_IN_SECONDS) {
                return dateMonthYear.format(new Date(milliseconds));
            } else {
                return dateMonth.format(new Date(milliseconds));
            }
        }
        return toTimeSpanString(deltaInSeconds);
    }

    private String toTimeSpanString(int deltaInSeconds) {
        if (deltaInSeconds <= 1) {
            return formats[NOW].format(null);
        } else if (deltaInSeconds < 60) {
            return formats[N_SECONDS_AGO].format(new Object[]{deltaInSeconds});
        }

        if (deltaInSeconds < 45 * 60) {
            int minutes = deltaInSeconds / 60;
            if (minutes == 1) {
                return formats[A_MINUTE_AGO].format(null);
            }
            return formats[N_MINUTES_AGO].format(new Object[]{minutes});
        }

        if (deltaInSeconds < 105 * 60) {// between 0:45 and 1:45 => 1
            return formats[AN_HOUR_AGO].format(null);
        }
        int hours = ((deltaInSeconds + 15 * 60) / ONE_HOUR_IN_SECONDS);
        return formats[N_HOURS_AGO].format(new Object[]{hours});
    }
}
