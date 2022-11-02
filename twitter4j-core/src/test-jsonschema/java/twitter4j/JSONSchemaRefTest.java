package twitter4j;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JSONSchemaRefTest {
    @Test
    void ref() {
        var extract = JSONSchema.extract("#/components/schemas/", """
                {
                  "components": {
                    "schemas": {
                      "HostPort": {
                        "type": "object",
                        "description": "A validly formatted URL.",
                        "example": "https://example.com",
                        "properties": {
                          "host": {
                            "type": "string"
                          },
                          "port": {
                            "type": "integer"
                          }
                        }
                      },
                      "ProblemFields": {
                        "type": "object",
                        "required": [
                          "type",
                          "theHost"
                        ],
                        "properties": {
                          "type": {
                            "type": "string",
                            "format": "uri"
                          },
                          "theHost": {
                            "$ref": "#/components/schemas/HostPort"
                          }
                        }
                      },
                      "Problem": {
                        "description": "An HTTP Problem Details object, as defined in IETF RFC 7807 (https://tools.ietf.org/html/rfc7807).",
                        "oneOf": [
                          {
                            "$ref": "#/components/schemas/GenericProblem"
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
                      }
                    }
                  }
                }""");

        assertEquals("#/components/schemas/HostPort", extract.get("#/components/schemas/HostPort").jsonPointer());
        for (JSONSchema value : extract.values()) {
            System.out.println(value.jsonPointer() + ":" + value.jsonPointer().getClass().getName() + ":" + value.typeName() + ":" + value.getJavaType(false, "twitter4j"));
        }

        JSONSchema problemFields = extract.get("#/components/schemas/ProblemFields");
        assertEquals("#/components/schemas/ProblemFields", problemFields.jsonPointer());
        assertEquals("""
                        @NotNull
                        private final String type;

                        @NotNull
                        private final HostPort theHost;""",
                problemFields.asFieldDeclarations("twitter4j.v2", null).codeFragment());
        assertEquals("""
                        this.type = json.getString("type");
                        this.theHost = json.has("theHost") ? new HostPortImpl(json.getJSONObject("theHost")) : null;""",
                problemFields.asConstructorAssignments("twitter4j.v2").codeFragment());
        assertEquals("""
                        @NotNull
                        @Override
                        public String getType() {
                            return type;
                        }

                        @NotNull
                        @Override
                        public HostPort getTheHost() {
                            return theHost;
                        }
                        """,
                problemFields.asGetterImplementations("twitter4j.v2",null).codeFragment());
        assertEquals("""
                        /**
                         * @return type
                         */
                        @NotNull
                        String getType();
                        
                        /**
                         * @return theHost: A validly formatted URL.
                         */
                        @NotNull
                        HostPort getTheHost();
                        """,
                problemFields.asGetterDeclarations("twitter4j.v2",null).codeFragment());

        assertEquals("""
                        package twitter4j;
                                                
                        import org.jetbrains.annotations.NotNull;
                        import twitter4j.v2.HostPort;
                                                
                        import javax.annotation.processing.Generated;
                                                
                        /**
                         * ProblemFields
                         */
                        @Generated(value = "twitter4j.JSONSchema", date = "dateStr", comments = "#/components/schemas/ProblemFields")
                        class ProblemFieldsImpl implements twitter4j.v2.ProblemFields {
                            @NotNull
                            private final String type;
                                                
                            @NotNull
                            private final HostPort theHost;
                                                
                            @SuppressWarnings("ConstantConditions")
                            ProblemFieldsImpl(JSONObject json) {
                                this.type = json.getString("type");
                                this.theHost = json.has("theHost") ? new HostPortImpl(json.getJSONObject("theHost")) : null;
                            }
                                                
                            @NotNull
                            @Override
                            public String getType() {
                                return type;
                            }
                                                
                            @NotNull
                            @Override
                            public HostPort getTheHost() {
                                return theHost;
                            }
                        }
                        """,
                problemFields.asJavaImpl("twitter4j", "twitter4j.v2").content()
                        .replaceAll("date = \"[0-9\\-:ZT]+\"", "date = \"dateStr\""));

        JavaFile javaFile = problemFields.asInterface("twitter4j.v2");
        assertEquals("ProblemFields.java", javaFile.fileName());
        String interfaceDeclaration = javaFile.content();
        assertEquals("""
                        package twitter4j.v2;
                                                
                        import org.jetbrains.annotations.NotNull;
                                                
                        /**
                         * ProblemFields
                         */
                        public interface ProblemFields {
                            /**
                             * @return type
                             */
                            @NotNull
                            String getType();
                            
                            /**
                             * @return theHost: A validly formatted URL.
                             */
                            @NotNull
                            HostPort getTheHost();
                        }
                        """,
                interfaceDeclaration);

        JavaFile problem = extract.get("#/components/schemas/Problem").asJavaImpl("twitter4j", "twitter4j.v2");
        assertEquals("""
                package twitter4j;
                                
                import org.jetbrains.annotations.Nullable;
                import twitter4j.v2.GenericProblem;
                                
                import javax.annotation.processing.Generated;
                                
                /**
                 * An HTTP Problem Details object, as defined in IETF RFC 7807 (<a href="https://tools.ietf.org/html/rfc7807">https://tools.ietf.org/html/rfc7807</a>).
                 */
                @Generated(value = "twitter4j.JSONSchema", date = "dateStr", comments = "#/components/schemas/Problem")
                class ProblemImpl implements twitter4j.v2.Problem {
                    @Nullable
                    private final GenericProblem genericProblem;
                                
                    ProblemImpl(JSONObject json) {
                        this.genericProblem = json.has("GenericProblem") ? new GenericProblemImpl(json.getJSONObject("GenericProblem")) : null;
                    }
                                
                    @Nullable
                    @Override
                    public GenericProblem getGenericProblem() {
                        return genericProblem;
                    }
                }
                """, problem.content()
                .replaceAll("date = \"[0-9\\-:ZT]+\"", "date = \"dateStr\""));
        assertEquals("ProblemImpl.java", problem.fileName());
    }
}
