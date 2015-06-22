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
package twitter4j.management;

import java.util.Map;

/**
 * Simple MBean interface for APIStatistics. Method-level statistics are exposed
 * as a Map of formatted strings
 *
 * @author Nick Dellamaggiore (nick.dellamaggiore at gmail.com)
 * @see APIStatisticsOpenMBean for a dynamic version of this mbean with tabular representation
 */
public interface APIStatisticsMBean extends InvocationStatistics {
    public Map<String, String> getMethodLevelSummariesAsString();

    public String getMethodLevelSummary(String methodName);

    public Iterable<? extends InvocationStatistics> getInvocationStatistics();
}
