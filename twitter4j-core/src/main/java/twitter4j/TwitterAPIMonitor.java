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

import twitter4j.conf.ConfigurationContext;
import twitter4j.internal.logging.Logger;
import twitter4j.management.APIStatistics;
import twitter4j.management.APIStatisticsMBean;
import twitter4j.management.APIStatisticsOpenMBean;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Singleton instance of all Twitter API monitoring. Handles URL parsing and "wire off" logic.
 * We could avoid using a singleton here if Twitter objects were instantiated
 * from a factory.
 *
 * @author Nick Dellamaggiore (nick.dellamaggiore <at> gmail.com)
 * @since Twitter4J 2.2.1
 */
public class TwitterAPIMonitor {
    private static final Logger logger = Logger.getLogger(TwitterAPIMonitor.class);
    // https?:\/\/[^\/]+\/[0-9.]*\/([a-zA-Z_\.]*).*
    // finds the "method" part a Twitter REST API url, ignoring member-specific resource names
    private static final Pattern pattern =
            Pattern.compile("https?:\\/\\/[^\\/]+\\/[0-9.]*\\/([a-zA-Z_\\.]*).*");

    private static final TwitterAPIMonitor SINGLETON = new TwitterAPIMonitor();

    private static final APIStatistics STATISTICS = new APIStatistics(100);


    static {
        boolean isJDK14orEarlier = false;
        try {
            String versionStr = System.getProperty("java.specification.version");
            if (versionStr != null) {
                isJDK14orEarlier = 1.5d > Double.parseDouble(versionStr);
            }
            if (ConfigurationContext.getInstance().isDalvik()) {
                // quick and dirty workaround for TFJ-296
                // it must be an Android/Dalvik/Harmony side issue!!!!
                System.setProperty("http.keepAlive", "false");
            }
        } catch (SecurityException ignore) {
            // Unsigned applets are not allowed to access System properties
            isJDK14orEarlier = true;
        }
        try {

            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            if (isJDK14orEarlier) {
                ObjectName oName = new ObjectName("twitter4j.mbean:type=APIStatistics");
                mbs.registerMBean(STATISTICS, oName);
            } else {
                ObjectName oName = new ObjectName("twitter4j.mbean:type=APIStatisticsOpenMBean");
                APIStatisticsOpenMBean openMBean = new APIStatisticsOpenMBean(STATISTICS);
                mbs.registerMBean(openMBean, oName);
            }
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (MBeanRegistrationException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (NotCompliantMBeanException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    /**
     * Constructor
     */
    private TwitterAPIMonitor() {
    }

    public static TwitterAPIMonitor getInstance() {
        return SINGLETON;
    }

    public APIStatisticsMBean getStatistics() {
        return STATISTICS;
    }

    void methodCalled(String twitterUrl, long elapsedTime, boolean success) {
        Matcher matcher = pattern.matcher(twitterUrl);
        if (matcher.matches() && matcher.groupCount() > 0) {
            String method = matcher.group(1);
            STATISTICS.methodCalled(method, elapsedTime, success);
        }
    }
}
