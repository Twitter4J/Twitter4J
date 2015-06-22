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

import junit.framework.TestCase;

import javax.management.AttributeList;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for APIStatistics, MBeans
 *
 * @author Nick Dellamaggiore (nick.dellamaggiore at gmail.com)
 */
public class MBeansTest extends TestCase {
    public MBeansTest(String name) {
        super(name);
    }

    /**
     * Tests statistics calculation for a single method
     */
    public void testInvocationStatisticsCalculator() throws Exception {
        InvocationStatisticsCalculator calc = new InvocationStatisticsCalculator("foo", 5);

        assertEquals("foo", calc.getName());
        checkCalculator(calc, 0, 0, 0, 0);

        calc.increment(100, true);
        checkCalculator(calc, 1, 0, 100, 100);

        calc.increment(100, true);
        checkCalculator(calc, 2, 0, 200, 100);

        calc.increment(400, false);
        checkCalculator(calc, 3, 1, 600, 200);

        // test rollover for average
        calc.increment(200, true);
        calc.increment(200, true);
        calc.increment(200, true);
        checkCalculator(calc, 6, 1, 1200, 220);

        for (int i = 0; i < 1000; i++) {
            calc.increment(i, true);
        }
        checkCalculator(calc, 1006, 1, 500700, 997);

        // test reset, sure it still works after resetting
        calc.reset();
        checkCalculator(calc, 0, 0, 0, 0);

        calc.increment(100, true);
        checkCalculator(calc, 1, 0, 100, 100);
    }

    /**
     * Tests statistics calculation/aggregation for an entire API
     */
    public void testAPIStatistics() throws Exception {
        APIStatistics stats = new APIStatistics(5);

        checkCalculator(stats, 0, 0, 0, 0);
        assertFalse(stats.getInvocationStatistics().iterator().hasNext());

        stats.methodCalled("foo", 100, true);
        checkCalculator(stats, 1, 0, 100, 100);
        checkMethodStats(stats, "foo", 1, 0, 100, 100);

        stats.methodCalled("bar", 100, true);
        checkCalculator(stats, 2, 0, 200, 100);
        checkMethodStats(stats, "foo", 1, 0, 100, 100);
        checkMethodStats(stats, "bar", 1, 0, 100, 100);

        stats.methodCalled("foo", 400, false);
        checkCalculator(stats, 3, 1, 600, 200);
        checkMethodStats(stats, "foo", 2, 1, 500, 250);
        checkMethodStats(stats, "bar", 1, 0, 100, 100);

        // test rollover for average
        stats.methodCalled("foo", 200, true);
        stats.methodCalled("bar", 200, true);
        stats.methodCalled("baz", 200, true);
        checkCalculator(stats, 6, 1, 1200, 220);
        checkMethodStats(stats, "foo", 3, 1, 700, 233);
        checkMethodStats(stats, "bar", 2, 0, 300, 150);
        checkMethodStats(stats, "baz", 1, 0, 200, 200);

        // test reset, sure it still works after resetting
        stats.reset();
        checkCalculator(stats, 0, 0, 0, 0);
        assertFalse(stats.getInvocationStatistics().iterator().hasNext());

        stats.methodCalled("foo", 100, true);
        checkCalculator(stats, 1, 0, 100, 100);
        checkMethodStats(stats, "foo", 1, 0, 100, 100);
    }

    /**
     * Tests exposure of API statistics via a dynamic MBean
     */
    public void testAPIStatisticsOpenMBean() throws Exception {
        APIStatistics stats = new APIStatistics(5);
        APIStatisticsOpenMBean openMBean = new APIStatisticsOpenMBean(stats);

        // sanity check to ensure metadata accurately describes dynamic attributes
        MBeanInfo info = openMBean.getMBeanInfo();
        assertEquals(5, info.getAttributes().length);
        assertEquals(1, info.getOperations().length);

        List<String> attrNames = new ArrayList<String>();
        for (MBeanAttributeInfo attr : info.getAttributes()) {
            assertNotNull(openMBean.getAttribute(attr.getName()));
            attrNames.add(attr.getName());
        }
        AttributeList attrList = openMBean.getAttributes(attrNames.toArray(new String[attrNames.size()]));
        assertNotNull(attrList);
        assertEquals(5, attrList.size());

        // check stats (empty case)
        Long callCount = (Long) openMBean.getAttribute("callCount");
        assertEquals(0, callCount.longValue());
        Long errorCount = (Long) openMBean.getAttribute("errorCount");
        assertEquals(0, callCount.longValue());
        Long totalTime = (Long) openMBean.getAttribute("totalTime");
        assertEquals(0, totalTime.longValue());
        Long averageTime = (Long) openMBean.getAttribute("averageTime");
        assertEquals(0, averageTime.longValue());

        // check table (empty case)
        TabularData table = (TabularData) openMBean.getAttribute("statisticsTable");
        assertTrue(table.isEmpty());

        stats.methodCalled("foo", 100, true);

        // check stats (populated case)
        callCount = (Long) openMBean.getAttribute("callCount");
        assertEquals(1, callCount.longValue());
        errorCount = (Long) openMBean.getAttribute("errorCount");
        assertEquals(0, errorCount.longValue());
        totalTime = (Long) openMBean.getAttribute("totalTime");
        assertEquals(100, totalTime.longValue());
        averageTime = (Long) openMBean.getAttribute("averageTime");
        assertEquals(100, averageTime.longValue());

        // check table (populated  case)
        table = (TabularData) openMBean.getAttribute("statisticsTable");
        assertFalse(table.isEmpty());
        assertEquals(1, table.keySet().size());

        CompositeData data = table.get(new Object[]{"foo"});
        assertNotNull(data);
        String[] columnNames = new String[]{"methodName", "callCount", "totalTime", "avgTime"};
        Object[] columnValues = data.getAll(columnNames);
        assertEquals(columnNames.length, columnValues.length);
        assertEquals("foo", columnValues[0]);
        assertEquals(1, ((Long) columnValues[1]).longValue());
        assertEquals(100, ((Long) columnValues[2]).longValue());
        assertEquals(100, ((Long) columnValues[3]).longValue());

        // check reset
        openMBean.invoke("reset", new Object[0], new String[0]);
        checkCalculator(stats, 0, 0, 0, 0);
        assertFalse(stats.getInvocationStatistics().iterator().hasNext());
    }

    // *****************
    // Helper methods
    // *****************

    private void checkMethodStats(APIStatistics apiStats,
                                  String methodName,
                                  long callCount,
                                  long errorCount,
                                  long totalTime,
                                  long avgTime) {
        InvocationStatistics methodStats = null;

        for (InvocationStatistics s : apiStats.getInvocationStatistics()) {
            if (s.getName().equals(methodName)) {
                methodStats = s;
                break;
            }
        }

        if (methodStats != null) {
            checkCalculator(methodStats, callCount, errorCount, totalTime, avgTime);
        } else {
            fail("No stats available for method with name '" + methodName + "'");
        }
    }

    private void checkCalculator(InvocationStatistics calc,
                                 long callCount,
                                 long errorCount,
                                 long totalTime,
                                 long avgTime) {
        assertEquals(callCount, calc.getCallCount());
        assertEquals(errorCount, calc.getErrorCount());
        assertEquals(totalTime, calc.getTotalTime());
        assertEquals(avgTime, calc.getAverageTime());
    }
}