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


/**
 * Object that collects/aggregates statistics for the invocation of a given method.
 *
 * @author Nick Dellamaggiore (nick.dellamaggiore <at> gmail.com)
 */
public class InvocationStatisticsCalculator implements InvocationStatistics {
    private String name;
    private long[] times;
    private int index;
    private long callCount;
    private long errorCount;
    private long totalTime;

    /**
     * @param name        the name of this API method
     * @param historySize the number of calls to track (for invocation time averaging)
     */
    public InvocationStatisticsCalculator(String name, int historySize) {
        this.name = name;
        times = new long[historySize];
    }

    void increment(long time, boolean success) {
        callCount++;
        errorCount += success ? 0 : 1;
        totalTime += time;

        times[index] = time;

        if (++index >= times.length)
            index = 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getCallCount() {
        return callCount;
    }

    @Override
    public long getErrorCount() {
        return errorCount;
    }

    @Override
    public long getTotalTime() {
        return totalTime;
    }

    @Override
    public synchronized long getAverageTime() {
        int stopIndex = Math.min(Math.abs((int) callCount), times.length);
        if (stopIndex == 0) {
            return 0;
        }

        long totalTime = 0;
        for (int i = 0; i < stopIndex; i++) {
            totalTime += times[i];

        }
        return totalTime / stopIndex;
    }

    @Override
    public synchronized void reset() {
        callCount = 0;
        errorCount = 0;
        totalTime = 0;
        times = new long[times.length];
        index = 0;
    }

    @Override
    public String toString() {
        // StringBuilder is faster... do we still need to support JDK 1.4?
        StringBuilder sb = new StringBuilder();
        sb.append("calls=").append(getCallCount()).append(",")
                .append("errors=").append(getErrorCount()).append(",")
                .append("totalTime=").append(getTotalTime()).append(",")
                .append("avgTime=").append(getAverageTime());

        return sb.toString();
    }
}