package twitter4j;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JSONSchemaNoTypeTest {
    @Test
    void noType() {
        var extract = JSONSchema.extract("#/components/schemas/", """
                {
                  "components": {
                    "schemas": {
                      "ResourceNotFoundProblem": {
                        "description": "A problem that indicates that a given Tweet, User, etc. does not exist.",
                        "type": "object",
                        "allOf": [
                          {
                            "$ref": "#/components/schemas/ProblemFields"
                          }
                        ],
                        "required": [
                          "parameter",
                          "value",
                          "resource_id",
                          "resource_type"
                        ],
                        "properties": {
                          "type": {
                            "type": "string",
                            "enum": [
                              "https://api.twitter.com/labs/2/problems/resource-not-found"
                            ]
                          },
                          "parameter": {
                            "type": "string",
                            "minLength": 1
                          },
                          "value": {
                            "description": "Value will match the schema of the field."
                          },
                          "resource_id": {
                            "type": "string"
                          },
                          "resource_type": {
                            "type": "string",
                            "enum": [
                              "user",
                              "tweet",
                              "media"
                            ]
                          }
                        }
                      },
                      "ProblemFields": {
                        "type": "object",
                        "required": [
                          "type",
                          "title",
                          "detail"
                        ],
                        "properties": {
                          "type": {
                            "type": "string",
                            "format": "uri"
                          },
                          "title": {
                            "type": "string"
                          },
                          "detail": {
                            "type": "string"
                          }
                        }
                      }
                    }
                  }
                }
                """);

        JSONSchema resourceNotFoundProblem = extract.get("ResourceNotFoundProblem");
        assertEquals("#/components/schemas/ResourceNotFoundProblem", resourceNotFoundProblem.jsonPointer());


        JavaFile javaFile = resourceNotFoundProblem.asInterface("twitter4j.v2");
        assertEquals("ResourceNotFoundProblem.java", javaFile.fileName());
        String interfaceDeclaration = javaFile.content();
        assertEquals("""
                        package twitter4j.v2;
                                                
                        import org.jetbrains.annotations.NotNull;
                        import org.jetbrains.annotations.Nullable;
                                                
                        /**
                         * A problem that indicates that a given Tweet, User, etc. does not exist.
                         */
                        public interface ResourceNotFoundProblem {
                            /**
                             * @return type
                             */
                            @Nullable
                            String getType();
                                                
                            /**
                             * @return parameter
                             */
                            @NotNull
                            String getParameter();
                                                
                            /**
                             * @return Value will match the schema of the field.
                             */
                            @NotNull
                            String getValue();
                                                
                            /**
                             * @return resource_id
                             */
                            @NotNull
                            String getResourceId();
                                                
                            /**
                             * resource_type
                             */
                            enum ResourceType {
                                USER("user"),
                                TWEET("tweet"),
                                MEDIA("media");
                                public final String value;
                                                
                                ResourceType(String value) {
                                    this.value = value;
                                }
                                                
                                @Override
                                public String toString() {
                                    return value;
                                }
                                                
                                public static ResourceType of(String str) {
                                    for (ResourceType value : ResourceType.values()) {
                                        if (value.value.equals(str)) {
                                            return value;
                                        }
                                    }
                                    return null;
                                }
                            }
                                                
                            /**
                             * @return resource_type
                             */
                            @NotNull
                            ResourceType getResourceType();
                                                
                            /**
                             * @return\s
                             */
                            @NotNull
                            ProblemFields getProblemFields();
                        }
                        """,
                interfaceDeclaration);
        assertEquals("""
                package twitter4j;
                                
                import org.jetbrains.annotations.NotNull;
                import org.jetbrains.annotations.Nullable;
                                
                import twitter4j.v2.ProblemFields;
                                
                /**
                 * A problem that indicates that a given Tweet, User, etc. does not exist.
                 */
                class ResourceNotFoundProblemImpl implements twitter4j.v2.ResourceNotFoundProblem {
                    @Nullable
                    private final String type;
                                
                    @NotNull
                    private final String parameter;
                                
                    @NotNull
                    private final String value;
                                
                    @NotNull
                    private final String resourceId;
                                
                    @NotNull
                    private final ResourceType resourceType;
                                
                    @NotNull
                    private final ProblemFields problemFields;
                                
                    ResourceNotFoundProblemImpl(JSONObject json) {
                        this.type = json.getString("type");
                        this.parameter = json.getString("parameter");
                        this.value = json.getString("value");
                        this.resourceId = json.getString("resource_id");
                        this.resourceType = ResourceType.of(json.getString("resource_type"));
                        this.problemFields = json.has("ProblemFields") ? new ProblemFields(json.getJSONObject("ProblemFields")) : null;
                    }
                                
                    @Nullable
                    @Override
                    public String getType() {
                        return type;
                    }
                                
                    @NotNull
                    @Override
                    public String getParameter() {
                        return parameter;
                    }
                                
                    @NotNull
                    @Override
                    public String getValue() {
                        return value;
                    }
                                
                    @NotNull
                    @Override
                    public String getResourceId() {
                        return resourceId;
                    }
                                
                    @NotNull
                    @Override
                    public ResourceType getResourceType() {
                        return resourceType;
                    }
                                
                    @NotNull
                    @Override
                    public ProblemFields getProblemFields() {
                        return problemFields;
                    }
                }
                """, resourceNotFoundProblem.asJavaImpl("twitter4j", "twitter4j.v2"));
    }

}
