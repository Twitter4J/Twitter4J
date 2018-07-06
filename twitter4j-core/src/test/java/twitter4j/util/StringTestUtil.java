package twitter4j.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StringTestUtil {

    private StringTestUtil() {
        // Prevents instantiation
    }

    public static String fromInputStream(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder response = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return response.toString();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception t) {
                    // Really nothing we can do
                }
            }
        }
    }
}
