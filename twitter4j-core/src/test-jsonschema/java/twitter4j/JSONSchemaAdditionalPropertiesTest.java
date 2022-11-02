package twitter4j;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JSONSchemaAdditionalPropertiesTest {
    @Test
    void additionalProperties() {
        var extract = JSONSchema.extract("#/components/schemas/", """
                {
                  "components": {
                    "schemas": {
                      "InvalidRequestProblem": {
                        "description": "A problem that indicates this request is invalid. http://twitter4j.org/",
                        "type": "object",
                        "allOf": [
                          {
                            "$ref": "#/components/schemas/ProblemFields"
                          }
                        ],
                        "properties": {
                          "type": {
                            "type": "string",
                            "enum": [
                              "https://api.twitter.com/labs/2/problems/invalid-request"
                            ]
                          },
                          "errors": {
                            "type": "array",
                            "items": {
                              "type": "object",
                              "properties": {
                                "parameters": {
                                  "type": "object",
                                  "additionalProperties": {
                                    "type": "array",
                                    "items": {
                                      "type": "string"
                                    }
                                  }
                                },
                                "message": {
                                  "type": "string"
                                }
                              }
                            }
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

        JSONSchema invalidRequestProblem = extract.get("#/components/schemas/InvalidRequestProblem");
        assertEquals("#/components/schemas/InvalidRequestProblem", invalidRequestProblem.jsonPointer());
        JSONSchema errors = extract.get("#/components/schemas/InvalidRequestProblem/properties/errors");
        assertEquals("""
                /**
                 * @return errors
                 */
                List<Error> getErrors();
                """, errors.asGetterDeclaration(true, "twitter4j.v2", null).codeFragment());
        assertEquals("""
                package twitter4j.v2;
                                
                import org.jetbrains.annotations.Nullable;
                                
                /**
                 * Error
                 */
                public interface Error {
                    /**
                     * @return parameters
                     */
                    @Nullable
                    String getParameters();
                                
                    /**
                     * @return message
                     */
                    @Nullable
                    String getMessage();
                }
                """, extract.get("#/components/schemas/InvalidRequestProblem/properties/errors/items").asInterface("twitter4j.v2").content());

        JSONSchema problemFields = extract.get("#/components/schemas/ProblemFields");
        assertEquals("""
                /**
                 * @return ProblemFields
                 */
                @NotNull
                ProblemFields getProblemFields();
                """, problemFields.asGetterDeclaration(true, "twitter4j.v2", null).codeFragment());

        JavaFile javaFile = invalidRequestProblem.asInterface("twitter4j.v2");
        assertEquals("InvalidRequestProblem.java", javaFile.fileName());
        String interfaceDeclaration = javaFile.content();
        assertEquals("""
                        package twitter4j.v2;
                                                
                        import org.jetbrains.annotations.NotNull;
                        import org.jetbrains.annotations.Nullable;
                                                
                        import java.util.List;
                                                
                        /**
                         * A problem that indicates this request is invalid. <a href="http://twitter4j.org/">http://twitter4j.org/</a>
                         */
                        public interface InvalidRequestProblem {
                            /**
                             * @return type
                             */
                            @Nullable
                            String getType();
                                                
                            /**
                             * @return errors
                             */
                            List<Error> getErrors();
                                                
                            /**
                             * @return ProblemFields
                             */
                            @NotNull
                            ProblemFields getProblemFields();
                        }
                        """,
                interfaceDeclaration);
    }
}
