package twitter4j;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JSONSchemaObjectPropertyTest {
    @Test
    void ref() {
        var extract = JSONSchema.extract("#/components/schemas/", """
                {
                  "components": {
                    "schemas": {
                      "attachments": {
                        "type": "object",
                        "description": "Specifies the type of attachments (if any) present in this Tweet.",
                        "properties": {
                          "media_keys": {
                            "type": "array",
                            "description": "A list of Media Keys for each one of the media attachments (if media are attached).",
                            "items": {
                              "$ref": "#/components/schemas/MediaKey"
                            },
                            "minItems": 1
                          },
                          "poll_ids": {
                            "type": "array",
                            "description": "A list of poll IDs (if polls are attached).",
                            "items": {
                              "$ref": "#/components/schemas/PollId"
                            },
                            "minItems": 1
                          }
                        }
                      },
                      "MediaKey": {
                        "type": "string",
                        "description": "The Media Key identifier for this attachment.",
                        "pattern": "^([0-9]+)_([0-9]+)$"
                      },
                      "PollId": {
                        "type": "string",
                        "description": "Unique identifier of this poll.",
                        "pattern": "^[0-9]{1,19}$"
                      }
                    }
                  }
                }""");

        JSONSchema attachments = extract.get("#/components/schemas/attachments");
        assertEquals("#/components/schemas/attachments", attachments.jsonPointer());


        JavaFile javaFile = attachments.asInterface("twitter4j.v2", false);
        assertEquals("Attachments.java", javaFile.fileName());
        assertEquals("""
                        package twitter4j.v2;
                                                
                        import javax.annotation.processing.Generated;
                        import java.util.List;
                                                
                        /**
                         * Specifies the type of attachments (if any) present in this Tweet.
                         */
                        @Generated(value = "twitter4j.JSONSchema", date = "dateStr", comments = "#/components/schemas/attachments")
                        public interface Attachments {
                            /**
                             * @return A list of Media Keys for each one of the media attachments (if media are attached).
                             */
                            List<String> getMediaKeys();
                                                
                            /**
                             * @return A list of poll IDs (if polls are attached).
                             */
                            List<String> getPollIds();
                        }
                        """,
                javaFile.content().replaceAll("date = \"[0-9\\-:ZT]+\"", "date = \"dateStr\""));
    }
}
