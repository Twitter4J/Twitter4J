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

import twitter4j.management.APIStatistics;
import twitter4j.management.APIStatisticsOpenMBean;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * Registers MBeans with the platform mbean server. Run this class and then
 * fire up JConsole to see the mbeans in action. Be sure to start the VM with
 * the following system property enabled: -Dcom.sun.management.jmxremote
 *
 * @author Nick Dellamaggiore (nick.dellamaggiore at gmail.com)
 */
public class MBeanServerRunner {
    public static void main(String[] args) throws Exception {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("twitter4j.mbean:type=APIStatistics");
        ObjectName name2 = new ObjectName("twitter4j.mbean:type=APIStatisticsOpenMBean");
        APIStatistics statsMBean = new APIStatistics(100);
        mbs.registerMBean(statsMBean, name);
        APIStatisticsOpenMBean openMBean = new APIStatisticsOpenMBean(statsMBean);
        mbs.registerMBean(openMBean, name2);

        for (int i = 0; i < 10; i++) {
            statsMBean.methodCalled("foo", 5, true);
            statsMBean.methodCalled("bar", 2, true);
            statsMBean.methodCalled("baz", 10, true);
            statsMBean.methodCalled("foo", 2, false);
        }

        Thread.sleep(1000 * 60 * 60);
    }
}