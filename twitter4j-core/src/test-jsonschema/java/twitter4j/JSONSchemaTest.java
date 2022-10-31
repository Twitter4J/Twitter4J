package twitter4j;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void integer() {
        var extract = JSONSchema.extract("#/", """
                {
                        "MediaHeight" : {
                         "type" : "integer",
                         "minimum" : 0,
                         "description" : "The height of the media in pixels"
                       }
                 
                }""");
        assertEquals(1, extract.size());
        JSONSchema mediaHeight = extract.get("MediaHeight");
        assertEquals("""
                        @Nullable
                        @Range(from = 0, to = Long.MAX_VALUE)
                        private final Long mediaHeight;""",
                mediaHeight.asFieldDeclaration(false, "twitter4j.v2", null).codeFragment());
        assertEquals("""
                        this.mediaHeight = json.getLongValue("MediaHeight");""",
                mediaHeight.asConstructorAssignment(false, null));
        assertEquals("this.mediaHeight = json.getLong(\"MediaHeight\");",
                mediaHeight.asConstructorAssignment(true, null));
        assertEquals("""
                        @Nullable
                        @Range(from = 0, to = Long.MAX_VALUE)
                        @Override
                        public Long getMediaHeight() {
                            return mediaHeight;
                        }
                        """,
                mediaHeight.asGetterImplementation(false, "twitter4j.v2", null).codeFragment());

        assertThrows(UnsupportedOperationException.class, () -> mediaHeight.asJavaImpl("twitter4j", "twitter4j.v2"));
        assertThrows(UnsupportedOperationException.class, () -> mediaHeight.asInterface("twitter4j.v2"));
    }

    @Test
    void number() {
        var extract = JSONSchema.extract("#/", """
                {
                    "items" : {
                           "type" : "number",
                           "format" : "double",
                           "minimum" : -180,
                           "maximum" : 180
                    }
                }""");
        assertEquals(1, extract.size());
        JSONSchema items = extract.get("items");
        assertEquals("""
                        @Nullable
                        @Range(from = -180, to = 180)
                        private final Double items;""",
                items.asFieldDeclaration(false, "twitter4j.v2", null).codeFragment());
        assertEquals("""
                        this.items = json.getDoubleValue("items");""",
                items.asConstructorAssignment(false, null));
        assertEquals("this.items = json.getDouble(\"items\");",
                items.asConstructorAssignment(true, null));
        assertEquals("""
                        @Nullable
                        @Range(from = -180, to = 180)
                        @Override
                        public Double getItems() {
                            return items;
                        }
                        """,
                items.asGetterImplementation(false, "twitter4j.v2", null).codeFragment());

        assertThrows(UnsupportedOperationException.class, () -> items.asJavaImpl("twitter4j", "twitter4j.v2"));
        assertThrows(UnsupportedOperationException.class, () -> items.asInterface("twitter4j.v2"));
    }

    @Test
    void integerMinMax() {

        var extract = JSONSchema.extract("#/", """
                {       "HTTPStatusCode" : {
                          "type" : "integer",
                          "minimum" : 100,
                          "maximum" : 599,
                          "description" : "HTTP Status Code."
                        }
                  
                }""");
        assertEquals(1, extract.size());
        JSONSchema hTTPStatusCode = extract.get("HTTPStatusCode");
        assertEquals("@Nullable\n@Range(from = 100, to = 599)\nprivate final Integer hTTPStatusCode;",
                hTTPStatusCode.asFieldDeclaration(false, "twitter4j.v2", null).codeFragment());
        assertEquals("""
                        this.hTTPStatusCode = json.getIntValue("HTTPStatusCode");""",
                hTTPStatusCode.asConstructorAssignment(false, null));
        assertEquals("this.hTTPStatusCode = json.getInt(\"HTTPStatusCode\");",
                hTTPStatusCode.asConstructorAssignment(true, null));
        assertEquals("""
                        @Nullable
                        @Range(from = 100, to = 599)
                        @Override
                        public Integer getHTTPStatusCode() {
                            return hTTPStatusCode;
                        }
                        """,
                hTTPStatusCode.asGetterImplementation(false, "twitter4j.v2", null).codeFragment());

        assertThrows(UnsupportedOperationException.class, () -> hTTPStatusCode.asJavaImpl("twitter4j", "twitter4j.v2"));
        assertThrows(UnsupportedOperationException.class, () -> hTTPStatusCode.asInterface("twitter4j.v2"));
    }

    @Test
    void int32() {

        var extract = JSONSchema.extract("#/", """
                {               "code" : {
                                     "type" : "integer",
                                             "format" : "int32"
                                 },
                                                           "HTTPStatusCode" : {
                            "type" : "integer",
                            "minimum" : 100,
                            "maximum" : 599,
                            "description" : "HTTP Status Code."
                          }

                  
                }""");
        assertEquals(2, extract.size());
        JSONSchema code = extract.get("code");
        assertEquals("@Nullable\nprivate final Integer code;",
                code.asFieldDeclaration(false, "twitter4j.v2", null).codeFragment());
        assertEquals("private final int code;",
                code.asFieldDeclaration(true, "twitter4j.v2", null).codeFragment());
        assertEquals("""
                        this.code = json.getIntValue("code");""",
                code.asConstructorAssignment(false, null));
        assertEquals("this.code = json.getInt(\"code\");",
                code.asConstructorAssignment(true, null));
        assertEquals("""
                        @Nullable
                        @Override
                        public Integer getCode() {
                            return code;
                        }
                        """,
                code.asGetterImplementation(false, "twitter4j.v2",null).codeFragment());
        assertEquals("""
                        @Override
                        public int getCode() {
                            return code;
                        }
                        """,
                code.asGetterImplementation(true, "twitter4j.v2",null).codeFragment());


        JSONSchema hTTPStatusCode = extract.get("HTTPStatusCode");
        assertEquals("""
                /**
                 * @return HTTP Status Code.
                 */
                @Nullable
                @Range(from = 100, to = 599)
                Integer getHTTPStatusCode();
                """, hTTPStatusCode.asGetterDeclaration(false, "twitter4j.v2",null).codeFragment());
        assertEquals("""
                /**
                 * @return HTTP Status Code.
                 */
                @Range(from = 100, to = 599)
                int getHTTPStatusCode();
                """, hTTPStatusCode.asGetterDeclaration(true, "twitter4j.v2",null).codeFragment());

    }

    @Test
    void bool() {

        var extract = JSONSchema.extract("#/", """
                  {
                    "possibly_sensitive" : {
                      "type" : "boolean",
                      "description" : "Indicates if this Tweet contains URLs marked as sensitive, for example content suitable for mature audiences."
                    }
                }""");
        assertEquals(1, extract.size());
        JSONSchema possiblySensitive = extract.get("possibly_sensitive");
        assertEquals("@Nullable\nprivate final Boolean possiblySensitive;",
                possiblySensitive.asFieldDeclaration(false, "twitter4j.v2", null).codeFragment());
        assertEquals("this.possiblySensitive = json.getBooleanValue(\"possibly_sensitive\");",
                possiblySensitive.asConstructorAssignment(false, null));
        assertEquals("this.possiblySensitive = json.getBoolean(\"possibly_sensitive\");",
                possiblySensitive.asConstructorAssignment(true, null));
        assertEquals("""
                        @Nullable
                        @Override
                        public Boolean getPossiblySensitive() {
                            return possiblySensitive;
                        }
                        """,
                possiblySensitive.asGetterImplementation(false, "twitter4j.v2",null).codeFragment());

        assertThrows(UnsupportedOperationException.class, () -> possiblySensitive.asJavaImpl("twitter4j", "twitter4j.v2"));
        assertThrows(UnsupportedOperationException.class, () -> possiblySensitive.asInterface("twitter4j.v2"));
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
        JSONSchema problemFields = extract.get("ProblemFields");
        assertEquals("#/ProblemFields", problemFields.jsonPointer());
        assertEquals("""
                this.problemFields = json.has("ProblemFields") ? new ProblemFields(json.getJSONObject("ProblemFields")) : null;""", problemFields.asConstructorAssignment(true, null));
        assertEquals("""
                this.problemFields = json.has("ProblemFields") ? new ProblemFields(json.getJSONObject("ProblemFields")) : null;""", problemFields.asConstructorAssignment(false, null));
        assertEquals("""
                        @NotNull
                        private final String type;

                        @NotNull
                        private final String title;

                        @Nullable
                        private final LocalDateTime detail;
                                                
                        @Nullable
                        @Range(from = 100, to = 599)
                        private final Integer hTTPStatusCode;
                        """,
                problemFields.asFieldDeclarations("twitter4j.v2", null).codeFragment());
        assertEquals("""
                        this.type = json.getString("type");
                        this.title = json.getString("title");
                        this.detail = json.getLocalDateTime("detail");
                        this.hTTPStatusCode = json.getIntValue("HTTPStatusCode");""",
                problemFields.asConstructorAssignments("twitter4j.v2"));
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
                problemFields.asGetterImplementations("twitter4j.v2", null).codeFragment());

        String javaImpl = problemFields.asJavaImpl("twitter4j", "twitter4j.v2");
        assertEquals("""
                        package twitter4j;
                                                
                        import org.jetbrains.annotations.NotNull;
                        import org.jetbrains.annotations.Nullable;
                        import org.jetbrains.annotations.Range;
                                                
                        import java.time.LocalDateTime;
                                                
                        /**
                         * ProblemFields
                         */
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
                javaImpl);

        JavaFile javaFile = problemFields.asInterface("twitter4j.v2");
        assertEquals("ProblemFields.java", javaFile.fileName());
        String interfaceDeclaration = javaFile.content();
        assertEquals("""
                        package twitter4j.v2;
                                                
                        import org.jetbrains.annotations.NotNull;
                        import org.jetbrains.annotations.Nullable;
                        import org.jetbrains.annotations.Range;
                                                
                        import java.time.LocalDateTime;
                                                
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
                interfaceDeclaration);


    }
}