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
        assertEquals("""
                package twitter4j;
                                
                import org.jetbrains.annotations.Nullable;
                import twitter4j.v2.PromotedMetrics;
                                
                import javax.annotation.processing.Generated;
                                
                /**
                 * Video
                 */
                @Generated(value = "twitter4j.JSONSchema", date = "dateStr", comments = "#/components/schemas/Video")
                class VideoImpl implements twitter4j.v2.Video {
                    @Nullable
                    private final PromotedMetrics promotedMetrics;
                                
                    VideoImpl(JSONObject json) {
                        this.promotedMetrics = json.has("promoted_metrics") ? new PromotedMetricsImpl(json.getJSONObject("promoted_metrics")) : null;
                    }
                                
                    @Nullable
                    @Override
                    public PromotedMetrics getPromotedMetrics() {
                        return promotedMetrics;
                    }
                }
                """, extract.get("#/components/schemas/Video").asJavaImpl("twitter4j", "twitter4j.v2").content()
                .replaceAll("date = \"[0-9\\-:ZT]+\"", "date = \"dateStr\""));
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

    }

}
