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

package examples.json;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;
import twitter4j.v1.Status;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Example application that gets public timeline and store raw JSON strings into statuses/ directory..<br>
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class SaveRawJSON {
    /**
     * Usage: java twitter4j.examples.json.SaveRawJSON
     *
     * @param args String[]
     */
    public static void main(String[] args) {
        var timelines = Twitter.getInstance().v1().timelines();
        System.out.println("Saving public timeline.");
        try {
            //noinspection ResultOfMethodCallIgnored
            new File("statuses").mkdir();
            List<Status> statuses = timelines.getHomeTimeline();
            for (Status status : statuses) {
                String rawJSON = TwitterObjectFactory.getRawJSON(status);
                String fileName = "statuses/" + status.getId() + ".json";
                storeJSON(rawJSON, fileName);
                System.out.println(fileName + " - " + status.getText());
            }
            System.out.print("\ndone.");
            System.exit(0);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Failed to store tweets: " + ioe.getMessage());
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        }
    }

    private static void storeJSON(String rawJSON, String fileName) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(osw)) {
            bw.write(rawJSON);
            bw.flush();
        }
    }
}
