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

        JSONSchema resourceNotFoundProblem = extract.get("#/components/schemas/ResourceNotFoundProblem");
        assertEquals("#/components/schemas/ResourceNotFoundProblem", resourceNotFoundProblem.jsonPointer());


        JavaFile javaFile = resourceNotFoundProblem.asInterface("twitter4j.v2", false);
        assertEquals("ResourceNotFoundProblem.java", javaFile.fileName());
        assertEquals("""
                        package twitter4j.v2;
                                                
                        import org.jetbrains.annotations.NotNull;
                        import org.jetbrains.annotations.Nullable;
                                                
                        import javax.annotation.processing.Generated;
                                                
                        /**
                         * A problem that indicates that a given Tweet, User, etc. does not exist.
                         */
                        @Generated(value = "twitter4j.JSONSchema", date = "dateStr", comments = "#/components/schemas/ResourceNotFoundProblem")
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
                                /**
                                 * user
                                 */
                                USER("user"),
                                /**
                                 * tweet
                                 */
                                TWEET("tweet"),
                                /**
                                 * media
                                 */
                                MEDIA("media");
                                /**
                                 * value
                                 */
                                public final String value;
                                                
                                ResourceType(String value) {
                                    this.value = value;
                                }
                                                
                                @Override
                                public String toString() {
                                    return value;
                                }
                                /**
                                 * Returns the enum constant of the specified enum class with the specified name.
                                 * @param name the name of the constant to return
                                 * @return the enum constant of the specified enum class with the specified name,
                                 * or null if the enum constant is not found.\s
                                 */
                                public static ResourceType of(String name) {
                                    for (ResourceType value : ResourceType.values()) {
                                        if (value.value.equals(name)) {
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
                             * @return ProblemFields
                             */
                            @NotNull
                            ProblemFields getProblemFields();
                        }
                        """,
                javaFile.content().replaceAll("date = \"[0-9\\-:ZT]+\"", "date = \"dateStr\""));
        assertEquals("""
                package twitter4j;
                                
                import org.jetbrains.annotations.NotNull;
                import org.jetbrains.annotations.Nullable;
                import twitter4j.v2.ProblemFields;
                                
                import javax.annotation.processing.Generated;
                                
                /**
                 * A problem that indicates that a given Tweet, User, etc. does not exist.
                 */
                @Generated(value = "twitter4j.JSONSchema", date = "dateStr", comments = "#/components/schemas/ResourceNotFoundProblem")
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
                                
                    @SuppressWarnings("ConstantConditions")
                    ResourceNotFoundProblemImpl(JSONObject json) {
                        this.type = json.getString("type");
                        this.parameter = json.getString("parameter");
                        this.value = json.getString("value");
                        this.resourceId = json.getString("resource_id");
                        this.resourceType = ResourceType.of(json.getString("resource_type"));
                        this.problemFields = json.has("ProblemFields") ? new ProblemFieldsImpl(json.getJSONObject("ProblemFields")) : null;
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
                """, resourceNotFoundProblem.asJavaImpl("twitter4j", "twitter4j.v2", false).content()
                .replaceAll("date = \"[0-9\\-:ZT]+\"", "date = \"dateStr\""));
    }

}
