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
 * InvocationStatistics
 * @author Nick Dellamaggiore (nick.dellamaggiore at gmail.com)
 */
public interface InvocationStatistics {
    /**
     * return name
     * @return name
     */
    String getName();

    /**
     * return call count
     * @return call count
     */
    long getCallCount();

    /**
     * return error count
     * @return  error count
     */
    long getErrorCount();

    /**
     * returns total time
     * @return total time
     */
    long getTotalTime();

    /**
     * return average time
     * @return average time
     */
    long getAverageTime();

    /**
     * reset statistics
     */
    void reset();
}
