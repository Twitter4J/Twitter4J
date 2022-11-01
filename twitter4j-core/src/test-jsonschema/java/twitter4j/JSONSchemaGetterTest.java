package twitter4j;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JSONSchemaGetterTest {
    @Test
    void getter() {
        var extract = JSONSchema.extract("#/components/schemas/", """
                {
                  "components": {
                    "schemas": {
                      "Video": {
                        "type": "object",
                        "properties": {
                          "promoted_metrics": {
                            "type": "object",
                            "description": "Promoted nonpublic engagement metrics for the Media at the time of the request.",
                            "properties": {
                              "playback_0_count": {
                                "type": "integer",
                                "format": "int32",
                                "description": "Number of users who made it through 0% of the video."
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }""");
        assertEquals("""
                /**
                 * @return Promoted nonpublic engagement metrics for the Media at the time of the request.
                 */
                @Nullable
                PromotedMetrics getPromotedMetrics();
                """, extract.get("#/components/schemas/Video/properties/promoted_metrics").asGetterDeclaration(false, "twitter4j", null).codeFragment());
    }

    @Test
    void getter2() {
        var extract = JSONSchema.extract("#/components/schemas/", """
                {
                  "components": {
                    "schemas": {
                      "entities": {
                        "type": "object",
                        "description": "A list of metadata found in the user's profile description.",
                        "properties": {
                          "url": {
                            "type": "object",
                            "description": "Expanded details for the URL specified in the user's profile, with start and end indices.",
                            "properties": {
                              "urls": {
                                "type": "array",
                                "items": {
                                  "$ref": "#/components/schemas/UrlEntity"
                                },
                                "minItems": 1
                              }
                            }
                          }
                        }
                      },
                      "UrlEntity": {
                        "description": "Represent the portion of text recognized as a URL, and its start and end position within the text.",
                        "allOf": [
                          {
                            "$ref": "#/components/schemas/EntityIndices"
                          }
                        ]
                      },
                      "EntityIndices": {
                        "type": "object",
                        "description": "Represent a boundary range (start and end index) for a recognized entity (for example a hashtag or a mention). `start` must be smaller than `end`.",
                        "required": [
                          "start",
                          "end"
                        ],
                        "properties": {
                          "start": {
                            "type": "integer",
                            "minimum": 0,
                            "description": "Index (zero-based) at which position this entity starts."
                          },
                          "end": {
                            "type": "integer",
                            "minimum": 0,
                            "description": "Index (zero-based) at which position this entity ends."
                          }
                        }
                      }
                    }
                  }
                }""");
        assertEquals("""
                package twitter4j;
                                
                import org.jetbrains.annotations.Nullable;
                                
                /**
                 * A list of metadata found in the user's profile description.
                 */
                public interface Entities {
                    /**
                     * @return Expanded details for the URL specified in the user's profile, with start and end indices.
                     */
                    @Nullable
                    Url getUrl();
                }
                """, extract.get("#/components/schemas/entities").asInterface("twitter4j").content());
        System.out.println(extract.values());

        for (JSONSchema value : extract.values()) {
            if (value instanceof ObjectSchema) {
                JavaFile javaFile = value.asInterface("twitter4j.v2");
                System.out.println(javaFile.fileName());

//                String javaInterface = javaFile.content();

//                Files.write(Path.of("twitter4j-core", "src", "test-jsonschema", "java", "twitter4j", "v2", javaFile.fileName()), javaInterface.getBytes());
            }

        }
    }

}
