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

//        assertEquals(6, extract.size());
        JSONSchema attachments = extract.get("#/components/schemas/attachments");
        assertEquals("#/components/schemas/attachments", attachments.jsonPointer());
//        for (JSONSchema value : extract.values()) {
//            System.out.println(value.jsonPointer()+":"+value.jsonPointer().getClass().getName()+":"+value.typeName()+":"+value.getJavaType(false, "twitter4j"));
//        }


        JavaFile javaFile = attachments.asInterface("twitter4j.v2");
        assertEquals("Attachments.java", javaFile.fileName());
        String interfaceDeclaration = javaFile.content();
        assertEquals("""
                        package twitter4j.v2;
                                                
                        import java.util.List;
                                                
                        /**
                         * Specifies the type of attachments (if any) present in this Tweet.
                         */
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
                interfaceDeclaration);
    }
}
