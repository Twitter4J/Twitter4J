package twitter4j;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JSONSchemaOneOfTest {
    @Test
    void oneOf() {
        var extract = JSONSchema.extract("#/components/schemas/", """
                {
                  "components": {
                    "schemas": {
                      "Problem": {
                        "description": "An HTTP Problem Details object, as defined in IETF RFC 7807 (https://tools.ietf.org/html/rfc7807).",
                        "oneOf": [
                          {
                            "$ref": "#/components/schemas/GenericProblem"
                          },
                          {
                            "$ref": "#/components/schemas/InvalidRequestProblem"
                          }
                        ],
                        "discriminator": {
                          "propertyName": "type",
                          "mapping": {
                            "about:blank": "#/components/schemas/GenericProblem",
                            "https://api.twitter.com/labs/2/problems/invalid-request": "#/components/schemas/InvalidRequestProblem",
                            "https://api.twitter.com/labs/2/problems/client-forbidden": "#/components/schemas/ClientForbiddenProblem",
                            "https://api.twitter.com/labs/2/problems/resource-not-found": "#/components/schemas/ResourceNotFoundProblem",
                            "https://api.twitter.com/labs/2/problems/not-authorized-for-resource": "#/components/schemas/ResourceUnauthorizedProblem",
                            "https://api.twitter.com/labs/2/problems/disallowed-resource": "#/components/schemas/DisallowedResourceProblem",
                            "https://api.twitter.com/labs/2/problems/unsupported-authentication": "#/components/schemas/UnsupportedAuthenticationProblem",
                            "https://api.twitter.com/labs/2/problems/usage-capped": "#/components/schemas/UsageCapExceededProblem"
                          }
                        }
                      },
                      "GenericProblem": {
                        "description": "A generic problem with no additional information beyond that provided by the HTTP status code.",
                        "allOf": [
                          {
                            "$ref": "#/components/schemas/ProblemFields"
                          }
                        ],
                        "required": [
                          "status"
                        ],
                        "properties": {
                          "type": {
                            "type": "string",
                            "enum": [
                              "about:blank"
                            ]
                          },
                          "status": {
                            "type": "integer"
                          }
                        }
                      },
                      "InvalidRequestProblem": {
                        "description": "A problem that indicates this request is invalid.",
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
                      }
                    }
                  }
                }
                """);

        JSONSchema problem = extract.get("Problem");
        assertEquals("#/components/schemas/Problem", problem.jsonPointer());


        JavaFile javaFile = problem.asInterface("twitter4j.v2");
        assertEquals("Problem.java", javaFile.fileName());
        String interfaceDeclaration = javaFile.content();
        assertEquals("""
                        package twitter4j.v2;
                                                
                        import org.jetbrains.annotations.Nullable;
                                                
                        /**
                         * An HTTP Problem Details object, as defined in IETF RFC 7807 (https://tools.ietf.org/html/rfc7807).
                         */
                        public interface Problem {
                            /**
                             * @return A generic problem with no additional information beyond that provided by the HTTP status code.
                             */
                            @Nullable
                            GenericProblem getGenericProblem();
                                                
                            /**
                             * @return A problem that indicates this request is invalid.
                             */
                            @Nullable
                            InvalidRequestProblem getInvalidRequestProblem();
                        }
                        """,
                interfaceDeclaration);
    }

}
