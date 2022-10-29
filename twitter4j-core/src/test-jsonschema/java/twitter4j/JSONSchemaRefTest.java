package twitter4j;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JSONSchemaRefTest {
    @Test
    void ref() {
        var extract = JSONSchema.extract("#/", """
                {
                "URL" : {
                        "type" : "string",
                        "description" : "A validly formatted URL.",
                        "format" : "uri",
                        "example" : "https://example.com"
                      },
                      "ProblemFields" : {
                        "type" : "object",
                        "required" : [ "type", "title"],
                        "properties" : {
                          "type" : {
                            "type" : "string",
                            "format" : "uri"
                          },
                          "title" : {
                            "$ref" : "#/URL"
                          }
                        }
                      }
                }""");

        assertEquals(4, extract.size());
        JSONSchema problemFields = extract.get("ProblemFields");
        assertEquals("""
                        @NotNull
                        private final String type;

                        @NotNull
                        private final String title;
                        """,
                problemFields.asFieldDeclarations());
        assertEquals("""
                        this.type = json.getString("type");
                        this.title = json.getString("title");""",
                problemFields.asConstructorAssignments());
        assertEquals("""
                        @NotNull
                        @Override
                        public String getType() {
                            return type;
                        }

                        @NotNull
                        @Override
                        public String getTitle() {
                            return title;
                        }
                        """,
                problemFields.asGetterImplementations());

        String javaImpl = problemFields.asJavaImpl("twitter4j", "twitter4j.v2");
        assertEquals("""
                        package twitter4j;
                                                
                        import org.jetbrains.annotations.NotNull;
                                                
                        /**
                         * ProblemFields
                         */
                        class ProblemFieldsImpl implements twitter4j.v2.ProblemFields {
                            @NotNull
                            private final String type;
                                                
                            @NotNull
                            private final String title;
                                                
                            ProblemFieldsImpl(JSONObject json) {
                                this.type = json.getString("type");
                                this.title = json.getString("title");
                            }
                                                
                            @NotNull
                            @Override
                            public String getType() {
                                return type;
                            }
                                                
                            @NotNull
                            @Override
                            public String getTitle() {
                                return title;
                            }
                        }
                        """,
                javaImpl);

        String interfaceDeclaration = problemFields.asInterface("twitter4j.v2");
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
                             * @return A validly formatted URL.
                             */
                            @NotNull
                            String getTitle();
                        }
                        """,
                interfaceDeclaration);
    }
}
