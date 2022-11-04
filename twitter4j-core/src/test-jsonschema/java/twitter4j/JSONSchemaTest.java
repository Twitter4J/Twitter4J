package twitter4j;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JSONSchemaTest {
    @Test
    void hoge() {
        assertEquals("    hoge\n    foo", JSONSchema.indent("""
                hoge
                foo
                """, 4));
        assertEquals("    hoge\n    foo", JSONSchema.indent("""
                hoge
                foo""", 4));
    }



    @Test
    void object() {
        var extract = JSONSchema.extract("#/", """
                {
                      "ProblemFields" : {
                        "type" : "object",
                        "required" : [ "type", "title"],
                        "properties" : {
                          "type" : {
                            "type" : "string",
                            "format" : "uri"
                          },
                          "title" : {
                            "type" : "string"
                          },
                          "detail" : {
                            "type" : "string",
                            "format" : "date-time"
                          },
                          "HTTPStatusCode" : {
                            "type" : "integer",
                            "minimum" : 100,
                            "maximum" : 599,
                            "description" : "HTTP Status Code."
                          }
                        }
                      }
                }""");
        assertEquals(5, extract.size());
        JSONSchema problemFields = extract.get("#/ProblemFields");
        assertEquals("#/ProblemFields", problemFields.jsonPointer());
        assertEquals("""
                        this.problemFields = json.has("ProblemFields") ? new ProblemFieldsImpl(json.getJSONObject("ProblemFields")) : null;""",
                problemFields.asConstructorAssignment(true, null).codeFragment());
        assertEquals("""
                        this.problemFields = json.has("ProblemFields") ? new ProblemFieldsImpl(json.getJSONObject("ProblemFields")) : null;""",
                problemFields.asConstructorAssignment(false, null).codeFragment());
        assertEquals("""
                        @NotNull
                        private final String type;

                        @NotNull
                        private final String title;

                        @Nullable
                        private final LocalDateTime detail;
                                                
                        @Nullable
                        @Range(from = 100, to = 599)
                        private final Integer hTTPStatusCode;""",
                problemFields.asFieldDeclarations("twitter4j.v2", null).codeFragment());
        assertEquals("""
                        this.type = json.getString("type");
                        this.title = json.getString("title");
                        this.detail = json.getLocalDateTime("detail");
                        this.hTTPStatusCode = json.getIntValue("HTTPStatusCode");""",
                problemFields.asConstructorAssignments("twitter4j.v2").codeFragment());
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

                        @Nullable
                        @Override
                        public LocalDateTime getDetail() {
                            return detail;
                        }
                                                
                        @Nullable
                        @Range(from = 100, to = 599)
                        @Override
                        public Integer getHTTPStatusCode() {
                            return hTTPStatusCode;
                        }
                        """,
                problemFields.asGetterImplementations("twitter4j.v2", null, false).codeFragment());

        assertEquals("""
                        package twitter4j;
                                                
                        import org.jetbrains.annotations.NotNull;
                        import org.jetbrains.annotations.Nullable;
                        import org.jetbrains.annotations.Range;
                                                
                        import javax.annotation.processing.Generated;
                        import java.time.LocalDateTime;
                                                
                        /**
                         * ProblemFields
                         */
                        @Generated(value = "twitter4j.JSONSchema", date = "dateStr", comments = "#/ProblemFields")
                        class ProblemFieldsImpl implements twitter4j.v2.ProblemFields {
                            @NotNull
                            private final String type;
                                                
                            @NotNull
                            private final String title;
                                                
                            @Nullable
                            private final LocalDateTime detail;
                                                
                            @Nullable
                            @Range(from = 100, to = 599)
                            private final Integer hTTPStatusCode;
                                                
                            @SuppressWarnings("ConstantConditions")
                            ProblemFieldsImpl(JSONObject json) {
                                this.type = json.getString("type");
                                this.title = json.getString("title");
                                this.detail = json.getLocalDateTime("detail");
                                this.hTTPStatusCode = json.getIntValue("HTTPStatusCode");
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
                                                
                            @Nullable
                            @Override
                            public LocalDateTime getDetail() {
                                return detail;
                            }
                                                
                            @Nullable
                            @Range(from = 100, to = 599)
                            @Override
                            public Integer getHTTPStatusCode() {
                                return hTTPStatusCode;
                            }
                        }
                        """,
                problemFields.asJavaImpl("twitter4j", "twitter4j.v2", false).content()
                        .replaceAll("date = \"[0-9\\-:ZT]+\"", "date = \"dateStr\""));

        JavaFile javaFile = problemFields.asInterface("twitter4j.v2", false);
        assertEquals("ProblemFields.java", javaFile.fileName());
        assertEquals("""
                        package twitter4j.v2;
                                                
                        import org.jetbrains.annotations.NotNull;
                        import org.jetbrains.annotations.Nullable;
                        import org.jetbrains.annotations.Range;
                                                
                        import javax.annotation.processing.Generated;
                        import java.time.LocalDateTime;
                                                
                        /**
                         * ProblemFields
                         */
                        @Generated(value = "twitter4j.JSONSchema", date = "dateStr", comments = "#/ProblemFields")
                        public interface ProblemFields {
                            /**
                             * @return type
                             */
                            @NotNull
                            String getType();
                            
                            /**
                             * @return title
                             */
                            @NotNull
                            String getTitle();
                            
                            /**
                             * @return detail
                             */
                            @Nullable
                            LocalDateTime getDetail();
                                                
                            /**
                             * @return HTTP Status Code.
                             */
                            @Nullable
                            @Range(from = 100, to = 599)
                            Integer getHTTPStatusCode();
                        }
                        """,
                javaFile.content().replaceAll("date = \"[0-9\\-:ZT]+\"", "date = \"dateStr\""));


    }
}